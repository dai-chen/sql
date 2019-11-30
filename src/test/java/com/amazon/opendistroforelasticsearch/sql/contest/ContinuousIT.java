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

package com.amazon.opendistroforelasticsearch.sql.contest;

import com.amazon.opendistroforelasticsearch.sql.esintgtest.SQLIntegTestCase;
import com.amazon.opendistroforelasticsearch.sql.esintgtest.TestsConstants;
import org.junit.Test;

import java.io.IOException;

//@TestMethodProviders({ContinuousIT.DynamicTestCases.class})
public class ContinuousIT extends SQLIntegTestCase {

    @Override
    protected void init() throws Exception {
        loadIndex(Index.ACCOUNT);
    }

    @Test
    public void test() throws IOException {
        executeQuery("SELECT * FROM " + TestsConstants.TEST_INDEX_ACCOUNT);
    }

    /*
    This won't work because JUnit 4 only cares about Method instance rather than class instance.
    public static class DynamicTestCases implements TestMethodProvider {
        @Override
        public Collection<Method> getTestMethods(Class<?> suiteClass, ClassModel suiteClassModel) {
            SQLESTestCaseIT testCase = new SQLESTestCaseIT() {
                @Override
                public void test() {
                    System.out.println("hello");
                }
            };

            try {
                Method testMethod = testCase.getClass().getDeclaredMethod("test");
                return Collections.singleton(testMethod);

            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("e");
            }

        }
    }
    */

}
