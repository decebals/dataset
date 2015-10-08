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

import java.util.List;

/**
 * @author Decebal Suiu
 */
public class InMemoryDataSet implements DataSet {

    private List<Object[]> rows;
    private int cursorPosition;
    private DataSetMetaData metaData;

    public InMemoryDataSet(List<Object[]> rows) {
        this(rows, null);
    }

    public InMemoryDataSet(List<Object[]> rows, DataSetMetaData metaData) {
        if (rows == null) {
            throw new DataSetException("The 'rows' field cannot be null");
        }

        this.rows = rows;

        if (metaData != null) {
            if (!rows.isEmpty() && (rows.get(0).length != metaData.getFieldCount())) {
                throw new DataSetException("Invalid field count");
            }

            this.metaData = metaData;
        } else {
            if (rows.isEmpty()) {
                throw new DataSetException("Cannot create a meta data");
            }

            // TODO lazy creation in getMetaData !?
            this.metaData = createDefaultMetaData(rows);
        }
    }

    @Override
    public DataSetMetaData getMetaData() {
        return metaData;
    }

    @Override
    public boolean next() {
        if (cursorPosition < rows.size()) {
            cursorPosition++;
            return true;
        }

        return false;
    }

    @Override
    public int getCursorPosition() {
        return cursorPosition;
    }

    @Override
    public Object getObject(int fieldIndex) {
        return rows.get(cursorPosition - 1)[fieldIndex];
    }

    @Override
    public Object getObject(String fieldName) {
        return getObject(metaData.getFieldIndex(fieldName));
    }

    @Override
    public void close() {
        // do nothing
    }

    protected DataSetMetaData createDefaultMetaData(List<Object[]> rows) {
        BaseDataSetMetaData metaData = new BaseDataSetMetaData();
        for (int i = 0; i < rows.get(0).length; i++) {
            metaData.addField("Field_" + i, String.class);
        }

        return metaData;
    }

}
