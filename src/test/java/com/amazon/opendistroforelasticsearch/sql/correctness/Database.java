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

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Abstraction for different databases.
 */
public interface Database {

    void create(String tableName, String schema);

    void insert(String tableName, String[] columnNames, List<String[]> batch);

    DBResult select(String query);

    class DBResult {
        private final String databaseName;
        private final Row columnNames;
        private final Collection<Row> dataRows;

        public DBResult(String databaseName, Row columnNames, Collection<Row> rows) {
            this.databaseName = databaseName;
            this.columnNames = columnNames;
            this.dataRows = rows;
        }

        public void addRow(Row row) {
            dataRows.add(row);
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public Row getColumnNames() {
            return columnNames;
        }

        public Collection<Row> getDataRows() {
            return dataRows;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DBResult dbResult = (DBResult) o;
            return //names.equals(dbResult.names) &&
                dataRows.equals(dbResult.dataRows);
        }

        @Override
        public int hashCode() {
            return Objects.hash(/*names,*/ dataRows);
        }

        @Override
        public String toString() {
            return "DBResult: " + dataRows.stream().map(Row::toString).collect(Collectors.joining("\n"));
        }
    }

    class Row {
        private final Collection<?> columns;

        public Row(Collection<?> columns) {
            this.columns = columns;
        }

        public Collection<?> getColumns() {
            return columns;
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
