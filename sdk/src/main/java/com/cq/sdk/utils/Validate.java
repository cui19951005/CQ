package com.cq.sdk.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CuiYaLei on 2016/8/13.
 */
public final class Validate {
    public static final boolean number(String val){
        Pattern pattern=Pattern.compile("\\d+");
        Matcher matcher=pattern.matcher(val);
        return matcher.find();
    }
    public static final boolean number(String val,int min,int max){
        Pattern pattern=Pattern.compile("\\d{"+min+","+max+"}");
        Matcher matcher=pattern.matcher(val);
        return matcher.find();
    }
    public static final boolean mobile(String val){
        Pattern pattern=Pattern.compile("1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}");
        Matcher matcher=pattern.matcher(val);
        return matcher.find();
    }
    public static final boolean telephone(String val){
        return Pattern.compile("\\d{3}-\\d{8}|\\d{4}-\\{7,8}").matcher(val).find();
    }
    public static final boolean email(String val){
        return Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?").matcher(val).find();
    }
    public static final boolean id(String val){
        return Pattern.compile("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$").matcher(val).find();
    }
    public static final boolean matcher(String pattern,String val){
        return Pattern.compile(pattern).matcher(val).find();
    }
    public static final boolean date(String format,String val){
        try {
            Time.toDate(val,format);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
