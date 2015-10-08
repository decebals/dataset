/*
 * Copyright (C) 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.fortsoft.dataset.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Decebal Suiu
 */
public class BaseDataSetMetaData implements DataSetMetaData {

    private List<String> fieldNames;
    private List<Class<?>> fieldClasses;

    public BaseDataSetMetaData() {
        fieldNames = new ArrayList<>();
        fieldClasses = new ArrayList<>();
    }

    @Override
    public String getFieldName(int fieldIndex) {
        return fieldNames.get(fieldIndex);
    }

    @Override
    public int getFieldIndex(String fieldName) {
        return fieldNames.indexOf(fieldName);
    }

    @Override
    public Class<?> getFieldClass(int fieldIndex) {
        return fieldClasses.get(fieldIndex);
    }

    @Override
    public int getFieldCount() {
        return fieldNames.size();
    }

    public void addField(String fieldName, Class<?> fieldClass, int fieldIndex) {
        fieldNames.add(fieldIndex, fieldName);
        fieldClasses.add(fieldIndex, fieldClass);
    }

    public void addField(String fieldName, Class<?> fieldClass) {
        fieldNames.add(fieldName);
        fieldClasses.add(fieldClass);
    }

}
