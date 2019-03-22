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

package com.amazon.opendistroforelasticsearch.sql.context;

import com.amazon.opendistroforelasticsearch.sql.context.cursor.CursorContextId;
import com.amazon.opendistroforelasticsearch.sql.domain.QueryStatement;
import com.amazon.opendistroforelasticsearch.sql.executor.format.Option;
import com.amazon.opendistroforelasticsearch.sql.query.QueryAction;
import com.amazon.opendistroforelasticsearch.sql.query.SqlElasticRequestBuilder;
import com.amazon.opendistroforelasticsearch.sql.request.SqlRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHits;

import java.util.Objects;

/**
 * Contextual query action prevents actual parsing/translating happen and make use of context to handle the request.
 */
public class ContextualQueryAction implements QueryAction {

    private final SqlRequest sqlRequest;
    private final QueryContextManager queryContextMgr;
    private final QueryAction queryAction;

    /** Context ID generated or parsed out of request */
    private ContextId contextId;

    public ContextualQueryAction(SqlRequest request, QueryContextManager manager) {
        this(request, manager, manager.find(request).queryAction());
    }

    public ContextualQueryAction(SqlRequest request, QueryContextManager manager, QueryAction action) {
        this.sqlRequest = request;
        this.queryContextMgr = manager;
        this.queryAction = action;
    }

    public SearchHits execute() {
        if (queryContextMgr.isNewContext(sqlRequest)) {
            contextId = queryContextMgr.handleBuildEvent(sqlRequest, queryAction);
        } else {
            contextId = new CursorContextId(sqlRequest); //TODO: hide and relate all cursor factory logic together
        }
        queryContextMgr.handleFetchEvent(contextId);
        return SearchHits.empty();
    }

    @Override
    public Option[] options() {
        Objects.requireNonNull(contextId, "Context ID is not ready");
        return new Option[]{ contextId };
    }

    @Override
    public SqlElasticRequestBuilder explain() {
        throw new UnsupportedOperationException("Contextual query is only supported by our pretty formatter");
    }

    @Override
    public QueryStatement getQueryStatement() {
        return queryAction.getQueryStatement();
    }

    @Override
    public Client getClient() {
        return queryAction.getClient();
    }

    @Override
    public void setSqlRequest(SqlRequest sqlRequest) {
    }

}
