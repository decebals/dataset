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
public class ResultSetDataSetTest extends AbstractDataSetTest {

    @Override
    public DataSet createDataSet() {
        String dbUrl = "jdbc:derby:test-db;create=false";
        String dbUser = "";
        String dbPass = "";
        String dbDriver = "org.apache.derby.jdbc.EmbeddedDriver";
        String sql = "select * from Timesheet";

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

        // "TIMESHEETID","DATA","HOURSNO","EMPLOYEEID","PROJECTID","WORKCODEID","OBS"
        int fieldCount = dataSetMetaData.getFieldCount();
        assertEquals(7, fieldCount);
        assertEquals("TIMESHEETID", dataSetMetaData.getFieldName(0));
        assertEquals("OBS", dataSetMetaData.getFieldName(fieldCount - 1));
    }

    @Test
    public void testData() {
        assertTrue(dataSet.next());
        assertEquals(1, dataSet.getCursorPosition());
        assertEquals(1, dataSet.getObject(0));
        assertEquals(1, dataSet.getObject("TIMESHEETID"));
        assertEquals(Date.valueOf("2012-08-01"), dataSet.getObject(1));
        assertEquals(Date.valueOf("2012-08-01"), dataSet.getObject("DATA"));

        int count = 1;
        while (dataSet.next()) {
            count++;
        }
        assertEquals(count, dataSet.getCursorPosition());
    }

}
