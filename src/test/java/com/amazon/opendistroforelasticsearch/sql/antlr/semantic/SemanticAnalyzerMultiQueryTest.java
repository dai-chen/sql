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

package com.amazon.opendistroforelasticsearch.sql.antlr.semantic;

import org.junit.Test;

/**
 * Semantic analyzer tests for multi query like UNION and MINUS
 */
public class SemanticAnalyzerMultiQueryTest extends SemanticAnalyzerTestBase {

    @Test
    public void unionDifferentResultTypeOfTwoQueriesShouldFail() {
        expectValidationFailWithErrorMessages(
            "SELECT balance FROM semantics UNION SELECT address FROM semantics",
            "Operator [UNION] cannot work with [(DOUBLE), (TEXT)]."
        );
    }

    @Test
    public void unionDifferentNumberOfResultTypeOfTwoQueriesShouldFail() {
        expectValidationFailWithErrorMessages(
            "SELECT balance FROM semantics UNION SELECT balance, age FROM semantics",
            "Operator [UNION] cannot work with [(DOUBLE), (DOUBLE, INTEGER)]."
        );
    }

    @Test
    public void minusDifferentResultTypeOfTwoQueriesShouldFail() {
        expectValidationFailWithErrorMessages(
            "SELECT p.active FROM semantics s, s.projects p MINUS SELECT address FROM semantics",
            "Operator [MINUS] cannot work with [(BOOLEAN), (TEXT)]."
        );
    }

    @Test
    public void unionSameResultTypeOfTwoQueriesShouldPass() {
        validate("SELECT balance FROM semantics UNION SELECT balance FROM semantics");
    }

    @Test
    public void unionCompatibleResultTypeOfTwoQueriesShouldPass() {
        validate("SELECT balance FROM semantics UNION SELECT age FROM semantics");
        validate("SELECT address FROM semantics UNION ALL SELECT city FROM semantics");
    }

    @Test
    public void minusSameResultTypeOfTwoQueriesShouldPass() {
        validate("SELECT s.projects.active FROM semantics s UNION SELECT p.active FROM semantics s, s.projects p");
    }

    @Test
    public void minusCompatibleResultTypeOfTwoQueriesShouldPass() {
        validate("SELECT address FROM semantics MINUS SELECT manager.name.keyword FROM semantics");
    }

    @Test
    public void unionSelectStarWithExtraFieldOfTwoQueriesShouldPass() {
        expectValidationFailWithErrorMessages(
            "SELECT * FROM semantics UNION SELECT *, city FROM semantics",
            "Operator [UNION] cannot work with [(), (KEYWORD)]."
        );
    }

    @Test
    public void minusSelectStarWithExtraFieldOfTwoQueriesShouldPass() {
        expectValidationFailWithErrorMessages(
            "SELECT *, address, balance FROM semantics MINUS SELECT * FROM semantics",
            "Operator [MINUS] cannot work with [(TEXT, DOUBLE), ()]."
        );
    }

    @Test
    public void unionSelectStarOfTwoQueriesShouldPass() {
        validate("SELECT * FROM semantics UNION SELECT * FROM semantics");
        validate("SELECT *, age FROM semantics UNION SELECT *, balance FROM semantics");
    }

}
