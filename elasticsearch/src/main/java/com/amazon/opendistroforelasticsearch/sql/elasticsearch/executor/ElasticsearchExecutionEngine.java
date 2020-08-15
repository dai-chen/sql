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

package com.amazon.opendistroforelasticsearch.sql.elasticsearch.executor;

import com.amazon.opendistroforelasticsearch.sql.common.response.ResponseListener;
import com.amazon.opendistroforelasticsearch.sql.data.model.ExprValue;
import com.amazon.opendistroforelasticsearch.sql.elasticsearch.client.ElasticsearchClient;
import com.amazon.opendistroforelasticsearch.sql.elasticsearch.executor.protector.ExecutionProtector;
import com.amazon.opendistroforelasticsearch.sql.executor.ExecutionEngine;
import com.amazon.opendistroforelasticsearch.sql.executor.explain.Explain;
import com.amazon.opendistroforelasticsearch.sql.executor.explain.Profiler;
import com.amazon.opendistroforelasticsearch.sql.planner.physical.PhysicalPlan;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

/** Elasticsearch execution engine implementation. */
@RequiredArgsConstructor
public class ElasticsearchExecutionEngine implements ExecutionEngine {

  private final ElasticsearchClient client;

  private final ExecutionProtector executionProtector;

  @Override
  public void execute(PhysicalPlan physicalPlan, ResponseListener<QueryResponse> listener) {
    PhysicalPlan plan = executionProtector.protect(physicalPlan);
    client.schedule(
        () -> {
          try {
            List<ExprValue> result = new ArrayList<>();
            plan.open();

            while (plan.hasNext()) {
              result.add(plan.next());
            }

            QueryResponse response = new QueryResponse(physicalPlan.schema(), result);
            listener.onResponse(response);
          } catch (Exception e) {
            listener.onFailure(e);
          } finally {
            plan.close();
          }
        });
  }

  @Override
  public void explain(PhysicalPlan physicalPlan, boolean isProfiling,
                      ResponseListener<String> listener) {
    client.schedule(() -> {
      try {
        Profiler profiler = new Profiler();
        Explain explain = new Explain(profiler);
        if (isProfiling) {
          try (PhysicalPlan plan = executionProtector.protect(
                                    profiler.apply(physicalPlan))) {
            plan.open();
            while (plan.hasNext()) {
              plan.next();
            }
            listener.onResponse(explain.apply(plan));
          }
        } else {
          listener.onResponse(explain.apply(physicalPlan));
        }
      } catch (Exception e) {
        listener.onFailure(e);
      }
    });
  }

}
