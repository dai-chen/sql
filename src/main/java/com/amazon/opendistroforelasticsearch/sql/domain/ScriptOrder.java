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

package com.amazon.opendistroforelasticsearch.sql.domain;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import static org.elasticsearch.search.sort.ScriptSortBuilder.ScriptSortType.NUMBER;

public class ScriptOrder extends Order {

    private final String script;

    public ScriptOrder(String nestedPath, String script, String type) {
        super(nestedPath, "_script", type);

        this.script = script;
    }

    public void explain(SearchRequestBuilder request) {
        request.addSort(
            SortBuilders.scriptSort(
                new Script(script),
                NUMBER
            ).order(SortOrder.valueOf(getType()))
        );
    }
}
