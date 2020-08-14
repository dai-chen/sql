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

package com.amazon.opendistroforelasticsearch.sql.executor;

import com.amazon.opendistroforelasticsearch.sql.data.model.ExprValue;
import com.amazon.opendistroforelasticsearch.sql.expression.NamedExpression;
import com.amazon.opendistroforelasticsearch.sql.planner.physical.FilterOperator;
import com.amazon.opendistroforelasticsearch.sql.planner.physical.PhysicalPlan;
import com.amazon.opendistroforelasticsearch.sql.planner.physical.PhysicalPlanNodeVisitor;
import com.amazon.opendistroforelasticsearch.sql.planner.physical.ProjectOperator;
import com.amazon.opendistroforelasticsearch.sql.storage.TableScanOperator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * Visitor that explains a physical plan to JSON format.
 */
@RequiredArgsConstructor
public class Explain extends PhysicalPlanNodeVisitor<JsonNode, ObjectNode>
                     implements Function<PhysicalPlan, String> {

  private final ObjectMapper mapper = new ObjectMapper();

  private final boolean isProfiling;

  private final Map<PhysicalPlan, ProfilingState> profilingStates = new IdentityHashMap<>();

  public PhysicalPlan profile(PhysicalPlan plan) {
    return plan.accept(new PhysicalPlanNodeVisitor<PhysicalPlan, Object>() {

      @Override
      public PhysicalPlan visitProject(ProjectOperator node, Object context) {
        ProfilingState state = new ProfilingState();
        PhysicalPlan child = node.getInput().accept(this, context);
        ProjectOperator clone = new ProjectOperator(child, node.getProjectList());
        profilingStates.put(clone, state);
        return new Profiler(clone, state);
      }

      @Override
      public PhysicalPlan visitFilter(FilterOperator node, Object context) {
        ProfilingState state = new ProfilingState();
        PhysicalPlan child = node.getInput().accept(this, context);
        FilterOperator clone = new FilterOperator(child, node.getConditions());
        profilingStates.put(clone, state);
        return new Profiler(clone, state);
      }

      @Override
      public PhysicalPlan visitTableScan(TableScanOperator node, Object context) {
        ProfilingState state = new ProfilingState();
        profilingStates.put(node, state);
        return new Profiler(node, state);
      }
    }, null);
  }

  @Override
  public String apply(PhysicalPlan plan) {
    return plan.accept(this, mapper.createObjectNode()).toPrettyString();
  }

  @Override
  protected JsonNode visitNode(PhysicalPlan node, ObjectNode context) {
    return explain(node, context, description -> {});
  }

  @Override
  public JsonNode visitProject(ProjectOperator node, ObjectNode context) {
    return explain(node, context, description -> {
      String projectList = node.getProjectList()
                               .stream()
                               .map(NamedExpression::getName)
                               .collect(Collectors.joining(", "));
      description.put("fields", projectList);
    });
  }

  @Override
  public JsonNode visitFilter(FilterOperator node, ObjectNode context) {
    return explain(node, context, description ->
        description.put("conditions", node.getConditions().toString()));
  }

  @Override
  public JsonNode visitTableScan(TableScanOperator node, ObjectNode context) {
    return explain(node, context, description -> description.put("request", node.toString()));
  }

  private JsonNode explain(PhysicalPlan node, ObjectNode parent,
                           Consumer<ObjectNode> describe) {
    ObjectNode json = mapper.createObjectNode();
    parent.set(getOperatorName(node), json);

    ObjectNode description = mapper.createObjectNode();
    json.set("description", description);

    ProfilingState state = profilingStates.get(node);
    if (state != null) {
      ObjectNode profile = mapper.createObjectNode();
      profile.put("rows", state.rows);
      profile.put("elapsed", state.elapsed);
      json.set("profile", profile);
    }

    describe.accept(description);
    explainChild(node, json);
    return parent;
  }

  private void explainChild(PhysicalPlan node, ObjectNode json) {
    if (node.getChild().isEmpty()) {
      return;
    }

    PhysicalPlan child = node.getChild().get(0);
    child.accept(this, json);
  }

  private String getOperatorName(PhysicalPlan node) {
    return node.getClass().getSimpleName();
  }

  private static class Watcher {

  }

  /**
   * Should be updated by single thread assuming that each operator
   * is not executed concurrently.
   */
  private static class ProfilingState {
    private int rows;
    private long elapsed;
  }

  @RequiredArgsConstructor
  private static class Profiler extends PhysicalPlan {
    private final PhysicalPlan child;
    private final ProfilingState state;

    @Override
    public <R, C> R accept(PhysicalPlanNodeVisitor<R, C> visitor, C context) {
      return child.accept(visitor, context);
    }

    @Override
    public List<PhysicalPlan> getChild() {
      return Collections.singletonList(child);
    }

    @Override
    public boolean hasNext() {
      return child.hasNext();
    }

    @Override
    public ExprValue next() {
      try {
        return child.next();
      } finally {
        state.rows++;
        state.elapsed = 123;
      }
    }
  }

}
