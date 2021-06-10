package com.quickshare.common.xml;

import java.io.File;
import java.io.FileInputStream;

public class FileUtil {

    public static String readFile(String resource){
        String encoding = "UTF-8";
        String path = FileUtil.class.getClassLoader().getResource(resource).getFile();
        File file = new File(path);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(filecontent);
            return new String(filecontent, encoding);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(in!=null){
                try{
                    in.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
