package com.horace.toolbackend.util;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class JacksonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Parse JSON string into a JsonNode object.
     *
     * @param json JSON string to be parsed (not blank)
     * @return JsonNode representation of the JSON string
     */
    public static JsonNode readTree(String json) {
        return objectMapper.readTree(json);
    }
}
