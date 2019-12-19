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

package com.amazon.opendistroforelasticsearch.sql.doctest.core.markup;

import com.amazon.opendistroforelasticsearch.sql.esintgtest.TestUtils;

import java.io.Closeable;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Document for different format and markup
 */
public interface Document extends Closeable {

    String DOCUMENT_FOLDER_ROOT = "docs/user/";

    /**
     * Remove checked IOException in method signature.
     */
    @Override
    void close();

    Document section(String title);

    Document subSection(String title);

    Document paragraph(String text);

    Document codeBlock(String description, String code);

    Document table(String description, String table);

    default Document code(String code) {
        return this;
    }

    static Path path(String templateRelativePath) {
        return Paths.get(TestUtils.getResourceFilePath(DOCUMENT_FOLDER_ROOT + templateRelativePath));
    }

}
