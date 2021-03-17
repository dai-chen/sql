/*
 *    Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License").
 *    You may not use this file except in compliance with the License.
 *    A copy of the License is located at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file. This file is distributed
 *    on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *    express or implied. See the License for the specific language governing
 *    permissions and limitations under the License.
 *
 */

package com.amazon.opendistroforelasticsearch.sql.data.model;

import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amazon.opendistroforelasticsearch.sql.exception.ExpressionEvaluationException;
import org.junit.jupiter.api.Test;

class ExprStringValueTest {

  @Test
  void boolean_value_from_bool_text() {
    assertTrue(new ExprStringValue("true").booleanValue());
    assertTrue(new ExprStringValue("True").booleanValue());
    assertFalse(new ExprStringValue("false").booleanValue());
    assertFalse(new ExprStringValue("FALSE").booleanValue());
  }

  @Test
  void boolean_value_from_invalid_type() {
    assertThrows(ExpressionEvaluationException.class,
        () -> new ExprStringValue("1").booleanValue());
  }

  @Test
  void equal_to_same_type() {
    assertTrue(new ExprStringValue("hello").equal(new ExprStringValue("hello")));
    assertTrue(new ExprStringValue("true").equal(ExprBooleanValue.of(TRUE)));
  }

  @Test
  void equal_to_other_type() {
    assertTrue(new ExprStringValue("hello").equal(new ExprStringValue("hello")));
    assertTrue(new ExprStringValue("true").equal(ExprBooleanValue.of(TRUE)));
  }

}