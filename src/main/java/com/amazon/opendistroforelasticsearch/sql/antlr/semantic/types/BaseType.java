package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Base data types
 */
public enum BaseType implements Type {
    TYPE_ERROR,
    UNKNOWN,

    SHORT, LONG,
    INTEGER(SHORT, LONG),
    DOUBLE,
    FLOAT(DOUBLE),
    NUMBER(INTEGER, FLOAT),

    TEXT, KEYWORD,
    STRING(TEXT, KEYWORD),

    DATE_NANOS,
    DATE(DATE_NANOS),

    BOOLEAN,

    //BINARY,
    //RANGE,

    ES_TYPE(NUMBER, STRING, DATE, BOOLEAN);


    private static final Map<String, BaseType> ALL_BASE_TYPES;
    static {
        ImmutableMap.Builder<String, BaseType> builder = new ImmutableMap.Builder<>();
        for (BaseType type : BaseType.values()) {
            builder.put(type.name(), type);
        }
        ALL_BASE_TYPES = builder.build();
    }

    private BaseType parent;
    private final BaseType[] subTypes;


    BaseType(BaseType... subTypes) {
        this.subTypes = subTypes;
        for (BaseType subType : subTypes) {
            subType.parent = this;
        }
    }

    public static Type typeIn(Map<String, Object> mapping) {
        String typeStr = ((String) mapping.get("type")).toUpperCase();
        return ALL_BASE_TYPES.getOrDefault(typeStr, UNKNOWN);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public Type apply(Type... actualTypes) {
        if (actualTypes.length != 1) {
            return TYPE_ERROR;
        }
        return isCompatible(actualTypes[0]) ? actualTypes[0] : TYPE_ERROR;
    }

    public boolean isCompatible(Type other) {
        // Skip compatibility check if type is unknown
        if (this == UNKNOWN || other == UNKNOWN) {
            return true;
        }

        if (!(other instanceof BaseType)) {
            return false;
        }

        // One way compatibility
        BaseType cur = (BaseType) other;
        while (cur != null && cur != this) {
            cur = cur.parent;
        }
        return cur != null;
    }

}
