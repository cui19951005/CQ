package com.cq.sdk.utils;

import java.util.regex.Pattern;

/**
 * Created by admin on 2016/9/6.
 */
public class PackUtils {
    public static final Pattern generatePattern(String packName){
        return Pattern.compile(packName.replace("(..)","\\(*\\)").replace(".","\\.").replace("*",".*").replace("?","."));
    }
}
