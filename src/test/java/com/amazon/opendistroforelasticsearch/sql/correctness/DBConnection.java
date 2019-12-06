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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface DBConnection {

    void create(String tableName, String schema);

    void insert(String tableName, String[] fieldNames, List<String[]> batch);

    DBResult select(String query);

    class DBResult {
        private final Row names;
        private final Collection<Row> rows;

        public DBResult(Row names, Collection<Row> rows) {
            this.names = names;
            this.rows = rows;
        }

        public void addRow(Row row) {
            rows.add(row);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DBResult dbResult = (DBResult) o;
            return //names.equals(dbResult.names) &&
                rows.equals(dbResult.rows);
        }

        @Override
        public int hashCode() {
            return Objects.hash(/*names,*/ rows);
        }

        @Override
        public String toString() {
            return "DBResult: " + rows.stream().map(Row::toString).collect(Collectors.joining("\n"));
        }
    }

    class Row {
        private final Collection<?> columns;

        public Row(Collection<?> columns) {
            this.columns = columns;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Row row = (Row) o;
            return columns.equals(row.columns);
        }

        @Override
        public int hashCode() {
            return Objects.hash(columns);
        }

        @Override
        public String toString() {
            return "Row " + columns;
        }
    }

}
