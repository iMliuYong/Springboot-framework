package com.quickshare.config.encryptor.encryptors;

import com.quickshare.common.encrytor.DesUtils;
import com.quickshare.config.encryptor.Encryptor;
import com.quickshare.config.encryptor.EncryptorType;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;

/**
 * 数据库连接加密
 * @author liu_ke
 */
@EncryptorType(value = "userPwd", caption = "文件中用户密码加密")
public class FileUserPwdEncryptor implements Encryptor {

    @Override
    public void encrypt(BufferedReader br) throws Exception {
        System.out.print("请输入用户名：");
        String userCode = br.readLine().trim();
        if(StringUtils.isEmpty(userCode)){
            throw new Exception("用户名不能为空。");
        }
        System.out.print("请输入密码：");
        String pwd = br.readLine();
        int l = userCode.length();
        int cnt = 8/l;
        String key = userCode;
        while (cnt > 0){
            key = key + userCode;
            cnt--;
        }
        key = key.substring(0,8);
        String encryptContent = DesUtils.encrypt(pwd,key);
        System.out.println(encryptContent);
    }
}
