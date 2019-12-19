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

import com.google.common.base.Strings;

import java.io.PrintWriter;

/**
 * ReStructure Text document
 */
public class RstDocument implements Document {

    private final PrintWriter docWriter;
    private final PrintWriter codeWriter;

    public RstDocument(PrintWriter docWriter, PrintWriter codeWriter) {
        this.docWriter = docWriter;
        this.codeWriter = codeWriter;
    }

    @Override
    public Document section(String title) {
        return printTitleWithUnderline(title, "=");
    }

    @Override
    public Document subSection(String title) {
        return printTitleWithUnderline(title, "-");
    }

    @Override
    public Document paragraph(String text) {
        return println(text);
    }

    @Override
    public Document codeBlock(String description, String code) {
        if (!Strings.isNullOrEmpty(code)) {
            return println(description + "::", indent(code));
        }
        return this;
    }

    @Override
    public Document code(String code) {
        if (!Strings.isNullOrEmpty(code)) {
            printCode(code);
        }
        return this;
    }

    @Override
    public Document viewInConsole() {
        docWriter.println(
            "`View in Console "
            + "<http://ec2co-ecsel-1n2oqfionlflu-1608091797.us-west-2.elb.amazonaws.com/app/kibana#/dev_tools/console?load_from="
            + "https://raw.githubusercontent.com/dai-chen/sql/docs-for-long-living-es/docs/user/admin/settings.rst.console>`_");
        docWriter.println();
        return this;
    }

    @Override
    public Document table(String description, String table) {
        if (!Strings.isNullOrEmpty(table)) {
            // RST table is different and not supposed to indent
            return println(description + "::", table);
        }
        return this;
    }

    @Override
    public void close() {
        codeWriter.close();
        docWriter.close();
    }

    private Document printTitleWithUnderline(String title, String underlineChar) {
        return print(
            title,
            Strings.repeat(underlineChar, title.length())
        );
    }

    /** Print each line with a blank line at last */
    private Document print(String... lines) {
        for (String line : lines) {
            docWriter.println(line);
        }
        docWriter.println();
        return this;
    }

    /** Print each line with a blank line followed */
    private Document println(String... lines) {
        for (String line : lines) {
            docWriter.println(line);
            docWriter.println();
        }
        return this;
    }

    private void printCode(String code) {
        codeWriter.println(code);
        codeWriter.println();
    }

    private String indent(String text) {
        return "\t" + text.replaceAll("\\n", "\n\t");
    }

}
