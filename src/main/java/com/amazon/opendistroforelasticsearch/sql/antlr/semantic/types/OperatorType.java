package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.TYPE_ERROR;

public class OperatorType implements Type {

    private final String name;

    public OperatorType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type apply(Type... actualTypes) {
        if (actualTypes.length != 2) {
            return TYPE_ERROR;
        }

        Type isLeftCompatibleWithRight = actualTypes[0].apply(actualTypes[1]);
        if (isLeftCompatibleWithRight == TYPE_ERROR) {
            return actualTypes[1].apply(actualTypes[0]);
        }
        return TYPE_ERROR;
    }

}
