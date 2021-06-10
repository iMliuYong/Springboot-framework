package com.quickshare.config.encryptor;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liu_ke
 */
public class Application {

    private final static Map<String,String> encryptors = new HashMap(){
        {
            put("1","oauth");
            put("2","database");
            put("3","rabbitmq");
            put("4","des");
            put("5","userPwd");
        }
    };
    private final static Map<String,String> decryptors = new HashMap(){
        {
            put("1","des");
        }
    };

    public static void main(String[] args) throws Exception {

        Map<String,String> encryptorNames = EncryptorFactory.lstNames();
        Map<String,String> encryptorTips = new HashMap<>();
        Map<String,String> decryptorNames = DecryptorFactory.lstNames();
        Map<String,String> decryptorTips = new HashMap<>();
        for (Map.Entry<String,String> encryptor: encryptors.entrySet()){
            if(encryptorNames.containsKey(encryptor.getValue())){
                encryptorTips.put(encryptor.getKey(),encryptorNames.get(encryptor.getValue()));
            }
        }
        String encryptorTip = String.join("\n",
                encryptorTips.entrySet().stream().map(p->p.getKey()+"."+p.getValue()).collect(Collectors.toList()));
        for (Map.Entry<String,String> decryptor: decryptors.entrySet()){
            if(decryptorNames.containsKey(decryptor.getValue())){
                decryptorTips.put(decryptor.getKey(),decryptorNames.get(decryptor.getValue()));
            }
        }
        String decryptorTip = String.join("\n",
                decryptorTips.entrySet().stream().map(p->p.getKey()+"."+p.getValue()).collect(Collectors.toList()));

        String mode = "e";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (!"exit".equals(mode.toLowerCase())){
            if("e".equals(mode.toLowerCase())){
                mode = encrypt(encryptorTip,br);
            }
            else if("d".equals(mode.toLowerCase())){
                mode = decrypt(decryptorTip,br);
            }
        }
    }

    private static String encrypt(String tip, BufferedReader br) throws Exception {
        String mode = null;
        while(true){
            System.out.println("--------------------------------------------");
            System.out.println("加密模式：\n"+tip+"\nD:解密\nexit:退出");
            System.out.println("--------------------------------------------");
            System.out.print("请输入：");
            mode = br.readLine();

            if("exit".equals(mode.toLowerCase()) ||
                    "d".equals(mode.toLowerCase())){
                break;
            }

            String name = encryptors.getOrDefault(mode,null);
            if(StringUtils.isEmpty(name)){
                System.out.println("输入有误！");
                continue;
            }
            Encryptor encryptor = EncryptorFactory.get(name);
            if(encryptor == null){
                System.out.println("不支持的加密器！");
                continue;
            }
            encryptor.encrypt(br);
        }
        return mode;
    }

    private static String decrypt(String tip, BufferedReader br) throws Exception {
        String mode = null;
        while(true){
            System.out.println("--------------------------------------------");
            System.out.println("解密模式：\n"+tip+"\nE:加密\nexit:退出");
            System.out.println("--------------------------------------------");
            System.out.print("请输入：");
            mode = br.readLine();

            if("exit".equals(mode.toLowerCase()) ||
                    "e".equals(mode.toLowerCase())){
                break;
            }

            String name = decryptors.getOrDefault(mode,null);
            if(StringUtils.isEmpty(name)){
                System.out.println("输入有误！");
                continue;
            }
            Decryptor encryptor = DecryptorFactory.get(name);
            if(encryptor == null){
                System.out.println("不支持的解密器！");
                continue;
            }
            encryptor.decrypt(br);
        }
        return mode;
    }
}
