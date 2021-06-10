package com.quickshare.common.xml;

import com.quickshare.common.json.JsonConvert;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xml解析by dom4j
 * @author liu_ke
 */
public class XmlUtil {

    /**
     *
     * 通过Map创建XML,Map可以多层转换
     * @param parentName
     * @param params
     * @param isCDATA
     * @return	String-->XML
     */
    public static String fromMap(String parentName, Map<String, Object> params, boolean isCDATA){
        Document doc = DocumentHelper.createDocument();
        doc.addElement(parentName);
        String xml = iteratorXml(doc.getRootElement(),parentName,params,isCDATA,true);
        return formatXML(xml);
    }

    /**
     *
     * 通过Map创建XML,Map可以多层转换
     * 可以自定义parent节点
     *
     * @param params
     * @return	String-->XML
     */
    public static String fromMap(String parentName,Map<String, Object> params){
        Document doc = DocumentHelper.createDocument();
        doc.addElement(parentName);
        String xml = iteratorXml(doc.getRootElement(),parentName,params,false,true);
        return formatXML(xml);
    }

    /**
     *
     * 通过Map创建XML,Map可以多层转
     * 固定节点parent为Document
     *
     * @param params
     * @return	String-->XML
     */
    public static String fromMap(Map<String, Object> params){
        String parentName = params.keySet().stream().findFirst().get();
        Document doc = DocumentHelper.createDocument();
        doc.addElement(parentName);
        String xml = iteratorXml(doc.getRootElement(),parentName,(Map<String, Object>)params.get(parentName),false,true);
        return formatXML(xml);
    }

    /**
     *
     * MapToXml循环遍历创建xml节点
     * 此方法在value中加入CDATA标识符
     *
     * @param element 根节点
     * @param parentName 子节点名字
     * @param map map数据
     * @return String-->Xml
     */
    @SuppressWarnings("unchecked")
    private static String iteratorXml(Element element, String parentName, Map<String,Object> map, boolean isCDATA, boolean addParent) {
        Element xmlElement = element;
        if(addParent){
            xmlElement = element.addElement(parentName);
        }
        for(String key:map.keySet()){
            Object obj = map.get(key);
            if (obj instanceof Map) {
                iteratorXml(xmlElement, key, (Map<String, Object>)obj, isCDATA,true);
            }
            else if(obj instanceof List){
                //xmlElement = xmlElement.addElement(key);
                for (Map item : JsonConvert.toArray(JsonConvert.toString(obj),Map.class)) {
                    iteratorXml(xmlElement, key, item, isCDATA,false);
                }
            }
            else {
                String value = obj == null ? "" : obj.toString();
                if (isCDATA) {
                    xmlElement.addElement(key).addCDATA(value);
                } else {
                    xmlElement.addElement(key).addText(value);
                }
            }
        }
        return xmlElement.asXML();
    }

    /**
     * 格式化xml,显示为容易看的XML格式
     *
     * @param inputXML
     * @return
     */
    public static String formatXML(String inputXML){
        String requestXML = null;
        XMLWriter writer = null;
        Document document = null;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(new StringReader(inputXML));
            if (document != null) {
                StringWriter stringWriter = new StringWriter();
                //格式化，每一级前的空格
                OutputFormat format = new OutputFormat("	", true);
                //xml声明与内容是否添加空行
                format.setNewLineAfterDeclaration(false);
                //是否设置xml声明头部
                format.setSuppressDeclaration(false);
                //设置分行
                format.setNewlines(true);
                writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
                requestXML = stringWriter.getBuffer().toString();
            }
            return requestXML;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {

                }
            }
        }
    }


    /**
     *
     * 通过XML转换为Map<String,Object>
     *
     * @param xml 为String类型的Xml
     * @return 第一个为Root节点，Root节点之后为Root的元素，如果为多层，可以通过key获取下一层Map
     */
    public static Map<String, Object> toMap(String xml) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        if (doc == null) {
            return map;
        }
        Element rootElement = doc.getRootElement();
        elementTomap(rootElement,map);
        return map;
    }

    /***
     *
     * XmlToMap核心方法，里面有递归调用
     *
     * @param outele
     * @param outmap
     */
    @SuppressWarnings("unchecked")
    protected static Map<String, Object> elementTomap (Element outele,Map<String,Object> outmap) {
        List<Element> list = outele.elements();
        int size = list.size();
        if(size == 0){
            outmap.put(outele.getName(), outele.getTextTrim());
        }else{
            Map<String, Object> innermap = new HashMap<String, Object>();
            for(Element ele1 : list){
                String eleName = ele1.getName();
                Object obj =  innermap.get(eleName);
                if(obj == null){
                    elementTomap(ele1,innermap);
                }else{
                    if(obj instanceof java.util.Map){
                        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                        list1.add((Map<String, Object>) innermap.remove(eleName));
                        elementTomap(ele1,innermap);
                        list1.add((Map<String, Object>) innermap.remove(eleName));
                        innermap.put(eleName, list1);
                    }else{
                        elementTomap(ele1,innermap);
                        ((List<Map<String, Object>>)obj).add(innermap);
                    }
                }
            }
            outmap.put(outele.getName(), innermap);
        }
        return outmap;
    }
}