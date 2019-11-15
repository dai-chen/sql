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

import com.amazon.opendistroforelasticsearch.sql.doctest.core.dsl.DocBuilder;
import com.amazon.opendistroforelasticsearch.sql.doctest.core.dsl.DocTestConfig;
import com.amazon.opendistroforelasticsearch.sql.doctest.core.dsl.Section;
import com.amazon.opendistroforelasticsearch.sql.doctest.core.markup.Document;
import com.amazon.opendistroforelasticsearch.sql.esintgtest.SQLIntegTestCase;
import com.carrotsearch.randomizedtesting.AnnotatedMethodProvider;
import com.carrotsearch.randomizedtesting.TestMethodAndParams;
import com.carrotsearch.randomizedtesting.annotations.TestCaseOrdering;
import com.carrotsearch.randomizedtesting.annotations.TestMethodProviders;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.test.ESIntegTestCase.ClusterScope;

import java.nio.file.Path;
import java.util.Comparator;

import static org.elasticsearch.test.ESIntegTestCase.Scope.SUITE;

/**
 * Documentation test base class
 */
@TestMethodProviders({DocTest.SectionMethod.class})
@TestCaseOrdering(value = DocTest.SectionOrder.class)
@ClusterScope(scope= SUITE, numDataNodes=1, supportsDedicatedMasters=false, transportClientRatio=1)
public abstract class DocTest extends SQLIntegTestCase implements DocBuilder {

    @Override
    protected void init() throws Exception {
        DocTestConfig config = getClass().getAnnotation(DocTestConfig.class);
        loadTestData(config);
        copyTemplateToDocument(config);
    }

    @Override
    public RestClient restClient() {
        return getRestClient();
    }

    private void loadTestData(DocTestConfig config) {
        String[] testFilePaths = config.testData();
        TestData testData = new TestData(testFilePaths);
        testData.loadToES(this);
    }

    private void copyTemplateToDocument(DocTestConfig config) {
        Path docPath = Document.path(config.template());
        Template template = new Template(config.template());
        template.copyToDocument(docPath);
    }

    public static class SectionMethod extends AnnotatedMethodProvider {
        public SectionMethod() {
            super(Section.class);
        }
    }

    public static class SectionOrder implements Comparator<TestMethodAndParams> {
        @Override
        public int compare(TestMethodAndParams method1, TestMethodAndParams method2) {
            return Integer.compare(order(method1), order(method2));
        }

        private int order(TestMethodAndParams method) {
            Section section = method.getTestMethod().getAnnotation(Section.class);
            return section.value();
        }
    }

}
