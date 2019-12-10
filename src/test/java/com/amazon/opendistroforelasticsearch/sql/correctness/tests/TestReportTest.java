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

import com.amazon.opendistroforelasticsearch.sql.correctness.TestReport;
import com.amazon.opendistroforelasticsearch.sql.correctness.TestReport.ErrorTestCase;
import com.amazon.opendistroforelasticsearch.sql.correctness.TestReport.FailedTestCase;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashSet;

import static com.amazon.opendistroforelasticsearch.sql.correctness.DBConnection.DBResult;
import static com.amazon.opendistroforelasticsearch.sql.correctness.DBConnection.Row;
import static com.amazon.opendistroforelasticsearch.sql.correctness.TestReport.SuccessTestCase;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.junit.Assert.fail;

public class TestReportTest {

    private TestReport report = new TestReport();

    @Test
    public void testSuccessReport() {
        report.addTestCase(new SuccessTestCase("SELECT * FROM accounts"));
        JSONObject actual = new JSONObject(report.report());
        JSONObject expected = new JSONObject(
            "{" +
                "  \"summary\": {" +
                "    \"total\": 1," +
                "    \"success\": 1," +
                "    \"failure\": 0" +
                "  }," +
                "  \"tests\": [" +
                "    {" +
                "      \"id\": 1," +
                "      \"result\": 'Success'," +
                "      \"sql\": \"SELECT * FROM accounts\"," +
                "    }" +
                "  ]" +
                "}"
        );

        if (!actual.similar(expected)) {
            fail("Actual JSON is different from expected: " + actual);
        }
    }

    @Test
    public void testFailedReport() {
        report.addTestCase(new FailedTestCase("SELECT * FROM accounts", new HashSet<>(asList(
            new DBResult("Elasticsearch", new Row(asList("firstName")), singleton(new Row(asList("hello")))),
            new DBResult("H2", new Row(asList("firstName")), singleton(new Row(asList("world"))))
        ))));
        JSONObject actual = new JSONObject(report.report());
        JSONObject expected = new JSONObject(
            "{" +
                "  \"summary\": {" +
                "    \"total\": 1," +
                "    \"success\": 0," +
                "    \"failure\": 1" +
                "  }," +
                "  \"tests\": [" +
                "    {" +
                "      \"id\": 1," +
                "      \"result\": 'Failed'," +
                "      \"sql\": \"SELECT * FROM accounts\"," +
                "      \"resultSets\": [" +
                "        {" +
                "          \"database\": \"Elasticsearch\"," +
                "          \"resultSet\": {" +
                "            \"schema\": [\"firstName\"]," +
                "            \"dataRows\": [[\"hello\"]]" +
                "          }" +
                "        }," +
                "        {" +
                "          \"database\": \"H2\"," +
                "          \"resultSet\": {" +
                "            \"schema\": [\"firstName\"]," +
                "            \"dataRows\": [[\"world\"]]" +
                "          }" +
                "        }" +
                "      ]" +
                "    }" +
                "  ]" +
                "}"
        );

        if (!actual.similar(expected)) {
            fail("Actual JSON is different from expected: " + actual.toString(2));
        }
    }

    @Test
    public void testErrorReport() {
        report.addTestCase(new ErrorTestCase("SELECT * FROM", "Missing table name in query"));
        JSONObject actual = new JSONObject(report.report());
        JSONObject expected = new JSONObject(
            "{" +
                "  \"summary\": {" +
                "    \"total\": 1," +
                "    \"success\": 0," +
                "    \"failure\": 1" +
                "  }," +
                "  \"tests\": [" +
                "    {" +
                "      \"id\": 1," +
                "      \"result\": 'Failed'," +
                "      \"sql\": \"SELECT * FROM\"," +
                "      \"reason\": \"Missing table name in query\"," +
                "    }" +
                "  ]" +
                "}"
        );

        if (!actual.similar(expected)) {
            fail("Actual JSON is different from expected: " + actual);
        }
    }

}
