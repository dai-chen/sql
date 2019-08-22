package com.amazon.opendistroforelasticsearch.sql.antlr.semantic;

import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.QuerySpecificationContext;
import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParserBaseVisitor;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.ScalarFunctionType;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.Type;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.TypeExpression;
import com.amazon.opendistroforelasticsearch.sql.esdomain.LocalClusterState;

import java.util.ArrayList;
import java.util.List;

import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.BinaryComparasionPredicateContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.ComparisonOperatorContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.DecimalLiteralContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FromClauseContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FullColumnNameContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FunctionArgsContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FunctionNameBaseContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.ScalarFunctionCallContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.StringLiteralContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.TableNameContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.UidContext;

/**
 *  Semantic analysis
 */
public class OpenDistroSemanticAnalyzer extends OpenDistroSqlParserBaseVisitor<Type> {

    /** Original sql query for troubleshooting information */
    private final String sql;

    private final GenericSemanticAnalyzer analyzer = new GenericSemanticAnalyzer();

    public OpenDistroSemanticAnalyzer(String sql) {
        this.sql = sql;
    }

    @Override
    public Type visitQuerySpecification(QuerySpecificationContext ctx) {
        return analyzer.visitQuery(() -> {

            // Always visit FROM clause first to define symbols
            visit(ctx.fromClause());

            for (int i = 0; i < ctx.getChildCount(); i++) {
                if (ctx.getChild(i) != ctx.fromClause()) {
                    visit(ctx.getChild(i));
                }
            }
        });
    }

    @Override
    public Type visitFromClause(FromClauseContext ctx) {
        return analyzer.visitWhereClause(() -> {
            super.visitFromClause(ctx);
        });
    }

    @Override
    public Type visitTableName(TableNameContext ctx) {
        String indexName = getTextFrom(ctx.fullId().uid(0));
        return analyzer.visitIndexName(LocalClusterState.state(), indexName);
    }

    @Override
    public Type visitFullColumnName(FullColumnNameContext ctx) {
        return analyzer.visitFieldName(getTextFrom(ctx.uid()));
    }

    // This check should be able to accomplish in grammar
    @Override
    public Type visitScalarFunctionCall(ScalarFunctionCallContext ctx) {
        TypeExpression funcType = (TypeExpression) visit(ctx.scalarFunctionName());
        TypeExpression actualArgTypes = (TypeExpression) visit(ctx.functionArgs());
        return analyzer.visitFunctionCall(funcType, actualArgTypes);
    }

    @Override
    public Type visitFunctionNameBase(FunctionNameBaseContext ctx) {
        return analyzer.visitFunctionName(ctx.getText());
    }

    @Override
    public Type visitComparisonOperator(ComparisonOperatorContext ctx) {
        return analyzer.visitOperatorName(ctx.getText());
    }

    @Override
    public Type visitFunctionArgs(FunctionArgsContext ctx) {
        List<Type> types = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            Type type = visit(ctx.getChild(i));
            if (type != null) {
                types.add(type);
            }
        }
        return TypeExpression.of(types.toArray(new Type[0]));
    }

    // Better semantic check example for overloading operator '='
    @Override
    public Type visitBinaryComparasionPredicate(BinaryComparasionPredicateContext ctx) {
        TypeExpression opType = (TypeExpression) visit(ctx.comparisonOperator());
        TypeExpression actualArgTypes = TypeExpression.of(visit(ctx.predicate(0)), visit(ctx.predicate(1)));
        return analyzer.visitFunctionCall(opType, actualArgTypes);
    }

    @Override
    public Type visitStringLiteral(StringLiteralContext ctx) {
        return analyzer.visitString(ctx.getText());
    }

    @Override
    public Type visitDecimalLiteral(DecimalLiteralContext ctx) {
        return analyzer.visitNumber(ctx.getText());
    }

    @Override
    protected Type defaultResult() {
        return null;
    }

    @Override
    protected Type aggregateResult(Type aggregate, Type nextResult) {
        if (nextResult != null) { // should call Attribute method for synthesis
            return nextResult;
        }
        return aggregate;
    }

    private String getTextFrom(UidContext uid) {
        return uid.simpleId().ID().getText(); // NPE possible when ID() = null
    }

}
