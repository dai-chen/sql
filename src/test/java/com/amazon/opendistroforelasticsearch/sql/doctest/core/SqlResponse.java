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

package com.amazon.opendistroforelasticsearch.sql.doctest.core;

import com.amazon.opendistroforelasticsearch.sql.esintgtest.TestUtils;
import com.amazon.opendistroforelasticsearch.sql.utils.StringUtils;
import org.elasticsearch.client.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class SqlResponse {

    private final Response response;

    public SqlResponse(Response response) {
        this.response = response;
    }

    @Override
    public String toString() {
        JSONObject body = body();
        JSONArray schema = body.getJSONArray("schema");
        JSONArray rows = body.getJSONArray("datarows");

        Object[] header = new Object[schema.length()];
        for (int i = 0; i < header.length; i++) {
            JSONObject nameType = schema.getJSONObject(i);
            header[i] = StringUtils.format("%s (%s)", nameType.get("name"), nameType.get("type"));
        }

        DataTable table = new DataTable(header);
        for (Object row : rows) {
            table.addRow(((JSONArray) row).toList().toArray());
        }
        return table.toString();
    }

    private JSONObject body() {
        try {
            return new JSONObject(TestUtils.getResponseBody(response));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read response body", e);
        }
    }
}
