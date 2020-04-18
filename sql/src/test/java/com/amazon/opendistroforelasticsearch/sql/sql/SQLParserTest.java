/*
 *   Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License").
 *   You may not use this file except in compliance with the License.
 *   A copy of the License is located at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file. This file is distributed
 *   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */

package com.amazon.opendistroforelasticsearch.sql.sql;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SQLParserTest {

    private SQLParser parser = new SQLParser();

    @Test
    public void parseDecimalLiteral() {
        Projection projection = parser.parse("SELECT 1");
        Projection expected = new Projection();
        expected.add("1", new Expression("1"));
        assertEquals(expected, projection);
    }

    @Test
    public void testBooleanLiteral() {
        Projection projection = parser.parse("SELECT TRUE");
        Projection expected = new Projection();
        expected.add("TRUE", new Expression("TRUE"));
        assertEquals(expected, projection);
    }

    @Test
    public void parseFieldAlias() {
        Projection projection = parser.parse("SELECT 'helloworld' AS name");
        Projection expected = new Projection();
        expected.add("name", new Expression("'helloworld'"));
        assertEquals(expected, projection);
    }

    @Test
    public void parseArithmeticExpression() {
        Projection projection = parser.parse("SELECT (1 + 2) * 3 AS age");
        Projection expected = new Projection();
        expected.add("age", new Expression("(1+2)*3"));
        assertEquals(expected, projection);
    }

}
