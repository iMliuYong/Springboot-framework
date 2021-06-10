package com.quickshare.common.encrytor;

import java.io.UnsupportedEncodingException;

/**
 * md5加密相关
 * @author liu_ke
 */
public class MD5Utils {

    /**
     * md5加密
     * @param msg 待加密字符串
     * @return 获取md5后转为base64
     * @throws Exception
     */
    public static String encrypt(String msg){
        try {
            return encrypt(msg.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * md5加密
     * @param data 待加密字节数据
     * @return 获取md5后转为base64
     * @throws Exception
     */
    public static String encrypt(byte[] data){
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
