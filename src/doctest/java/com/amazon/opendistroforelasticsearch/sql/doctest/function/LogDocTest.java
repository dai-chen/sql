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

package com.amazon.opendistroforelasticsearch.sql.doctest.function;

import com.amazon.opendistroforelasticsearch.sql.doctest.core.Summary;
import com.amazon.opendistroforelasticsearch.sql.doctest.core.DocTest;
import com.amazon.opendistroforelasticsearch.sql.utils.StringUtils;
import org.junit.Test;

import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.function.ScalarFunction.LOG;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.function.ScalarFunction.LOG10;
import static com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.function.ScalarFunction.LOG2;

@Summary(
    name = "LOG",
    description = "Logarithm is the inverse function to exponentiation"
)
public class LogDocTest extends DocTest {

    @Override
    public String syntax() {
        return StringUtils.format(
            "Basic LOG function can be used with/without base argument: %s. " +
            "And LOG2 and LOG10 is also provided for convenience: %s or %s.",
            LOG.usage(), LOG2.usage(), LOG10.usage());
    }

    @Test
    public void test() {
        System.out.println("hello world");
    }

}
