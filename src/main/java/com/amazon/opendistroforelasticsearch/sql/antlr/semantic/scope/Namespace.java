package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.scope;

public enum Namespace {

    FIELD_NAME("Field"),
    FUNCTION_NAME("Function"),
    OPERATOR_NAME("Operator");

    private final String name;

    Namespace(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
