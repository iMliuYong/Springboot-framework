package com.quickshare.config.encryptor.encryptors;

import com.quickshare.config.encryptor.Encryptor;
import com.quickshare.config.encryptor.EncryptorType;
import com.quickshare.common.encrytor.DesUtils;

import java.io.BufferedReader;

/**
 * @author liu_ke
 */
@EncryptorType(value = "oauth", caption = "OAUTH密码")
public class OAuthEncryptor implements Encryptor {
    @Override
    public void encrypt(BufferedReader br) throws Exception {
        System.out.print("请输入客户ID：");
        String customerId = br.readLine();
        if(customerId.length() != 8){
            System.out.println("输入的客户ID有误。");
        }
        else{
            String encryptContent = DesUtils.encrypt(customerId,customerId);
            System.out.println(encryptContent);
        }
    }
}
