package ro.fortsoft.dataset.core;

/**
 * Exception thrown for dataset-related failures.
 *
 * @author Decebal Suiu
 */
public class DataSetException extends RuntimeException {

    public DataSetException(String message) {
        super(message);
    }

    public DataSetException(Throwable cause) {
        super(cause);
    }

    public DataSetException(String message, Throwable cause) {
        super(message, cause);
    }

}
