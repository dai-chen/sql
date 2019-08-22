package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.scope.Namespace;

public enum OperatorType implements TypeExpression {
    EQUAL("=");

    private final String name;

    OperatorType(String name) {
        this.name = name;
    }

    @Override
    public Namespace getNamespace() {
        return Namespace.OPERATOR_NAME;
    }

    @Override
    public Type[] inputTypes() {
        return null;
    }

    @Override
    public Type outputType() {
        return BaseType.BOOLEAN;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCompatible(Type other) {
        if (!(other instanceof TypeExpression)) {
            return false;
        }
        return ((TypeExpression) other).inputTypes()[0] == ((TypeExpression) other).inputTypes()[1];
    }

}
