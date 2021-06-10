package com.quickshare.config.encryptor.encryptors;

import com.quickshare.config.encryptor.Encryptor;
import com.quickshare.config.encryptor.EncryptorType;
import com.quickshare.common.encrytor.DesUtils;

import java.io.BufferedReader;

/**
 * 数据库连接加密
 * @author liu_ke
 */
@EncryptorType(value = "database", caption = "数据库连接加密")
public class DatabaseEncryptor implements Encryptor {

    private String key = "quckshre";

    @Override
    public void encrypt(BufferedReader br) throws Exception {
        System.out.print("请输入数据库密码：");
        String pwd = br.readLine();
        String encryptContent = DesUtils.encrypt(pwd,key);
        System.out.println(encryptContent);
    }
}
