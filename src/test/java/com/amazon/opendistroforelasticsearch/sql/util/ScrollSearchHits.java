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

package com.amazon.opendistroforelasticsearch.sql.util;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;

/**
 * Mock SearchHits and slice and return in batch.
 */
public class ScrollSearchHits implements Answer<SearchHits> {

    private final SearchHit[] allHits;

    private final int batchSize; //TODO: should be inferred from mock object dynamically

    private int callCnt;

    public ScrollSearchHits(SearchHit[] allHits, int batchSize) {
        this.allHits = allHits;
        this.batchSize = batchSize;
    }

    @Override
    public SearchHits answer(InvocationOnMock invocation) {
        SearchHit[] curBatch;
        if (isNoMoreBatch()) {
            curBatch = new SearchHit[0];
        } else {
            curBatch = currentBatch();
            callCnt++;
        }
        return new SearchHits(curBatch, allHits.length, 0);
    }

    public void reset() {
        callCnt = 0;
    }

    private boolean isNoMoreBatch() {
        return callCnt > allHits.length / batchSize;
    }

    private SearchHit[] currentBatch() {
        return Arrays.copyOfRange(allHits, startIndex(), endIndex());
    }

    private int startIndex() {
        return callCnt * batchSize;
    }

    private int endIndex() {
        return Math.min(startIndex() + batchSize, allHits.length);
    }

}
