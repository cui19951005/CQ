package com.cq.sdk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
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
    public static String md5(File file)  {
        String value = null;
        FileInputStream in = null;
        try {
            in=new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}
