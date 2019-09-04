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

package com.amazon.opendistroforelasticsearch.sql.antlr;

import com.amazon.opendistroforelasticsearch.sql.antlr.syntax.SyntaxAnalysisException;
import org.junit.Test;

/**
 * Test cases focused on illegal syntax testing (blacklist) along with a few normal cases not covered previously.
 * All other normal cases should be covered in existing unit test and IT.
 */
public class SyntaxAnalysisTest {

    /** In reality exception occurs before reaching new parser for now */
    @Test(expected = SyntaxAnalysisException.class)
    public void unknownKeywordShouldThrowException() {
        analyze("INSERT INTO accounts VALUES ('a')");
    }

    //@Test(expected = SyntaxAnalysisException.class)
    public void unknownLiteralShouldThrowException() {
    }

    /**
     * Why we need to let it go and verify in semantic analyzer?
     *  Parser treats LOG123 a valid column and stops at '(' which gives wrong location and expected token
     *  In this case it's hard for parser to figure out if this is a wrong function name indeed or not.
     *  So we let it pass as an UDF and fail in semantic analyzer with meaningful message.
     */
    @Test //(expected = SyntaxAnalysisException.class)
    public void unknownFunctionShouldThrowException() {
        analyze("SELECT * FROM accounts WHERE LOG123(balance) = 1");
    }

    @Test(expected = SyntaxAnalysisException.class)
    public void unknownOperatorShouldThrowException() {
        analyze("SELECT * FROM accounts WHERE age <=> 1");
    }

    @Test(expected = SyntaxAnalysisException.class)
    public void missingFromClauseShouldThrowException() {
        analyze("SELECT 1");
    }

    @Test
    public void someKeywordsShouldBeAbleToUseAsIdentifier() {
        analyze("SELECT AVG(balance) AS avg FROM accounts");
    }

    public void dateLiteralShouldSucceed() {
    }

    private void analyze(String sql) {
        new OpenDistroSqlAnalyzer().analyze(sql);
    }
}
