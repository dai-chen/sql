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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Doc {

    private String name;

    private String description;

    private List<String> syntax = new ArrayList<>();

    private List<UseCase> useCases = new ArrayList<>();

    private List<String> constraints = new ArrayList<>();

    private static class UseCase {
        private String query;
        private JSONObject response;
    }

    public void format() { // abstraction needed
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Syntax: " + syntax);
        System.out.println("Use Case: " + useCases);
        System.out.println("Constraints: " + constraints);
    }
}
