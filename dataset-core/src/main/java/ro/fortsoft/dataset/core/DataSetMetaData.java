package ro.fortsoft.dataset.core;

/**
 * An object that can be used to get information about the types and properties of the columns in
 * a <code>DataSet</code> object.
 *
 * @author Decebal Suiu
 */
public interface DataSetMetaData {

    public String getFieldName(int fieldIndex);

    public int getFieldIndex(String fieldName);

    public Class<?> getFieldClass(int fieldIndex);

    /**
     * Gets number of fields in this data source.
     */
    public int getFieldCount();

    /**
     * Gets the native type of this column. A native type is the name of the data type as defined in the datastore.
     *
     * @return the name of the native type.
     */
//    public String getNativeType();

}
