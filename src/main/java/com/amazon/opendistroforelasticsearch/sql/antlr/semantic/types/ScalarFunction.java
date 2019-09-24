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

package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types;

import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.DOUBLE;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.NUMBER;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.BaseType.STRING;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.Generic.T;

/**
 * Scalar SQL function
 */
public enum ScalarFunction implements TypeExpression {

    ABS(func(T(NUMBER)).to(T)),
    ACOS(func(T(NUMBER)).to(T)),
    ASIN(func(T(NUMBER)).to(T)),
    ATAN(func(T(NUMBER)).to(T)),
    ATAN2(func(T(NUMBER)).to(T)),
    CBRT(func(T(NUMBER)).to(T)),
    CEIL(func(T(NUMBER)).to(T)),
    CONCAT(func(T(NUMBER)).to(T)),
    CONCAT_WS(func(T(NUMBER)).to(T)),
    COS(func(T(NUMBER)).to(T)),
    COSH(func(T(NUMBER)).to(T)),
    DATE_FORMAT(func(T(NUMBER)).to(T)),
    DEGREES(func(T(NUMBER)).to(T)),
    E(func().to(DOUBLE)),
    EXP(func(T(NUMBER)).to(T)),
    EXPM1(func(T(NUMBER)).to(T)),
    FLOOR(func(T(NUMBER)).to(T)),
    LOG(func(T(NUMBER)).to(T)),
    LOG2(func(T(NUMBER)).to(T)),
    LOG10(func(T(NUMBER)).to(T)),
    LOWER(func(T(STRING)).to(T)),
    PI(func().to(DOUBLE)),
    POW(func(T(NUMBER)).to(T)),
    RADIANS(func(T(NUMBER)).to(T)),
    RANDOM(func(T(NUMBER)).to(T)),
    RINT(func(T(NUMBER)).to(T)),
    ROUND(func(T(NUMBER)).to(T)),
    SIN(func(T(NUMBER)).to(T)),
    SINH(func(T(NUMBER)).to(T)),
    SQRT(func(T(NUMBER)).to(T)),
    TAN(func(T(NUMBER)).to(T)),
    UPPER(func(T(STRING)).to(T));

    private final TypeExpressionSpec spec;

    ScalarFunction(TypeExpressionSpec spec) {
        this.spec = spec;
    }

    @Override
    public TypeExpressionSpec spec() {
        return spec;
    }

    private static TypeExpressionSpec func(Type... argTypes) {
        return new TypeExpressionSpec().map(argTypes);
    }

}
