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

package com.amazon.opendistroforelasticsearch.sql.correctness;

import com.amazon.opendistroforelasticsearch.sql.correctness.DBConnection.DBResult;
import com.amazon.opendistroforelasticsearch.sql.correctness.DBConnection.Row;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Test report class to generate JSON report.
 */
public class TestReport {

    private final List<TestCaseReport> testCases = new ArrayList<>();

    public void addTestCase(TestCaseReport testCase) {
        testCases.add(testCase);
    }

    public String report() {
        JSONObject report = new JSONObject();
        JSONArray tests = new JSONArray();
        report.put("tests", tests);

        int success = 0, failure = 0;
        for (int i = 0; i < testCases.size(); i++) {
            TestCaseReport testCase = testCases.get(i);
            if (testCase.isSuccess()) {
                success++;
            } else {
                failure++;
            }

            JSONObject test = testCase.report();
            test.put("id", i + 1);
            tests.put(test);
        }

        JSONObject summary = new JSONObject();
        summary.put("total", testCases.size());
        summary.put("success", success);
        summary.put("failure", failure);
        report.put("summary", summary);
        return report.toString(2);
    }

    public static abstract class TestCaseReport {
        private final boolean isSuccess;
        private final String sql;

        public TestCaseReport(boolean isSuccess, String sql) {
            this.isSuccess = isSuccess;
            this.sql = sql;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public JSONObject report() {
            JSONObject report = new JSONObject();
            report.put("result", (isSuccess ? "Success" : "Failed"));
            report.put("sql", sql);
            return report;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestCaseReport that = (TestCaseReport) o;
            return isSuccess == that.isSuccess &&
                sql.equals(that.sql);
        }

        @Override
        public int hashCode() {
            return Objects.hash(isSuccess, sql);
        }

        @Override
        public String toString() {
            return "TestCaseReport{" +
                "isSuccess=" + isSuccess +
                ", sql='" + sql + '\'' +
                '}';
        }
    }

    public static class SuccessTestCase extends TestCaseReport {

        public SuccessTestCase(String sql) {
            super(true, sql);
        }
    }

    public static class FailedTestCase extends TestCaseReport {
        private final Collection<DBResult> results;

        public FailedTestCase(String sql, Collection<DBResult> results) {
            super(false, sql);
            this.results = results;
        }

        @Override
        public JSONObject report() {
            JSONObject report = super.report();
            JSONArray resultSets = new JSONArray();
            report.put("resultSets", resultSets);

            for (DBResult result : results) {
                JSONObject json = new JSONObject();
                json.put("database", result.getDatabaseName());
                resultSets.put(json);

                JSONObject resultSet = new JSONObject();
                json.put("resultSet", resultSet);

                resultSet.put("schema", new JSONArray());
                result.getColumnNameAndTypes().forEach((name, type) -> {
                    JSONObject nameAndType = new JSONObject();
                    nameAndType.put("name", name);
                    nameAndType.put("type", type);
                    resultSet.getJSONArray("schema").put(nameAndType);
                });

                resultSet.put("dataRows", new JSONArray());
                for (Row row : result.getDataRows()) {
                    resultSet.getJSONArray("dataRows").put(row.getColumns());
                }
            }
            return report;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            FailedTestCase that = (FailedTestCase) o;
            return results.equals(that.results);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), results);
        }

        @Override
        public String toString() {
            return "FailedTestCase{" +
                "results=" + results +
                '}';
        }
    }

    public static class ErrorTestCase extends TestCaseReport {
        private final String reason;

        public ErrorTestCase(String sql, String reason) {
            super(false, sql);
            this.reason = reason;
        }

        @Override
        public JSONObject report() {
            JSONObject report = super.report();
            report.put("reason", reason);
            return report;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            ErrorTestCase that = (ErrorTestCase) o;
            return reason.equals(that.reason);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), reason);
        }

        @Override
        public String toString() {
            return "ErrorTestCase{" +
                "reason='" + reason + '\'' +
                '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestReport that = (TestReport) o;
        return testCases.equals(that.testCases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testCases);
    }

    @Override
    public String toString() {
        return "TestReport{" +
            "testCases=" + testCases +
            '}';
    }
}
