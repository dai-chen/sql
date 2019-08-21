package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

public interface Type {

    boolean isCompatible(Type other);

}
