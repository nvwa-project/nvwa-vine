package work.nvwa.vine.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

import java.io.IOException;
import java.io.InputStream;

public final class YamlUtils {
    private static final ObjectMapper YAML_OBJECT_MAPPER;

    static {
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true)
                .configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
        YAML_OBJECT_MAPPER = new ObjectMapper(yamlFactory);
        YAML_OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        YAML_OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        YAML_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        YAML_OBJECT_MAPPER.registerModule(new KotlinModule.Builder().build());
        YAML_OBJECT_MAPPER.addHandler(new DeserializationProblemHandler() {

            @Override
            public JavaType handleMissingTypeId(DeserializationContext ctxt, JavaType baseType, TypeIdResolver idResolver, String failureMsg) throws IOException {
                if (!baseType.isInterface() && !baseType.isAbstract()) {
                    return baseType;
                }
                return super.handleMissingTypeId(ctxt, baseType, idResolver, failureMsg);
            }
        });
    }

    public static <T> T fromYaml(String yaml, Class<T> clazz) throws JsonProcessingException {
        return YAML_OBJECT_MAPPER.readValue(yaml, clazz);
    }

    public static <T> T fromYaml(String yaml, TypeReference<T> valueTypeRef) throws JsonProcessingException {
        return YAML_OBJECT_MAPPER.readValue(yaml, valueTypeRef);
    }

    public static <T> T fromYaml(InputStream inputStream, Class<T> clazz) throws IOException {
        return YAML_OBJECT_MAPPER.readValue(inputStream, clazz);
    }

    public static <T> T fromYaml(InputStream inputStream, TypeReference<T> valueTypeRef) throws IOException {
        return YAML_OBJECT_MAPPER.readValue(inputStream, valueTypeRef);
    }

    public static <T> T fromYamlWithoutEnclosure(String yaml, Class<T> clazz) throws JsonProcessingException {
        return YAML_OBJECT_MAPPER.readValue(withoutEnclosure(yaml), clazz);
    }

    public static <T> T fromYamlWithoutEnclosure(String yaml, TypeReference<T> valueTypeRef) throws JsonProcessingException {
        return YAML_OBJECT_MAPPER.readValue(withoutEnclosure(yaml), valueTypeRef);
    }

    public static String toYaml(Object obj) throws JsonProcessingException {
        return YAML_OBJECT_MAPPER.writeValueAsString(obj);
    }

    private static String withoutEnclosure(String yaml) {
        if (yaml == null) {
            return null;
        }
        if (yaml.startsWith("```yaml")) {
            yaml = yaml.substring(7);
        } else if (yaml.endsWith("```")) {
            yaml = yaml.substring(3);
        }
        if (yaml.endsWith("```")) {
            yaml = yaml.substring(0, yaml.length() - 3);
        }
        return yaml;
    }

}
