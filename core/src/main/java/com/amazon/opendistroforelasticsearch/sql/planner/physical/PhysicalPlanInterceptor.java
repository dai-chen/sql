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

package com.amazon.opendistroforelasticsearch.sql.planner.physical;

import com.amazon.opendistroforelasticsearch.sql.storage.TableScanOperator;
import lombok.RequiredArgsConstructor;

/**
 * Physical plan interceptor that intercepts each operator by a given function.
 * @param <C>   context
 */
@RequiredArgsConstructor
public abstract class PhysicalPlanInterceptor<C> extends PhysicalPlanNodeVisitor<PhysicalPlan, C> {

  /**
   * Aspect that intercepts each operator to manipulate or wrap.
   * @param node    physical operator node
   * @return        node after intercepted
   */
  protected abstract PhysicalPlan intercept(PhysicalPlan node);

  @Override
  public PhysicalPlan visitFilter(FilterOperator node, C context) {
    return intercept(
        new FilterOperator(
            visitInput(node.getInput(), context),
            node.getConditions()));
  }

  @Override
  public PhysicalPlan visitAggregation(AggregationOperator node, C context) {
    return intercept(
        new AggregationOperator(
            visitInput(node.getInput(), context),
            node.getAggregatorList(),
            node.getGroupByExprList()));
  }

  @Override
  public PhysicalPlan visitRename(RenameOperator node, C context) {
    return intercept(
        new RenameOperator(
            visitInput(node.getInput(), context),
            node.getMapping()));
  }

  @Override
  public PhysicalPlan visitTableScan(TableScanOperator node, C context) {
    return intercept(node);
  }

  @Override
  public PhysicalPlan visitProject(ProjectOperator node, C context) {
    return intercept(
        new ProjectOperator(
            visitInput(node.getInput(), context),
            node.getProjectList()));
  }

  @Override
  public PhysicalPlan visitRemove(RemoveOperator node, C context) {
    return intercept(
        new RemoveOperator(
            visitInput(node.getInput(), context),
            node.getRemoveList()));
  }

  @Override
  public PhysicalPlan visitEval(EvalOperator node, C context) {
    return intercept(
        new EvalOperator(
            visitInput(node.getInput(), context),
            node.getExpressionList()));
  }

  @Override
  public PhysicalPlan visitDedupe(DedupeOperator node, C context) {
    return intercept(
        new DedupeOperator(
            visitInput(node.getInput(), context),
            node.getDedupeList(),
            node.getAllowedDuplication(),
            node.getKeepEmpty(),
            node.getConsecutive()));
  }

  @Override
  public PhysicalPlan visitSort(SortOperator node, C context) {
    return intercept(
        new SortOperator(
            visitInput(node.getInput(), context),
            node.getCount(),
            node.getSortList()));
  }

  @Override
  public PhysicalPlan visitValues(ValuesOperator node, C context) {
    return intercept(node);
  }

  PhysicalPlan visitInput(PhysicalPlan node, C context) {
    if (null == node) {
      return node;
    } else {
      return node.accept(this, context);
    }
  }

}
