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

package com.amazon.opendistroforelasticsearch.sql.sql;

import com.amazon.opendistroforelasticsearch.sql.common.antlr.CaseInsensitiveCharStream;
import com.amazon.opendistroforelasticsearch.sql.sql.antlr.parser.OpenDistroSqlLexer;
import com.amazon.opendistroforelasticsearch.sql.sql.antlr.parser.OpenDistroSqlParser;
import com.amazon.opendistroforelasticsearch.sql.sql.antlr.parser.OpenDistroSqlParser.RootContext;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * SQL parser
 */
public class SQLParser {

    /**
     * Parse sql query to AST.
     */
    public Projection parse(String sql) {
        OpenDistroSqlParser parser =
            new OpenDistroSqlParser(
                new CommonTokenStream(
                    new OpenDistroSqlLexer(
                        new CaseInsensitiveCharStream(sql))));
        RootContext parseTree = parser.root();
        return parseTree.accept(new ASTBuilder());
    }

}
