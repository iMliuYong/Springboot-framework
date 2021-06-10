package com.quickshare.framework.file.filesystem;

import com.quickshare.common.io.FileUtil;
import com.quickshare.framework.file.ConditionalOnFileStorage;
import com.quickshare.framework.file.FileService;
import com.quickshare.framework.file.FileStorageType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author liu_ke
 */
@Service
@ConditionalOnFileStorage(type = FileStorageType.FileSystem)
public class FileSystemServiceImpl implements FileService {

    @Value("${file.filesystem.location:}")
    private String fileLocation1;

    @Value("${quickshare.file.location:}")
    private String fileLocation2;

    @Override
    public boolean resourceExists(String name) {
        return false;
    }

    @Override
    public boolean createResource(String name) {
        return false;
    }

    @Override
    public InputStream getObject(String resourceName, String objectName) {
        return null;
    }

    @Override
    public boolean removeObject(String resourceName, String objectName) {
        return false;
    }

    @Override
    public String putObject(String resourceName, InputStream stream) {
        return null;
    }

    @Override
    public Map<String, String> putObjects(String resourceName, Map<String, InputStream> streamMap) {
        String fileLocation = getFileLocation();
        if(StringUtils.isEmpty(fileLocation)){
            throw new RuntimeException("使用文件系统存储文件，需先配置存储路径[file.filesystem.location]。");
        }

        String directoryPath = Paths.get(fileLocation).resolve(resourceName).toString();
        String logPath = Paths.get(fileLocation).resolve("log").toString();
        try {
            return FileUtil.saveFiles(streamMap, directoryPath, logPath, 3);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean putObject(String resourceName, String objectName, InputStream stream) {
        return false;
    }

    private String getFileLocation(){
        if(StringUtils.isNotEmpty(fileLocation1)){
            return fileLocation1;
        }
        else if(StringUtils.isNotEmpty(fileLocation2)){
            return fileLocation2;
        }
        else {
            return null;
        }
    }
}
