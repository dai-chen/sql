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

import com.amazon.opendistroforelasticsearch.sql.esintgtest.SQLIntegTestCase;
import com.google.common.io.Resources;
import org.elasticsearch.client.Node;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test cases for Tableau integration testing
 */
public class TableauIT extends SQLIntegTestCase {

    @Test
    public void testIntegrationWithTableau() {
        //iterateFileByLine("correctness/tableau_integration_tests.txt", this::executeQuery);
        DBConnection[] connections = getDBConnections();
        CorrectnessTestCase correctnessTest = new CorrectnessTestCase(connections);
        correctnessTest.initialize("kibana_sample_data_flights.json", "kibana_sample_data_flights.csv");
        TestReport report = correctnessTest.verify(readTestQueriesInFile("correctness/tableau_integration_tests_full.txt"));
        correctnessTest.report(report);
    }

    private List<String> readTestQueriesInFile(String filePath) {
        try {
            URL url = Resources.getResource(filePath);
            return Files.lines(Paths.get(url.toURI())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private DBConnection[] getDBConnections() {
        Node node = getRestClient().getNodes().get(0);
        return new DBConnection[]{
            new ESConnection("jdbc:elasticsearch://" + node.getHost(), client()),
            new JDBCConnection("H2", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"),
            new JDBCConnection("SQLite", "jdbc:sqlite:memory:myDb"),
            //new JDBCConnection("Apache Derby", "jdbc:derby:memory:myDb;create=true"),
        };
    }
}
