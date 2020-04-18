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

package com.amazon.opendistroforelasticsearch.sql.sql.engine;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * N-ary tuple
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Tuple implements Iterable<Entry<String, Object>> {

    private final Map<String, Object> valueByNames;

    public Tuple() {
        this(Collections.emptyMap());
    }

    public boolean isEmpty() {
        return valueByNames.isEmpty();
    }

    public int size() {
        return valueByNames.size();
    }

    public Object resolve(String name) {
        return valueByNames.get(name);
    }

    @Override
    public Iterator<Entry<String, Object>> iterator() {
        return valueByNames.entrySet().iterator();
    }

    public void iterate(BiConsumer<Integer, Entry<String, Object>> consumer) {
        Entry<String, Object>[] entries = valueByNames.entrySet().toArray(new Entry[0]);
        for (int i = 0; i < entries.length; i++) {
            consumer.accept(i, entries[i]);
        }
    }
}
