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

package com.amazon.opendistroforelasticsearch.sql.doctest.core;

import com.amazon.opendistroforelasticsearch.sql.utils.StringUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class SqlRequest {

    private final Request request;

    public SqlRequest(String method, String endpoint, String body, UrlParam... params) {
        this.request = makeRequest(method, endpoint, body, params);
    }

    SqlResponse send(RestClient client) {
        try {
            return new SqlResponse(client.performRequest(request));
        } catch (IOException e) {
            throw new IllegalStateException(StringUtils.format(
                "Exception occurred during sending request %s", request), e);
        }
    }

    private Request makeRequest(String method, String endpoint, String body, UrlParam[] params) {
        Request request = new Request(method, endpoint);
        request.setJsonEntity(body);
        for (UrlParam param : params) {
            request.addParameter(param.key, param.value);
        }

        RequestOptions.Builder restOptionsBuilder = RequestOptions.DEFAULT.toBuilder();
        restOptionsBuilder.addHeader("Content-Type", "application/json");
        request.setOptions(restOptionsBuilder);
        return request;
    }

    static class UrlParam {
        String key;
        String value;

        UrlParam(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(request.getMethod()).
            append(" ").
            append(request.getEndpoint());

        if (!request.getParameters().isEmpty()) {
            str.append("?");
            request.getParameters().forEach(
                (k, v) -> str.append(k).append("=").append(v)
            );
        }

        str.append('\n').
            append(request.getEntity());
        return str.toString();
    }
}
