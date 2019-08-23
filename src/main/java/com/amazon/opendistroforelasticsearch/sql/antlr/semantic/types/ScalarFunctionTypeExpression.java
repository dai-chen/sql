package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.NUMBER;
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
    };

    @Override
    public String getName() {
        return name();
    }

}
