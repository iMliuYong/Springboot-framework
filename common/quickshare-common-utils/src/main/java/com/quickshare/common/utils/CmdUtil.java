package com.quickshare.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 命令
 * @author liu_ke
 */
public class CmdUtil {

    public static ExecResult executeLocalCmd(String[] cmdarray,String[] params) {
        ExecResult result = new ExecResult();
        try{
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmdarray,params);

            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader bufferStdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuffer stringErrBuffer = new StringBuffer();
            List<String> output = new ArrayList<>();
            String temp;
            String temp2;

            while ((temp = bufferReader.readLine()) != null) {
                if(!StringUtils.isEmpty(temp)){
                    output.add(temp);
                }
            }

            while((temp2 = bufferStdErr.readLine())!= null){
                stringErrBuffer.append(temp2);
            }

            int code = process.waitFor();
            result.setCode(code);
            result.setError(stringErrBuffer.toString());
            result.setOutput(output);
        }
        catch (InterruptedException | IOException e){
            result.setError(e.getMessage());
        }
        finally {
            return result;
        }
    }
}
