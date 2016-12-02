package com.cq.sdk.utils;

import java.util.UUID;

/**
 * Created by admin on 2016/11/15.
 */
public final class ID {
    public static String time(){
        return String.valueOf(Thread.currentThread().getId())+String.valueOf(System.currentTimeMillis())+Random.value(10000,99999);
    }
    public static final String timeHex(){
        return Hex.baseNum(ID.time(),10,36);
    }
    public static final String UUID(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
}
