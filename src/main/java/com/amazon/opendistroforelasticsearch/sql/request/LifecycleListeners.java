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

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregated lifecycle listener which notifies all listeners.
 */
public class LifecycleListeners implements LifecycleListener {

    private List<LifecycleListener> listeners = new ArrayList<>();

    @Override
    public void onStart(SqlRequest request) {
        listeners.forEach(listener -> listener.onStart(request));
    }

    @Override
    public void onStartParsing(SqlRequest request) {
        listeners.forEach(listener -> listener.onStartParsing(request));
    }

    @Override
    public void onCompleteParsing(SqlRequest request) {
        listeners.forEach(listener -> listener.onCompleteParsing(request));
    }

    @Override
    public void onStartTranslating(SqlRequest request) {
        listeners.forEach(listener -> listener.onStartTranslating(request));
    }

    @Override
    public void onCompleteTranslating(SqlRequest request) {
        listeners.forEach(listener -> listener.onCompleteTranslating(request));
    }

    @Override
    public void onStartExecuting(SqlRequest request) {
        listeners.forEach(listener -> listener.onStartExecuting(request));
    }

    @Override
    public void onCompleteExecuting(SqlRequest request) {
        listeners.forEach(listener -> listener.onCompleteExecuting(request));
    }

    @Override
    public void onComplete(SqlRequest request) {
        listeners.forEach(listener -> listener.onComplete(request));
    }

    @Override
    public void onException(SqlRequest request, Throwable t) {
        listeners.forEach(listener -> listener.onException(request, t));
    }
}
