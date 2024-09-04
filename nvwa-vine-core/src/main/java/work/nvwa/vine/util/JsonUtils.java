package work.nvwa.vine.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, valueTypeRef);
    }

    public static <T> T fromJson(InputStream inputStream, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(inputStream, clazz);
    }

    public static <T> T fromJson(InputStream inputStream, TypeReference<T> valueTypeRef) throws IOException {
        return OBJECT_MAPPER.readValue(inputStream, valueTypeRef);
    }

    public static <T> T fromJsonWithoutEnclosure(String json, Class<T> clazz) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(withoutEnclosure(json), clazz);
    }

    public static <T> T fromJsonWithoutEnclosure(String json, TypeReference<T> valueTypeRef) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(withoutEnclosure(json), valueTypeRef);
    }

    private static String withoutEnclosure(String json) {
        if (json == null) {
            return null;
        }
        if (json.startsWith("```json")) {
            json = json.substring(7);
        } else if (json.endsWith("```")) {
            json = json.substring(3);
        }
        if (json.endsWith("```")) {
            json = json.substring(0, json.length() - 3);
        }
        return json;
    }

}
