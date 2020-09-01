/*
 *    Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License").
 *    You may not use this file except in compliance with the License.
 *    A copy of the License is located at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file. This file is distributed
 *    on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *    express or implied. See the License for the specific language governing
 *    permissions and limitations under the License.
 *
 */

package com.amazon.opendistroforelasticsearch.sql.expression.window;

import com.amazon.opendistroforelasticsearch.sql.expression.function.BuiltinFunctionName;
import com.amazon.opendistroforelasticsearch.sql.expression.function.BuiltinFunctionRepository;
import com.amazon.opendistroforelasticsearch.sql.expression.function.FunctionBuilder;
import com.amazon.opendistroforelasticsearch.sql.expression.function.FunctionName;
import com.amazon.opendistroforelasticsearch.sql.expression.function.FunctionResolver;
import com.amazon.opendistroforelasticsearch.sql.expression.function.FunctionSignature;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WindowFunctions {

  public void register(BuiltinFunctionRepository repository) {
    repository.register(rowNumber());
  }

  private FunctionResolver rowNumber() {
    return rankingFunction(BuiltinFunctionName.ROW_NUMBER.getName(), RowNumberFunction::new);
  }

  private FunctionResolver rankingFunction(FunctionName functionName,
                                           Supplier<RankingWindowFunction> constructor) {
    FunctionSignature functionSignature = new FunctionSignature(functionName, Collections.emptyList());
    FunctionBuilder functionBuilder = arguments -> constructor.get();
    return new FunctionResolver(functionName, ImmutableMap.of(functionSignature, functionBuilder));
  }

}