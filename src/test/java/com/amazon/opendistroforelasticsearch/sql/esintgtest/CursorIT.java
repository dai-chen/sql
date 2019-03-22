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

import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.test.ESIntegTestCase;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.util.Assert;

import java.util.Set;

/**
 *
 */
@ESIntegTestCase.SuiteScopeTestCase
@ThreadLeakScope(ThreadLeakScope.Scope.NONE)
@Ignore
public class CursorIT extends SQLIntegTestCase {

    @Override
    public void setupSuiteScopeCluster() throws Exception {
        AdminClient adminClient = this.admin();
        Client esClient = ESIntegTestCase.client();
        loadAccountIndex(adminClient, esClient);
        loadPeopleIndex(adminClient, esClient);
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

    //TODO: jdbc, raw ...

    private void compareResultWithAndWithoutCursor(String query) {
        Assert.equals(
            queryWithCursor(query),
            queryWithoutCursor(query)
        );
    }

    private JSONObject queryWithCursor(String query) {
        return null;
    }

    private JSONObject queryWithoutCursor(String query) {
        return null;
    }

}
