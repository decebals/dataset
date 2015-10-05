package ro.fortsoft.dataset.csv;

import org.junit.Test;
import ro.fortsoft.dataset.core.AbstractDataSetTest;
import ro.fortsoft.dataset.core.DataSet;
import ro.fortsoft.dataset.core.DataSetMetaData;

import static org.junit.Assert.*;

/**
 * @author Decebal Suiu
 */
public class CsvDataSetTest extends AbstractDataSetTest {

    @Override
    public DataSet createDataSet() {
        return new CsvDataSet.Builder(getResourceStream("test.csv")).build();
    }

    @Test
    public void testMetaData() {
        DataSetMetaData dataSetMetaData = dataSet.getMetaData();

        // "first_name","last_name","company_name","address","city","county","state","zip","phone1","phone2","email","web"
        int fieldCount = dataSetMetaData.getFieldCount();
        assertEquals(12, fieldCount);
//        assertEquals("Field_0", dataSetMetaData.getFieldName(0));
        assertEquals("first_name", dataSetMetaData.getFieldName(0));
//        assertEquals("Field_11", dataSetMetaData.getFieldName(fieldCount - 1));
        assertEquals("web", dataSetMetaData.getFieldName(fieldCount - 1));
    }

    @Test
    public void testData() {
        assertTrue(dataSet.next());
        assertEquals(1, dataSet.getCursorPosition());
        assertEquals("James", dataSet.getObject(0));
        assertEquals("James", dataSet.getObject("first_name"));
        assertEquals("Butt", dataSet.getObject(1));
        assertEquals("Butt", dataSet.getObject("last_name"));

        int count = 1;
        while (dataSet.next()) {
            count++;
        }
        assertEquals(500, dataSet.getCursorPosition());
        assertEquals(500, count);
    }

}
