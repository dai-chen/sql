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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Abstraction for different databases.
 */
public interface DBConnection {

    void create(String tableName, String schema);

    void insert(String tableName, String[] columnNames, List<String[]> batch);

    DBResult select(String query);

    void close();

    class DBResult {
        private final String databaseName;
        private final Map<String, String> colTypeByName;
        private final Collection<Row> dataRows;

        public DBResult(String databaseName, Map<String, String> colTypeByName, Collection<Row> rows) {
            this.databaseName = databaseName;
            this.colTypeByName = colTypeByName;
            this.dataRows = rows;
        }

        public void addRow(Row row) {
            dataRows.add(row);
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public Map<String, String> getColumnNameAndTypes() {
            return colTypeByName;
        }

        public Collection<Row> getDataRows() {
            return dataRows;
        }

        public boolean isCloseTo(DBResult other) {
            //TODO compare header ignore cases
            if (dataRows.size() != other.dataRows.size()) {
                return false;
            }

            Iterator<Row> it = dataRows.iterator();
            Iterator<Row> otherIt = other.dataRows.iterator();
            while (it.hasNext()) {
                Row row = it.next();
                Row otherRow = otherIt.next();
                if (!row.isCloseTo(otherRow)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DBResult result = (DBResult) o;
            return /*databaseName.equals(result.databaseName) &&
                colTypeByName.equals(result.colTypeByName) && */
                dataRows.equals(result.dataRows);
        }

        @Override
        public int hashCode() {
            return Objects.hash(/*databaseName, colTypeByName,*/ dataRows);
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

        public boolean isCloseTo(Row other) {
            Iterator<?> it = columns.iterator();
            Iterator<?> otherIt = other.columns.iterator();
            while (it.hasNext()) {
                Object value = it.next();
                Object otherValue = otherIt.next();

                if (value instanceof Float) {
                    if (!(otherValue instanceof Float) || isDeltaLarge((Float) value, (Float) otherValue)) {
                        return false;
                    }
                } else if (value instanceof Double) {
                    if (!(otherValue instanceof Double) || isDeltaLarge((Double) value, (Double) otherValue)) {
                        return false;
                    }
                } else {
                    if (!value.equals(otherValue)) {
                        return false;
                    }
                }
            }
            return true;
        }

        private boolean isDeltaLarge(double num1, double num2) {
            return Math.abs(num1 - num2) >= 0.01;
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
            return "Row: " + columns;
        }
    }

}
