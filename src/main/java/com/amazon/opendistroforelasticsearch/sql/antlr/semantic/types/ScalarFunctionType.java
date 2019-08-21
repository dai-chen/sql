package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.INT;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.NUMBER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.STRING;

public enum ScalarFunctionType implements TypeExpression {

    ABS(NUMBER, NUMBER),
    SUBSTRING(STRING, STRING, INT); // positive?

    private final Type outputType;
    private final Type[] inputTypes;

    ScalarFunctionType(Type outputType, Type... inputTypes) {
        this.outputType = outputType;
        this.inputTypes = inputTypes;
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
        return "(" + Arrays.stream(inputTypes).map(Type::toString).collect(Collectors.joining(", ")) + ") -> " + outputType;
    }
}
