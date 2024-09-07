package com.alinesno.infra.base.search.enums;

import lombok.Getter;

/**
 * 索引类型枚举类
 */
@Getter
public enum IndexTypeEnums {

    // 按天daily/按月month
    DAILY("daily", "按天"),
    MONTH("month", "按月");

    private final String value;
    private final String label;

    IndexTypeEnums(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static IndexTypeEnums getByValue(String value) {
        for (IndexTypeEnums constant : IndexTypeEnums.values()) {
            if (constant.value.equals(value)) {
                return constant;
            }
        }
        return null;
    }
}
