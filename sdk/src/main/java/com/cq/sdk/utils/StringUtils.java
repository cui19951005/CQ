package com.cq.sdk.utils;

import java.util.Map;

/**
 * Created by admin on 2016/9/2.
 */
public final class StringUtils {

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
    public static final String replace(String str,String[] c){
        while (true){
            String temp=str;
            for(String item : c){
                temp=temp.replace(item,"");
            }
            if(temp==str){
                break;
            }else{
                str=temp;
            }
        }
        return str;
    }
    public static final String subString(String text,String left,String right){
        int leftIndex=text.indexOf(left)+left.length();
        int rightIndex=text.indexOf(right);
        if(leftIndex==-1 || rightIndex==-1){
            return null;
        }
        return text.substring(text.indexOf(left)+left.length(),text.indexOf(right));
    }
    public static final String subString(String text,String left,String right,boolean lr){
        if(lr){
            int leftIndex=text.indexOf(left);
            int rightIndex=text.indexOf(right)+right.length();
            if(leftIndex==-1 || rightIndex==-1){
                return null;
            }
            return text.substring(leftIndex,rightIndex);
        }else {
            return StringUtils.subString(text,left,right);
        }
    }
    public static final String left(String text,String left){
        return text.substring(0,text.indexOf(left));
    }
    public static final int subStringInt(String text, String left, String right){
        int index=text.indexOf(left);
        int baseIndex=-1,count=0;
        String temp=text.substring(index,text.indexOf(right)+right.length());
        while (true){
            baseIndex=temp.indexOf(left,baseIndex+1);
            if(baseIndex!=-1){
                count++;
            }else{
                break;
            }
        }
        baseIndex=-1;
        while (true){
            int indexOf=text.indexOf(right,baseIndex+1);
            if(count==0){
                break;
            }else if(indexOf!=-1){
                count--;
                baseIndex=indexOf;
            }else{
                break;
            }
        }
        return baseIndex;
        //return text.substring(index,baseIndex+right.length());
    }
    public static final String subStringSymmetric(String text, String left, String right){
        return text.substring(text.indexOf(left)+left.length(),StringUtils.subStringInt(text,left,right));
    }
}
