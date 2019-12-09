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

import com.amazon.opendistroforelasticsearch.sql.correctness.Database;
import com.amazon.opendistroforelasticsearch.sql.correctness.TestData;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TestDataTest {

    @Mock
    private Database connection;

    private TestData testData = new TestData("test_data_test.json", "test_data_test.csv");

    @Test
    public void createShouldCreateSchemaUsingFileName() {
        testData.createTable(connection);
        verify(connection).create(eq("test_data_test"), anyString());
    }

    @Ignore
    @Test
    public void loadDataShouldLoadAllFieldsToTableUsingFileName() {
        testData.loadData(connection);
        verify(connection).insert(
            "test_data_test",
            new String[]{"AvgTicketPrice", "Cancelled", "Carrier", "dayOfWeek", "timestamp"},
            Arrays.asList(
                new String[]{"1000", "true", "Delta", "1", "123"},
                new String[]{"2000", "false", "Southwest", "2", "456"}
            )
        );
    }

}
