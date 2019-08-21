package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Base type for data types in Elasticsearch
 */
public enum BaseType implements Type {
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
    public boolean isCompatible(Type other) {
        // Skip compatibility check if type is unknown
        if (this == UNKNOWN || other == UNKNOWN) {
            return true;
        }

        if (!(other instanceof BaseType)) {
            return false;
        }

        // Two way
        //BaseType lca = findLCA(ES_TYPE, this, (BaseType) other);
        //return lca == this || lca == other;

        // One way compatibility
        return isParentTypeOf((BaseType) other);
    }

    public boolean isParentTypeOf(BaseType other) {
        BaseType cur = other;
        while (cur != null && cur != this) {
            cur = cur.parent;
        }
        return cur != null;
    }

    /**
     * Find lowest common ancestor in the tree for types
     */
    private BaseType findLCA(BaseType root, BaseType type1, BaseType type2) {
        if (root == null) {
            return null;
        }
        if (type1 == type2) {
            return type1;
        }
        if (root == type1 || root == type2) {
            return root;
        }

        BaseType lcaOrEitherType = null;
        for (BaseType subTypeTree : root.subTypes) {
            BaseType temp = findLCA(subTypeTree, type1, type2);
            if (temp == null) {
                continue;
            }

            if (lcaOrEitherType == null) {
                lcaOrEitherType = temp;
            } else if (lcaOrEitherType != temp) {
                lcaOrEitherType = root;
                break;
            }
        }
        return lcaOrEitherType;
    }

    @Override
    public String toString() {
        return name();
    }
}
