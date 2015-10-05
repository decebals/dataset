package ro.fortsoft.dataset.core;

import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

/**
 * @author Decebal Suiu
 */
public class ResultSetDataSetTest2 extends AbstractDataSetTest {

    @Override
    public DataSet createDataSet() {
        String dbUrl = "jdbc:mysql://vs511.intranet.asf.ro/nextdemo";
        String dbUser = "nextuser";
        String dbPass = "zefcaHet5";
        String dbDriver = "com.mysql.jdbc.Driver";
        String sql = "select * from DOWNLOADS";

        try {
            Class.forName(dbDriver);
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            return new ResultSetDataSet(resultSet);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DataSetException("Cannot create data set", e);
        }
    }

    @Test
    public void testMetaData() {
        DataSetMetaData dataSetMetaData = dataSet.getMetaData();

        // "date","ip","file","country","city"
        int fieldCount = dataSetMetaData.getFieldCount();
        assertEquals(5, fieldCount);
        assertEquals("date", dataSetMetaData.getFieldName(0));
        assertEquals("city", dataSetMetaData.getFieldName(fieldCount - 1));
    }

    @Test
    public void testData() {
        assertTrue(dataSet.next());
        assertEquals(1, dataSet.getCursorPosition());
        assertEquals(Date.valueOf("2008-10-06"), dataSet.getObject(0));
        assertEquals(Date.valueOf("2008-10-06"), dataSet.getObject("date"));
        assertEquals("192.168.12.124", dataSet.getObject(1));
        assertEquals("192.168.12.124", dataSet.getObject("ip"));

        int count = 1;
        while (dataSet.next()) {
            count++;
        }
        assertEquals(count, dataSet.getCursorPosition());
    }

}
