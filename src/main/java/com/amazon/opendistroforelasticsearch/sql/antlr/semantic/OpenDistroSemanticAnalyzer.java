package com.amazon.opendistroforelasticsearch.sql.antlr.semantic;

import com.amazon.opendistroforelasticsearch.sql.antlr.StringSimilarity;
import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser;
import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.QuerySpecificationContext;
import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParserBaseVisitor;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.scope.Environment;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.ScalarFunctionType;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.Type;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.TypeExpression;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import static com.amazon.opendistroforelasticsearch.sql.antlr.SqlAnalysisExceptionBuilder.semanticException;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.*;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.AggregateWindowedFunctionContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.BinaryComparasionPredicateContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.DecimalLiteralContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FromClauseContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FullColumnNameContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FunctionArgsContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.FunctionNameBaseContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.ScalarFunctionCallContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.StringLiteralContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.TableNameContext;
import static com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser.UidContext;
import static com.amazon.opendistroforelasticsearch.sql.esdomain.LocalClusterState.FieldMappings;
import static com.amazon.opendistroforelasticsearch.sql.esdomain.LocalClusterState.IndexMappings;
import static com.amazon.opendistroforelasticsearch.sql.esdomain.LocalClusterState.state;

/**
 *  Semantic analysis
 */
public class OpenDistroSemanticAnalyzer extends OpenDistroSqlParserBaseVisitor<Type> {

    /** Original sql query for troubleshooting information */
    private final String sql;

    /** Environment stack for symbol scope management */
    private Environment<String, Type> currentEnv = new Environment<>(null);


    public OpenDistroSemanticAnalyzer(String sql) {
        this.sql = sql;
    }

    @Override
    public Type visitQuerySpecification(QuerySpecificationContext ctx) {
        return visitInNewEnvironment(() -> {
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
    public Type visitTableName(TableNameContext ctx) {
        String indexName = getTextFrom(ctx.fullId().uid(0));
        IndexMappings indexMappings = state().getFieldMappings(new String[]{ indexName });
        FieldMappings mappings = indexMappings.firstMapping().firstMapping();
        mappings.data().forEach(
            (fieldName, mapping) -> currentEnv.define(fieldName, BaseType.typeIn(mapping)) //TODO: table alias and undefined type in our system
        );
        return defaultResult();
    }

    @Override
    public Type visitFullColumnName(FullColumnNameContext ctx) {
        String fieldName = getTextFrom(ctx.uid());
        Optional<Type> type = currentEnv.resolve(fieldName);
        if (!type.isPresent()) {
            List<String> suggestedWords = new StringSimilarity(currentEnv.allSymbols()).similarTo(fieldName);
            throw semanticException("Field [%s] cannot be found.", fieldName).
                at(sql, ctx).suggestion("Did you mean [%s]?", String.join(", ", suggestedWords)).build();
        }
        return type.get();
    }

    // This check should be able to accomplish in grammar
    @Override
    public Type visitScalarFunctionCall(ScalarFunctionCallContext ctx) {
        TypeExpression funcSpec = (TypeExpression) visit(ctx.scalarFunctionName());
        Type realArgTypes = visit(ctx.functionArgs());
        if (!funcSpec.isCompatible(realArgTypes)) {
            throw semanticException(
                "Function [%s] can only work with %s instead of [%s].",
                ctx.scalarFunctionName().getText(), Arrays.toString(funcSpec.inputTypes()), realArgTypes
            ).at(sql, ctx).suggestion("Usage: %s.", funcSpec).build();
        }
        return funcSpec.outputType();
    }

    @Override
    public Type visitFunctionNameBase(FunctionNameBaseContext ctx) {
        return visitFunctionName(ctx, ctx.getText());
    }

    @Override
    public Type visitAggregateWindowedFunction(AggregateWindowedFunctionContext ctx) {
        return visitFunctionName(ctx, ctx.MAX().getText());
    }

    private Type visitFunctionName(ParserRuleContext ctx, String funcName) {
        Optional<Type> type = currentEnv.resolve(funcName);
        if (!type.isPresent()) {
            throw semanticException("Function [%s] can not be used here.", funcName).
                at(sql, ctx).suggestion("You could use the following functions: [%s]", currentEnv.allSymbols()).build();
        }
        return type.get();
    }

    @Override
    public Type visitFunctionArgs(FunctionArgsContext ctx) {
        Type[] types = new Type[ctx.getChildCount()];
        for (int i = 0; i < types.length; i++) {
            types[i] = visit(ctx.getChild(i));
        }
        return TypeExpression.of(types);
    }

    // Better semantic check example for overloading operator '='
    @Override
    public Type visitBinaryComparasionPredicate(BinaryComparasionPredicateContext ctx) {
        String op = ctx.comparisonOperator().getText();
        Type leftType = visit(ctx.predicate(0));
        Type rightType = visit(ctx.predicate(1));
        if (!leftType.isCompatible(rightType)) {
            throw semanticException(
                "Type of left side [%s] and right side [%s] are not compatible for operator ['%s'].",
                leftType, rightType, op
            ).at(sql, ctx).build();
        }
        return leftType;
    }

    /*
    @Override
    public Type visitSubstringFunctionCall(SubstringFunctionCallContext ctx) {
        TypeExpression funcSpec = (TypeExpression) currentEnv.resolve("SUBSTRING");
        Type realArgTypes = visit(ctx.);
        if (!funcSpec.isCompatible(realArgTypes)) {
            throw semanticException(
                "Function [%s] can only work with [%s] instead of [%s].",
                ctx.scalarFunctionName().getText(), funcSpec.inputTypes(), realArgTypes
            ).at(sql, ctx).suggestion("Usage: %s.", funcSpec).build();
        }
        return funcSpec.outputType();
    }
    */

    @Override
    public Type visitFromClause(FromClauseContext ctx) {
        return visitInNewEnvironment(() -> {
            for (ScalarFunctionType type : ScalarFunctionType.values()) {
                currentEnv.define(type.name(), type);
            }
            return super.visitFromClause(ctx);
        });
    }

    @Override
    public Type visitStringLiteral(StringLiteralContext ctx) {
        return BaseType.STRING;
    }

    @Override
    public Type visitDecimalLiteral(DecimalLiteralContext ctx) {
        return BaseType.NUMBER;
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

    private Type visitInNewEnvironment(Runnable visit) {
        return visitInNewEnvironment(() -> {
            visit.run();
            return defaultResult();
        });
    }

    private Type visitInNewEnvironment(Callable<Type> visit) {
        Environment<String, Type> parent = currentEnv;
        currentEnv = new Environment<>(currentEnv);

        Type type;
        try {
            type = visit.call();
        } catch (SemanticAnalysisException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        currentEnv = parent;
        return type;
    }

    private String getTextFrom(UidContext uid) {
        return uid.simpleId().ID().getText(); // NPE possible when ID() = null
    }

}
