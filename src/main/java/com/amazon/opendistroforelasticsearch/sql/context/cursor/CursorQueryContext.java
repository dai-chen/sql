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

package com.amazon.opendistroforelasticsearch.sql.context.cursor;

import com.amazon.opendistroforelasticsearch.sql.context.QueryContext;
import com.amazon.opendistroforelasticsearch.sql.exception.SqlParseException;
import com.amazon.opendistroforelasticsearch.sql.executor.ElasticHitsExecutor;
import com.amazon.opendistroforelasticsearch.sql.executor.join.QueryPlanElasticExecutor;
import com.amazon.opendistroforelasticsearch.sql.query.QueryAction;
import com.amazon.opendistroforelasticsearch.sql.query.SqlElasticRequestBuilder;
import com.amazon.opendistroforelasticsearch.sql.query.join.ESJoinQueryAction;
import com.amazon.opendistroforelasticsearch.sql.query.planner.HashJoinQueryPlanRequestBuilder;

/**
 * Query context for cursor support
 */
public class CursorQueryContext implements QueryContext {

    private final QueryAction queryAction;
    private final ElasticHitsExecutor executor;

    public CursorQueryContext(QueryAction action) {
        this.queryAction = action;
        this.executor = createExecutor();
    }

    @Override
    public QueryAction queryAction() {
        return queryAction;
    }

    @Override
    public void handle(Event event) {

    }

    private ElasticHitsExecutor createExecutor() {
        if (queryAction instanceof ESJoinQueryAction) {
            try {
                SqlElasticRequestBuilder request = queryAction.explain();
                if (request instanceof HashJoinQueryPlanRequestBuilder) {
                    return new QueryPlanElasticExecutor((HashJoinQueryPlanRequestBuilder) request);
                }
            }
            catch (SqlParseException e) {
                throw new IllegalStateException("Failed to create executor for new query context", e);
            }
        }
        throw new UnsupportedOperationException("Don't support cursor for query action: " + queryAction.getClass().getSimpleName());
    }

}
