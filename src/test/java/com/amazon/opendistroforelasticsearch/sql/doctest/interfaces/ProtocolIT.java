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

import static com.amazon.opendistroforelasticsearch.sql.doctest.core.RequestFormat.NO_REQUEST;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.ResponseFormat.ORIGINAL;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.ResponseFormat.PRETTY_JSON;

@DocTestConfig(
    template = "interfaces/protocol.rst",
    testData = {"testdata/accounts.json"}
)
public class ProtocolIT extends DocTest {

    @Section(
        title = "DSL Format",
        description = "By default the plugin returns original response from Elasticsearch engine in JSON",
        response = PRETTY_JSON,
        explainRequest = NO_REQUEST
    )
    public void test1() {
        post("SELECT firstname, lastname, age, city FROM accounts LIMIT 2"); //TODO how to pass default format
    }

    @Section(
        title = "JDBC Format",
        description = "JDBC format is provided for JDBC driver or client side that needs schema and data formatted in table",
        response = PRETTY_JSON,
        explainRequest = NO_REQUEST
    )
    public void test2() {
        post("SELECT firstname, lastname, age, city FROM accounts LIMIT 2", "format=jdbc");
    }

    @Section(
        title = "CSV Format",
        description = "And you can also use CSV format to download result set in csv format",
        response = ORIGINAL,
        explainRequest = NO_REQUEST
    )
    public void test3() {
        post("SELECT firstname, lastname, age, city FROM accounts", "format=csv");
    }

    @Section(
        title = "RAW Format",
        description = "Additionally you can also use RAW format to pipe the result with other command line tool",
        response = ORIGINAL,
        explainRequest = NO_REQUEST
    )
    public void test4() {
        post("SELECT firstname, lastname, age, city FROM accounts", "format=raw");
    }

}
