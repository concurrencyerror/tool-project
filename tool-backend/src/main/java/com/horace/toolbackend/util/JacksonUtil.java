package com.horace.toolbackend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.horace.toolbackend.exception.JsonConvertException;

public final class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JacksonUtil() {
    }

    /**
     * Parse JSON string into a JsonNode object.
     *
     * @param json JSON string to be parsed (not blank)
     * @return JsonNode representation of the JSON string
     */
    public static JsonNode readTree(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new JsonConvertException("Failed to parse json", e);
        }
    }

    /**
     * Generates formatted json.
     *
     * @param obj entity object
     * @param <T> object type to serialize
     * @return pretty json
     */
    public static <T> String writePrettyValueAsString(T obj) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonConvertException("Failed to serialize object to pretty json", e);
        }
    }

    public static <T> String safePrettyValueAsString(T obj) {
        try {
            return writePrettyValueAsString(obj);
        } catch (JsonConvertException e) {
            return String.valueOf(obj);
        }
    }

    public static <T> String toJson(T obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonConvertException("Failed to serialize object to json", e);
        }
    }
}
