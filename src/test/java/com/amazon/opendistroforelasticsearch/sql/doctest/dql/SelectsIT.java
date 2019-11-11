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

package com.amazon.opendistroforelasticsearch.sql.doctest.dql;

import com.amazon.opendistroforelasticsearch.sql.doctest.annotation.DocTestConfig;
import com.amazon.opendistroforelasticsearch.sql.doctest.annotation.Section;
import com.amazon.opendistroforelasticsearch.sql.doctest.core.DocTest;
import org.junit.Test;

@DocTestConfig(
    template = "dql/selects.rst",
    testData = {"testdata/accounts.json"}
)
public class SelectsIT extends DocTest {

    @Test
    public void test1() {
        section(
            title("Select"),
            description(""),
            example(
                description("SELECT and FROM clause are basic part of query to specify which fields from which index to fetch"),
                query("SELECT balance, firstname, lastname FROM accounts")
            ),
            example(
                description(
                    "Optional you can alias index or field name for clarity and rename.",
                    "In SQL standard, full table name can be used if table alias not present. Otherwise use table alias.",
                    "In both cases, table name and table alias are optional."
                ),
                query("SELECT a.balance AS bal, a.firstname AS first, a.lastname AS last FROM accounts a")
            )
        );
    }

    @Section(
        title = "Where",
        description = "WHERE clause can filter out the result set based on conditions"
    )
    public void test3() {
        post("SELECT balance, firstname, lastname FROM accounts WHERE balance > 10000");
    }

    @Section(
        title = "Group By",
        description = "GROUP BY clause can be used to aggregate result of WHERE on some field(s)"
    )
    public void test4() {
        post("SELECT state, AVG(balance) FROM accounts GROUP BY state");
    }

    @Section(
        title = "Having",
        description = "HAVING clause can help filter the result of GROUP BY"
    )
    public void test5() {
        post("SELECT state, AVG(balance) AS avg FROM accounts GROUP BY state HAVING avg > 10000");
    }

}
