package com.cq.sdk.utils;

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
}
