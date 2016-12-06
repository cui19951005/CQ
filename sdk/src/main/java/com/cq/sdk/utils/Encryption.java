package com.cq.sdk.utils;

import java.security.MessageDigest;

/**
 * Created by admin on 2016/9/14.
 */
public final class Encryption {
    public final static ByteSet md5(ByteSet btInput) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("md5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput.getByteSet());
            return ByteSet.parse(mdInst.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public final static String md5(String text){
        return Encryption.md5(ByteSet.parse(text.getBytes())).toStringHex().replace(",","");
    }
}
