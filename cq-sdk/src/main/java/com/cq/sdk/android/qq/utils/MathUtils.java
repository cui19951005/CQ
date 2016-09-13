package com.cq.sdk.android.qq.utils;

/**
 * Created by CuiYaLei on 2016/8/14.
 */
public class MathUtils {
    public static final long toULong(int val){
        return val & 0x0FFFFFFFF;
    }
    public static final int toUInt(short val){
        return val & 0x0FFFF;
    }
    public static final short toUShort(byte b){
        return (short) (b & 0x0FF);
    }
}
