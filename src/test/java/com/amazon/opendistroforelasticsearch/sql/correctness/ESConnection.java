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

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ESConnection implements DBConnection {

    private final DBConnection connection;
    private final Client client;
    //private final RestClient restClient;

    public ESConnection(String connectionUrl, Client client) {
        this.connection = new JDBCConnection("Elasticsearch", connectionUrl);
        this.client = client;
    }

    @Override
    public void create(String tableName, String schema) {
        CreateIndexResponse resp = client.admin().indices().create(
            new CreateIndexRequest(tableName).mapping("_doc", schema, XContentType.JSON)
        ).actionGet();

        if (!resp.isAcknowledged()) {
            throw new IllegalStateException("Failed to create index [" + tableName + "]");
        }
    }

    @Override
    public void insert(String tableName, String[] columnNames, List<String[]> batch) {
        BulkRequestBuilder bulkReq = client.prepareBulk();
        for (String[] fieldValues : batch) {
            try {
                XContentBuilder json = jsonBuilder().startObject();
                for (int i = 0; i < columnNames.length; i++) {
                    json.field(columnNames[i], fieldValues[i]);
                }
                bulkReq.add(client.prepareIndex(tableName, "_doc").setSource(json.endObject()));

            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        BulkResponse bulkResp = bulkReq.get();
        if (bulkResp.hasFailures()) {
            throw new IllegalStateException(bulkResp.buildFailureMessage());
        }
    }

    @Override
    public DBResult select(String query) {
        /*
        try {
            String endpoint = "/_opendistro/_sql?format=jdbc";
            String requestBody = makeRequest(query);

            Request sqlRequest = new Request("POST", endpoint);
            sqlRequest.setJsonEntity(requestBody);

            Response response = restClient.performRequest(sqlRequest);
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            String body = TestUtils.getResponseBody(response, true);

            JSONObject json = new JSONObject(body);
            JSONArray schema = json.getJSONArray("schema");
            List<String> names = StreamSupport.stream(schema.spliterator(), false).
                                               map(nameType -> ((JSONObject) nameType).getString("name")).
                                               collect(Collectors.toList());
            DBResult result = new DBResult(new Row(names), new HashSet<>());
            JSONArray dataRows = json.getJSONArray("datarows");
            for (Object row : dataRows) {
                result.addRow(new Row(((JSONArray) row).toList()));
            }
            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
        return connection.select(query);
    }

    private String makeRequest(String query) {
        return String.format("{\n" +
            "  \"query\": \"%s\"\n" +
            "}", query);
    }

}
