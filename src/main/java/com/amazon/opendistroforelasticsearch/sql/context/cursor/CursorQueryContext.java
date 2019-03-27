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

import com.amazon.opendistroforelasticsearch.sql.context.ContextId;
import com.amazon.opendistroforelasticsearch.sql.context.QueryContext;
import com.amazon.opendistroforelasticsearch.sql.context.fsm.ESScrollFSM;
import com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM;
import com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM.Event;
import com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM.EventType;
import com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM.Page;
import com.amazon.opendistroforelasticsearch.sql.query.QueryAction;
import com.amazon.opendistroforelasticsearch.sql.request.SqlRequest;
import org.elasticsearch.search.SearchHits;

import java.util.Objects;

import static com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM.EventType.BUILD;
import static com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM.EventType.FETCH;

/**
 * Query context for cursor support
 */
public class CursorQueryContext implements QueryContext {

    private final QueryAction queryAction;

    private final FSM fsm;

    private ContextId contextId;

    public CursorQueryContext(SqlRequest request, QueryAction action) {
        this.queryAction = action;
        this.fsm = new ESScrollFSM();
        //this.executor = createExecutor(action);

        fire(BUILD, request);
    }

    @Override
    public ContextId getId() {
        Objects.requireNonNull(contextId, "Context ID is not generated yet");
        return contextId;
    }

    @Override
    public SearchHits fetch(SqlRequest request) {
        fire(FETCH, request);

        Page page = fsm.getPage();
        if (contextId == null) {
            contextId = new CursorContextId(page.getScrollId());
        }
        return page.getResult();
    }

    /*
    private ElasticHitsExecutor createExecutor(QueryAction action) {
        if (action instanceof ESJoinQueryAction) {
            try {
                SqlElasticRequestBuilder request = action.explain();
                if (request instanceof HashJoinQueryPlanRequestBuilder) {
                    return new QueryPlanElasticExecutor((HashJoinQueryPlanRequestBuilder) request);
                }
            }
            catch (SqlParseException e) {
                throw new IllegalStateException("Failed to create executor for new query context", e);
            }
        }
        throw new UnsupportedOperationException("Don't support cursor for query action: " + action.getClass().getSimpleName());
    }
    */

    private void fire(EventType type, SqlRequest request) {
        fsm.handle(new Event(type, request, queryAction));
    }

}
