package work.nvwa.vine.context;

import org.springframework.util.StringUtils;
import work.nvwa.vine.annotation.ChatSchemaType;
import work.nvwa.vine.metadata.FieldSchemaMetadata;
import work.nvwa.vine.metadata.TypeSchemaMetadata;
import work.nvwa.vine.prompt.VinePrompter;

import java.lang.reflect.Field;
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
        List<FieldSchemaMetadata> subFields = Arrays.stream(clazz.getDeclaredFields()).map(this::getFieldSchemaMetadata).toList();
        String basicInfo = name;
        if (StringUtils.hasLength(description)) {
            basicInfo += vinePrompter.delimiter(description);
        }

        StringBuilder subFieldsInfo = new StringBuilder();
        StringBuilder fullSchemaInfo = new StringBuilder(vinePrompter.header(3, basicInfo));
        if (!subFields.isEmpty()) {
            subFieldsInfo.append(subFields.stream().map(FieldSchemaMetadata::getSchema).collect(Collectors.joining(vinePrompter.newLine())));
            fullSchemaInfo.append(vinePrompter.newLine()).append(subFieldsInfo);
        }
        TypeSchemaMetadata typeSchemaMetadata = new TypeSchemaMetadata(name, basicInfo, subFieldsInfo.toString(), fullSchemaInfo.toString(), subFields);
        schemaMap.put(clazz, typeSchemaMetadata);
        return typeSchemaMetadata;
    }

    private FieldSchemaMetadata getFieldSchemaMetadata(Field field) {
        TypeSchemaMetadata typeSchemaMetadata = getSchemaMetadata(field.getGenericType());
        return new FieldSchemaMetadata(field, typeSchemaMetadata);
    }
}
