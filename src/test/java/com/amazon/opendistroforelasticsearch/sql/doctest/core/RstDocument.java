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

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RstDocument implements Document {

    private PrintWriter docWriter;

    RstDocument(String templatePath, String documentPath) throws IOException {
        Path template = Paths.get(templatePath);
        Path document = Paths.get(documentPath);
        docWriter = new PrintWriter(Files.newBufferedWriter(
            Files.copy(template,
                       //Files.createFile(document),
                       document,
                       StandardCopyOption.REPLACE_EXISTING))
        );
    }

    @Override
    public void addExample(String description, String example) {
        docWriter.println(description + "::");
        docWriter.println();
        docWriter.println("\t" + example);
    }

}
