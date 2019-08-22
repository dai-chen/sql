package com.amazon.opendistroforelasticsearch.sql.antlr.semantic;

import com.amazon.opendistroforelasticsearch.sql.antlr.StringSimilarity;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.scope.Environment;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.scope.Namespace;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.scope.Symbol;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.OperatorType;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.ScalarFunctionType;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.Type;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.TypeExpression;
import com.amazon.opendistroforelasticsearch.sql.esdomain.LocalClusterState;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import static com.amazon.opendistroforelasticsearch.sql.antlr.SqlAnalysisExceptionBuilder.semanticException;

/**
 * Generic semantic analyzer that independent of concrete parser generated by specific ANTLR grammar.
 */
public class GenericSemanticAnalyzer {

    /** Environment stack for symbol scope management */
    private Environment environment = new Environment(null);


    /******************************************************************************
     *                              Definition
     ******************************************************************************/

    public Type visitIndexName(LocalClusterState clusterState, String indexName) {
        LocalClusterState.IndexMappings indexMappings = clusterState.getFieldMappings(new String[]{ indexName });
        LocalClusterState.FieldMappings mappings = indexMappings.firstMapping().firstMapping();
        mappings.data().forEach(
            (fieldName, mapping) -> environment.define(new Symbol(Namespace.FIELD_NAME, fieldName), BaseType.typeIn(mapping)) //TODO: table alias and undefined type in our system
        );
        return null;
    }

    public Type visitQuery(Runnable visit) {
        environment = new Environment(environment);

        for (OperatorType type : OperatorType.values()) {
            environment.define(new Symbol(Namespace.OPERATOR_NAME, type.getName()), type);
        }

        visit.run();

        environment = environment.getParent();
        return null;
    }

    public Type visitWhereClause(Runnable visitDeep) {
        environment = new Environment(environment);

        for (ScalarFunctionType type : ScalarFunctionType.values()) {
            environment.define(new Symbol(Namespace.FUNCTION_NAME, type.getName()), type);
        }

        visitDeep.run();

        environment = environment.getParent();
        return null;
    }


    /******************************************************************************
     *                              Function & Operator
     ******************************************************************************/

    public Type visitFunctionCall(TypeExpression funcType, TypeExpression actualArgTypes) {
        if (!funcType.isCompatible(actualArgTypes)) {
            throw semanticException(
                "[%s] can only work with [%s] instead of [%s].",
                funcType.getName(), Arrays.toString(funcType.inputTypes()), actualArgTypes
            )./*at(sql, ctx).*/suggestion("Usage: %s.", funcType).build();
        }
        return funcType.outputType();
    }


    /******************************************************************************
     *                              Identifier
     ******************************************************************************/

    public Type visitFieldName(String fieldName) {
        return resolve(new Symbol(Namespace.FIELD_NAME, fieldName));
    }

    public Type visitFunctionName(String funcName) {
        return resolve(new Symbol(Namespace.FUNCTION_NAME, funcName));
    }

    public Type visitOperatorName(String opName) {
        return resolve(new Symbol(Namespace.OPERATOR_NAME, opName));
    }

    public Type resolve(Symbol symbol) {
        Optional<Type> type = environment.resolve(symbol);
        if (!type.isPresent()) {
            List<String> suggestedWords = new StringSimilarity(environment.allSymbolsIn(symbol.getNamespace())).similarTo(symbol.getName());
            throw semanticException("%s cannot be found.", symbol).
                /*at(sql, ctx).*/suggestion("Did you mean [%s]?", String.join(", ", suggestedWords)).build();
        }
        return type.get();
    }

    /******************************************************************************
     *                      Constant and literal
     ******************************************************************************/

    public Type visitString(String text) {
        return BaseType.STRING;
    }

    public Type visitNumber(String text) { //TODO: float or integer?
        return BaseType.NUMBER;
    }

    public Type visitBoolean(String text) {
        return BaseType.BOOLEAN;
    }

}
