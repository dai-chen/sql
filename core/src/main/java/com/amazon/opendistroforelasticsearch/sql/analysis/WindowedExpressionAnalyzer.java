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

package com.amazon.opendistroforelasticsearch.sql.analysis;

import static com.amazon.opendistroforelasticsearch.sql.ast.tree.Sort.SortOption.PPL_ASC;
import static com.amazon.opendistroforelasticsearch.sql.ast.tree.Sort.SortOption.PPL_DESC;

import com.amazon.opendistroforelasticsearch.sql.analysis.symbol.Namespace;
import com.amazon.opendistroforelasticsearch.sql.analysis.symbol.Symbol;
import com.amazon.opendistroforelasticsearch.sql.ast.AbstractNodeVisitor;
import com.amazon.opendistroforelasticsearch.sql.ast.expression.Alias;
import com.amazon.opendistroforelasticsearch.sql.ast.expression.UnresolvedExpression;
import com.amazon.opendistroforelasticsearch.sql.ast.expression.WindowFunction;
import com.amazon.opendistroforelasticsearch.sql.ast.tree.Sort;
import com.amazon.opendistroforelasticsearch.sql.expression.Expression;
import com.amazon.opendistroforelasticsearch.sql.expression.window.WindowDefinition;
import com.amazon.opendistroforelasticsearch.sql.planner.logical.LogicalPlan;
import com.amazon.opendistroforelasticsearch.sql.planner.logical.LogicalSort;
import com.amazon.opendistroforelasticsearch.sql.planner.logical.LogicalWindow;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Windowed expression analyzer that analyzes window function expression.
 */
@RequiredArgsConstructor
public class WindowedExpressionAnalyzer extends AbstractNodeVisitor<LogicalPlan, AnalysisContext> {

  /**
   * Expression analyzer.
   */
  private final ExpressionAnalyzer expressionAnalyzer;

  /**
   * Child node.
   */
  private final LogicalPlan child;

  /**
   * Analyze the given project item and return window operator if window function found.
   * @param projectItem   project item
   * @param context       analysis context
   * @return              window operator or original child if not windowed
   */
  public LogicalPlan analyze(UnresolvedExpression projectItem, AnalysisContext context) {
    if (isWindowed(projectItem)) {
      return projectItem.accept(this, context);
    }
    return child;
  }

  @Override
  public LogicalPlan visitAlias(Alias node, AnalysisContext context) {
    return node.getDelegated().accept(this, context);
  }

  @Override
  public LogicalPlan visitWindowFunction(WindowFunction node, AnalysisContext context) {
    Expression windowFunction = expressionAnalyzer.analyze(node, context);
    List<Expression> partitionByList = analyzePartitionList(node, context);
    List<Pair<Sort.SortOption, Expression>> sortList = analyzeSortList(node, context);
    WindowDefinition windowDefinition = new WindowDefinition(partitionByList, sortList);

    context.peek().define(
        new Symbol(Namespace.FIELD_NAME, windowFunction.toString()), windowFunction.type());

    return new LogicalWindow(
        new LogicalSort(child, 1000, windowDefinition.getAllSortItems()),
        windowFunction,
        windowDefinition);
  }

  private boolean isWindowed(UnresolvedExpression projectItem) {
    return (projectItem instanceof Alias)
        && (((Alias) projectItem).getDelegated() instanceof WindowFunction);
  }

  private List<Expression> analyzePartitionList(WindowFunction node, AnalysisContext context) {
    return node.getPartitionByList()
               .stream()
               .map(expr -> expressionAnalyzer.analyze(expr, context))
               .collect(Collectors.toList());
  }

  private List<Pair<Sort.SortOption, Expression>> analyzeSortList(WindowFunction node,
                                                                  AnalysisContext context) {
    return node.getSortList()
               .stream()
               .map(pair -> ImmutablePair
                   .of(getSortOption(pair.getLeft()),
                       expressionAnalyzer.analyze(pair.getRight(), context)))
               .collect(Collectors.toList());
  }

  private Sort.SortOption getSortOption(String option) {
    return "ASC".equalsIgnoreCase(option) ? PPL_ASC : PPL_DESC;
  }

}
