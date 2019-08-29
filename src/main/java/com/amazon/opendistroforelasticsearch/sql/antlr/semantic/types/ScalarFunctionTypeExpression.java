package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.INTEGER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.NUMBER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.STRING;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.TYPE_ERROR;

/**
 * Scalar function type
 */
public enum ScalarFunctionTypeExpression implements Type {

    ABS() {
        @Override
        public Type apply(Type... actualTypes) {
            if (actualTypes.length != 1) {
                return TYPE_ERROR;
            }
            return NUMBER.apply(actualTypes);
        }

        @Override
        public String toString() {
            return "ABS(Number) -> Number";
        }
    },
    SUBSTRING(){
        @Override
        public Type apply(Type... actualTypes) {
            if (actualTypes.length != 2) {
                return TYPE_ERROR;
            }

            if (STRING.apply(actualTypes[0]) == TYPE_ERROR) {
                return TYPE_ERROR;
            }

            if (INTEGER.apply(actualTypes[1]) == TYPE_ERROR) {
                return TYPE_ERROR;
            }
            return actualTypes[0];
        }

        @Override
        public String toString() {
            return "SUBSTRING(STRING, INTEGER) -> STRING";
        }
    };

    @Override
    public String getName() {
        return name();
    }

}
