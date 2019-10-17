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

package com.amazon.opendistroforelasticsearch.sql.unittest.rewriter.alias;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.amazon.opendistroforelasticsearch.sql.rewriter.alias.FieldNamePrefixRemoveRule;
import com.amazon.opendistroforelasticsearch.sql.util.SqlParserUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for field name prefix remove rule.
 */
public class FieldNamePrefixRemoveRuleTest {

    @Test
    public void queryWithUnAliasedTableNameShouldMatch() {
        query("SELECT account.age FROM accounts").shouldMatchRule();
    }

    @Test
    public void queryWithUnAliasedTableNameInSubQueryShouldMatch() {
        query(
            "SELECT * FROM test t WHERE t.name IN (SELECT accounts.name FROM accounts)"
        ).shouldMatchRule();
    }

    @Test
    public void queryWithoutUnAliasedTableNameShouldNotMatch() {
        query("SELECT a.age FROM accounts a").shouldNotMatchRule();
        query("SELECT accounts.age FROM accounts accounts").shouldNotMatchRule();
        query("SELECT age FROM accounts/_doc a").shouldNotMatchRule();
        query("SELECT age FROM account* a").shouldNotMatchRule();
    }

    @Test
    public void identifierPrefixedByUnAliasedTableNameShouldBeRemoved() {
        query("SELECT accounts.age FROM accounts").
            shouldBeAfterRewrite("SELECT age FROM accounts");
    }

    @Test
    public void identifierPrefixedByUnAliasedTableNameEverywhereShouldBeRemoved() {
        query(
            "SELECT accounts.age, AVG(accounts.salary) FROM accounts WHERE accounts.age > 10 " +
            "GROUP BY accounts.age HAVING AVG(accounts.balance) > 1000 ORDER BY accounts.age"
        ).shouldBeAfterRewrite(
            "SELECT age, AVG(salary) FROM accounts WHERE age > 10 " +
            "GROUP BY age HAVING AVG(balance) > 1000 ORDER BY age"
        );
    }

    private QueryAssertion query(String sql) {
        return new QueryAssertion(sql);
    }

    private static class QueryAssertion {

        private final FieldNamePrefixRemoveRule rule = new FieldNamePrefixRemoveRule();

        private final SQLQueryExpr expr;

        private QueryAssertion(String sql) {
            this.expr = SqlParserUtils.parse(sql);
        }

        public void shouldMatchRule() {
            Assert.assertTrue(match());
        }

        public void shouldNotMatchRule() {
            Assert.assertFalse(match());
        }

        public void shouldBeAfterRewrite(String expected) {
            shouldMatchRule();
            rule.rewrite(expr);
            Assert.assertEquals(
                SQLUtils.toMySqlString(SqlParserUtils.parse(expected)),
                SQLUtils.toMySqlString(expr)
            );
        }

        private boolean match() {
            return rule.match(expr);
        }
    }

}
