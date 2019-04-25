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
import com.amazon.opendistroforelasticsearch.sql.context.cursor.CursorQueryContext;
import com.amazon.opendistroforelasticsearch.sql.query.QueryAction;
import com.amazon.opendistroforelasticsearch.sql.request.SqlRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Query context manager
 */
public class QueryContextManager {

    private final Map<ContextId, QueryContext> contextById = new HashMap<>(); // TODO: concurrency control and expiration policy

    public boolean isContextual(SqlRequest request) {
        return isNewContext(request) || isExistingContext(request); // TODO: assume cursor exists upon specified without validation for now
    }

    /**
     * Create query context if not any. Reuse only if found locally which is distributed by coordinator.
     * @return
     */
    public QueryContext create() {
        CursorQueryContext context = new CursorQueryContext();
        return context;
    }

    /**
     * Does query request have contextual field which indicates that parsing/translating work can be skipped.
     *
     * @param request   query request
     * @return          true if request has contextual field
     */
    private boolean isExistingContext(SqlRequest request) {
        return !request.cursor().isEmpty();
    }

    private boolean isNewContext(SqlRequest request) {
        return request.fetchSize() > 0;
    }


    public QueryContext get(SqlRequest request, QueryAction action) {
        if (isExistingContext(request)) {
            return contextById.remove(new CursorContextId(request));
        }
        return new CursorQueryContext();
    }

    public void update(QueryContext context) {
        contextById.put(context.getId(), context);
    }

}
