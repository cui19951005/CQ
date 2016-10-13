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
}
