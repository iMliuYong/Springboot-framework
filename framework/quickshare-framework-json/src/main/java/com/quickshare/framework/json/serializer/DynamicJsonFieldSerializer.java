package com.quickshare.framework.json.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickshare.framework.json.filter.DynamicJsonFieldFilter;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;

/**
 * 每个客户提供不同的序列号器
 * @author liu_ke
 */
public class DynamicJsonFieldSerializer {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final ObjectMapper mapper;

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        mapper = new ObjectMapper();
        mapper.setDateFormat(dateFormat);
        // 允许对象忽略json中不存在的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许出现单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 忽视为空的属性
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    DynamicJsonFieldFilter jsonFilter = new DynamicJsonFieldFilter();

    public void filter(String className, String include, String filter) {
        try {
            Class clazz = Class.forName(className);
            if (clazz == null) {
                return;
            }
            if (StringUtils.isNotBlank(include)) {
                jsonFilter.include(clazz, include.split(","));
            }
            if (StringUtils.isNotBlank(filter)) {
                jsonFilter.filter(clazz, filter.split(","));
            }
            mapper.addMixIn(clazz, jsonFilter.getClass());
        } catch (ClassNotFoundException e) {
            return;
        }
    }

    public String toJson(Object obj) {
        try {
            mapper.setFilterProvider(jsonFilter);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("转换json字符失败!");
        }
    }
}
