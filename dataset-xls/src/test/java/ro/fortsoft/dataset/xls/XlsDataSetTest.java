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
package ro.fortsoft.dataset.xls;

import org.junit.Test;
import ro.fortsoft.dataset.core.AbstractDataSetTest;
import ro.fortsoft.dataset.core.DataSet;
import ro.fortsoft.dataset.core.DataSetMetaData;

import static org.junit.Assert.*;

/**
 * @author Decebal Suiu
 */
public class XlsDataSetTest extends AbstractDataSetTest {

    @Override
    public DataSet createDataSet() {
        return new XlsDataSet.Builder(getResourceStream("test.xls")).build();
    }

    @Test
    public void testMetaData() {
        DataSetMetaData dataSetMetaData = dataSet.getMetaData();

        // "city","country","date","file","ip","count","product","version"
        int fieldCount = dataSetMetaData.getFieldCount();
        assertEquals(8, fieldCount);
        assertEquals("city", dataSetMetaData.getFieldName(0));
        assertEquals("version", dataSetMetaData.getFieldName(fieldCount - 1));
    }

    @Test
    public void testData() {
        assertTrue(dataSet.next());
        assertEquals(1, dataSet.getCursorPosition());
        assertEquals("Milan", dataSet.getObject(0));
        assertEquals("Milan", dataSet.getObject("city"));
        assertEquals("ITALY", dataSet.getObject(1));
        assertEquals("ITALY", dataSet.getObject("country"));

        int count = 1;
        while (dataSet.next()) {
            count++;
        }
        assertEquals(1214, dataSet.getCursorPosition());
        assertEquals(1214, count);
    }

}
