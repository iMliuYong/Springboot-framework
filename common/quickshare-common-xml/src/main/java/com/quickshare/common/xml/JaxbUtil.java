package com.quickshare.common.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author liu_ke
 */
public class JaxbUtil {

    private final static Logger logger = LoggerFactory.getLogger("default");

    /**
     * xml转化到JavaBean
     * @param xml
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T convertToBean(String xml, Class<T> c) {
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return t;
    }

    /**
     * JavaBean转换成xml.
     *
     * @param obj bean实体
     * @return
     */
    public static String convertToXml(Object obj) {
        return convertToXml(obj,"UTF-8",null,false,true);
    }

    /**
     * JavaBean转换成xml.
     *
     * @param obj bean实体
     * @param encoding 默认编码UTF-8
     * @param schemaLocation 命名空间
     * @param formatted 是否格式化输出
     * @param removeFragment 是否移除xml摘要
     * @return
     */
    public static String convertToXml(Object obj, String encoding,String schemaLocation,boolean formatted,boolean removeFragment) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            if(schemaLocation!=null && !"".equals(schemaLocation)){
                marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);
            }
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);

            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

        return result;
    }
}
