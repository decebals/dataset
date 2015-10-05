package ro.fortsoft.dataset.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ro.fortsoft.dataset.core.DataSet;
import ro.fortsoft.dataset.core.DataSetBuilder;
import ro.fortsoft.dataset.core.DataSetException;
import ro.fortsoft.dataset.core.DataSetMetaData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Decebal Suiu
 */
public class XmlDataSet implements DataSet {

    private InputStream inputStream;
    private String expression;
    private Map<String, String> fieldsExpression;
    private DataSetMetaData metaData;

    private NodeList rows;
    private int cursorPosition;

    private XmlDataSet(InputStream inputStream) {
        this.inputStream = inputStream;

        fieldsExpression = new HashMap<>();
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

        if (cursorPosition < rows.getLength()) {
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
        Node node = rows.item(cursorPosition - 1);

        if (node == null) {
            return null;
        }

        String fieldName = metaData.getFieldName(fieldIndex);
        String fieldExpression = fieldsExpression.get(fieldName);
        if (fieldExpression == null) {
            return node.getTextContent();
        }

        XPath xpath = createXPath();

        try {
            Node fieldNode = (Node) xpath.compile(fieldExpression).evaluate(node, XPathConstants.NODE);
            // TODO convert
            return fieldNode.getTextContent();
        } catch (XPathExpressionException e) {
            throw new DataSetException(e);
        }
    }

    @Override
    public Object getObject(String fieldName) {
        return getObject(metaData.getFieldIndex(fieldName));
    }

    @Override
    public void close() {
        // TODO
    }

    /*
    protected DataSetMetaData createDefaultMetaData() {
        BaseDataSetMetaData metaData = new BaseDataSetMetaData();

        // TODO

        return metaData;
    }
    */

    protected void init() {
        Document document = createDocument(inputStream);
        XPath xpath = createXPath();

        try {
            rows = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new DataSetException(e);
        }
    }

    protected Document createDocument(InputStream inputStream) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setValidating(false);

        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            return builder.parse(inputStream);
        } catch (Exception e) {
            throw new DataSetException(e);
        }
    }

    protected XPath createXPath() {
        XPathFactory factory = XPathFactory.newInstance();

        return factory.newXPath();
    }

    public static class Builder implements DataSetBuilder {

        private XmlDataSet dataSet;

        public Builder(InputStream inputStream) {
            dataSet = new XmlDataSet(inputStream);
        }

        public Builder setMetaData(DataSetMetaData metaData) {
            dataSet.metaData = metaData;

            return this;
        }

        public Builder setExpression(String expression) {
            dataSet.expression = expression;

            return this;
        }

        public Builder setFieldExpression(String fieldName, String fieldExpression) {
            dataSet.fieldsExpression.put(fieldName, fieldExpression);

            return this;
        }

        @Override
        public XmlDataSet build() {
            // TODO perform validation on mandatory fields
            return dataSet;
        }

    }

}
