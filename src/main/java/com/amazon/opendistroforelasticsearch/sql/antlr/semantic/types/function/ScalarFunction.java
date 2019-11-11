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

package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.function;

import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.Type;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.TypeExpression;
import com.amazon.opendistroforelasticsearch.sql.utils.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.base.ESDataType.DATE;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.base.ESDataType.DOUBLE;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.base.ESDataType.INTEGER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.base.ESDataType.NUMBER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.base.ESDataType.STRING;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.special.Generic.T;

/**
 * Scalar SQL function
 */
public enum ScalarFunction implements TypeExpression {

    ABS(func(T(NUMBER)).to(T)), // translate to Java: <T extends Number> T ABS(T)
    ASCII(func(T(STRING)).to(T)),
    //ASIN(func(T(NUMBER)).to(T)),
    ATAN(func(T(NUMBER)).to(T)),
    ATAN2(func(T(NUMBER), NUMBER).to(T)),
    CBRT(func(T(NUMBER)).to(T)),
    CEIL(func(T(NUMBER)).to(T)),
    CONCAT(), // TODO: varargs support required
    CONCAT_WS(),
    COS(func(T(NUMBER)).to(T)),
    COSH(func(T(NUMBER)).to(T)),
    COT(func(T(NUMBER)).to(T)),
    DATE_FORMAT(
        //func(DATE, STRING).to(STRING),
        //func(DATE, STRING, STRING).to(STRING)
    ),
    DEGREES(func(T(NUMBER)).to(T)),
    E(func().to(DOUBLE)),
    EXP(func(T(NUMBER)).to(T)),
    EXPM1(func(T(NUMBER)).to(T)),
    FLOOR(func(T(NUMBER)).to(T)),
    LENGTH(func(STRING).to(INTEGER)
),
    LOCATE(
            func(STRING, STRING, INTEGER).to(INTEGER),
            func(STRING, STRING).to(INTEGER)
    ),
    LOG(
        func(T(NUMBER)).to(T),
        func(T(NUMBER), NUMBER).to(T)
    ),
    LOG2(func(T(NUMBER)).to(T)),
    LOG10(func(T(NUMBER)).to(T)),
    LN(func(T(NUMBER)).to(T)),
    LOWER(
        //func(T(STRING)).to(T)
        //func(T(STRING), STRING).to(T)
    ),
    LTRIM(func(T(STRING)).to(T)),
    PI(func().to(DOUBLE)),
    POW, POWER(
        //func(T(NUMBER)).to(T),
        //func(T(NUMBER), NUMBER).to(T)
    ),
    RADIANS(func(T(NUMBER)).to(T)),
    //RANDOM(func(T(NUMBER)).to(T)),
    REPLACE(func(T(STRING), STRING, STRING).to(T)),
    RINT(func(T(NUMBER)).to(T)),
    ROUND(func(T(NUMBER)).to(T)),
    RTRIM(func(T(STRING)).to(T)),
    SIGN(func(T(NUMBER)).to(T)),
    SIGNUM(func(T(NUMBER)).to(T)),
    SIN(func(T(NUMBER)).to(T)),
    SINH(func(T(NUMBER)).to(T)),
    SQRT(func(T(NUMBER)).to(T)),
    SUBSTRING(
        "The SUBSTRING() function extracts a substring from a string.",
        func(
            arg("string", T(STRING)),
            arg("start position", INTEGER),
            arg("length", INTEGER)
        ).to(T)
    ),
    TAN(func(T(NUMBER)).to(T)),
    UPPER(
        //func(T(STRING)).to(T)
        //func(T(STRING), STRING).to(T)
    ),
    YEAR(func(DATE).to(INTEGER));

    private String description;

    private final TypeExpressionSpec[] specifications;

    ScalarFunction(String description, TypeExpressionSpec... specifications) {
        this.description = description;
        this.specifications = specifications;
    }

    ScalarFunction(TypeExpressionSpec... specifications) {
        this.specifications = specifications;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public TypeExpressionSpec[] specifications() {
        return specifications;
    }

    public String getDescription() {
        return description;
    }

    public String getSyntax() {
        StringBuilder help = new StringBuilder();
        help.append("Specifications: ").
             append(Arrays.stream(specifications).
                           map(TypeExpressionSpec::toString).
                           collect(Collectors.joining(", "))).
             append("\n\n").
             append("Semantics:\n\n");

        for (int i = 0; i < specifications.length; i++) {
            help.append(StringUtils.format("%s. ", i + 1)).
                 append(StringUtils.format(
                    "The function accepts %s.",
                    StreamSupport.stream(specifications[i].spliterator(), false).
                                  map(TypeExpression.Argument::toString).
                                  collect(Collectors.joining(" and ")))).
                 append('\n');
        }
        return help.toString();
    }

    private static TypeExpressionSpec func() {
        return new TypeExpressionSpec().map(new Type[0]);
    }

    private static TypeExpressionSpec func(Argument... args) {
        return new TypeExpressionSpec().map(args);
    }

    private static TypeExpressionSpec func(Type... argTypes) {
        return new TypeExpressionSpec().map(argTypes);
    }

    private static Argument arg(String name, Type type) {
        return new Argument(name, type);
    }

    @Override
    public String toString() {
        return "Function [" + name() + "]";
    }
}
