package com.quickshare.common.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@DisplayName("XmlValidator测试类")
public class XmlValidatorTest {

    @Test
    @DisplayName("xml-valid")
    public void valid() throws UnsupportedEncodingException {
        String schemaXml = FileUtil.readFile("cd1001.xsd");
        String xml = FileUtil.readFile("cd1001.xml");
        InputStream schemaStream = new ByteArrayInputStream(schemaXml.getBytes("utf-8"));
        XmlValidator validator = new XmlValidator(schemaStream);
        String validMsg = validator.valid(xml);
        System.out.println(validMsg);
    }
}
