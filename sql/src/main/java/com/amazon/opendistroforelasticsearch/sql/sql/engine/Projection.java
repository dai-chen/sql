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

package com.amazon.opendistroforelasticsearch.sql.sql.engine;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fake logical operator for projection in relational theory
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Projection {

    /**
     * In relational theory projection is basically pick columns in a relation.
     * But with extension, projection could transform value by expression and rename during conventional projection.
     * Here is the map from projected name to expression.
     */
    private final Map<String, Expression> projects = new LinkedHashMap<>();

    public void add(String name, Expression expression) {
        projects.put(name, expression);
    }

    public Tuple project(Tuple input) {
        Map<String, Object> output = new LinkedHashMap<>(); // projected tuple is ordered
        projects.forEach((name, expression) -> {
            output.put(
                name,
                expression.evaluate(input)
            );
        });
        return new Tuple(output);
    }

}
