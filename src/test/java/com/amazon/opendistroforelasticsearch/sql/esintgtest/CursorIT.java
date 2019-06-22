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

package com.amazon.opendistroforelasticsearch.sql.esintgtest;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.amazon.opendistroforelasticsearch.sql.esintgtest.CursorIT.Request.body;
import static com.amazon.opendistroforelasticsearch.sql.esintgtest.CursorIT.Request.cursor;
import static com.amazon.opendistroforelasticsearch.sql.esintgtest.CursorIT.Request.fetchSize;
import static com.amazon.opendistroforelasticsearch.sql.esintgtest.CursorIT.Request.format;
import static com.amazon.opendistroforelasticsearch.sql.esintgtest.CursorIT.Request.params;
import static com.amazon.opendistroforelasticsearch.sql.esintgtest.CursorIT.Request.query;
import static com.amazon.opendistroforelasticsearch.sql.esintgtest.CursorIT.Request.request;
import static com.amazon.opendistroforelasticsearch.sql.esintgtest.SQLIntegTestCase.Index.ACCOUNT;
import static com.amazon.opendistroforelasticsearch.sql.esintgtest.SQLIntegTestCase.Index.PEOPLE;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toMap;

/**
 * Integration test for cursor support
 */
public class CursorIT extends SQLIntegTestCase {

    @Override
    public void init() throws Exception {
        loadIndex(ACCOUNT);
    }

    @Test
    public void select() {
        compareResultWithAndWithoutCursor(
            "SELECT firstname, lastname, age FROM accounts"
        );
    }

    @Ignore
    @Test
    public void groupBy() {
        // Scroll doesn't support aggregation
    }

    @Ignore
    @Test
    public void join() {
    }

    private void compareResultWithAndWithoutCursor(String query) {
        assertEquals(
            queryWithCursor(query, 10),
            queryWithoutCursor(query)
        );
    }

    private Set<Object> queryWithCursor(String sql, int size) {
        Set<Object> result = new HashSet<>();

        // First request
        Result page = execute(
            request(
                body(
                    query(sql),
                    fetchSize(size)
                ),
                params(format("jdbc"))
            )
        );

        while (!page.isEmpty()) {
            result.addAll(page.rows);

            // Following requests
            page = execute(
                request(
                    body(
                        cursor(page.cursor)
                    ),
                    params(format("jdbc"))
                )
            );
        }
        return result;
    }

    /** Hack to call non-static executeRequest() */
    private Result execute(Request request) {
        try {
            return request.execute(this);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private Set<Object> queryWithoutCursor(String sql) {
        return execute(
            request(
                body(
                    query(sql)
                )
            )
        ).rows;
    }

    /**
     * Request body builder to build request body in readable DSL way.
     * And execute request built to get the result.
     *
     *  request
     *    body(
     *      query("SELECT... "),
     *      fetchSize(5)
     *    ),
     *    param(
     *      format("jdbc")
     *    )
     *  )
     */
    static class Request {

        private final String body;
        private final Map<String, String> params;

        Request(String body, Map<String, String> params) {
            this.body = body;
            this.params = params;
        }

        public static Request request(String body) {
            return request(body, emptyMap());
        }

        public static Request request(String body, Map<String, String> params) {
            return new Request(body, params);
        }

        public static String body(Field... fields) {
            return Arrays.stream(fields).
                map(f -> f.name + ": " + f.value).
                collect(Collectors.joining(",", "{", "}"));
        }

        public static Map<String, String> params(Param... params) {
            return Arrays.stream(params).
                collect(toMap(p -> p.key, p -> p.value));
        }

        public static Param format(String format) {
            return new Param("format", format);
        }

        public static Field query(String query) {
            return new Field("query", query);
        }

        public static Field fetchSize(int fetchSize) {
            return new Field("fetch_size", fetchSize);
        }

        public static Field cursor(String cursor) {
            return new Field("cursor", cursor);
        }

        public Result execute(SQLIntegTestCase test) throws IOException {
            JSONObject json = test.executeRequest(body, params);
            Set<Object> rows = new HashSet<>(json.getJSONArray("dataRows").toList());
            String cursor = json.optString("cursor");
            return new Result(rows, cursor);
        }

        static class Field {
            private String name;
            private Object value;

            Field(String name, Object value) {
                this.name = name;
                this.value = value;
            }
        }

        static class Param {
            private String key;
            private String value;

            Param(String key, String value) {
                this.key = key;
                this.value = value;
            }

            public String getKey() {
                return key;
            }

        }
    }

    static class Result {
        private final Set<Object> rows;
        private final String cursor;

        Result(Set<Object> rows, String cursor) {
            this.rows = rows;
            this.cursor = cursor;
        }

        boolean isEmpty() {
            return rows.isEmpty();
        }
    }

}
