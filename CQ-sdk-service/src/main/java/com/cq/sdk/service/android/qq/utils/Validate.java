package com.cq.sdk.service.android.qq.utils;

/**
 * Created by admin on 2016/9/2.
 */
public class Validate extends com.cq.sdk.service.utils.Validate {
    public static final boolean qqAccount(String qq){
        if(qq.length()>=5 && qq.length()<=12 && Validate.number(qq)){
            return true;
        }
        return false;
    }
}
