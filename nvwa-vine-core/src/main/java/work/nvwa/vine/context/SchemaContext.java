package work.nvwa.vine.context;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.util.StringUtils;
import work.nvwa.vine.SerializationType;
import work.nvwa.vine.annotation.ChatSchemaType;
import work.nvwa.vine.example.ChatActionExample;
import work.nvwa.vine.example.ExampleCase;
import work.nvwa.vine.metadata.ChatActionMetadata;
import work.nvwa.vine.metadata.FieldSchemaMetadata;
import work.nvwa.vine.metadata.TypeSchemaMetadata;
import work.nvwa.vine.prompt.VinePrompter;
import work.nvwa.vine.util.JsonUtils;
import work.nvwa.vine.util.YamlUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SchemaContext {
    private final ConcurrentHashMap<Type, TypeSchemaMetadata> schemaMap = new ConcurrentHashMap<>();
    private final VinePrompter vinePrompter;

    public SchemaContext(VinePrompter vinePrompter) {
        this.vinePrompter = vinePrompter;
    }

    public String buildSchemaPrompt(Type type) {
        TypeSchemaMetadata rootSchema = getSchemaMetadata(type);
        StringBuilder schema = new StringBuilder(rootSchema.getSubFieldsInfo()).append("\n\n");
        schemaMap.keySet().stream().filter(it -> it != type).forEach(it -> schema.append(schemaMap.get(it).getFullSchemaInfo()).append("\n\n"));
        return schema.toString();
    }

    private TypeSchemaMetadata getSchemaMetadata(Type type) {
        if (schemaMap.containsKey(type)) {
            return schemaMap.get(type);
        }
        String schemaType = switch (type.getTypeName()) {
            case "java.lang.String" -> "String";
            case "byte", "java.lang.Byte", "short", "java.lang.Short", "int", "java.lang.Integer", "long", "java.lang.Long" -> "Int";
            case "float", "java.lang.Float", "double", "java.lang.Double", "java.math.BigDecimal" -> "Number";
            case "boolean", "java.lang.Boolean" -> "Boolean";
            default -> null;
        };
        if (schemaType != null) {
            return new TypeSchemaMetadata(schemaType);
        }
        if (type instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (parameterizedType.getRawType() instanceof Class<?> rowType) {
                if (Collection.class.isAssignableFrom(rowType)) {
                    if (actualTypeArguments.length > 0) {
                        TypeSchemaMetadata itemType = getSchemaMetadata(actualTypeArguments[0]);
                        return new TypeSchemaMetadata(itemType.getName() + " Array");
                    }
                } else if (Map.class.isAssignableFrom(rowType)) {
                    if (actualTypeArguments.length > 1) {
                        TypeSchemaMetadata keyType = getSchemaMetadata(actualTypeArguments[0]);
                        TypeSchemaMetadata valueType = getSchemaMetadata(actualTypeArguments[1]);
                        return new TypeSchemaMetadata("Map<" + keyType.getName() + ", " + valueType.getName() + ">");
                    }
                } else {
                    // TODO: other parameterized types
                    throw new IllegalArgumentException("Unsupported parameterized type: " + rowType.getTypeName());
                }
            }
        } else if (type instanceof Class<?> clazz) {
            if (clazz.isEnum()) {
                String enumValues = Arrays.stream(clazz.getEnumConstants()).map(Object::toString).collect(Collectors.joining(", "));
                return new TypeSchemaMetadata("Enum, such as " + enumValues);
            } else if (clazz.isArray()) {
                TypeSchemaMetadata itemType = getSchemaMetadata(clazz.getComponentType());
                return new TypeSchemaMetadata(itemType.getName() + " Array");
            } else {
                return getSchemaMetadataByClass(clazz);
            }
        }
        throw new IllegalArgumentException("Unsupported type: " + type.getTypeName());
    }

    public String buildSystemPrompt(Method method, String systemPromptPrefix, SerializationType serializationType, Class<? extends ChatActionExample>[] exampleClasses) {
        StringBuilder systemPrompt = new StringBuilder();
        if (method.getGenericReturnType() == Void.TYPE) {
            throw new IllegalArgumentException("The chat action method " + method.getDeclaringClass().getCanonicalName() + "." + method.getName() + " cannot return void");
        }
        String schema = buildSchemaPrompt(method.getGenericReturnType());
        if (StringUtils.hasLength(schema)) {
            if (!systemPrompt.isEmpty()) {
                systemPrompt.append(vinePrompter.newLine(2));
            }
            systemPrompt.append(vinePrompter.header(2, vinePrompter.returnSchemaTitle()));
            systemPrompt.append(vinePrompter.newLine());
            systemPrompt.append(schema);
        }
        if (exampleClasses != null && exampleClasses.length > 0) {
            StringBuilder examplePrompt = new StringBuilder();
            List<String> parameterNames = Arrays.stream(method.getParameters()).map(Parameter::getName).toList();
            for (int i = 0; i < exampleClasses.length; i++) {
                Class<? extends ChatActionExample> exampleClass = exampleClasses[i];
                try {
                    ChatActionExample example = exampleClass.getDeclaredConstructor().newInstance();
                    ExampleCase exampleCase = example.exampleCase();
                    if (parameterNames.size() != exampleCase.parameters().length) {
                        String message = String.format("The example case %s parameters does not match the method %s.%s parameters", exampleClass.getCanonicalName(), method.getDeclaringClass().getCanonicalName(), method.getName());
                        throw new IllegalArgumentException(message);
                    }
                    examplePrompt.append(vinePrompter.header(2, vinePrompter.exampleTitle())).append(i + 1);
                    examplePrompt.append(vinePrompter.newLine());
                    if (!parameterNames.isEmpty()) {
                        examplePrompt.append(vinePrompter.header(3, vinePrompter.exampleParametersTitle())).append(vinePrompter.newLine());
                        for (int j = 0; j < exampleCase.parameters().length; j++) {
                            Object parameter = exampleCase.parameters()[j];
                            String parameterName = parameterNames.get(j);
                            examplePrompt.append(vinePrompter.newLine());
                            examplePrompt.append(vinePrompter.header(4, parameterName)).append(vinePrompter.newLine());
                            examplePrompt.append(buildObjectPrompt(serializationType, parameter)).append(vinePrompter.newLine(2));
                        }
                    }
                    examplePrompt.append(vinePrompter.header(3, vinePrompter.exampleReturnTitle())).append(vinePrompter.newLine());
                    examplePrompt.append(buildObjectPrompt(serializationType, exampleCase.returnValue()));
                    examplePrompt.append(vinePrompter.newLine(2));
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                    String message = String.format("The example class %s must have a public no-args constructor", exampleClass.getCanonicalName());
                    throw new IllegalArgumentException(message);
                }
            }
            if (!examplePrompt.isEmpty()) {
                if (!systemPromptPrefix.isEmpty()) {
                    systemPrompt.append(vinePrompter.newLine(2));
                }
                systemPrompt.append(examplePrompt);
            }
        }
        return systemPrompt.toString();
    }

    public String buildUserMessage(ChatActionMetadata chatActionMetadata, Map<String, Object> parameters) {
        StringBuilder userMessage = new StringBuilder(chatActionMetadata.getUserPrompt());
        if (parameters != null && !parameters.isEmpty()) {
            String parametersPrompt = parameters.entrySet().stream()
                    .map(entry -> buildParameterPrompt(entry.getKey(), entry.getValue(), chatActionMetadata.getSerializationType()))
                    .collect(Collectors.joining(vinePrompter.newLine()));
            if (!parametersPrompt.isEmpty()) {
                userMessage.append(vinePrompter.newLine(2));
                userMessage.append(vinePrompter.header(2));
                userMessage.append(vinePrompter.inputParameterTitle());
                userMessage.append(vinePrompter.newLine(2));
                userMessage.append(parametersPrompt);
            }
        }
        if (StringUtils.hasLength(userMessage)) {
            userMessage.append(vinePrompter.newLine(2));
        }
        userMessage.append(vinePrompter.newLine(2));
        switch (chatActionMetadata.getSerializationType()) {
            case Json -> userMessage.append(vinePrompter.returnJsonFormat());
            case Yaml -> userMessage.append(vinePrompter.returnYamlFormat());
        }
        return userMessage.toString();
    }

    private String buildParameterPrompt(String name, Object value, SerializationType serializationType) {
        return vinePrompter.header(3) + name + vinePrompter.newLine() + buildObjectPrompt(serializationType, value);
    }

    private String buildObjectPrompt(SerializationType serializationType, Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isPrimitive()) {
            return obj.toString();
        }
        if(obj instanceof String) {
            return (String) obj;
        }
        try {
            return switch (serializationType) {
                case Yaml -> YamlUtils.toYaml(obj);
                case Json -> JsonUtils.toJson(obj);
            };
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private TypeSchemaMetadata getSchemaMetadataByClass(Class<?> clazz) {
        ChatSchemaType chatSchemaType = clazz.getDeclaredAnnotation(ChatSchemaType.class);
        String name = clazz.getSimpleName();
        String description = "";
        if (chatSchemaType != null) {
            if (!chatSchemaType.name().isEmpty()) {
                name = chatSchemaType.name();
            }
            if (!chatSchemaType.description().isEmpty()) {
                description = chatSchemaType.description();
            }
        }

        TypeSchemaMetadata typeSchemaMetadata = new TypeSchemaMetadata(name);
        schemaMap.put(clazz, typeSchemaMetadata);
        String basicInfo = name;
        if (StringUtils.hasLength(description)) {
            basicInfo += vinePrompter.delimiter(description);
        }

        List<FieldSchemaMetadata> subFields = Arrays.stream(clazz.getDeclaredFields()).filter(this::filterFiled).map(this::getFieldSchemaMetadata).toList();
        StringBuilder subFieldsInfo = new StringBuilder();
        StringBuilder fullSchemaInfo = new StringBuilder(vinePrompter.header(3, basicInfo));
        if (!subFields.isEmpty()) {
            subFieldsInfo.append(subFields.stream().map(FieldSchemaMetadata::getSchema).collect(Collectors.joining(vinePrompter.newLine())));
            fullSchemaInfo.append(vinePrompter.newLine()).append(subFieldsInfo);
        }
        typeSchemaMetadata.fillingInfo(basicInfo, subFieldsInfo.toString(), fullSchemaInfo.toString(), subFields);
        return typeSchemaMetadata;
    }

    private boolean filterFiled(Field field) {
        if (Modifier.isStatic(field.getModifiers())) {
            return false;
        }
        return true;
    }

    private FieldSchemaMetadata getFieldSchemaMetadata(Field field) {
        TypeSchemaMetadata typeSchemaMetadata = getSchemaMetadata(field.getGenericType());
        return new FieldSchemaMetadata(field, typeSchemaMetadata);
    }
}
