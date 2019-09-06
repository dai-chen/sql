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

package com.amazon.opendistroforelasticsearch.sql.antlr.syntax;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.IntervalSet;

/**
 * Syntax analysis error listener that handles any syntax error by throwing exception with useful information.
 */
public class SyntaxAnalysisErrorListener extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg,
                            RecognitionException e) {

        CommonTokenStream tokens = (CommonTokenStream) recognizer.getInputStream();
        Token offendingToken = (Token) offendingSymbol;
        String query = tokens.getText();

        // As official JavaDoc says, null means parser was able to recover from the error
        // In other words, "msg" argument includes the information we want.
        String suggestion;
        if (e == null) {
            suggestion = "More details: " + msg;
        } else {
            IntervalSet followSet = e.getExpectedTokens();
            suggestion = "Expecting tokens: " + followSet.toString(recognizer.getVocabulary());
        }

        throw new SqlSyntaxAnalysisException(
            "Failed to parse query due to offending symbol [%s] at: '%s' <--- HERE... %s",
            offendingToken.getText(),
            query.substring(0, offendingToken.getStopIndex() + 1),
            suggestion
        );
    }

}