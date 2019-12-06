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
import com.amazon.opendistroforelasticsearch.sql.utils.StringUtils;
import org.elasticsearch.client.Node;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;

/**
 * Correctness test base class that intercepts query method to perform more testing by comparing with multiple databases.
 */
public abstract class CorrectnessTestCase extends SQLIntegTestCase {

    protected void prepareTableAndData(String schemaFile, String dataFile) throws Exception {
        TestData testData = new TestData(schemaFile, dataFile);
        for (DBConnection conn : getConnections()) {
            testData.create(conn);
            testData.loadData(conn);
        }
    }

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

}
