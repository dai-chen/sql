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

import com.google.common.io.Resources;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.amazon.opendistroforelasticsearch.sql.correctness.DBConnection.DBResult;
import static com.amazon.opendistroforelasticsearch.sql.correctness.TestReport.ErrorTestCase;
import static com.amazon.opendistroforelasticsearch.sql.correctness.TestReport.FailedTestCase;
import static com.amazon.opendistroforelasticsearch.sql.correctness.TestReport.SuccessTestCase;

/**
 * Correctness test base class that intercepts query method to perform more testing by comparing with multiple databases.
 */
public class CorrectnessTestCase {

    /**
     * Database connection for test data load and query.
     * Assumption is that the first connection is for the database to be targeted such as Elasticsearch.
     */
    private final DBConnection[] connections;

    public CorrectnessTestCase(DBConnection[] connections) {
        this.connections = connections;
    }

    public void initialize(String schemaFilePath, String dataFilePath) {
        TestData testData = new TestData(schemaFilePath, dataFilePath);
        for (DBConnection conn : connections) {
            testData.createTable(conn);
            testData.loadData(conn);
        }
    }

    public TestReport verify(List<String> sqls) {
        TestReport report = new TestReport();
        for (String sql : sqls) {
            DBResult esResult;
            try {
                esResult = connections[0].select(sql);

                int otherDbWithError = 0;
                String reasons = "";
                for (int i = 1; i < connections.length; i++) {
                    try {
                        DBResult otherDbResult = connections[i].select(sql);
                        //if (esResult.isCloseTo(otherDbResult)) {
                        if (esResult.equals(otherDbResult)) {
                            report.addTestCase(new SuccessTestCase(sql));
                        } else {
                            report.addTestCase(new FailedTestCase(sql, Arrays.asList(esResult, otherDbResult)));
                        }
                        break;
                    } catch (Exception e) {
                        // Ignore
                        otherDbWithError++;
                        reasons += extractRootCause(e) + ";";
                    }
                }

                if (otherDbWithError == connections.length - 1) {
                    report.addTestCase(new ErrorTestCase(sql, "No other databases support this query: " + reasons));
                }
            } catch (Exception e) {
                report.addTestCase(new ErrorTestCase(sql, extractRootCause(e)));
            }
        }
        return report;
    }

    public void report(TestReport report) {
        try {
            URL url = Resources.getResource("correctness/report.json");
            Files.write(Paths.get(url.toURI()), report.report().getBytes());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            for (DBConnection conn : connections) {
                conn.close();
            }
        }
    }

    private String extractRootCause(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }

        if (e.getLocalizedMessage() != null) {
            return e.getLocalizedMessage();
        }
        if (e.getMessage() != null) {
            return e.getMessage();
        }
        return e.toString();
    }

}
