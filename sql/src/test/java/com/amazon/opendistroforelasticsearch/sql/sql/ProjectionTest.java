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

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Fake logical operator for projection in relational theory
 */
public class ProjectionTest {

    @Test
    public void emptyProjectionShouldProjectAnyInputTupleToEmptyTuple() {
        Projection projection = new Projection();
        Tuple input1 = new Tuple();
        Tuple output1 = projection.project(input1);
        assertTrue(output1.isEmpty());

        Tuple input2 = new Tuple(Map.of("name", "John"));
        Tuple output2 = projection.project(input2);
        assertTrue(output2.isEmpty());
    }

    @Test
    public void projectionShouldReturnDecimalLiteral() {
        Projection projection = new Projection();
        projection.add("age", new Expression("1"));

        Tuple input = new Tuple();
        Tuple output = projection.project(input);
        assertEquals(new Tuple(Map.of("age", 1)), output);
    }

    @Test
    public void projectionShouldReturnStringLiteral() {
        Projection projection = new Projection();
        projection.add("name", new Expression("'John'"));

        Tuple input = new Tuple();
        Tuple output = projection.project(input);
        assertEquals(new Tuple(Map.of("name", "John")), output);
    }

    @Test
    public void projectionShouldReturnArithmeticExpression() {
        Projection projection = new Projection();
        projection.add("age", new Expression("1 + 2"));

        Tuple input = new Tuple();
        Tuple output = projection.project(input);
        assertEquals(new Tuple(Map.of("age", 3)), output);
    }

}
