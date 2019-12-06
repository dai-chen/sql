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

package com.amazon.opendistroforelasticsearch.sql.correctness;

import com.google.common.io.Resources;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * Test cases for Tableau integration testing
 */
public class TableauIT extends CorrectnessTestCase {

    @Override
    protected void init() throws Exception {
        prepareTableAndData("kibana_sample_data_flights.json", "kibana_sample_data_flights.csv");
    }

    @Test
    public void testIntegrationWithTableau() {
        iterateFileByLine("correctness/tableau_integration_tests.txt", this::executeQuery);
    }

    private void iterateFileByLine(String filePath, Consumer<String> query) {
        try {
            URL url = Resources.getResource(filePath);
            Files.lines(Paths.get(url.toURI())).forEach(query);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
