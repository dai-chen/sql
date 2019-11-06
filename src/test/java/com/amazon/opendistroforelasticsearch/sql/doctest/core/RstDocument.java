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

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.APPEND;

public class RstDocument implements Document {

    private final Path document;

    RstDocument(String documentPath) {
        document = Paths.get(documentPath);
    }

    @Override
    public void copyFrom(String templatePath) {
        Path template = Paths.get(templatePath);
        try {
            Files.copy(template, document, REPLACE_EXISTING, COPY_ATTRIBUTES);
        } catch (IOException e) {
            throw new IllegalStateException(StringUtils.format(
                "Failed to copy from template [%s] to document file [%s]", template, document), e);
        }
    }

    @Override
    public void addExample(String description, String example) {
        try (PrintWriter docWriter = new PrintWriter(Files.newBufferedWriter(document, APPEND))) {
            docWriter.println(description + "::");
            docWriter.println();
            docWriter.println("\t" + example);
        } catch (IOException e) {
            throw new IllegalStateException(StringUtils.format(
                "Failed to open document file [%s]", document), e);
        }
    }

}
