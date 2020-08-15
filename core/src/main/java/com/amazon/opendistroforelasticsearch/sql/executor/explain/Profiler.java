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

package com.amazon.opendistroforelasticsearch.sql.executor.explain;

import com.amazon.opendistroforelasticsearch.sql.data.model.ExprValue;
import com.amazon.opendistroforelasticsearch.sql.planner.physical.PhysicalPlan;
import com.amazon.opendistroforelasticsearch.sql.planner.physical.PhysicalPlanInterceptor;
import com.amazon.opendistroforelasticsearch.sql.planner.physical.PhysicalPlanNodeVisitor;
import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Profiler that intercepts physical operator by profiling method.
 */
public class Profiler extends PhysicalPlanInterceptor<Object>
                      implements Function<PhysicalPlan, PhysicalPlan> {

  private final Map<PhysicalPlan, Profile> profiles = new IdentityHashMap<>();

  private Ticker ticker;

  public Profiler() {
    this(Ticker.systemTicker());
  }

  public Profiler(Ticker ticker) {
    this.ticker = ticker;
  }

  @Override
  public PhysicalPlan apply(PhysicalPlan plan) {
    return plan.accept(this, null);
  }

  /**
   * Get profile by node object reference.
   * @param node    node object
   * @return        profile
   */
  public Profile find(PhysicalPlan node) {
    return profiles.get(node);
  }

  @Override
  protected PhysicalPlan intercept(PhysicalPlan node) {
    Profile profile = new Profile();
    profiles.put(node, profile);
    return new ProfileCollector(node, profile, Stopwatch.createUnstarted(ticker));
  }

  /**
   * Should be updated by single thread assuming that each operator
   * is not executed concurrently.
   */
  @Data
  public static class Profile {
    private int rows;
    private long elapsed;
  }

  @RequiredArgsConstructor
  private static class ProfileCollector extends PhysicalPlan {
    private final PhysicalPlan child;
    private final Profile profile;
    private final Stopwatch watch;

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
      watch.start();
      try {
        return child.next();
      } finally {
        watch.stop();
        profile.rows++;
        profile.elapsed = watch.elapsed(TimeUnit.MILLISECONDS);
      }
    }
  }

}
