package com.quickshare.common.encrytor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * DES加解密工具
 * @author liu_ke
 */
public class DesUtils {

    private final static byte[] Keys = { (byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78, (byte)0x90, (byte)0xAB, (byte)0xCD, (byte)0xEF };

    /**
     * 将base 64 code DES解密
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {

        DESKeySpec keySpec=new DESKeySpec(decryptKey.getBytes());
        SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("DES");
        SecretKey key=keyFactory.generateSecret(keySpec);

        Cipher cipher=Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(Keys));
        byte[] result=cipher.doFinal(Base64Utils.decode(encryptStr));
        return  new String(result);
    }

    /**
     * DES加密为base 64 code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        DESKeySpec keySpec=new DESKeySpec(encryptKey.getBytes());
        SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("DES");
        SecretKey key=keyFactory.generateSecret(keySpec);

        Cipher cipher=Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(Keys));
        byte[] result=cipher.doFinal(content.getBytes("utf-8"));
        return Base64Utils.encode(result);
    }
}
