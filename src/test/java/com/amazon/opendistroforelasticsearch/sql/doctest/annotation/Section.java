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

package com.amazon.opendistroforelasticsearch.sql.doctest.annotation;

import com.amazon.opendistroforelasticsearch.sql.doctest.core.RequestFormat;
import com.amazon.opendistroforelasticsearch.sql.doctest.core.ResponseFormat;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.amazon.opendistroforelasticsearch.sql.doctest.core.RequestFormat.KIBANA;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.ResponseFormat.PRETTY_JSON;
import static com.amazon.opendistroforelasticsearch.sql.doctest.core.ResponseFormat.TABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(value = METHOD)
public @interface Section {

    int order() default 0;

    String title();

    String description();

    RequestFormat request() default KIBANA;

    ResponseFormat response() default TABLE;

    RequestFormat explainRequest() default KIBANA;

    ResponseFormat explainResponse() default PRETTY_JSON;

}
