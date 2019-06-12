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

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.amazon.opendistroforelasticsearch.sql.esintgtest.SQLIntegTestCase.Index.ACCOUNT;
import static com.amazon.opendistroforelasticsearch.sql.esintgtest.SQLIntegTestCase.Index.PEOPLE;

/**
 * Integration test for cursor support
 */
@Ignore
public class CursorIT extends SQLIntegTestCase {

    @Override
    public void init() throws Exception {
        loadIndex(ACCOUNT);
        loadIndex(PEOPLE);
    }

    @Test
    public void select() {
        compareResultWithAndWithoutCursor(
            "SELECT firstname, lastname, age FROM accounts"
        );
    }

    @Test
    public void groupBy() {

    }

    @Test
    public void join() {

    }

    private void compareResultWithAndWithoutCursor(String query) {
        assertEquals(
            queryWithCursor(query),
            queryWithoutCursor(query)
        );
    }

    private Set<Object> queryWithCursor(String query) {
        return null;
    }

    private Set<Object> queryWithoutCursor(String query) {
        try {
            JSONObject json = executeQuery(query);
            JSONArray dataRows = json.getJSONArray("dataRows");
            return new HashSet<>(dataRows.toList());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }


}
