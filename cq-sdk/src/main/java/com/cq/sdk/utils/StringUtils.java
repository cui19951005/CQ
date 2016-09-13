package com.cq.sdk.utils;

import java.io.File;

/**
 * Created by admin on 2016/9/2.
 */
public class StringUtils {
    public static final String filePathConvertPack(File file,String packName){
        String[] array=packName.split("\\.");
        StringBuilder sb=new StringBuilder("/");
        for(String item : array){
            if(item.indexOf("*")==-1){
                sb.append(item);
                sb.append("/");
            }else{
                break;
            }
        }
        String string=sb.toString();
        String root=Thread.currentThread().getClass().getResource(string).getFile();
        root=root.substring(1,root.length()-string.length()+1);
        String pack=file.getAbsolutePath().substring(root.length()).replace("\\","/").replace("/",".");
        pack=pack.substring(0,pack.lastIndexOf("."));
        return pack;
    }

    /**
     * 替换空格为标准
     * @param str 字符串
     * @return
     */
    public static final String replaceSpace(String str){
        do{
            String temp=str.replace("  "," ");
            if(temp.equals(str)){
                return temp;
            }
        }while (true);
    }
}
