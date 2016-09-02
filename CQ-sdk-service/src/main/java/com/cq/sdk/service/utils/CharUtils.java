package com.cq.sdk.service.utils;
import java.util.*;

/**
 * Created by admin on 2016/8/22.
 */
public class CharUtils {
    public static char[] subChars(char[] chars,int startIndex,int endIndex){
        if(startIndex==0&&endIndex==chars.length){
            return chars;
        }
        return Arrays.copyOfRange(chars,startIndex,endIndex);
    }

    /**
     * 忽略负数
     * @param l
     * @return
     */
    public static char[] valueOf(long l){
        char[] buf = new char[(l < 0) ? Number.NumberSize(-l) + 1 : Number.NumberSize(l)];
        getChars(l, buf);
        return buf;
    }
/*    public static char[] valueOfA(long l){
        int size =  (l < 0) ? Number.NumberSize(-l) + 1 : Number.NumberSize(l);
        char[] chars =new char[size];
        for(int i=0;i<chars.length;i++){
            long val=Number.LONG_SIZE[chars.length-i-1];
            chars[i] = (char) (l % val + 48);
        }
        return null;
    }*/
    private final static void getChars(long i, char[] buf) {
        long q;
        long r;
        int charPos = buf.length;
        while (i > Integer.MAX_VALUE) {
            q = i / 100;
            // really: r = i - (q * 100);
            r = (int)(i - ((q << 6) + (q << 5) + (q << 2)));
            i = q;
            buf[--charPos] = (char) (r%10+48);
            buf[--charPos] = (char)(r/10+48);
        }

        // Get 2 digits/iteration using ints
        long q2;
        long i2 = i;
        while (i2 >= 65536) {
            q2 = i2 / 100;
            // really: r = i2 - (q * 100);
            r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
            i2 = q2;
            buf[--charPos] = (char) (r%10+48);
            buf[--charPos] = (char)(r/10+48);
        }
        while (true){
            q2 = (i2 * 52429) >>> (16+3);
            r = i2 - ((q2 << 3) + (q2 << 1));  // r = i2-(q2*10) ...
            if(r<10){
                buf[--charPos]=(char) (r+48);
            }else{
                buf[--charPos]=(char) (r+65);
            }
            i2 = q2;
            if (i2 == 0) break;
        }
    }
    public static int indexOf(char[] value,char[] dst,int startIndex){
        for(int i=startIndex;i<value.length;i++){
            boolean isTrue=true;
            if(i+dst.length<value.length) {
                for (int j = 0; j < dst.length; j++) {
                    if (value[i + j] != dst[j]) {
                        isTrue = false;
                        break;
                    }
                }
            }else{
                return -1;
            }
            if(isTrue){
                return i;
            }
        }
        return -1;
    }
    public static char[] suffixZeroize(char[] val,int length,char character){
        CharBuffer stringBuffer=new CharBuffer(val.length+length);
        stringBuffer.append(val);
        char[] chars=new char[length];
        for(int i=0;i<length;i++){
            chars[i]=character;
        }
        stringBuffer.append(chars);
        return stringBuffer.getValue();
    }
    public static char[] prefixZeroize(char[] val, int length,char character){
        CharBuffer charBuffer=new CharBuffer();
        for(int i=val.length;i<length;i++){
            charBuffer.append(character);
        }
        charBuffer.append(val);
        return charBuffer.getValue();
    }
    public static char[] fullCharacter(int length,char character){
        CharBuffer charBuffer=new CharBuffer();
        for(int i=0;i<length;i++){
            charBuffer.append(character);
        }
        return charBuffer.getValue();
    }

}