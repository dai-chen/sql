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

package com.amazon.opendistroforelasticsearch.sql.esdomain.mapping;

import com.amazon.opendistroforelasticsearch.sql.domain.Field;
import com.google.common.base.CharMatcher;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse.FieldMappingMetaData;

/**
 * Field mapping that parse native ES mapping.
 *
 * NOTE that approaches in this class is NOT reliable because of the ES mapping query API used.
 * We should deprecate this in future and parse field mapping in more solid way.
 */
public class FieldMapping {

    /** Field (name) to be parsed */
    private final String fieldName;

    /** Native mapping information returned from ES */
    private final Map<String, FieldMappingMetaData> typeMappings;

    /** Map of field name to Field object that specified in query explicitly */
    private final Map<String, Field> specifiedFieldByNames;


    public FieldMapping(String fieldName) {
        this(fieldName, emptyMap(), emptyMap());
    }

    public FieldMapping(String fieldName,
                        Map<String, FieldMappingMetaData> typeMappings,
                        Map<String, Field> specifiedFieldByNames) {

        this.fieldName = fieldName;
        this.typeMappings = typeMappings;
        this.specifiedFieldByNames = specifiedFieldByNames;
    }

    /**
     * Is field specified explicitly in query
     * @return true if specified
     */
    public boolean isSpecified() {
        return specifiedFieldByNames.containsKey(fieldName);
    }

    /**
     * Is field a property field, which means either object field or nested field.
     * @return true for property field
     */
    public boolean isPropertyField() {
        int numOfDots = CharMatcher.is('.').countIn(fieldName); // Move to our own StringUtils
        return numOfDots > 1 || (numOfDots == 1 && !isMultiField());
    }

    /**
     * Path of property field, for example "employee" in "employee.manager"
     * @return path of property field
     */
    public String path() {
        int lastDot = fieldName.lastIndexOf(".");
        return fieldName.substring(0, lastDot);
    }

    /**
     * Verify if property field matches wildcard pattern specified in query
     * @return true if matched
     */
    public boolean isWildcardSpecified() {
        return specifiedFieldByNames.containsKey(path() + ".*");
    }

    /**
     * Is field a/in multi-field, for example, field "a.keyword" in field "a"
     * @return true for multi field
     */
    public boolean isMultiField() {
        return fieldName.endsWith(".keyword");
    }

    /**
     * Is field meta field, such as _id, _index, _source etc.
     * @return true for meta field
     */
    public boolean isMetaField() {
        return fieldName.startsWith("_");
    }

    /**
     * Used to retrieve the type of fields from metaData map structures for both regular and nested fields
     */
    @SuppressWarnings("unchecked")
    public String type() {
        FieldMappingMetaData metaData = typeMappings.get(fieldName);
        Map<String, Object> source = metaData.sourceAsMap();
        String[] fieldPath = fieldName.split("\\.");

        /*
         * When field is not nested the metaData source is fieldName -> type
         * When it is nested or contains "." in general (ex. fieldName.nestedName) the source is nestedName -> type
         */
        Map<String, Object> fieldMapping;
        if (fieldPath.length < 2) {
            fieldMapping = (Map<String, Object>) source.get(fieldName);
        } else {
            fieldMapping = (Map<String, Object>) source.get(fieldPath[1]);
        }

        for (int i = 2; i < fieldPath.length; i++) {
            fieldMapping = (Map<String, Object>) fieldMapping.get(fieldPath[i]);
        }

        return (String) fieldMapping.get("type");
    }

}
