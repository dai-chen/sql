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
import com.amazon.opendistroforelasticsearch.sql.esintgtest.SQLIntegTestCase;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;

public class ContinuousIT extends SQLIntegTestCase {

    @Override
    protected void init() throws Exception {
        TestData testData = new TestData("kibana_sample_data_flights.json", "kibana_sample_data_flights.csv");
        for (DBConnection conn : getConnections()) {
            testData.create(conn);
            testData.loadData(conn);
        }
    }

    @Test
    public void test() {
        String[] testQueries = new String[]{
            "SELECT Carrier FROM kibana_sample_data_flights",
        };

        for (String query : testQueries) {
            Set<DBResult> results = Arrays.stream(getConnections()).
                                            map(conn -> conn.select(query)).
                                            collect(Collectors.toSet());
            assertThat(results, hasSize(1));
        }

        /*
        executeQuery("SELECT * FROM kibana_sample_data_flights");

        //Connection conn = DriverManager.getConnection(url, properties);
        Connection conn = getDBConnection();
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM kibana_sample_data_flights");
        while (result.next()) {
            System.out.println(result.getInt("age"));
        }
        */
    }

    private DBConnection[] getConnections() {
        return new DBConnection[]{
            new ESConnection(client(), getRestClient()),
            new JDBCConnection(getDBConnection()),
        };
    }

    private String getAddress() {
        String clusterAddresses = System.getProperty(TESTS_CLUSTER);
        return clusterAddresses.split(",")[0];
    }

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    private static Connection getDBConnection() {
        try {
            Class.forName(DB_DRIVER);
            return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /*
    This won't work because JUnit 4 only cares about Method instance rather than class instance.
    public static class DynamicTestCases implements TestMethodProvider {
        @Override
        public Collection<Method> getTestMethods(Class<?> suiteClass, ClassModel suiteClassModel) {
            SQLESTestCaseIT testCase = new SQLESTestCaseIT() {
                @Override
                public void test() {
                    System.out.println("hello");
                }
            };

            try {
                Method testMethod = testCase.getClass().getDeclaredMethod("test");
                return Collections.singleton(testMethod);

            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("e");
            }

        }
    }
    */

}
