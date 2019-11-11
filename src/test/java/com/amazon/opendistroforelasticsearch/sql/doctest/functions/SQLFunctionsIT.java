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

package com.amazon.opendistroforelasticsearch.sql.doctest.functions;

import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.Type;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.function.ScalarFunction;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.special.Generic;
import com.amazon.opendistroforelasticsearch.sql.doctest.annotation.DocTestConfig;
import com.amazon.opendistroforelasticsearch.sql.doctest.core.DocTest;
import com.amazon.opendistroforelasticsearch.sql.doctest.core.Document;
import com.amazon.opendistroforelasticsearch.sql.utils.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.TypeExpression.TypeExpressionSpec;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.base.ESDataType.DATE;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.base.ESDataType.DOUBLE;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.base.ESDataType.INTEGER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.base.ESDataType.NUMBER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.base.ESDataType.STRING;

@DocTestConfig(
    template = "functions/sqlfunctions.rst",
    testData = {"testdata/accounts.json"}
)
public class SQLFunctionsIT extends DocTest {

    @Test
    public void useSqlFunctions() {
        for (ScalarFunction function : ScalarFunction.values()) {
            try {
                addSectionForFunction(function);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate doc for function " + function, e);
            }
        }
    }

    private void addSectionForFunction(ScalarFunction function) {
        TypeExpressionSpec[] specs = function.specifications();
        Document.Example[] examples = new Document.Example[specs.length];
        for (int i = 0; i < specs.length; i++) {
            examples[i] = example(
                description("Syntax: ", specs[i].toString()),
                query(StringUtils.format(
                    "SELECT %s FROM accounts LIMIT 1", functionCall(function.getName(), specs[i])
                ))
            );
        }

        section(
            title(function.getName()),
            description("Syntax: " + Arrays.toString(specs)),
            examples
        );
    }

    private String functionCall(String functionName, TypeExpressionSpec spec) {
        return functionName + StreamSupport.stream(spec.spliterator(), false).
                                            map(this::bindFunctionArg).
                                            collect(Collectors.joining(", ", "(", ")"));
    }

    private String bindFunctionArg(Type type) {
        if (type instanceof Generic) {
            type = ((Generic) type).binding();
        }

        if (type == NUMBER || type == INTEGER) {
            return "10";
        } else if (type == DOUBLE) {
            return "2.0";
        } else if (type == STRING) {
            return "'abc'";
        } else if (type == DATE) {
            return "'2019-11-09'";
        } else {
            throw new UnsupportedOperationException("Unknown " + type);
        }
    }

    private static class FakeType implements Type {

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Type construct(List<Type> others) {
            return null;
        }

        @Override
        public String usage() {
            return null;
        }
    }

}
