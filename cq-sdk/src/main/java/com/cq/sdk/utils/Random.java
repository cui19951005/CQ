package com.cq.sdk.utils;

/**
 * Created by CuiYaLei on 2016/8/12.
 */
public final class Random {
    public static final int random(int min,int max){
       return (int)(Math.random()*(max-min+1)+min);
    }
}
