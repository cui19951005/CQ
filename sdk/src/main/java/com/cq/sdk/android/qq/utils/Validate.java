package com.cq.sdk.android.qq.utils;

/**
 * Created by admin on 2016/9/2.
 */
public class Validate  {
    public static final boolean account(String qq){
        if(qq.length()>=5 && qq.length()<=12 && com.cq.sdk.utils.Validate.number(qq)){
            return true;
        }
        return false;
    }
}
