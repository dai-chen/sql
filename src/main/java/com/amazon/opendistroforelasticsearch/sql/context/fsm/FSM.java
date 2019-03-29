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

import com.amazon.opendistroforelasticsearch.sql.query.QueryAction;
import com.amazon.opendistroforelasticsearch.sql.request.SqlRequest;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.elasticsearch.search.SearchHits;

import java.util.function.BiConsumer;

public abstract class FSM {

    private static final BiConsumer<FSM, Event> NOOP = (fsm, event) -> {};

    private static final Table<State, EventType, State> TRANSITION = HashBasedTable.create();

    private State curState = State.START;

    public FSM() {
        TRANSITION.put(State.START, EventType.BUILD, State.BUILDING);
        TRANSITION.put(State.START, EventType.FETCH, State.BUILDING);
        TRANSITION.put(State.START, EventType.CLEAR, State.CLEARING);

        TRANSITION.put(State.BUILDING, EventType.FETCH, State.LOCAL_FETCHING);

        TRANSITION.put(State.LOCAL_FETCHING, EventType.FETCH, State.LOCAL_FETCHING);
        TRANSITION.put(State.LOCAL_FETCHING, EventType.NO_MORE_DATA, State.REMOTE_FETCHING);

        TRANSITION.put(State.REMOTE_FETCHING, EventType.FETCH, State.REMOTE_FETCHING);
        TRANSITION.put(State.REMOTE_FETCHING, EventType.NO_MORE_DATA, State.CLEARING);

        TRANSITION.put(State.CLEARING, EventType.CLOSE, State.END);
    }

    public void handle(Event event) {
        State state = TRANSITION.get(curState, event.getType());
        if (state == null) {
            throw new IllegalStateException(
                String.format("Unknown state transition from state %s by event %s", curState, event));
        }

        State oldState = curState;
        curState = state;
        try {
            state.apply(this, event);
        }
        catch (Exception e) {
            curState = oldState;
            throw new IllegalStateException("Error happened in state transition handler method", e);
        }
    }

    public abstract Page getPage();

    protected abstract void build(Event event);

    protected abstract void fetchLocally(Event event);

    protected abstract void fetchRemotely(Event event);

    protected abstract void clear(Event event);

    /**
     * One page with scroll ID related.
     */
    public static class Page {
        public static final Page EMPTY = new Page(SearchHits.empty(), "");

        private SearchHits result;
        private String scrollId;

        public Page(SearchHits result, String scrollId) {
            this.result = result;
            this.scrollId = scrollId;
        }

        public SearchHits getResult() {
            return result;
        }

        public String getScrollId() {
            return scrollId;
        }

        @Override
        public String toString() {
            return "Page{" +
                "result=" + result +
                ", scrollId='" + scrollId + '\'' +
                '}';
        }
    }

    public enum State {
        START(NOOP),
        BUILDING(FSM::build),
        LOCAL_FETCHING(FSM::fetchLocally),
        REMOTE_FETCHING(FSM::fetchRemotely),
        CLEARING(FSM::clear),
        END(NOOP);

        private final BiConsumer<FSM, Event> handler;

        State(BiConsumer<FSM, Event> handler) {
            this.handler = handler;
        }

        public void apply(FSM fsm, Event event) {
            handler.accept(fsm, event);
        }
    }

    public enum EventType {
        BUILD, FETCH, NO_MORE_DATA, CLEAR, CLOSE
    }

    public static class Event {
        private final EventType type;
        private final SqlRequest request;
        private final QueryAction action;

        public Event(EventType type, Event event) {
            this.type = type;
            this.request = event.request;
            this.action = event.action;
        }

        public Event(EventType type, SqlRequest request, QueryAction action) {
            this.type = type;
            this.request = request;
            this.action = action;
        }

        public EventType getType() {
            return type;
        }

        public SqlRequest getRequest() {
            return request;
        }

        public QueryAction getAction() {
            return action;
        }
    }

}
