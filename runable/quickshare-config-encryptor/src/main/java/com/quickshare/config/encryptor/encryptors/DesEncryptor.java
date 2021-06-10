package com.quickshare.config.encryptor.encryptors;

import com.quickshare.config.encryptor.Encryptor;
import com.quickshare.config.encryptor.EncryptorType;
import com.quickshare.common.encrytor.DesUtils;

import java.io.BufferedReader;

/**
 * 数据库连接加密
 * @author liu_ke
 */
@EncryptorType(value = "des", caption = "DES加密")
public class DesEncryptor implements Encryptor {

    @Override
    public void encrypt(BufferedReader br) throws Exception {
        System.out.print("请输入密码：");
        String pwd = br.readLine();
        System.out.print("请输入Key：");
        String key = br.readLine();
        String encryptContent = DesUtils.encrypt(pwd,key);
        System.out.println(encryptContent);
    }
}
