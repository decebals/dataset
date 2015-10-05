package ro.fortsoft.dataset.core;

import java.io.Closeable;

/**
 * A logical representation of a set of data entities.
 *
 * @author Decebal Suiu
 */
public interface DataSet extends Closeable {

    /**
     * Retrieves the number, types and properties of this <code>DataSet</code> object's fields.
     *
     * @return the description of this <code>DataSet</code> object's fields
     */
    public DataSetMetaData getMetaData();

    /**
     * Moves the cursor froward one row from its current position.
     * A <code>DataSet</code> cursor is initially positioned before the first row; the first call to the method
     * <code>next</code> makes the first row the current row; the second call makes the second row the current row,
     * and so on.
     * <p>
     * When a call to the <code>next</code> method returns <code>false</code>,
     * the cursor is positioned after the last row.
     *
     * @return <code>true</code> if the new current row is valid;
     * <code>false</code> if there are no more rows
     */
    public boolean next();

    /**
     * Retrieves the current row number. The first row is number 1, the second number 2, and so on.
     *
     * @return the current row number; <code>0</code> if there is no current row
     */
     public int getCursorPosition();

    /**
     * Gets the value of the designated field in the current row of this <code>DataSet</code> object.
     */
    public Object getObject(int fieldIndex);

    public Object getObject(String fieldName);

    /**
     * Closes the DataSet and any resources it may be holding.
     */
    @Override
    public void close();

}
