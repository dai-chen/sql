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

package com.amazon.opendistroforelasticsearch.sql.rewriter.preprocess;

import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.amazon.opendistroforelasticsearch.sql.rewriter.RewriteRule;
import com.amazon.opendistroforelasticsearch.sql.rewriter.subquery.rewriter.SubqueryAliasRewriter;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Pre-process query.
 */
public class QueryPreprocessingRule implements RewriteRule<SQLQueryExpr> {

    /** This constant is meant to avoid confusion when using another boolean in visit method */
    private static final boolean CONTINUE = true;

    @Override
    public boolean match(SQLQueryExpr expr) {
        return isAnyUnAliasedTableInFromClause(expr);
    }

    @Override
    public void rewrite(SQLQueryExpr expr) {
        expr.accept(new SubqueryAliasRewriter());
    }

    private boolean isAnyUnAliasedTableInFromClause(SQLQueryExpr expr) {
        boolean[] isAnyUnAliased = new boolean[]{ false };
        expr.accept(new MySqlASTVisitorAdapter() {
            @Override
            public boolean visit(SQLExprTableSource table) {
                if (isNullOrEmpty(table.getAlias())) {
                    isAnyUnAliased[0] = true;
                    return !CONTINUE;
                }
                return CONTINUE;
            }
        });
        return isAnyUnAliased[0];
    }

}
