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

import com.google.common.base.Charsets;
import com.google.common.collect.Iterators;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Test data loader
 */
public class TestData {

    private final static String DEFAULT_FOLDER = "correctness/";
    private final String schemaFilePath;
    private final String dataFilePath;

    public TestData(String schemaFilePath, String dataFilePath) {
        this.schemaFilePath = DEFAULT_FOLDER + schemaFilePath;
        this.dataFilePath = DEFAULT_FOLDER + dataFilePath;
    }

    public void createTable(DBConnection conn) {
        try {
            URL url = Resources.getResource(schemaFilePath);
            String schema = Resources.toString(url, Charsets.UTF_8);
            conn.create(tableName(), schema);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void loadData(DBConnection conn) {
        try {
            URL url = Resources.getResource(dataFilePath);
            Stream<String[]> stream = Files.lines(Paths.get(url.toURI())).
                                      map(line -> line.split(","));

            Iterator<String[]> iterator = stream.iterator();
            String[] fieldNames = iterator.next();
            Iterators.partition(iterator, 100).
                      forEachRemaining(batch -> conn.insert(tableName(), fieldNames, batch));

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String tableName() {
        return dataFilePath.substring(
            dataFilePath.lastIndexOf('/') + 1,
            dataFilePath.lastIndexOf('.')
        );
    }

}
