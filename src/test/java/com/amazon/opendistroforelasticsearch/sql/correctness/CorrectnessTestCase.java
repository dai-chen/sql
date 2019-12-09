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
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.amazon.opendistroforelasticsearch.sql.correctness.Database.DBResult;
import static com.amazon.opendistroforelasticsearch.sql.correctness.TestReport.ErrorTestCase;
import static com.amazon.opendistroforelasticsearch.sql.correctness.TestReport.FailedTestCase;
import static com.amazon.opendistroforelasticsearch.sql.correctness.TestReport.SuccessTestCase;

/**
 * Correctness test base class that intercepts query method to perform more testing by comparing with multiple databases.
 */
public interface CorrectnessTestCase {


    default void prepareTableAndData(String schemaFile, String dataFile) {
        TestData testData = new TestData(schemaFile, dataFile);
        for (Database db : getDatabases()) {
            testData.createTable(db); //TODO: db.createTable(testData)?
            testData.loadData(db);
        }
    }

    default void verify(List<String> sqls) {
        TestReport report = new TestReport();

        for (String sql : sqls) {
            try {
                Set<DBResult> results = Arrays.stream(getDatabases()).
                    map(conn -> conn.select(sql)).
                    collect(Collectors.toSet());

                if (results.size() == 1) {
                    report.addTestCase(new SuccessTestCase(sql));
                } else {
                    report.addTestCase(new FailedTestCase(sql, results));
                }
            } catch (Exception e) {
                report.addTestCase(new ErrorTestCase(sql, e.getMessage()));
            }
        }

        try {
            URL url = Resources.getResource("correctness/report.json");
            Files.write(Paths.get(url.toURI()), report.report().getBytes());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        //assertThat(StringUtils.format(
        //    "Found difference between results from different databases when test query [%s]: %s ", sql, results),
        //    results, hasSize(1));
    }

    Database[] getDatabases();

    /*
    @Override
    protected JSONObject executeQuery(String sql) {
        Set<DBResult> results = Arrays.stream(getConnections()).
                                       map(conn -> conn.select(sql)).
                                       collect(Collectors.toSet());
        assertThat(StringUtils.format(
            "Found difference between results from different databases when test query [%s]: %s ", sql, results),
            results, hasSize(1));

        try {
            JSONObject response = super.executeQuery(sql);
            return response;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private DBConnection[] getConnections() {
        Node node = getRestClient().getNodes().get(0);
        return new DBConnection[]{
            //new ESConnection(client(), getRestClient()),
            new JDBCConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"),
            new ESConnection("jdbc:elasticsearch://" + node.getHost(), client()),
        };
    }
    */
}
