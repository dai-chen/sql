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

import com.amazon.opendistroforelasticsearch.sql.esintgtest.SQLIntegTestCase;
import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.runner.JUnitCore;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class CorrectnessTest {

    @TestFactory
    Collection<DynamicTest> dynamicTestsWithCollection() {
        return Arrays.asList(
            DynamicTest.dynamicTest("Simple test", () -> assertEquals(2, Math.addExact(1, 1))),
            DynamicTest.dynamicTest("SELECT * FROM accounts WHERE age > 10", this::ensureCorrectness)
        );
    }

    private void ensureCorrectness() {
        Assert.assertEquals(queryElasticsearch(), queryOtherDb());
    }

    private String queryElasticsearch() {
        JUnitCore runner = new JUnitCore();

        System.setProperty("query", "SELECT * FROM accounts");

        //runner.run(new OneTestCase("SELECT * FROM accounts"));
        runner.run(OneTestCase.class);

        /*
        runner.run(
            new SQLIntegTestCase() {

                public void test() throws IOException {
                    executeQuery("SELECT * FROM accounts");
                }

            }.getClass()
        );
        */
        return "";
    }

    private String queryOtherDb() {
        return "";
    }

    public static class OneTestCase extends SQLIntegTestCase {

        public void test() throws IOException {
            String sql = System.getProperty("query");
            executeQuery(sql);
        }
    }

}
