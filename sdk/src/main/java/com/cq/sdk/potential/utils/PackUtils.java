package com.cq.sdk.potential.utils;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/9/6.
 */
public class PackUtils {
    public static final Pattern generateNamePattern(String packName){
        StringBuilder sb=new StringBuilder("^");
        sb.append(packName.replace("(..)","\\(*\\)").replace(".","\\.").replace("*",".+").replace("?","."));
        sb.append("$");
        return Pattern.compile(sb.toString());
    }
    public static final Pattern generateFilePattern(String packName){
        StringBuilder sb=new StringBuilder("^");
        sb.append(packName.replace(".","/").replace("**",".+").replace("*","(\\w|\\d)+").replace("?","."));
        sb.append("$");
        return Pattern.compile(sb.toString());
    }
    public static final String filePathToPack(File file, String packName){
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
}
