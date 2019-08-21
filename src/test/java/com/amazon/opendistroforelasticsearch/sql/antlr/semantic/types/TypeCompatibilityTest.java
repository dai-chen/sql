package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import org.junit.Ignore;
import org.junit.Test;

import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.ES_TYPE;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.INTEGER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.KEYWORD;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.LONG;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.NUMBER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.SHORT;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.STRING;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.TEXT;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.UNKNOWN;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.ScalarFunctionType.ABS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TypeCompatibilityTest {

    @Test
    public void sameBaseTypeShouldBeCompatible() {
        assertTrue(INTEGER.isCompatible(INTEGER));
    }

    @Test
    public void parentBaseTypeShouldBeCompatibleWithSubBaseType() {
        assertTrue(NUMBER.isCompatible(LONG));
    }

    @Ignore("One way or two way compatibility?")
    @Test
    public void subBaseTypeShouldBeCompatibleWithParentBaseType() {
        assertTrue(KEYWORD.isCompatible(STRING));
    }

    @Test
    public void nonRelatedBaseTypeShouldNotBeCompatible() {
        assertFalse(SHORT.isCompatible(TEXT));
    }

    @Test
    public void unknownBaseTypeShouldBeCompatibleWithAnyBaseType() {
        assertTrue(UNKNOWN.isCompatible(INTEGER));
    }

    @Test
    public void anyBaseTypeShouldBeCompatibleWithUnknownBaseType() {
        assertTrue(LONG.isCompatible(UNKNOWN));
    }

    @Test
    public void functionOfSameBaseTypeShouldBeCompatible() {
        assertTrue(ABS.isCompatible(TypeExpression.of(NUMBER)));
    }

    @Test
    public void functionOfParentBaseTypeShouldBeCompatibleWithFunctionOfSubBaseType() {
        assertTrue(ABS.isCompatible(TypeExpression.of(LONG)));
    }

    @Test
    public void functionOfSubBaseTypeShouldNotBeCompatibleWithFunctionOfParentBaseType() {
        assertFalse(ABS.isCompatible(TypeExpression.of(ES_TYPE)));
    }

}
