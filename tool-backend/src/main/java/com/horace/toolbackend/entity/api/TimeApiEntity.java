package com.horace.toolbackend.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.horace.toolbackend.enums.DateType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeApiEntity {
    private String code;

    private Type type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Type {
        public DateType type;
    }
}
