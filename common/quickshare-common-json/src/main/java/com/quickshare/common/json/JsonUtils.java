package com.quickshare.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author
 */
public class JsonUtils {

    private static final ObjectMapper mapper = ObjectMapperFactory.getMapper();

    public static boolean isJson(String content) {
        boolean valid = true;
        try{
            mapper.readTree(content);
        } catch(JsonProcessingException e){
            valid = false;
        }
        return valid;
    }
}
