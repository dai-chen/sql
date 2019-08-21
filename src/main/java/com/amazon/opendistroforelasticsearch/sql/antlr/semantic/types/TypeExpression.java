package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Type expression
 */
public interface TypeExpression extends Type {

    Type[] inputTypes();

    Type outputType();

    static TypeExpression of(Type... types) {
        return new TypeExpression() {
            @Override
            public Type[] inputTypes() {
                return types;
            }

            @Override
            public Type outputType() {
                //throw new UnsupportedOperationException(
                //    "Temporary type expression for compatibility check doesn't have output type");
                return null;
            }

            @Override
            public String toString() {
                return "(" + Arrays.stream(inputTypes()).map(Type::toString).collect(Collectors.joining(", ")) + ")" +
                    (outputType() != null ? " -> " + outputType() : "");
            }
        };
    }

    /*
    default Type outputType() {
        Type[] types = types();
        if (types.length < 2) {
            throw new IllegalStateException(StringUtils.format(
                "Type expression [%s] has only [%s] type", this, types.length));
        }
        return types[types.length - 1];
    }
    */

    /*
    default boolean isCompatible(Type... otherInputTypes) {
        return isCompatible(new TypeExpression() {
            @Override
            public Type[] inputTypes() {
                return otherInputTypes;
            }

            @Override
            public Type outputType() {
                return null;
            }
        });
    }
     */

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
