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

package com.amazon.opendistroforelasticsearch.sql.unittest;

import com.amazon.opendistroforelasticsearch.sql.context.QueryContextManager;
import com.amazon.opendistroforelasticsearch.sql.request.SqlRequest;
import org.json.JSONObject;

/**
 * Cursor support unit test
 */
public class CursorTest {

    private QueryContextManager manager = new QueryContextManager();

    public void selectWithCursorLocally() {
        //manager.get(request("SELECT firstname FROM bank", ))
    }

    public void selectWithCursorRemotely() {
    }

    public void groupByWithCursorLocally() {
    }

    public void groupByWithCursorRemotely() {
    }

    public void joinWithCursorLocally() {
    }

    public void joinWithCursorRemotely() {
    }

    private SqlRequest request(String sql, int fetchSize) {
        return request(sql, new JSONObject("{\"fetch_size\":" + fetchSize + "}"));
    }

    private SqlRequest request(String sql, String cursor) {
        return request(sql, new JSONObject("{\"cursor\":" + cursor + "}"));
    }

    private SqlRequest request(String sql, JSONObject json) {
        return new SqlRequest(sql, json);
    }

}
