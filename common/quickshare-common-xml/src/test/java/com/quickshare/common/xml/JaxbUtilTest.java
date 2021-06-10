package com.quickshare.common.xml;

import com.quickshare.common.xml.bean.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("JaxbUtil测试类")
public class JaxbUtilTest {

    @Test
    @DisplayName("xml转换到Java对象测试")
    public void convertToJavaBean(){
        String xml = FileUtil.readFile("test.xml");
        User user = JaxbUtil.convertToBean(xml, User.class);
    }


}
