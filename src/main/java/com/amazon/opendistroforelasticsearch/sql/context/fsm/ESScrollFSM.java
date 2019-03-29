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
import com.amazon.opendistroforelasticsearch.sql.query.SqlElasticRequestBuilder;
import com.amazon.opendistroforelasticsearch.sql.query.SqlElasticSearchRequestBuilder;
import com.amazon.opendistroforelasticsearch.sql.request.SqlRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollAction;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;

import static com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM.EventType.CLOSE;
import static com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM.EventType.FETCH;
import static com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM.EventType.NO_MORE_DATA;

/**
 * FSM for cursor support by native ES Scroll API.
 */
public class ESScrollFSM extends FSM {

    private Page curPage = Page.EMPTY;

    public ESScrollFSM() {
    }

    @Override
    public Page getPage() {
        return curPage;
    }

    @Override
    protected void build(Event event) {
        // Nothing to build and maintain across the requests

        if (event.getType() == FETCH) {
            handle(new Event(FETCH, event));
        }
    }

    @Override
    protected void fetchLocally(Event event) {
        try {
            SearchResponse response = doFetch(explain(event));
            curPage = new Page(response.getHits(), response.getScrollId());

            if (curPage.getResult().getTotalHits() == 0) {
                handle(new Event(NO_MORE_DATA, event));
            }
        }
        catch (SqlParseException e) {
            throw new IllegalStateException("Failed to parse query", e);
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

    private SqlElasticRequestBuilder explain(Event event) throws SqlParseException {
        SqlRequest request = event.getRequest();
        if (!Strings.isEmpty(request.cursor())) {
            return new SqlElasticSearchRequestBuilder(
                new SearchScrollRequestBuilder(event.getAction().getClient(), SearchScrollAction.INSTANCE, request.cursor()).setScroll(new TimeValue(60 * 1000)));
        }

        SqlElasticRequestBuilder requestBuilder = event.getAction().explain();
        ((SearchRequestBuilder) requestBuilder.getBuilder()).setSize(request.fetchSize()).setScroll(new TimeValue(60 * 1000));

        // Set sort...

        return requestBuilder;
    }

    private SearchResponse doFetch(SqlElasticRequestBuilder requestBuilder) {
        ActionResponse response = requestBuilder.get();
        if (!(response instanceof SearchResponse)
            || Strings.isNullOrEmpty(((SearchResponse) response).getScrollId())) {
            throw new IllegalStateException("Couldn't find scroll ID in the ES response");
        }
        return (SearchResponse) response;
    }

}
