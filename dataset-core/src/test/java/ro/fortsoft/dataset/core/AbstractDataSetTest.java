package ro.fortsoft.dataset.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * @author Decebal Suiu
 */
public abstract class AbstractDataSetTest {

    protected DataSet dataSet;

    @Before
    public void setUp() {
        dataSet = createDataSet();
    }

    @After
    public void tearDown() {
        dataSet.close();
        dataSet = null;
    }

    @Test
    public void genericTest() {
        assertNotNull(dataSet);
        assertNotNull(dataSet.getMetaData());
    }

    public abstract DataSet createDataSet();

    protected InputStream getResourceStream(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }

    protected File getResourceFile(String name) {
        URL url = getClass().getClassLoader().getResource(name);

        return (url != null) ? new File(url.getFile()) : null;
    }

}
