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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Decebal Suiu
 */
public class ResultSetDataSet implements DataSet {

    private ResultSet resultSet;
    private DataSetMetaData metaData;

    private int cursorPosition;

    public ResultSetDataSet(ResultSet resultSet) {
        this.resultSet = resultSet;

        try {
            metaData = new MetaData(resultSet.getMetaData());
        } catch (SQLException e) {
            throw new DataSetException(e);
        }
    }

    @Override
    public DataSetMetaData getMetaData() {
        return metaData;
    }

    @Override
    public boolean next() {
        try {
            boolean hasNext = resultSet.next();
            if (hasNext) {
                cursorPosition++;
            }

            return hasNext;
        } catch (SQLException e) {
            throw new DataSetException(e);
        }
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    @Override
    public Object getObject(int fieldIndex) {
        try {
            return resultSet.getObject(fieldIndex + 1);
        } catch (SQLException e) {
            throw new DataSetException(e);
        }
    }

    @Override
    public Object getObject(String fieldName) {
        try {
            return resultSet.getObject(fieldName);
        } catch (SQLException e) {
            throw new DataSetException(e);
        }
    }

    @Override
    public void close() {
        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new DataSetException(e);
        }
    }

    public static class MetaData implements DataSetMetaData {

        private ResultSetMetaData resultSetMetaData;

        public MetaData(ResultSetMetaData resultSetMetaData) {
            this.resultSetMetaData = resultSetMetaData;
        }

        @Override
        public String getFieldName(int fieldIndex) {
            try {
                return resultSetMetaData.getColumnName(fieldIndex + 1);
            } catch (SQLException e) {
                throw new DataSetException(e);
            }
        }

        @Override
        public int getFieldIndex(String fieldName) {
            for (int i = 0; i < getFieldCount(); i++) {
                if (fieldName.equals(getFieldName(i))) {
                    return i;
                }
            }

            return -1;
        }

        @Override
        public Class<?> getFieldClass(int fieldIndex) {
            try {
                return getClass().getClassLoader().loadClass(resultSetMetaData.getColumnClassName(fieldIndex));
            } catch (SQLException | ClassNotFoundException e) {
                throw new DataSetException(e);
            }
        }

        @Override
        public int getFieldCount() {
            try {
                return resultSetMetaData.getColumnCount();
            } catch (SQLException e) {
                throw new DataSetException(e);
            }
        }

    }

}
