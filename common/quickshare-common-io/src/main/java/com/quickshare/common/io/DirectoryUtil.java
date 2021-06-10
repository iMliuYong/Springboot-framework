package com.quickshare.common.io;

import org.apache.commons.transaction.file.FileResourceManager;
import org.apache.commons.transaction.file.ResourceManagerException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 文件夹操作
 * @author liu_ke
 */
public final class DirectoryUtil {
    
    /**
     * 创建文件夹
     * @param path 文件夹路径
     */
    public static boolean createDirectory(String path){
        File directory = new File(path);
        if(!directory.exists()){
            return directory.mkdirs();
        }
        else{
            return true;
        }
    }

    /**
     * 创建文件夹
     * @param parent 父文件夹路径
     * @param child 子文件夹路径
     */
    public static boolean createDirectory(String parent, String child){
        File directory = new File(parent,child);
        if(!directory.exists()){
            return directory.mkdir();
        }
        else{
            return true;
        }
    }

    public static void clearDirectory(String path, Set<String> filenames, FileResourceManager frm, String txId) throws IOException,ResourceManagerException{

        Map<String,Set<String>> files = new HashMap<>();
        for (String filename: filenames) {
            Path fullpath = Paths.get(path,filename);
            String directory = fullpath.toString().substring(0,fullpath.toString().lastIndexOf(File.separator));
            String name = fullpath.getFileName().toString();
            if(!files.containsKey(directory)){
                files.put(directory,new HashSet());
            }
            files.get(directory).add(name);
        }
        for (String directory:files.keySet()) {
            clearDirectoryInternal(directory,files.get(directory),frm,txId);
        }
    }

    public static boolean clearDirectory(String path) throws IOException{
        return clearDirectory(path,false);
    }

    public static boolean deleteDirectory(String path) throws IOException{
        return clearDirectory(path,true);
    }

    private static boolean clearDirectory(String path,boolean isDelete) throws IOException{
        File directory = new File(path);
        if(isDelete == false && !directory.exists()){
            throw new IOException("文件夹不存在");
        }
        else{
            File[] files = directory.listFiles();
            if(files != null){
                for(File f: files) {
                    boolean result;
                    if(f.isDirectory()) {
                        result = clearDirectory(f.getPath());
                        f.delete();
                    } else {
                        result = f.delete();
                    }
                    if(result == false){
                        return false;
                    }
                }
            }
            if(isDelete){
                return directory.delete();
            }
            else {
                return true;
            }
        }
    }

    private static void clearDirectoryInternal(String path, Set<String> filename, FileResourceManager frm, String txId) throws IOException,ResourceManagerException{
        File directory = new File(path);
        if(!directory.exists()){
            throw new IOException("文件夹不存在");
        }
        else{
            File[] files = directory.listFiles();
            if(files != null){
                for(File f: files) {
                    if(f.isDirectory()){
                        continue;
                    }
                    if(!filename.contains(f.getName())){
                        frm.deleteResource(txId, f.getPath().replace(frm.getStoreDir(),""));
                    }
                }
            }
        }
    }
}
