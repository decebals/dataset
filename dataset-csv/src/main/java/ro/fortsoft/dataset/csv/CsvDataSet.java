package ro.fortsoft.dataset.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ro.fortsoft.dataset.core.BaseDataSetMetaData;
import ro.fortsoft.dataset.core.DataSet;
import ro.fortsoft.dataset.core.DataSetBuilder;
import ro.fortsoft.dataset.core.DataSetException;
import ro.fortsoft.dataset.core.DataSetMetaData;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Decebal Suiu
 */
public class CsvDataSet implements DataSet {

    private InputStream inputStream;
    private boolean useHeader = true;
    private boolean skipFirstDataRow;
    private DataSetMetaData metaData;

    private MappingIterator<Map<String, Object>> rows;
    private Map<String, Object> currentRow;
    private int cursorPosition;
//    private boolean defaultMetaData;

    private CsvDataSet(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public DataSetMetaData getMetaData() {
        return metaData;
    }

    @Override
    public boolean next() {
        if (cursorPosition == 0) {
            init();
        }

        boolean hasNext = rows.hasNext();
        if (hasNext) {
            currentRow = rows.next();
            cursorPosition++;
        }

        return hasNext;
    }

    @Override
    public int getCursorPosition() {
        return cursorPosition;
    }

    @Override
    public Object getObject(int fieldIndex) {
        return getObject(metaData.getFieldName(fieldIndex));
    }

    @Override
    public Object getObject(String fieldName) {
        return currentRow.get(fieldName);
    }

    @Override
    public void close() {
        if (rows != null) {
            try {
                rows.close();
            } catch (IOException e) {
                throw new DataSetException("Cannot close data set", e);
            }
        }
    }

    protected void init() {
        // test if rows was initialized by createDefaultMetaData method
        if (rows == null) {
            rows = createMappingIterator(inputStream);
        }
    }

    protected DataSetMetaData createDefaultMetaData() {
        BaseDataSetMetaData metaData = new BaseDataSetMetaData();

        rows = createMappingIterator(inputStream);
        CsvSchema parserSchema = (CsvSchema) rows.getParserSchema();
        int columnCount = parserSchema.size();
        for (int i = 0; i < columnCount; i++) {
            CsvSchema.Column column = parserSchema.column(i);
            CsvSchema.ColumnType columnType = column.getType();
            metaData.addField(column.getName(), columnType.getClass(), column.getIndex());
        }

        return metaData;
    }

    protected MappingIterator<Map<String, Object>> createMappingIterator(InputStream inputStream) {
        CsvMapper mapper = createCsvMapper();
        CsvSchema schema = createCsvSchema();
        ObjectReader reader = mapper.reader(Map.class).with(schema);

        try {
            return reader.readValues(inputStream);
        } catch (IOException e) {
            throw new DataSetException(e);
        }
    }

    protected CsvMapper createCsvMapper() {
        CsvMapper mapper = new CsvMapper();
//        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);

        return mapper;
    }

    protected CsvSchema createCsvSchema() {
        return CsvSchema.emptySchema()
                .withUseHeader(useHeader)
                .withSkipFirstDataRow(skipFirstDataRow);
    }

    public static class Builder implements DataSetBuilder {

        private CsvDataSet dataSet;

        public Builder(InputStream inputStream) {
            dataSet = new CsvDataSet(inputStream);
        }

        public Builder setMetaData(DataSetMetaData metaData) {
            dataSet.metaData = metaData;

            return this;
        }

        public Builder setUseHeader(boolean useHeader) {
            dataSet.useHeader = useHeader;

            return this;
        }

        public Builder setSkipFirstDataRow(boolean skipFirstDataRow) {
            dataSet.skipFirstDataRow = skipFirstDataRow;

            return this;
        }

        @Override
        public CsvDataSet build() {
            if (dataSet.metaData == null) {
                dataSet.metaData = dataSet.createDefaultMetaData();
//                dataSet.defaultMetaData = true;
                dataSet.useHeader = false;
            }

            return dataSet;
        }

    }

}
