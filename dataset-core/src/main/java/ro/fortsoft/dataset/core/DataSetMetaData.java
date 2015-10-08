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

/**
 * An object that can be used to get information about the types and properties of the columns in
 * a <code>DataSet</code> object.
 *
 * @author Decebal Suiu
 */
public interface DataSetMetaData {

    public String getFieldName(int fieldIndex);

    public int getFieldIndex(String fieldName);

    public Class<?> getFieldClass(int fieldIndex);

    /**
     * Gets number of fields in this data source.
     */
    public int getFieldCount();

    /**
     * Gets the native type of this column. A native type is the name of the data type as defined in the datastore.
     *
     * @return the name of the native type.
     */
//    public String getNativeType();

}
