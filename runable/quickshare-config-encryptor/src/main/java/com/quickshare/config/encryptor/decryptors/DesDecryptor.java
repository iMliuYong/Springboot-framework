package com.quickshare.config.encryptor.decryptors;

import com.quickshare.config.encryptor.Decryptor;
import com.quickshare.config.encryptor.DecryptorType;
import com.quickshare.common.encrytor.DesUtils;

import java.io.BufferedReader;

/**
 * @author liu_ke
 */
@DecryptorType(value = "des", caption = "DES")
public class DesDecryptor implements Decryptor {
    @Override
    public void decrypt(BufferedReader br) throws Exception {
        System.out.print("请输入密文：");
        String ciphertext = br.readLine();
        System.out.print("请输入密钥：");
        String key = br.readLine();
        if(key.length() != 8){
            System.out.println("输入的密钥有误。");
        }
        else{
            String encryptContent = DesUtils.decrypt(ciphertext,key);
            System.out.println(encryptContent);
        }
    }
}
