package com.cq.sdk.android.qq.utils;

/**
 * Created by admin on 2016/9/2.
 */
public class Validate extends com.cq.sdk.utils.Validate {
    public static final boolean qqAccount(String qq){
        if(qq.length()>=5 && qq.length()<=12 && Validate.number(qq)){
            return true;
        }
        return false;
    }
}
