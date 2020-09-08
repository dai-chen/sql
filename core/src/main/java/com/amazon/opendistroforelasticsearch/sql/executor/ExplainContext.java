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

import com.amazon.opendistroforelasticsearch.sql.planner.physical.PhysicalPlan;
import java.util.IdentityHashMap;
import java.util.Map;
import lombok.Data;

/**
 * Explain context.
 */
public class ExplainContext {

  private final Map<PhysicalPlan, Profile> profiles = new IdentityHashMap<>();

  public Profile getProfile(PhysicalPlan node) {
    return profiles.computeIfAbsent(node, key -> new Profile());
  }

  @Data
  public static class Profile {
    private int rows;
    private long elapsed;
  }

  @Data
  public static class Elapsed {
    private long open;
    private long hasNext;
    private long next;
  }

}
