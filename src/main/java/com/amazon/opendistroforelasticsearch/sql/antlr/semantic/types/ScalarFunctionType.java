package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.scope.Namespace;

import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.INTEGER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.NUMBER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.STRING;

/**
 * Scalar function type
 */
public enum ScalarFunctionType implements TypeExpression {

    ABS(NUMBER, NUMBER),
    SUBSTRING(STRING, STRING, INTEGER); // positive?

    private final Type outputType;
    private final Type[] inputTypes;

    ScalarFunctionType(Type outputType, Type... inputTypes) {
        this.outputType = outputType;
        this.inputTypes = inputTypes;
    }

    @Override
    public Namespace getNamespace() {
        return Namespace.FUNCTION_NAME;
    }

    @Override
    public Type[] inputTypes() {
        return inputTypes;
    }

    @Override
    public Type outputType() {
        return outputType;
    }

    @Override
    public String toString() {
        return format(name());
    }

    @Override
    public String getName() {
        return name();
    }
}
