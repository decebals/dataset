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
