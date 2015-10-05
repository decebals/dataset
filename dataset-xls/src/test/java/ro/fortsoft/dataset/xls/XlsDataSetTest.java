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
