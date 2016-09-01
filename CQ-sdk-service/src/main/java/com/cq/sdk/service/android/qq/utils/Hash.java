package com.cq.sdk.service.android.qq.utils;

import com.cq.sdk.service.utils.ByteSet;

import java.security.MessageDigest;

/**
 * Created by CuiYaLei on 2016/8/12.
 */
public class Hash {
    /**
     * md5加密
     * @param s
     * @return
     */
    public final static String MD5_S(ByteSet btInput) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput.getByteSet());
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            String md5=new String(str);
            StringBuffer stringBuffer=new StringBuffer();
            for(int i=0;i<md5.length()/2;i++){
                stringBuffer.append(md5.substring(i*2,(i+1)*2));
                if(i<md5.length()/2-1){
                    stringBuffer.append(" ");
                }
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 实现
     * @param s
     * @return
     */
    public final static ByteSet MD5_B(ByteSet s){
        return Bin.hex2Bin(MD5_S(s));
    }
    public static final ByteSet unQQTea(ByteSet content, ByteSet key){
        return Tea.decrypt(content,key);
    }
    public static final ByteSet QQTea(ByteSet content,ByteSet key){
        return Tea.encryption(content,key);
    }
}
