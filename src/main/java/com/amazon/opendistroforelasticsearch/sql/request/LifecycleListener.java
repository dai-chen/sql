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

package com.amazon.opendistroforelasticsearch.sql.request;

/**
 * Sql request lifecycle interface to register hook for action on different phases of a request
 */
public interface LifecycleListener {

    default void onStart(SqlRequest request) {}

    default void onStartParsing(SqlRequest request) {}

    default void onCompleteParsing(SqlRequest request) {}

    default void onStartTranslating(SqlRequest request) {}

    default void onCompleteTranslating(SqlRequest request) {}

    default void onStartExecuting(SqlRequest request) {}

    default void onCompleteExecuting(SqlRequest request) {}

    default void onComplete(SqlRequest request) {}

    default void onException(SqlRequest request, Throwable t) {}
}
