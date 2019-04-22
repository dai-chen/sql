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

package com.amazon.opendistroforelasticsearch.sql.query;

import com.amazon.opendistroforelasticsearch.sql.domain.QueryStatement;
import com.amazon.opendistroforelasticsearch.sql.exception.SqlParseException;
import com.amazon.opendistroforelasticsearch.sql.executor.format.Option;
import com.amazon.opendistroforelasticsearch.sql.request.SqlRequest;
import org.elasticsearch.client.Client;

public interface QueryAction {

    SqlElasticRequestBuilder explain() throws SqlParseException;

    QueryStatement getQueryStatement();

    Client getClient();

    void setSqlRequest(SqlRequest sqlRequest);

    SqlRequest getSqlRequest();

    default Option[] options() {
        return new Option[0];
    }
}
