package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

public interface Type {

    String getName();

    boolean isCompatible(Type other);

}
