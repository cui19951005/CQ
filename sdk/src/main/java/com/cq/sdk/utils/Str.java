package com.cq.sdk.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/9/2.
 */
public final class Str {
    public final static String SPACE=" ";
    public final static String EMPTY="";
    public static final String replace(String str,String[] c){
        while (true){
            String temp=str;
            for(String item : c){
                temp=temp.replace(item, Str.EMPTY);
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
        if (lr) {
            int leftIndex = text.indexOf(left);
            int rightIndex = text.indexOf(right,leftIndex) + right.length();
            if (leftIndex == -1 || rightIndex == -1) {
                return null;
            }
            try {
                return text.substring(leftIndex, rightIndex);
            }catch (Exception e){
                return  null;
            }
        } else {
            return Str.subString(text, left, right);
        }
    }
    public static final String left(String text,String left){
        return text.substring(0,text.indexOf(left));
    }
    public static final int subStringInt(String text, String left, String right){
        int baseIndex=text.indexOf(left)+left.length();
        text=text.substring(baseIndex);
        int count=0;
        for(int i=0;i<text.length()-right.length()+1;i++){
            String rightStr=text.substring(i,i+right.length());
            if (rightStr.equals(right)) {
                if (count == 0) {
                    return baseIndex + i;
                } else {
                    count--;
                }
            } else if (text.length() - i - 1 >= left.length() && text.substring(i, i + left.length()).equals(left)) {
                count++;
            }
        }
        return -1;
    }
    public static final String subStringSymmetric(String text, String left, String right){
        int index= Str.subStringInt(text,left,right);
        if(index!=-1) {
            return text.substring(text.indexOf(left) + left.length(), index);
        }else{
            return null;
        }
    }
    public static final String join(String str,String... array){
        return Str.join(str,0,array.length,array);
    }
    public static final String join(String str,int off,int length,String... array){
        StringBuilder sb=new StringBuilder();
        for(int i=off;i<off+length;i++){
            sb.append(array[i]);
            if(array.length!=i+1){
                sb.append(str);
            }
        }
        return sb.toString();
    }
    public static final String concat(String... array){
        StringBuilder stringBuilder=new StringBuilder();
        for(String item : array){
            stringBuilder.append(item);
        }
        return stringBuilder.toString();
    }
    public static final String decimal(BigDecimal bigDecimal,int decimal){
        return String.format("%."+decimal+"f",bigDecimal.doubleValue());
    }
    public static String unicode(String unicode){
        String[] array=unicode.split("////u");
        StringBuffer sb=new StringBuffer();
        for(int i=1;i<array.length;i++){
            sb.append((char)(int)Integer.valueOf(array[i]));
        }
        return sb.toString();
    }
}
