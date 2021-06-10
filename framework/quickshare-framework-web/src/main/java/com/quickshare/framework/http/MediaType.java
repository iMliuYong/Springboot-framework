package com.quickshare.framework.http;

import java.nio.charset.StandardCharsets;

/**
 * @author liu_ke
 */
public class MediaType {

    public static final org.springframework.http.MediaType APPLICATION_JSON_UTF8;
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    static {
        APPLICATION_JSON_UTF8 = new org.springframework.http.MediaType("application", "json", StandardCharsets.UTF_8);
    }
}
