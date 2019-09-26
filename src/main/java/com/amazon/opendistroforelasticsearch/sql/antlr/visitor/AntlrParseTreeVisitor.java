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

package com.amazon.opendistroforelasticsearch.sql.antlr.visitor;

import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FunctionArgsContext;
import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.QuerySpecificationContext;
import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParserBaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.AggregateWindowedFunctionContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.AtomTableItemContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.BinaryComparasionPredicateContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.BooleanLiteralContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.ComparisonOperatorContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.ConstantContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.DecimalLiteralContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FromClauseContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FullColumnNameContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FunctionNameBaseContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.RootContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.ScalarFunctionCallContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.SelectElementsContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.SimpleTableNameContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.StringLiteralContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.TableAndTypeNameContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.TableNameContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.UdfFunctionCallContext;

/**
 * ANTLR parse tree visitor to drive the analysis process.
 */
public class AntlrParseTreeVisitor<T extends Reducible> extends OpenDistroSqlParserBaseVisitor<T> {

    private final ParseTreeVisitor<T> visitor;

    public AntlrParseTreeVisitor(ParseTreeVisitor<T> visitor) {
        this.visitor = visitor;
    }

    @Override
    public T visitRoot(RootContext ctx) {
        visitor.visitRoot();
        return super.visitRoot(ctx);
    }

    @Override
    public T visitQuerySpecification(QuerySpecificationContext ctx) {
        visitor.visitQuery();

        // Enforce visit order because ANTLR is generic and unaware.
        // Always visit FROM clause first to define symbols
        visitFromClause(ctx.fromClause());
        visitSelectElements(ctx.selectElements());

        if (ctx.orderByClause() != null) {
            visitOrderByClause(ctx.orderByClause());
        }
        if (ctx.limitClause() != null) {
            visitLimitClause(ctx.limitClause());
        }

        return visitor.endVisitQuery();
    }

    @Override
    public T visitFromClause(FromClauseContext ctx) {
        visitor.visitFrom();
        //visitor.visitWhere();
        super.visitFromClause(ctx);
        return visitor.endVisitFrom();
    }

    /*
    @Override
    public T visitOuterJoin(OuterJoinContext ctx) {
        return super.visitOuterJoin(ctx);
    }

    @Override
    public T visitInnerJoin(InnerJoinContext ctx) {
        return super.visitInnerJoin(ctx);
    }
    */

    @Override
    public T visitAtomTableItem(AtomTableItemContext ctx) {
        Optional<String> alias = Optional.ofNullable(ctx.alias == null ? null : ctx.alias.getText());
        TableNameContext tableName = ctx.tableName();
        if (tableName instanceof SimpleTableNameContext) {
            visitor.visitIndexName(
                ((SimpleTableNameContext) tableName).fullId().getText(), alias
            );
        } else if (tableName instanceof TableAndTypeNameContext) {
            visitor.visitIndexName(
                ((TableAndTypeNameContext) tableName).uid(0).getText(), alias
            );
        } // else TODO: skip all analysis if TableNamePattern
        return defaultResult();
    }

    @Override
    public T visitFullColumnName(FullColumnNameContext ctx) {
        return visitor.visitFieldName(ctx.getText());
    }

    @Override
    public T visitUdfFunctionCall(UdfFunctionCallContext ctx) {
        return visitFunctionCall(visitor.visitFunctionName(ctx.fullId().getText()), ctx.functionArgs());
    }

    // This check should be able to accomplish in grammar
    @Override
    public T visitScalarFunctionCall(ScalarFunctionCallContext ctx) {
        return visitFunctionCall(visit(ctx.scalarFunctionName()), ctx.functionArgs());
    }

    private T visitFunctionCall(T func, ParserRuleContext ctx) {
        List<T> actualArgs;
        if (ctx == null || ctx.children == null) {
            actualArgs = Collections.emptyList();
        } else {
            actualArgs = ctx.children.stream().
                                      map(this::visit).
                                      filter(Objects::nonNull).
                                      collect(Collectors.toList());
        }
        return func.reduce(actualArgs);
    }

    @Override
    public T visitSelectElements(SelectElementsContext ctx) {
        //return visitor.visitSelectClause(() -> {
            super.visitSelectElements(ctx);
        //});
        return defaultResult();
    }

    @Override
    public T visitAggregateWindowedFunction(AggregateWindowedFunctionContext ctx) {
        return visitFunctionCall(
            visitor.visitFunctionName(ctx.getChild(0).getText()),
            ctx.functionArg()
        );
    }

    @Override
    public T visitFunctionNameBase(FunctionNameBaseContext ctx) {
        return visitor.visitFunctionName(ctx.getText());
    }

    @Override
    public T visitComparisonOperator(ComparisonOperatorContext ctx) {
        //return visitor.visitOperatorName(ctx.getText());
        return super.visitComparisonOperator(ctx);
    }

    // Better semantic check example for overloading operator '='
    @Override
    public T visitBinaryComparasionPredicate(BinaryComparasionPredicateContext ctx) {
        //T opType = visit(ctx.comparisonOperator());
        //List<T> actualArgTypes = Arrays.asList(visit(ctx.predicate(0)), visit(ctx.predicate(1)));
        //return opType.aggregate(actualArgTypes);
        return super.visitBinaryComparasionPredicate(ctx);
    }

    @Override
    public T visitConstant(ConstantContext ctx) {
        if (ctx.REAL_LITERAL() != null) {
            return visitor.visitNumber(ctx.getText());
        }
        return super.visitConstant(ctx);
    }

    @Override
    public T visitStringLiteral(StringLiteralContext ctx) {
        return visitor.visitString(ctx.getText());
    }

    @Override
    public T visitDecimalLiteral(DecimalLiteralContext ctx) {
        return visitor.visitNumber(ctx.getText());
    }

    @Override
    public T visitBooleanLiteral(BooleanLiteralContext ctx) {
        return visitor.visitBoolean(ctx.getText());
    }

    @Override
    protected T defaultResult() {
        return visitor.defaultValue();
    }

    @Override
    protected T aggregateResult(T aggregate, T nextResult) {
        if (nextResult != null) { // should call Attribute method for synthesis
            return nextResult;
        }
        return aggregate;
    }

}
