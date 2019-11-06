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

import com.amazon.opendistroforelasticsearch.sql.doctest.annotation.DocTestConfig;
import com.amazon.opendistroforelasticsearch.sql.esintgtest.SQLIntegTestCase;
import com.amazon.opendistroforelasticsearch.sql.esintgtest.TestUtils;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import static com.amazon.opendistroforelasticsearch.sql.doctest.core.SqlRequest.UrlParam;
import static com.amazon.opendistroforelasticsearch.sql.plugin.RestSqlAction.EXPLAIN_API_ENDPOINT;
import static com.amazon.opendistroforelasticsearch.sql.plugin.RestSqlAction.QUERY_API_ENDPOINT;

/**
 *
 */
public abstract class DocTest extends SQLIntegTestCase {

    private static final String ROOT = "src/test/resources/doctest/"; // TODO: configure for docTest sourceSet

    //@Rule
    //public DocTestRule rule = new DocTestRule();

    @Override
    protected void init() throws Exception {
        DocTestConfig config = getClass().getAnnotation(DocTestConfig.class);
        for (String data : config.testData()) {
            String indexName = "accounts";
            TestUtils.loadBulk(client(), ROOT + data, indexName);
            ensureGreen(indexName);
        }

        String templatePath = TestUtils.getResourceFilePath(ROOT + config.template());
        RstDocument document = new RstDocument(documentPath());
        document.copyFrom(templatePath);
    }

    protected void get(String sql) {
        SqlRequest request = new SqlRequest("GET", QUERY_API_ENDPOINT, "", new UrlParam("sql", sql));

        //document.addExample();

        request.send(getRestClient());
    }

    protected void post(String sql, String... keyValues) {
        String body = String.format("{\n" + "  \"query\": \"%s\"\n" + "}", sql);
        SqlRequest queryReq = new SqlRequest("POST", QUERY_API_ENDPOINT, body);
        SqlResponse queryResp = queryReq.send(getRestClient());

        SqlRequest explainReq = new SqlRequest("POST", EXPLAIN_API_ENDPOINT, sql);
        SqlResponse explainResp = queryReq.send(getRestClient());

        //document.addExample("test", request.toString());
        RstDocument document = new RstDocument(documentPath());
        document.addExample("Test", "test");
    }

    private String documentPath() {
        return TestUtils.getResourceFilePath(ROOT + getClass().getAnnotation(DocTestConfig.class).document());
    }

    private static class DocTestRule implements MethodRule {

        @Override
        public Statement apply(Statement base, FrameworkMethod method, Object target) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    System.out.println("=====" + method.getName());
                    method.invokeExplosively(target);
                }
            };
        }
    }

}

