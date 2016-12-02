package com.cq.sdk.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by CuiYaLei on 2016/8/12.
 */
public final class Number {

    public static int size(long x) {
        long p = 10;
        for (int i=1; i<19; i++) {
            if (x < p)
                return i;
            p = 10*p;
        }
        return 19;
    }

    /**
     * int整数转换为4字节的byte数组
     *
     * @param i
     *            整数
     * @return byte数组
     */
    public static ByteSet intToByte4(int i) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (i & 0xFF);
        targets[1] = (byte) (i >> 8 & 0xFF);
        targets[2] = (byte) (i >> 16 & 0xFF);
        targets[3] = (byte) (i >> 24 & 0xFF);
        return ByteSet.parse(targets);
    }

    /**
     * long整数转换为8字节的byte数组
     *
     * @param lo
     *            long整数
     * @return byte数组
     */
    public static ByteSet longToByte8(long lo) {
        byte[] targets = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((lo >>> offset) & 0xFF);
        }
        return ByteSet.parse(targets);
    }

    /**
     * short整数转换为2字节的byte数组
     *
     * @param s
     *            short整数
     * @return byte数组
     */
    public static ByteSet unsignedShortToByte2(int s) {
        byte[] targets = new byte[2];
        targets[1] = (byte) (s >> 8 & 0xFF);
        targets[0] = (byte) (s & 0xFF);
        return ByteSet.parse(targets);
    }

    /**
     * byte数组转换为无符号short整数
     *
     * @param bytes
     *            byte数组
     * @return short整数
     */
    public static short byte2ToUnsignedShort(ByteSet bytes) {
        return byte2ToUnsignedShort(bytes, 0);
    }

    /**
     * byte数组转换为无符号short整数
     *
     * @param bytes
     *            byte数组
     * @param off
     *            开始位置
     * @return short整数
     */
    public static short byte2ToUnsignedShort(ByteSet bytes, int off) {
        short high = bytes.get(off);
        short low = bytes.get(off + 1);
        return (short)( (high << 8 & 0xFF00) | (low & 0xFF));
    }

    /**
     * byte数组转换为int整数
     *
     * @param bytes
     *            byte数组
     * @return int整数
     */
    public static int byte4ToInt(ByteSet bytes) {
        int b0 = bytes.get(3) & 0xFF;
        int b1 = bytes.get(2) & 0xFF;
        int b2 = bytes.get(1) & 0xFF;
        int b3 = bytes.get(0) & 0xFF;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }
    public static long bytesToLong(ByteSet bytes) {
        ByteBuffer buffer= ByteBuffer.allocate(8);
        buffer.put(bytes.getByteSet(), 0, bytes.length());
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static int longToInt(long val){
        if(val<=Integer.MAX_VALUE){
            return (int) val;
        }else{
            return (int) -(Integer.MAX_VALUE+Integer.MAX_VALUE-val);
        }
    }
}
