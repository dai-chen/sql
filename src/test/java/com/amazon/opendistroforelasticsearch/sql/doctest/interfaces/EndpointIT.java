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

package com.amazon.opendistroforelasticsearch.sql.doctest.interfaces;

import com.amazon.opendistroforelasticsearch.sql.doctest.annotation.DocTestConfig;
import com.amazon.opendistroforelasticsearch.sql.doctest.annotation.Section;
import com.amazon.opendistroforelasticsearch.sql.doctest.core.DocTest;

import static com.amazon.opendistroforelasticsearch.sql.doctest.core.RequestFormat.CURL;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.RequestFormat.NO_REQUEST;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.ResponseFormat.NO_RESPONSE;

@DocTestConfig(
    template = "interfaces/endpoint.rst",
    testData = {"testdata/accounts.json"}
)
public class EndpointIT extends DocTest {

    @Section(
        title = "GET Request",
        description = "You can send HTTP GET request with your query embedded in URL",
        request = CURL,
        response = NO_RESPONSE,
        explainRequest = NO_REQUEST
    )
    public void test1() {
        get("SELECT * FROM accounts");
    }

    @Section(
        title = "POST Request",
        description = "You can also send HTTP POST request with your query in request body " +
            "and explain it to Elasticsearch domain specific language (DSL) in JSON",
        request = CURL,
        response = NO_RESPONSE,
        explainRequest = NO_REQUEST
    )
    public void test2() {
        post("SELECT * FROM accounts");
    }

    @Section(
        title = "Explain Query",
        description =
            "You can check out the translation of your query by explain endpoint. " +
            "The explain output is Elasticsearch domain specific language (DSL) in JSON format",
        request = NO_REQUEST,
        explainRequest = CURL
    )
    public void test3() {
        post("SELECT firstname, lastname FROM accounts WHERE balance > 10000");
    }

}
