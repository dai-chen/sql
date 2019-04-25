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

import com.amazon.opendistroforelasticsearch.sql.domain.QueryStatement;
import com.amazon.opendistroforelasticsearch.sql.executor.format.Option;
import com.amazon.opendistroforelasticsearch.sql.query.QueryAction;
import com.amazon.opendistroforelasticsearch.sql.query.SqlElasticRequestBuilder;
import com.amazon.opendistroforelasticsearch.sql.request.SqlRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHits;

import java.util.Objects;

/**
 * Contextual query action passes and uses query context to handle request eventually.
 */
public class ContextualQueryAction implements QueryAction {

    private final QueryContextManager queryContextMgr;

    private final SqlRequest sqlRequest;

    private final QueryAction queryAction;

    private QueryContext queryContext;

    public ContextualQueryAction(QueryContextManager manager, SqlRequest request, QueryAction action) {
        this.queryContextMgr = manager;
        this.sqlRequest = request;
        this.queryAction = action;
    }

    public SearchHits execute() {
        queryContext = queryContextMgr.get(sqlRequest, queryAction);
        SearchHits hits = queryContext.fetch(sqlRequest, queryAction);
        queryContextMgr.update(queryContext);
        return hits;
    }

    @Override
    public Option[] options() {
        Objects.requireNonNull(queryContext, "Query context is not set");
        return new Option[]{ queryContext.getId() };
    }

    @Override
    public SqlElasticRequestBuilder explain() {
        throw new UnsupportedOperationException("Contextual query is only supported by pretty formatter");
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
        queryAction.setSqlRequest(sqlRequest);
    }

    @Override
    public SqlRequest getSqlRequest() {
        return queryAction.getSqlRequest();
    }
}
