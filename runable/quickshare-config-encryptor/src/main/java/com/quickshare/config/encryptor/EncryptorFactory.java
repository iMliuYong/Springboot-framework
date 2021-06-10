package com.quickshare.config.encryptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author liu_ke
 */
public class EncryptorFactory {

    private final static Map<String,Encryptor> encryptors = new HashMap<>();
    private final static Map<String,String> encryptorNames = new HashMap<>();

    static {
        try {
            Set<Class<?>> set = new Scanner().getAnnotationClasses("com.quickshare.config.encryptor.encryptors", EncryptorType.class);
            for (Class<?> c : set) {
                Object bean = c.newInstance();
                EncryptorType annotation = c.getAnnotation(EncryptorType.class);
                encryptors.put(annotation.value(), (Encryptor) bean);
                encryptorNames.put(annotation.value(),annotation.caption());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String,String> lstNames(){
        return encryptorNames;
    }

    public static Encryptor get(String type){
        return encryptors.getOrDefault(type,null);
    }
}
