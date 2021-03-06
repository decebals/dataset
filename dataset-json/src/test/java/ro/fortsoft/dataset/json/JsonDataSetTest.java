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

import org.junit.Test;
import ro.fortsoft.dataset.core.AbstractDataSetTest;
import ro.fortsoft.dataset.core.BaseDataSetMetaData;
import ro.fortsoft.dataset.core.DataSet;
import ro.fortsoft.dataset.core.DataSetBuilder;
import ro.fortsoft.dataset.core.DataSetMetaData;

import static org.junit.Assert.*;

/**
 * @author Decebal Suiu
 */
public class JsonDataSetTest extends AbstractDataSetTest {

    @Override
    public DataSet createDataSet() {
        BaseDataSetMetaData dataSetMetaData = new BaseDataSetMetaData();
        dataSetMetaData.addField("name", String.class);
        dataSetMetaData.addField("gender", String.class);
        dataSetMetaData.addField("age", Integer.class);
        dataSetMetaData.addField("email", String.class);
        dataSetMetaData.addField("company", String.class);
        dataSetMetaData.addField("id", Integer.class);
        dataSetMetaData.addField("isActive", Boolean.class);

        DataSetBuilder builder = new JsonDataSet.Builder(getResourceStream("test.json"))
                .setMetaData(dataSetMetaData);

        return builder.build();
    }

    @Test
    public void testMetaData() {
        DataSetMetaData dataSetMetaData = dataSet.getMetaData();

        // "name","gender","age","email","company","id","isActive"
        int fieldCount = dataSetMetaData.getFieldCount();
        assertEquals(7, fieldCount);
        assertEquals("name", dataSetMetaData.getFieldName(0));
        assertEquals("isActive", dataSetMetaData.getFieldName(fieldCount - 1));
    }

    @Test
    public void testData() {
        assertTrue(dataSet.next());
        assertEquals(1, dataSet.getCursorPosition());
        assertEquals("Sharlene Pugh", dataSet.getObject(0));
        assertEquals("Sharlene Pugh", dataSet.getObject("name"));
        assertEquals("female", dataSet.getObject(1));
        assertEquals("female", dataSet.getObject("gender"));

        int count = 1;
        while (dataSet.next()) {
            count++;
        }
        assertEquals(8, dataSet.getCursorPosition());
        assertEquals(8, count);
    }

}
