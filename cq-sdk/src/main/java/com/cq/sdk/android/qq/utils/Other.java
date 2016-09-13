package com.cq.sdk.android.qq.utils;

/**
 * Created by CuiYaLei on 2016/8/14.
 */
public class Other {
    public static final long timeStamp(boolean ms){
        if(ms){
            return System.currentTimeMillis();
        }else{
            return System.currentTimeMillis()/1000;
        }
    }
}
