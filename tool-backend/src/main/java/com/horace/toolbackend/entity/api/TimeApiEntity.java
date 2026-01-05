package com.horace.toolbackend.entity.api;

import com.horace.toolbackend.enums.DateType;

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

    public static class Type {
        public DateType type;
    }
}
