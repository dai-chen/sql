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
import org.junit.Ignore;

@DocTestConfig(
    template = "config/endpoint.rst",
    testData = {"testdata/accounts.json"}
)
public class EndpointIT extends DocTest {

    @Ignore
    @Section(
        title = "GET Request",
        description = "You can post request with your query in request body"
    )
    public void testUseRequestParameterToAccessSQLPlugin() {
        get("SELECT * FROM accounts");
    }

    @Section(
        title = "GET Request",
        description = "You can post request with your query in request body"
    )
    public void testUseRequestBodyToAccessSQLPlugin() {
        post("SELECT * FROM accounts");
    }

}
