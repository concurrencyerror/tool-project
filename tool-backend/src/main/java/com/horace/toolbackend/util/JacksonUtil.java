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

    /**
     * 生成的是格式化的 json
     *
     * @param obj 实体类
     * @param <T> 需要生成 json的数据
     * @return 返回 json
     */
    public static <T> String writePrettyValueAsString(T obj) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }


    public static <T> String toJson(T obj) {
        return objectMapper.writeValueAsString(obj);
    }
}
