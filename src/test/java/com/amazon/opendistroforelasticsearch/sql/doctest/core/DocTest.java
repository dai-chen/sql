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
import com.amazon.opendistroforelasticsearch.sql.doctest.annotation.Section;
import com.amazon.opendistroforelasticsearch.sql.esintgtest.SQLIntegTestCase;
import com.amazon.opendistroforelasticsearch.sql.esintgtest.TestUtils;
import com.amazon.opendistroforelasticsearch.sql.utils.StringUtils;
import org.elasticsearch.test.ESIntegTestCase.ClusterScope;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.amazon.opendistroforelasticsearch.sql.doctest.core.RequestFormat.KIBANA;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.RequestFormat.NO_REQUEST;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.ResponseFormat.NO_RESPONSE;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.ResponseFormat.ORIGINAL;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.ResponseFormat.PRETTY_JSON;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.ResponseFormat.TABLE;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.SqlRequest.UrlParam;
import static com.amazon.opendistroforelasticsearch.sql.plugin.RestSqlAction.EXPLAIN_API_ENDPOINT;
import static com.amazon.opendistroforelasticsearch.sql.plugin.RestSqlAction.QUERY_API_ENDPOINT;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.elasticsearch.test.ESIntegTestCase.Scope;

/**
 * Documentation test base class
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // TODO Doesnt work!?
@ClusterScope(scope= Scope.SUITE, numDataNodes=1, supportsDedicatedMasters=false, transportClientRatio=1)
public abstract class DocTest extends SQLIntegTestCase {

    private static final String ROOT = "src/test/resources/doctest/"; // TODO: configure for docTest sourceSet

    @Override
    protected void init() throws Exception {
        DocTestConfig config = getClass().getAnnotation(DocTestConfig.class);
        loadTestData(config);
        copyTemplateToDocument(config);
    }

    protected void get(String sql) {
        SqlRequest request = new SqlRequest("GET", QUERY_API_ENDPOINT, "",
                                            new UrlParam("sql", sql),
                                            new UrlParam("format", "jdbc"));
        generateDocByQuery(sql, request);
    }

    protected void post(String sql, String... keyValues) { // TODO: pass down
        UrlParam[] params;
        if (keyValues.length == 0) {
            params = new UrlParam[]{ new UrlParam("format", "jdbc") };
        } else {
            params = Arrays.stream(keyValues).map(UrlParam::new).toArray(UrlParam[]::new);
        }

        String body = createBody(sql);
        SqlRequest request = new SqlRequest("POST", QUERY_API_ENDPOINT, body, params);
        generateDocByQuery(sql, request);
    }

    private void loadTestData(DocTestConfig config) {
        for (String data : config.testData()) {
            String indexName = "accounts"; // TODO: parse index name
            try {
                TestUtils.loadBulk(client(), ROOT + data, indexName);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to load test data from", e);
            }
            ensureGreen(indexName);
        }
    }

    private void copyTemplateToDocument(DocTestConfig config) {
        Path templatePath = templatePath(config);
        Path documentPath = documentPath(config);
        try {
            Files.createDirectories(documentPath.getParent());
            Files.copy(templatePath, documentPath, REPLACE_EXISTING, COPY_ATTRIBUTES);
        } catch (IOException e) {
            throw new IllegalStateException(StringUtils.format(
                "Failed to copy from template [%s] to document file [%s]", templatePath, documentPath), e);
        }
    }

    private Path templatePath(DocTestConfig config) {
        return Paths.get(TestUtils.getResourceFilePath(ROOT + "templates/" + config.template()));
    }

    private Path documentPath(DocTestConfig config) {
        String relativePath = config.document();
        if (relativePath.isEmpty()) {
            relativePath = "docs/user/" + config.template();
        }
        return Paths.get(TestUtils.getResourceFilePath(relativePath));
    }

    // -----------------------------------------------------------------
    // Internal DSL

    protected void section(String title, String description, Document.Example... examples) {
        section(title, description, "", examples);
    }

    protected void section(String title, String description, String syntax, Document.Example... examples) {
        DocTestConfig config = getClass().getAnnotation(DocTestConfig.class);
        try (RstDocument document = new RstDocument(documentPath(config))) {
            document.section(title).
                     subSection("Description").
                     paragraph(description);

                if (!syntax.isEmpty()) {
                    document.subSection("Syntax").
                             paragraph(syntax);
                }

                if (examples.length > 0) {
                    document.subSection("Examples");
                    for (Document.Example example : examples) {
                        document.codeBlock("SQL query", example.query).
                                 codeBlock("Explain", example.explainResult).
                                 codeBlock("Result set", example.result);
                    }
                }
        }
    }

    protected Document.Example example(String description, SqlRequest[] request) {
        Document.Example example = new Document.Example();
        example.description = description;
        example.query = KIBANA.format(request[0].request());
        example.result = TABLE.format(request[0].send(getRestClient()).body());
        example.explainQuery = KIBANA.format(request[1].request());
        example.explainResult = PRETTY_JSON.format(request[1].send(getRestClient()).body());
        return example;
    }

    protected String title(String title) {
        return title;
    }

    protected String description(String... sentences) {
        return String.join(" ", sentences);
    }

    protected String syntax(String... sentences) {
        return String.join(" ", sentences);
    }

    protected SqlRequest[] query(String sql, String... keyValues) {
        UrlParam[] params;
        if (keyValues.length == 0) {
            params = new UrlParam[]{ new UrlParam("format", "jdbc") };
        } else {
            params = Arrays.stream(keyValues).map(UrlParam::new).toArray(UrlParam[]::new);
        }

        String body = createBody(sql);
        return new SqlRequest[]{
            new SqlRequest("POST", QUERY_API_ENDPOINT, body, params),
            new SqlRequest("POST", EXPLAIN_API_ENDPOINT, body)
        };
    }

    // -----------------------------------------------------------------

    private String createBody(String sql) {
        return String.format("{\n" + "  \"query\": \"%s\"\n" + "}", sql);
    }

    private void generateDocByQuery(String sql, SqlRequest queryReq)  {
        DocTestConfig config = getClass().getAnnotation(DocTestConfig.class);
        Section sectionAnnotation = section();

        RstDocument document = new RstDocument(documentPath(config));
        Document.Section section = new Document.Section();
        section.title = sectionAnnotation.title();
        section.description = sectionAnnotation.description();

        Document.Example example = new Document.Example();
        if (sectionAnnotation.request() != NO_REQUEST) {
            example.query = sectionAnnotation.request().format(queryReq.request());
            if (sectionAnnotation.response() != NO_RESPONSE) {
                SqlResponse queryResp = queryReq.send(getRestClient());
                example.result = sectionAnnotation.response().format(queryResp.body());
            }
        }

        if (sectionAnnotation.explainRequest() != NO_REQUEST) {
            SqlRequest explainReq = new SqlRequest("POST", EXPLAIN_API_ENDPOINT, createBody(sql));
            example.explainQuery = sectionAnnotation.explainRequest().format(explainReq.request());
            if (sectionAnnotation.explainResponse() != NO_RESPONSE) {
                SqlResponse explainResp = explainReq.send(getRestClient());
                example.explainResult = sectionAnnotation.explainResponse().format(explainResp.body());
            }
        }

        section.examples = new Document.Example[]{ example };
        //document.add(section);
    }

    private Section section() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        try {
            String currentClassName = DocTest.class.getName();
            for (int i = 1; i < elements.length; i++) { // First level is Thread itself
                StackTraceElement element = elements[i];
                if (!element.getClassName().equals(currentClassName)) {
                    Method clazz = Class.forName(element.getClassName()).getDeclaredMethod(element.getMethodName());
                    return clazz.getAnnotation(Section.class);
                }
            }
            throw new IllegalStateException("Failed to find caller other than current class");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to find custom annotation on caller method", e);
        }
    }

}
