package com.quickshare.common.encrytor;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64工具
 * @author liu_ke
 */
public class Base64Utils {

    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String encode(byte[] bytes){
        return new Base64().encodeAsString(bytes);
    }

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] decode(String base64Code){
        return new Base64().decode(base64Code);
    }
}