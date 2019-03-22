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

import com.amazon.opendistroforelasticsearch.sql.query.QueryAction;

public interface QueryContext {

    /**
     * Query action with all information of the original query.
     * @return  query action
     */
    QueryAction queryAction();

    /**
     * Handle and transit state according to incoming event.
     * @param event     event
     */
    void handle(Event event);

    interface Event {
    }

    class BuildEvent implements Event {
    }

    class FetchEvent implements Event {
    }

    class ClearEvent implements Event {
    }
}
