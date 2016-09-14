package com.cq.sdk.utils;

import java.security.MessageDigest;

/**
 * Created by admin on 2016/9/14.
 */
public class Encryption {
    public final static ByteSet MD5(ByteSet btInput) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput.getByteSet());
            return ByteSet.parse(mdInst.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
