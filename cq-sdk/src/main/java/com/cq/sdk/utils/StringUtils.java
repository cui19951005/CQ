package com.cq.sdk.utils;

/**
 * Created by admin on 2016/9/2.
 */
public class StringUtils {

    /**
     * 替换多个空格为一个空格
     * @param str 字符串
     * @return
     */
    public static final String replaceSpace(String str){
        do{
            String temp=str.replace("  "," ");
            if(temp.equals(str)){
                return temp;
            }
            str=temp;
        }while (true);
    }
    public static final String subString(String text,String left,String right){
        return text.substring(text.indexOf(left)+left.length(),text.lastIndexOf(right));
    }
    public static final String left(String text,String left){
        return text.substring(0,text.indexOf(left));
    }
}
