package com.quickshare.common.io;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.file.FileResourceManager;
import org.apache.commons.transaction.file.ResourceManagerException;
import org.apache.commons.transaction.util.CommonsLoggingLogger;
import org.apache.commons.transaction.util.LoggerFacade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件操作
 * @author liu_ke
 */
public final class FileUtil {
    
    /**
     * 文件列表
     * @param files 文件列表
     * @param directoryPath 文件夹
     * @param mode 模式(0(默认)按文件名判断重复;
     *                  1删除文件夹所有文件;
     *                  2(暂不支持)不断累加，文件名-1,2,...;
     *                  3按md5值判断重复，目前设计按md5做文件名存储)
     */
    public static Map<String,String> saveFiles(Map<String,InputStream> files,String directoryPath,String logPath,int mode) throws Exception{
        if(!DirectoryUtil.createDirectory(directoryPath)){
            throw new IOException("文件夹创建失败");
        }
        String tmpDir = Paths.get(directoryPath,UUID.randomUUID().toString()).toString();
        Map<String,String> result = new HashMap<>();

        Log log =LogFactory.getLog(FileUtil.class);
        LoggerFacade logger = new CommonsLoggingLogger(log);
        FileResourceManager frm = new FileResourceManager(directoryPath, logPath,false, logger,true);

        String txId = "";
        try{
            frm.start();
            txId =frm.generatedUniqueTxId();
            frm.startTransaction(txId);

            saveTempFiles(files,tmpDir);
            // todo 增加模式2支持
            for (String filename : files.keySet()) {
                String filename2 = removeStartSeparator(filename);
                Path fullPath = Paths.get(directoryPath).resolve(filename2);
                DirectoryUtil.createDirectory(fullPath.toString().substring(0,fullPath.toString().lastIndexOf(File.separator)));
                String tmpFilePath = Paths.get(tmpDir,filename).toString();
                if(mode == 3){
                    InputStream is = new FileInputStream(tmpFilePath);
                    byte[] byt = new byte[is.available()];
                    is.read(byt);
                    is.close();
                    String md5 = DigestUtils.md5Hex(byt);
                    String newPath = removeStartSeparator(Paths.get(directoryPath,filename).toString().replace(directoryPath,""));
                    if(newPath.lastIndexOf(File.separator)>=0){
                        newPath = newPath.substring(0, newPath.lastIndexOf(File.separator)+1) + md5;
                    }
                    else{
                        newPath = md5;
                    }
                    if(!(new File(Paths.get(directoryPath,newPath).toString()).exists())){
                        frm.moveResource(txId,tmpFilePath.replace(directoryPath,""),newPath,true);
                    }
                    
                    result.put(filename, md5);
                }
                else{
                    frm.moveResource(txId,tmpFilePath.replace(directoryPath,""),
                        Paths.get(directoryPath,filename).toString().replace(directoryPath,""),true);
                    result.put(filename, filename);
                }
            }

            if(mode==1){
                DirectoryUtil.clearDirectory(directoryPath,files.keySet(), frm, txId);
            }
            frm.commitTransaction(txId);
        }
        catch(Exception e){
            try{
                frm.rollbackTransaction(txId);
            }
            catch(ResourceManagerException e1){
                throw new IOException("文件处理失败，回滚文件操作失败。");
            }
            finally {
                throw e;
            }
        }
        finally{
            DirectoryUtil.deleteDirectory(tmpDir);
            return result;
        }  
    }

    private static void saveTempFiles(Map<String,InputStream> files,String path) throws IOException{
        DirectoryUtil.createDirectory(path);
        for (Map.Entry<String, InputStream> keyvalue : files.entrySet()) {
            String filename2 = removeStartSeparator(keyvalue.getKey());
            Path fullPath = Paths.get(path).resolve(filename2);
            DirectoryUtil.createDirectory(fullPath.toString().substring(0,fullPath.toString().lastIndexOf(File.separator)));
            Files.copy(keyvalue.getValue(), fullPath,
                        StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static String removeStartSeparator(String path){
        path = Paths.get(path).toString();
        while (path.startsWith(File.separator)){
            path = path.substring(1);
        }
        return path;
    }
}
