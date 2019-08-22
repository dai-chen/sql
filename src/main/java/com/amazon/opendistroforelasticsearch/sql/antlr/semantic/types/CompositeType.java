package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

/**
 * Composite data type
 */
public enum CompositeType implements Type {
    OBJECT,
    NESTED;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean isCompatible(Type other) {
        return this == other;
    }

    @Override
    public String toString() {
        return name();
    }
}
