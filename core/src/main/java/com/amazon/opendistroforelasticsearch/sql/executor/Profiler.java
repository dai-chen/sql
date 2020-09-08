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
import com.amazon.opendistroforelasticsearch.sql.planner.physical.PhysicalPlan;
import com.amazon.opendistroforelasticsearch.sql.planner.physical.PhysicalPlanInterceptor;
import com.amazon.opendistroforelasticsearch.sql.planner.physical.PhysicalPlanNodeVisitor;
import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Profiler that intercepts physical operator by profiling method.
 */
public class Profiler extends PhysicalPlanInterceptor<ExplainContext> {

  private final Ticker ticker;

  public Profiler() {
    this(Ticker.systemTicker());
  }

  public Profiler(Ticker ticker) {
    this.ticker = ticker;
  }

  public PhysicalPlan apply(PhysicalPlan plan, ExplainContext context) {
    return plan.accept(this, context);
  }

  @Override
  protected PhysicalPlan intercept(PhysicalPlan node, ExplainContext context) {
    return new PhysicalPlan() {
      private final Stopwatch watch = Stopwatch.createUnstarted(ticker);

      @Override
      public <R, C> R accept(PhysicalPlanNodeVisitor<R, C> visitor, C context) {
        return node.accept(visitor, context);
      }

      @Override
      public List<PhysicalPlan> getChild() {
        return Collections.singletonList(node);
      }

      @Override
      public void open() {
        time(() -> {
          node.open();
          return null;
        });
      }

      @Override
      public boolean hasNext() {
        return time(node::hasNext);
      }

      @Override
      public ExprValue next() {
        context.getProfile(node).setElapsed(context.getProfile(node).getElapsed() + 1);
        return time(node::next);
      }

      private <T> T time(Callable<T> callable) {
        watch.start();
        try {
          return callable.call();
        } catch (Exception e) {
          throw new IllegalStateException(e);
        } finally {
          watch.stop();
          context.getProfile(node).setElapsed(
              context.getProfile(node).getElapsed() + watch.elapsed(TimeUnit.MILLISECONDS)
          );
        }
      }
    };
  }

}
