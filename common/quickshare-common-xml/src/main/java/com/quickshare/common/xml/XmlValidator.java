package com.quickshare.common.xml;

import com.sun.org.apache.xerces.internal.impl.Constants;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用XML Schema 验证xml
 * @author liu_ke
 */
public class XmlValidator {

    private final static String DEFAULT_CHARSET= "utf-8";

    private SAXReader reader;
    /**
     * 校验器
     */
    private static EntityResolver resolve;

    public XmlValidator(InputStream schema){
        //resolve = (arg0, arg1) -> new InputSource(schema);
        reader=new SAXReader(true);
        //reader.setEntityResolver(resolve);
        //符合的标准
        try {
            //reader.setFeature("http://apache.org/xml/features/validation/schema", false);
            //reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking",true);
            reader.setFeature(
                    Constants.XERCES_FEATURE_PREFIX + Constants.LOAD_EXTERNAL_DTD_FEATURE,
                    false);
            reader.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage","http://www.w3.org/2001/XMLSchema");
            reader.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource",schema);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public String valid(String xml){
        try {
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes(DEFAULT_CHARSET));
            reader.read(inputStream);
            return "";
        } catch (UnsupportedEncodingException e) {
            return e.getLocalizedMessage();
        } catch (DocumentException e) {
            return e.getNestedException().getLocalizedMessage();
        }
    }

    public Map<String, Object> toMap(String xml) throws XmlParseException{
        try {
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes(DEFAULT_CHARSET));
            Document doc = reader.read(inputStream);
            Map<String, Object> map = new HashMap<>();
            if (doc == null) {
                return map;
            }
            Element rootElement = doc.getRootElement();
            XmlUtil.elementTomap(rootElement,map);
            return map;
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (DocumentException e) {
            throw new XmlParseException(e.getNestedException().getLocalizedMessage());
        }
    }
}