package ro.fortsoft.dataset.xls;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ro.fortsoft.dataset.core.BaseDataSetMetaData;
import ro.fortsoft.dataset.core.DataSet;
import ro.fortsoft.dataset.core.DataSetBuilder;
import ro.fortsoft.dataset.core.DataSetException;
import ro.fortsoft.dataset.core.DataSetMetaData;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author Decebal Suiu
 */
public class XlsDataSet implements DataSet {

//    private InputStream inputStream;
    private int sheetIndex;
    private DataSetMetaData metaData;

    private Workbook workbook;
    private int cursorPosition;

    private XlsDataSet(InputStream inputStream) {
//        this.inputStream = inputStream;

        try {
            workbook = new HSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new DataSetException(e);
        }
    }

    @Override
    public DataSetMetaData getMetaData() {
        return metaData;
    }

    @Override
    public boolean next() {
        int rowCount = workbook.getSheetAt(sheetIndex).getLastRowNum();
        if (cursorPosition <= rowCount) {
            cursorPosition++;
            return true;
        }

        return false;
    }

    @Override
    public int getCursorPosition() {
        return cursorPosition;
    }

    @Override
    public Object getObject(int fieldIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Cell cell = sheet.getRow(cursorPosition).getCell(fieldIndex);
        if (cell == null) {
            return null;
        }

        Class<?> valueClass = metaData.getFieldClass(fieldIndex);
        if (String.class.equals(valueClass)) {
            return cell.getStringCellValue();
        } else if (Boolean.class.equals(valueClass)) {
            return cell.getBooleanCellValue();
        } else if (Number.class.isAssignableFrom(valueClass)) {
            return cell.getNumericCellValue();
        } else if (Date.class.isAssignableFrom(valueClass)) {
            return cell.getDateCellValue();
        }

        throw new DataSetException("Cannot convert cell value of field '" + metaData.getFieldName(fieldIndex) + "' to '" + valueClass + "'");
    }

    @Override
    public Object getObject(String fieldName) {
        return getObject(metaData.getFieldIndex(fieldName));
    }

    @Override
    public void close() {
        try {
            workbook.close();
        } catch (IOException e) {
            throw new DataSetException("Cannot close data set", e);
        }
    }

    protected DataSetMetaData createDefaultMetaData() {
        BaseDataSetMetaData metaData = new BaseDataSetMetaData();

        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Row row = sheet.getRow(0);
        int columnCount = row.getLastCellNum();
        for(int i = 0; i < columnCount; i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                int cellType = cell.getCellType();
                Class<?> valueClass = String.class;
                if (Cell.CELL_TYPE_BOOLEAN == cellType) {
                    valueClass = Boolean.class;
                } else if (Cell.CELL_TYPE_NUMERIC == cellType) {
                    valueClass = Double.class;
                }
                metaData.addField(cell.toString(), valueClass, i);
            } else {
                metaData.addField("Field_" + i, String.class, i);
            }
        }

        return metaData;
    }

    public static class Builder implements DataSetBuilder {

        private XlsDataSet dataSet;

        public Builder(InputStream inputStream) {
            dataSet = new XlsDataSet(inputStream);
        }

        public Builder setMetaData(DataSetMetaData metaData) {
            dataSet.metaData = metaData;

            return this;
        }

        public Builder setSheetIndex(int sheetIndex) {
            dataSet.sheetIndex = sheetIndex;

            return this;
        }

        @Override
        public XlsDataSet build() {
            if (dataSet.metaData == null) {
                dataSet.metaData = dataSet.createDefaultMetaData();
            }

            return dataSet;
        }

    }

}
