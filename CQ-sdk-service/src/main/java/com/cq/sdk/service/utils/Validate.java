package com.cq.sdk.service.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CuiYaLei on 2016/8/13.
 */
public class Validate {
    public static final boolean number(String val){
        Pattern pattern=Pattern.compile("\\d+");
        Matcher matcher=pattern.matcher(val);
        return matcher.matches();
    }
    public static final boolean qqAccount(String val){
        if(val.length()>=5 && val.length()<13 && Validate.number(val)){
            return true;
        }
        return false;
    }
}
