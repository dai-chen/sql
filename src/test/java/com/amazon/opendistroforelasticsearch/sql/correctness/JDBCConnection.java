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

import com.amazon.opendistroforelasticsearch.sql.utils.StringUtils;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Database connection by JDBC standards.
 */
public class JDBCConnection implements DBConnection {

    private final String databaseName;

    private final Connection connection;

    public JDBCConnection(String databaseName, String connectionUrl) {
        this.databaseName = databaseName;
        try {
            //Class.forName(driverName);
            connection = DriverManager.getConnection(connectionUrl);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void create(String tableName, String schema) {
        JSONObject json = (JSONObject) new JSONObject(schema).query("/_doc/properties");
        try {
            String types = json.keySet().stream().
                                         map(key -> key + " " + mapToJDBCType(json.getJSONObject(key).getString("type"))).
                                         collect(Collectors.joining(","));

            Statement stmt = connection.createStatement();
            stmt.executeUpdate(StringUtils.format("CREATE TABLE %s(%s)", tableName, types));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void insert(String tableName, String[] columnNames, List<String[]> batch) {
        try {
            Statement stmt = connection.createStatement();
            for (String[] fieldValues : batch) {

                String values = Arrays.stream(fieldValues).
                                       map(val -> val.replace("'", "''")).
                                       map(val -> "'" + val + "'").
                                       collect(Collectors.joining(","));

                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO ").
                    append(tableName).
                    append("(").
                    append(String.join(",", columnNames)).
                    append(") VALUES (").
                    append(values).
                    append(")");

                stmt.addBatch(sql.toString());

                //stmt.addBatch(StringUtils.format("INSERT INTO %s(%s) VALUES ('%s')",
                //    tableName, String.join(",", fieldNames), String.join("','", fieldValues)));
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public DBResult select(String query) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();

            Map<String, String> nameAndTypes = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                nameAndTypes.put(metaData.getColumnName(i), metaData.getColumnTypeName(i));
            }

            DBResult result = new DBResult(databaseName, nameAndTypes, new HashSet<>()); //TODO: List for ORDER BY
            while (resultSet.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= nameAndTypes.size(); i++) {

                    Object value = resultSet.getObject(i);
                    if (value instanceof Float || value instanceof Double) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        df.setRoundingMode(RoundingMode.CEILING);
                        String numStr = df.format(value);
                        value = (value instanceof Float) ? Float.parseFloat(numStr) : Double.parseDouble(numStr);
                    }
                    row.add(value);

                    //row.add(resultSet.getObject(i));
                }
                result.addRow(new Row(row));
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String mapToJDBCType(String esType) {
        switch (esType.toUpperCase()) {
            case "KEYWORD": return "TEXT";
            default: return esType;
        }
    }

}
