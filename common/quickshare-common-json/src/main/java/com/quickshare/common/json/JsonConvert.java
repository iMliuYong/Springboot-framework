package com.quickshare.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @author liu_ke
 */
public class JsonConvert {

    private static final ObjectMapper mapper = ObjectMapperFactory.getMapper();

    public static <T> T toObject(String json,Class<T> clazz){
        try {
            return mapper.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> List<T> toArray(String json, Class<T> clazz){
        try {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, clazz);
            return mapper.readValue(json,type);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String toString(Object o){
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
