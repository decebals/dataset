package ro.fortsoft.dataset.xml;

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
public class XmlDataSetTest extends AbstractDataSetTest {

    @Override
    public DataSet createDataSet() {
        BaseDataSetMetaData dataSetMetaData = new BaseDataSetMetaData();
//        dataSetMetaData.addField("date", Date.class);
        dataSetMetaData.addField("date", String.class);
        dataSetMetaData.addField("currency", String.class);
//        dataSetMetaData.addField("rate", Integer.class);
        dataSetMetaData.addField("rate", Integer.class);

        DataSetBuilder builder = new XmlDataSet.Builder(getResourceStream("test.xml"))
                .setMetaData(dataSetMetaData)
                .setExpression("//Cube/Rate")
                .setFieldExpression("currency", "./@currency")
                .setFieldExpression("date", "../@date");

        return builder.build();
    }

    @Test
    public void testMetaData() {
        DataSetMetaData dataSetMetaData = dataSet.getMetaData();

        // "date","currency","rate"
        int fieldCount = dataSetMetaData.getFieldCount();
        assertEquals(3, fieldCount);
        assertEquals("date", dataSetMetaData.getFieldName(0));
        assertEquals("rate", dataSetMetaData.getFieldName(fieldCount - 1));
    }

    @Test
    public void testData() {
        assertTrue(dataSet.next());
        assertEquals(1, dataSet.getCursorPosition());
//        assertEquals(Date.valueOf("2015-09-22"), dataSet.getObject(0));
        assertEquals("2015-09-22", dataSet.getObject(0));
//        assertEquals(Date.valueOf("2015-09-22"), dataSet.getObject("date"));
        assertEquals("2015-09-22", dataSet.getObject("date"));
        assertEquals("AED", dataSet.getObject(1));
        assertEquals("AED", dataSet.getObject("currency"));

        int count = 1;
        while (dataSet.next()) {
            count++;
        }
        assertEquals(31, dataSet.getCursorPosition());
        assertEquals(31, count);
    }

}
