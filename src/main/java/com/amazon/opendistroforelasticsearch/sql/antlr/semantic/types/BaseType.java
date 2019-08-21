package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import java.util.Arrays;
import java.util.Map;

public enum BaseType implements Type {

    INT, LONG,
    NUMBER(INT, LONG),

    TEXT, KEYWORD,
    STRING(TEXT, KEYWORD),

    TYPE(NUMBER, STRING);


    private final BaseType[] subTypes;

    BaseType(BaseType... subTypes) {
        this.subTypes = subTypes;
    }

    public static BaseType typeIn(Map<String, Object> mapping) {
        return valueOf(((String) mapping.get("type")).toUpperCase());
    }

    @Override
    public boolean isCompatible(Type other) {
        return this == other; // for simplicity, may need LCA
    }

    @Override
    public String toString() {
        return name();
    }
}
