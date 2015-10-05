package ro.fortsoft.dataset.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Decebal Suiu
 */
public class BaseDataSetMetaData implements DataSetMetaData {

    private List<String> fieldNames;
    private List<Class<?>> fieldClasses;

    public BaseDataSetMetaData() {
        fieldNames = new ArrayList<>();
        fieldClasses = new ArrayList<>();
    }

    @Override
    public String getFieldName(int fieldIndex) {
        return fieldNames.get(fieldIndex);
    }

    @Override
    public int getFieldIndex(String fieldName) {
        return fieldNames.indexOf(fieldName);
    }

    @Override
    public Class<?> getFieldClass(int fieldIndex) {
        return fieldClasses.get(fieldIndex);
    }

    @Override
    public int getFieldCount() {
        return fieldNames.size();
    }

    public void addField(String fieldName, Class<?> fieldClass, int fieldIndex) {
        fieldNames.add(fieldIndex, fieldName);
        fieldClasses.add(fieldIndex, fieldClass);
    }

    public void addField(String fieldName, Class<?> fieldClass) {
        fieldNames.add(fieldName);
        fieldClasses.add(fieldClass);
    }

}
