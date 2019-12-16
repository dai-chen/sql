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

package com.amazon.opendistroforelasticsearch.sql.correctness.tests;

import com.amazon.opendistroforelasticsearch.sql.correctness.report.ErrorTestCase;
import com.amazon.opendistroforelasticsearch.sql.correctness.report.FailedTestCase;
import com.amazon.opendistroforelasticsearch.sql.correctness.report.SuccessTestCase;
import com.amazon.opendistroforelasticsearch.sql.correctness.report.TestReport;
import com.amazon.opendistroforelasticsearch.sql.correctness.runner.ComparisonTest;
import com.amazon.opendistroforelasticsearch.sql.correctness.runner.connection.DBConnection;
import com.amazon.opendistroforelasticsearch.sql.correctness.runner.resultset.DBResult;
import com.amazon.opendistroforelasticsearch.sql.correctness.runner.resultset.Row;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ComparisonTest}
 */
@RunWith(MockitoJUnitRunner.class)
public class ComparisonTestTest {

    @Mock
    private DBConnection esConnection;

    @Mock
    private DBConnection otherDbConnection;

    private ComparisonTest correctnessTest;

    @Before
    public void setUp() {
        correctnessTest = new ComparisonTest(
            new DBConnection[]{esConnection, otherDbConnection}
        );
    }

    @Test
    public void testSuccess() {
        when(esConnection.select(anyString())).thenReturn(
            new DBResult("ES", ImmutableMap.of("firstname", "text"), asList(new Row(asList("John"))))
        );
        when(otherDbConnection.select(anyString())).thenReturn(
            new DBResult("Other DB", ImmutableMap.of("firstname", "text"), asList(new Row(asList("John"))))
        );

        TestReport expected = new TestReport();
        expected.addTestCase(new SuccessTestCase("SELECT * FROM accounts"));
        TestReport actual = correctnessTest.verify(asList("SELECT * FROM accounts"));
        assertEquals(expected, actual);
    }

    @Test
    public void testFailureDueToInconsistency() {
        DBResult esResult = new DBResult("ES", ImmutableMap.of("firstname", "text"), asList(new Row(asList("John"))));
        DBResult otherDbResult = new DBResult("Other DB", ImmutableMap.of("firstname", "text"), asList(new Row(asList("JOHN"))));
        when(esConnection.select(anyString())).thenReturn(esResult);
        when(otherDbConnection.select(anyString())).thenReturn(otherDbResult);

        TestReport expected = new TestReport();
        expected.addTestCase(new FailedTestCase("SELECT * FROM accounts", asList(esResult, otherDbResult)));
        TestReport actual = correctnessTest.verify(asList("SELECT * FROM accounts"));
        assertEquals(expected, actual);
    }

    @Test
    public void testErrorDueToESException() {
        when(esConnection.select(anyString())).thenThrow(new RuntimeException("All shards failure"));

        TestReport expected = new TestReport();
        expected.addTestCase(new ErrorTestCase("SELECT * FROM accounts", "All shards failure"));
        TestReport actual = correctnessTest.verify(asList("SELECT * FROM accounts"));
        assertEquals(expected, actual);
    }

    @Test
    public void testErrorDueToNoOtherDBSupportThisQuery() {
        when(esConnection.select(anyString())).thenReturn(
            new DBResult("ES", ImmutableMap.of("firstname", "text"), asList(new Row(asList("John"))))
        );
        when(otherDbConnection.select(anyString())).thenThrow(new RuntimeException("Unsupported feature"));

        TestReport expected = new TestReport();
        expected.addTestCase(new ErrorTestCase("SELECT * FROM accounts", "No other databases support this query: Unsupported feature;"));
        TestReport actual = correctnessTest.verify(asList("SELECT * FROM accounts"));
        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessWhenOneDBSupportThisQuery() {
        DBConnection anotherDbConnection = mock(DBConnection.class);
        correctnessTest = new ComparisonTest(
            new DBConnection[]{esConnection, otherDbConnection, anotherDbConnection}
        );

        when(esConnection.select(anyString())).thenReturn(
            new DBResult("ES", ImmutableMap.of("firstname", "text"), asList(new Row(asList("John"))))
        );
        when(otherDbConnection.select(anyString())).thenThrow(new RuntimeException("Unsupported feature"));
        when(anotherDbConnection.select(anyString())).thenReturn(
            new DBResult("Another DB", ImmutableMap.of("firstname", "text"), asList(new Row(asList("John"))))
        );

        TestReport expected = new TestReport();
        expected.addTestCase(new SuccessTestCase("SELECT * FROM accounts"));
        TestReport actual = correctnessTest.verify(asList("SELECT * FROM accounts"));
        assertEquals(expected, actual);
    }

}
