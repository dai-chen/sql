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
import com.amazon.opendistroforelasticsearch.sql.request.SqlRequest;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;

/**
 * Cursor context ID
 */
public class CursorContextId implements ContextId {

    private static final String CURSOR_FIELD_NAME = "cursor";

    /** Cursor ID generated first time and passed by client after */
    private final String cursorId;

    public CursorContextId(SqlRequest request) {
        this.cursorId = request.cursor().isEmpty() ? generate() : request.cursor();
    }

    public CursorContextId(String cursorId) {
        this.cursorId = cursorId;
    }

    @Override
    public void format(JSONObject json) {
        json.append(CURSOR_FIELD_NAME, cursorId);
    }

    private String generate() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CursorContextId that = (CursorContextId) o;
        return Objects.equals(cursorId, that.cursorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cursorId);
    }

    @Override
    public String toString() {
        return "CursorContextId{" +
            "cursorId='" + cursorId + '\'' +
            '}';
    }

}
