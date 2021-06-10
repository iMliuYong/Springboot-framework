package com.quickshare.config.encryptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author liu_ke
 */
public class DecryptorFactory {

    private final static Map<String,Decryptor> decryptors = new HashMap<>();
    private final static Map<String,String> decryptorNames = new HashMap<>();

    static {
        try {
            Set<Class<?>> set = new Scanner().getAnnotationClasses("com.quickshare.config.encryptor.decryptors", DecryptorType.class);
            for (Class<?> c : set) {
                Object bean = c.newInstance();
                DecryptorType annotation = c.getAnnotation(DecryptorType.class);
                decryptors.put(annotation.value(), (Decryptor) bean);
                decryptorNames.put(annotation.value(),annotation.caption());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String,String> lstNames(){
        return decryptorNames;
    }

    public static Decryptor get(String type){
        return decryptors.getOrDefault(type,null);
    }
}
