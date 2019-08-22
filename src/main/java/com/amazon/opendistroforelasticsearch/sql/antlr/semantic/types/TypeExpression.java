package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.scope.Namespace;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

/**
 * Type expression for function, operator etc.
 */
public interface TypeExpression extends Type {

    Namespace getNamespace();

    Type[] inputTypes();

    Type outputType();

    /**
     * Create temporary type expression for compatibility check which doesn't have output type
     */
    static TypeExpression of(Type... types) {
        return new TypeExpression() {
            @Override
            public String getName() {
                return "Temp";
            }

            @Override
            public Namespace getNamespace() {
                return null;
            }

            @Override
            public Type[] inputTypes() {
                return types;
            }

            @Override
            public Type outputType() {
                return null;
            }

            @Override
            public String toString() {
                return Arrays.toString(inputTypes());
            }
        };
    }

    /** toString is not allowed to be overridden as default method */
    default String format(String name) {
        return name +
            "(" +
                Arrays.stream(inputTypes()).
                       map(Type::toString).
                       collect(joining(", ")) +
            ") -> " + outputType();
    }

    @Override
    default boolean isCompatible(Type other) {
        if (!(other instanceof TypeExpression)) {
            return false;
        }

        Type[] types = inputTypes();
        Type[] otherTypes = ((TypeExpression) other).inputTypes();
        if (types.length != otherTypes.length) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if (!types[i].isCompatible(otherTypes[i])) {
                return false;
            }
        }
        return true;
    }

}
