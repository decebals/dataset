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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Decebal Suiu
 */
public class InMemoryDataSetTest extends AbstractDataSetTest {

    @Override
    public DataSet createDataSet() {
        List<Object[]> values = new ArrayList<>();
        Object[] row_1 = new Object[2];
        row_1[0] = "Jason";
        row_1[1] = 20;
        values.add(row_1);
        Object[] row_2 = new Object[2];
        row_2[0] = "Maria";
        row_2[1] = 18;
        values.add(row_2);

        /*
        BaseDataSetMetaData dataSetMetaData = new BaseDataSetMetaData();
        dataSetMetaData.addField("Name", String.class);
        dataSetMetaData.addField("Age", Integer.class);

        return new InMemoryDataSet(values, dataSetMetaData);
        */

        return new InMemoryDataSet(values);
    }

    @Test
    public void testMetaData() {
        DataSetMetaData dataSetMetaData = dataSet.getMetaData();

        int fieldCount = dataSetMetaData.getFieldCount();
        assertEquals(2, fieldCount);
        assertEquals("Field_0", dataSetMetaData.getFieldName(0));
        assertEquals("Field_1", dataSetMetaData.getFieldName(fieldCount - 1));
    }

    @Test
    public void testData() {
        assertTrue(dataSet.next());
        assertEquals(1, dataSet.getCursorPosition());
        assertEquals("Jason", dataSet.getObject(0));
        assertEquals("Jason", dataSet.getObject("Field_0"));
        assertEquals(20, dataSet.getObject(1));
        assertEquals(20, dataSet.getObject("Field_1"));

        assertTrue(dataSet.next());
        assertEquals(2, dataSet.getCursorPosition());
        assertEquals("Maria", dataSet.getObject(0));
        assertEquals("Maria", dataSet.getObject("Field_0"));
        assertEquals(18, dataSet.getObject(1));
        assertEquals(18, dataSet.getObject("Field_1"));

        assertFalse(dataSet.next());
    }

}
