package com.cq.sdk.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

/**
 * Created by admin on 2016/10/14.
 */
public final class Hex {
    /**
     * 16进制内任意进制转换
     * @param num
     * @param srcBase
     * @param destBase
     * @return
     */
    public static String baseNum(String num,int srcBase,int destBase){
        if(srcBase == destBase){
            return num;
        }
        char[] chars = num.toCharArray();
        int len = chars.length;
        if(destBase != 10){//目标进制不是十进制 先转化为十进制
            num = baseNum(num,srcBase,10);
        }else{
            int n = 0;
            for(int i = len - 1; i >=0; i--){
                int val=(int)(chars[i]);
                if(val<58){
                    n+=(val-48)*Math.pow(srcBase, len - i - 1);
                }else if(val>64&&val<91){
                    n+=(val-65+10)*Math.pow(srcBase, len - i - 1);
                }
            }
            return String.valueOf(n);
        }
        return baseString(new BigDecimal(num),BigDecimal.valueOf(destBase));
    }
    /**
     * 将数转为任意进制
     * @param num
     * @param base
     * @return
     */
    public static String baseString(BigDecimal num,BigDecimal base){
        StringBuilder str = new StringBuilder();
        Stack<Character> s = new Stack<>();
        while(!num.equals(BigDecimal.valueOf(0))){
            long val=num.divideAndRemainder(base)[1].longValue();
            if(val<10) {
                s.push((char)(val+48));
            }else{
                s.push((char)(val+65-10));
            }
            num=num.divide(base,0,RoundingMode.DOWN);
        }
        while(!s.isEmpty()){
            str.append(s.pop());
        }
        return str.toString();
    }
    public static String baseString(int num,int base){
        return Hex.baseString(BigDecimal.valueOf(num),BigDecimal.valueOf(base));
    }
}
