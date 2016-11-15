package com.cq.sdk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by admin on 2016/11/10.
 */
public final class FileUtils {
    public static String read(String fileName,String encoding){
        FileInputStream inputStream=null;
        try {
            File file=new File(fileName);
            if(!file.exists()){
                return null;
            }
            inputStream = new FileInputStream(file);
            int length;
            byte[] bytes=new byte[8192];
            StringBuilder sb=new StringBuilder();
            while ((length=inputStream.read(bytes))>0){
                sb.append(new String(bytes,0,length,encoding));
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            Logger.error("file not exists",e);
        } catch (IOException e) {
            Logger.error("file read error",e);
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
