package com.quickshare.common.encrytor;

import org.apache.commons.lang3.StringUtils;

/**
 * value加密器
 * @author liu_ke
 */
public class CodeValueCipher {

    public static String decrypt(String code,String encryptedValue){
        if(StringUtils.isEmpty(encryptedValue)){
            return encryptedValue;
        }
        try {
            String key = getKey(code);
            String value = DesUtils.decrypt(encryptedValue, key);
            return value;
        }
        catch (Exception e){
            return encryptedValue;
        }
    }

    public static String encrypt(String code,String value) throws Exception {
        String key = getKey(code);
        String encryptContent = DesUtils.encrypt(value,key);
        return encryptContent;
    }

    private static String getKey(String code){
        int l = code.length();
        int cnt = 8/l;
        String key = code;
        while (cnt > 0){
            key = key + code;
            cnt--;
        }
        key = key.substring(0,8);
        return key;
    }
}
