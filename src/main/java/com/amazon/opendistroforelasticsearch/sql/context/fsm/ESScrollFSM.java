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

package com.amazon.opendistroforelasticsearch.sql.context.fsm;

import com.amazon.opendistroforelasticsearch.sql.exception.SqlParseException;
import com.amazon.opendistroforelasticsearch.sql.query.QueryAction;
import com.amazon.opendistroforelasticsearch.sql.query.SqlElasticRequestBuilder;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;

import java.util.Objects;

import static com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM.EventType.CLOSE;
import static com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM.EventType.NO_MORE_DATA;

/**
 * FSM for cursor support by native ES Scroll API.
 */
public class ESScrollFSM extends FSM {

    private final QueryAction queryAction;

    private SqlElasticRequestBuilder requestBuilder;

    private Page curPage = Page.EMPTY;

    public ESScrollFSM(QueryAction action) {
        this.queryAction = action;
    }

    @Override
    public Page getPage() {
        return curPage;
    }

    @Override
    protected void build(Event event) {
        try {
            requestBuilder = queryAction.explain();
        } catch (SqlParseException e) {
            throw new IllegalStateException("Failed to parse query", e);
        }
    }

    @Override
    protected void fetchLocally(Event event) {
        Objects.requireNonNull(requestBuilder, "Request builder is not created yet");

        ActionResponse response = requestBuilder.get();
        if (!(response instanceof SearchResponse)
            || Strings.isNullOrEmpty(((SearchResponse) response).getScrollId())) {
            throw new IllegalStateException("Unsupported request which is not scroll search request");
        }

        SearchResponse searchResponse = (SearchResponse) response;
        curPage = new Page(searchResponse.getHits(), searchResponse.getScrollId());

        if (curPage.getResult().getTotalHits() == 0) {
            handle(new Event(NO_MORE_DATA, event));
        }
    }

    @Override
    protected void fetchRemotely(Event event) {
        // Do nothing since scroll context is maintained by ES across the cluster
        handle(new Event(NO_MORE_DATA, event));
    }

    @Override
    protected void clear(Event event) {
        handle(new Event(CLOSE, event));
    }
}
