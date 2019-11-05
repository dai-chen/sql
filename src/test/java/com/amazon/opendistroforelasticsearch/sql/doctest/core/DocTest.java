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
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.Objects;

import static com.amazon.opendistroforelasticsearch.sql.doctest.core.SqlRequest.UrlParam;
import static com.amazon.opendistroforelasticsearch.sql.plugin.RestSqlAction.QUERY_API_ENDPOINT;

/**
 *
 */
public abstract class DocTest extends SQLIntegTestCase {

    private static final String ROOT = "src/test/resources/doctest/"; // TODO: configure for docTest sourceSet

    //@Rule
    //public DocTestRule rule = new DocTestRule();

    private Document document;

    @Override
    protected void init() throws Exception {
        DocTestConfig config = getClass().getAnnotation(DocTestConfig.class);
        for (String data : config.testData()) {
            String indexName = "accounts";
            TestUtils.loadBulk(client(), ROOT + data, indexName);
            ensureGreen(indexName);
        }

        document = new RstDocument(
            TestUtils.getResourceFilePath(ROOT + config.template()),
            TestUtils.getResourceFilePath(ROOT + config.document())
        );
    }

    protected void get(String sql) {
        SqlRequest request = new SqlRequest("GET", QUERY_API_ENDPOINT, "", new UrlParam("sql", sql));

        //document.addExample();

        request.send(getRestClient());
    }

    protected void post(String sql, String... keyValues) {
        Objects.requireNonNull(document);

        String body = String.format("{\n" + "  \"query\": \"%s\"\n" + "}", sql);
        SqlRequest request = new SqlRequest("POST", QUERY_API_ENDPOINT, body);
        SqlResponse response = request.send(getRestClient());

        document.addExample("test", request.toString());
    }

}

class DocTestRule implements MethodRule {

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
