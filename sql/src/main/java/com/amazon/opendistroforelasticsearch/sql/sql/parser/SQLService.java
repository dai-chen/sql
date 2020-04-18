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

package com.amazon.opendistroforelasticsearch.sql.sql.parser;

import com.amazon.opendistroforelasticsearch.sql.sql.engine.Projection;
import com.amazon.opendistroforelasticsearch.sql.sql.engine.Tuple;
import com.amazon.opendistroforelasticsearch.sql.sql.request.SQLQueryRequest;
import com.amazon.opendistroforelasticsearch.sql.sql.response.SQLQueryResponse;
import com.amazon.opendistroforelasticsearch.sql.sql.response.Schema;
import org.json.JSONObject;

/**
 * Orch...
 */
public class SQLService {

    public String execute(SQLQueryRequest request) {
        try {
            Projection ast = new SQLParser().parse(request.getSqlQuery());
            return planAndExecute(ast);
        } catch (SQLSyntaxError error) {
            return null;
        }
    }

    private String planAndExecute(Projection ast) {
        Tuple result = ast.project(new Tuple());

        Schema[] schemas = new Schema[result.size()];
        Object[][] dataRows = { new Object[result.size()] };

        result.iterate((i, entry) -> {
            schemas[i] = new Schema(entry.getKey(), getType(entry.getValue()));
            dataRows[0][i] = entry.getValue();
        });

        SQLQueryResponse response = new SQLQueryResponse(1, 1, 200, schemas, dataRows);
        JSONObject json = new JSONObject(response);
        return json.toString(2);
    }

    private String getType(Object value) {
        return value.getClass().getSimpleName();
    }
}
