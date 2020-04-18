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

package com.amazon.opendistroforelasticsearch.sql.sql;

import com.amazon.opendistroforelasticsearch.sql.sql.antlr.parser.OpenDistroSqlParserBaseVisitor;

import static com.amazon.opendistroforelasticsearch.sql.sql.antlr.parser.OpenDistroSqlParser.SelectElementContext;
import static com.amazon.opendistroforelasticsearch.sql.sql.antlr.parser.OpenDistroSqlParser.SelectElementsContext;
import static com.amazon.opendistroforelasticsearch.sql.sql.antlr.parser.OpenDistroSqlParser.UidContext;

/**
 * Fake query engine to demonstrate how new SQL frontend interacts with backend.
 * For simplicity, rely on java.lang.Object for base type and type check.
 */
public class ASTBuilder extends OpenDistroSqlParserBaseVisitor<Projection> {

    @Override
    public Projection visitSelectElements(SelectElementsContext ctx) {
        Projection projection = new Projection();
        for (SelectElementContext element : ctx.selectElement()) {
            projection.add(
                getAlias(element),
                new Expression(element.expr.getText())
            );
        }
        return projection;
    }

    private String getAlias(SelectElementContext ctx) {
        UidContext alias = ctx.alias;
        if (alias == null) {
            return ctx.expr.getText();
        }
        return alias.getText();
    }

    @Override
    protected Projection aggregateResult(Projection aggregate, Projection nextResult) {
        return nextResult != null ? nextResult : aggregate;
    }
}
