package com.horace.toolbackend.enums;

/**
 * 节假日枚举
 *
 * @author Horace
 */
public enum DateType {
    workDay("0", "工作日"),
    weekend("1", "周末"),
    holiday("2", "节日"),
    change("3", "调休");

    public final String code;

    public final String msg;

    DateType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
