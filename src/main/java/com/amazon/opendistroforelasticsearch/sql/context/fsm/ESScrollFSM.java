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

import com.amazon.opendistroforelasticsearch.sql.context.Scrollable;
import com.amazon.opendistroforelasticsearch.sql.exception.SqlParseException;
import com.amazon.opendistroforelasticsearch.sql.query.QueryAction;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;

import static com.amazon.opendistroforelasticsearch.sql.context.fsm.FSM.EventType.CLOSE;
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
        // Nothing to maintain across the requests
    }

    @Override
    protected void fetchLocally(Event event) {
        SearchResponse response = doFetch(addScrollContext(event));
        curPage = new Page(response.getHits(), response.getScrollId());

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

    private QueryAction addScrollContext(Event event) {
        QueryAction action = event.getAction();
        if (!(action instanceof Scrollable)) {
            throw new IllegalStateException("Query action is not scrollable");
        }

        ((Scrollable) action).setFetchSize(event.getRequest().fetchSize());
        ((Scrollable) action).setScrollId(event.getRequest().cursor());
        return action;
    }

    private SearchResponse doFetch(QueryAction action) {
        try {
            ActionResponse response = action.explain().get();
            if (!(response instanceof SearchResponse)
                || Strings.isNullOrEmpty(((SearchResponse) response).getScrollId())) {
                throw new IllegalStateException("Couldn't find scroll ID in the ES response");
            }
            return (SearchResponse) response;
        }
        catch (SqlParseException e) {
            throw new IllegalStateException("Failed to parse query", e);
        }
    }

}
