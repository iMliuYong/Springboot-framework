package com.quickshare.common.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@DisplayName("JaxbUtil测试类")
public class XmlUtilTest {

    @Test
    @DisplayName("xml转换到Java对象测试")
    public void convertToJavaBean(){
        String xml = FileUtil.readFile("cd0001.xml");
        Map map = XmlUtil.toMap(xml);
    }
}
