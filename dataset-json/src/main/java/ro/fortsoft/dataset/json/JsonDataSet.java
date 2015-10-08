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
package ro.fortsoft.dataset.json;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import ro.fortsoft.dataset.core.DataSet;
import ro.fortsoft.dataset.core.DataSetBuilder;
import ro.fortsoft.dataset.core.DataSetException;
import ro.fortsoft.dataset.core.DataSetMetaData;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Decebal Suiu
 */
public class JsonDataSet implements DataSet {

    private InputStream inputStream;
    private DataSetMetaData metaData;

    private MappingIterator<Map<String, Object>> rows;
    private Map<String, Object> currentRow;
    private int cursorPosition;

    private JsonDataSet(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public DataSetMetaData getMetaData() {
        return metaData;
    }

    @Override
    public boolean next() {
        if (cursorPosition == 0) {
            init();
        }

        boolean hasNext = rows.hasNext();
        if (hasNext) {
            currentRow = rows.next();
            cursorPosition++;
        }

        return hasNext;
    }

    @Override
    public int getCursorPosition() {
        return cursorPosition;
    }

    @Override
    public Object getObject(int fieldIndex) {
        return getObject(metaData.getFieldName(fieldIndex));
    }

    @Override
    public Object getObject(String fieldName) {
        return currentRow.get(fieldName);
    }

    @Override
    public void close() {
        if (rows != null) {
            try {
                rows.close();
            } catch (IOException e) {
                throw new DataSetException("Cannot close data set", e);
            }
        }
    }

    /*
    protected DataSetMetaData createDefaultMetaData() {
        BaseDataSetMetaData metaData = new BaseDataSetMetaData();
        JsonSchema parserSchema = (JsonSchema) rows.getParserSchema();
        int columnCount = parserSchema.size();
        for (int i = 0; i < columnCount; i++) {
            CsvSchema.Column column = parserSchema.column(i);
            CsvSchema.ColumnType columnType = column.getType();
            metaData.addField(column.getIndex(), column.getName(), columnType.getClass());
        }

        return metaData;
    }
    */

    protected void init() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.reader(Map.class);

        try {
            rows = reader.readValues(inputStream);
        } catch (IOException e) {
            throw new DataSetException(e);
        }
    }

    public static class Builder implements DataSetBuilder {

        private JsonDataSet dataSet;

        public Builder(InputStream inputStream) {
            dataSet = new JsonDataSet(inputStream);
        }

        public Builder setMetaData(DataSetMetaData metaData) {
            dataSet.metaData = metaData;

            return this;
        }

        @Override
        public JsonDataSet build() {
            if (dataSet.metaData == null) {
//                dataSet.metaData = dataSet.createDefaultMetaData();
                throw new IllegalArgumentException("JsonDataSet.metaData cannot be null");
            }

            return dataSet;
        }

    }

}
