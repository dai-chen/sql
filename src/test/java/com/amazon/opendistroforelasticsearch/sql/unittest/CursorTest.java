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

package com.amazon.opendistroforelasticsearch.sql.unittest;

import com.amazon.opendistroforelasticsearch.sql.context.QueryContext;
import com.amazon.opendistroforelasticsearch.sql.context.cursor.CursorQueryContext;
import com.amazon.opendistroforelasticsearch.sql.exception.SqlParseException;
import com.amazon.opendistroforelasticsearch.sql.query.QueryAction;
import com.amazon.opendistroforelasticsearch.sql.query.SqlElasticSearchRequestBuilder;
import com.amazon.opendistroforelasticsearch.sql.request.SqlRequest;
import com.amazon.opendistroforelasticsearch.sql.util.ScrollSearchHits;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Cursor support unit test
 */
@RunWith(MockitoJUnitRunner.class)
public class CursorTest {

    private QueryContext context = new CursorQueryContext();

    @Mock
    private QueryAction action;

    @Mock
    private SearchResponse response;

    @Before
    public void setUp() throws SqlParseException {
        SearchRequestBuilder reqBuilder = mock(SearchRequestBuilder.class);
        when(action.explain()).thenReturn(new SqlElasticSearchRequestBuilder(reqBuilder));
        when(reqBuilder.setSize(any(Integer.class))).thenReturn(reqBuilder);
        when(reqBuilder.setScroll(any(TimeValue.class))).thenReturn(reqBuilder);
        when(reqBuilder.get()).thenReturn(response);
        when(response.getScrollId()).thenReturn("1");
    }

    @Test
    public void selectWithCursorLocally() {
        final String sql = "SELECT firstname FROM bank";
        final int fetchSize = 2;
        mockSearchHits(5, fetchSize);

        int[] expectSizes = {2, 2, 1};
        for (int expectSize : expectSizes) {
            SearchHits hits = context.fetch(request(sql, fetchSize), action);
            assertThat(hits.getHits(), arrayWithSize(expectSize));
        }
    }

    public void selectWithCursorRemotely() {
    }

    public void groupByWithCursorLocally() {
    }

    public void groupByWithCursorRemotely() {
    }

    public void joinWithCursorLocally() {
    }

    public void joinWithCursorRemotely() {
    }

    private SqlRequest request(String sql, int fetchSize) {
        return request(sql, new JSONObject("{\"fetch_size\":" + fetchSize + "}"));
    }

    private SqlRequest request(String sql, String cursor) {
        return request(sql, new JSONObject("{\"cursor\":" + cursor + "}"));
    }

    private SqlRequest request(String sql) {
        return new SqlRequest(sql, new JSONObject());
    }

    private SqlRequest request(String sql, JSONObject json) {
        return new SqlRequest(sql, json);
    }

    private void mockSearchHits(int total, int batchSize) {
        SearchHit[] hits = new SearchHit[total];
        for (int i = 0; i < total; i++) {
            hits[i] = new SearchHit(i);
        }
        when(response.getHits()).then(new ScrollSearchHits(hits, batchSize));
    }

}
