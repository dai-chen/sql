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
import com.google.common.base.Strings;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.APPEND;

/**
 * ReStructure Text
 */
public class RstDocument implements Document {

    private final Path documentPath;

    RstDocument(Path documentPath) {
        this.documentPath = documentPath;
    }

    @Override
    public void add(Section section) {
        try (PrintWriter docWriter = new PrintWriter(Files.newBufferedWriter(documentPath, APPEND))) {
            docWriter.println();
            docWriter.println(section.title);
            docWriter.println(Strings.repeat("=", section.title.length()));
            docWriter.println();
            docWriter.println(section.description + "::");

            for (Example example : section.examples) {
                docWriter.println();
                docWriter.println(indent(example.query));
                docWriter.println();
                docWriter.println(example.response);
                docWriter.println();
            }
        } catch (IOException e) {
            throw new IllegalStateException(StringUtils.format(
                "Failed to open document file [%s]", documentPath), e);
        }
    }

    private String indent(String original) {
        return "\t" + original.replaceAll("\\n", "\n\t");
    }

}
