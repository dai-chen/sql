package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

public interface Type {

    String getName();

    Type apply(Type... actualTypes);

}
