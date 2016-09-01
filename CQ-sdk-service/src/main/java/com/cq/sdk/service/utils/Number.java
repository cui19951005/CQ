package com.cq.sdk.service.utils;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by CuiYaLei on 2016/8/12.
 */
public class Number {
    private char[] value;
    private boolean isNegative;
    public Number(String val) {
        if(val.charAt(0)=='-'){
            this.isNegative=true;
            this.value = val.substring(1).toCharArray();
        }else{
            this.value = val.toCharArray();
        }
    }
    public Number(long val){
        if(val<0){
            val=-val;
            this.isNegative=true;
        }
        this.value=CharUtils.valueOf(val);
    }
    private Number(char[] chars){
        this.value=chars;
    }
    private Number add(char[] number){
        int minLen;
        char[] value= Arrays.copyOfRange(this.value,0,this.value.length),bc=number;
        if(value.length>number.length){
            minLen=number.length;
            bc= CharUtils.prefixZeroize(number,value.length,'0');
        }else{
            minLen=value.length;
            value= CharUtils.prefixZeroize(value,number.length,'0');
        }
        int base=18;
        int length=minLen/base+1;
        long lastVal=0;
        CharBuffer cb=new CharBuffer();
        for(int i=length-1;i>=0;i--){
            int nowBase=(i+1)*base;
            int endLen=value.length>nowBase?nowBase:value.length;
            long c = Number.parseLong(CharUtils.subChars(value,i * base,endLen));
            long d = Number.parseLong(CharUtils.subChars(bc,i * base, endLen));
            char[] result=CharUtils.valueOf(c+d+lastVal);
            lastVal=0;
            if(result.length>base) {
                int len=result.length>base?result.length-base:0;
                cb.append(CharUtils.subChars(result,len,result.length));
                lastVal = Number.parseLong(CharUtils.subChars(result,0, len));
            }else{
                cb.append(result);
            }
        }
        this.value=cb.getValue();
        return this;
    }
    private char[] subtraction(char[] number){
        char[] value=this.value.clone();
        CharBuffer sb=new CharBuffer();
        int baseLen=18;
        int maxLen=0;
        int length=0;
        if(value.length>number.length){
            maxLen=value.length;
            number=CharUtils.prefixZeroize(number,value.length-number.length,'0');
        }else{
            maxLen=number.length;
            value=CharUtils.prefixZeroize(value,number.length-value.length,'0');
        }
        length=maxLen/baseLen+(maxLen%baseLen==0?0:1);
        for(int i=0;i<length;i++) {
            int startIndex = i * baseLen;
            int endIndex = (i + 1) * baseLen;
            endIndex = endIndex > maxLen ? maxLen : endIndex;
            char[] c = CharUtils.subChars(value,startIndex, endIndex);
            char[] d = CharUtils.subChars(number,startIndex, endIndex);
            long e = Number.parseLong(c) - Number.parseLong(d);
            char[] es = CharUtils.valueOf(e);
            if (e < 0) {
                if(sb.length()==0) {
                    if(e!=0) {
                        sb.append(es);
                    }
                }else{
                    sb.substring(startIndex-baseLen<0?0:startIndex-baseLen,startIndex);
                    char[] fs = CharUtils.fullCharacter(baseLen, '9');
                    long f = Number.parseLong(fs);
                    sb.append(f + e + 1);
                }

            } else {
                sb.append(CharUtils.prefixZeroize(es,baseLen-es.length,'0'));
            }
        }
        return sb.getValue();
    }
    private char[] multiply(char[] number){
        CharBuffer charBuffer=new CharBuffer();
        char[] value=this.value.clone();
        int baseLen=(Number.LONG_SIZE.length-2)/2;//-2防止超过long最大值除以2防止两数相乘超过最大
        int numberLen=value.length/baseLen+(value.length%baseLen==0?0:1);
        for(int i=0;i<numberLen;i++){
            int numberStartIndex=i*baseLen;
            int numberEndIndex=(i+1)*baseLen;
            numberEndIndex=numberEndIndex>value.length?value.length:numberEndIndex;
            long numberLong=Number.parseLong(CharUtils.subChars(value,numberStartIndex,numberEndIndex));
            int bLen=number.length/baseLen+(number.length%baseLen==0?0:1);
            for(int j=0;j<bLen;j++){
                int dStartIndex=j*baseLen;
                int dEndIndex=(j+1)*baseLen;
                dEndIndex=dEndIndex>number.length?number.length:dEndIndex;
                long d=Number.parseLong(CharUtils.subChars(number,dStartIndex,dEndIndex));
                long f=numberLong*d;
                char[] val=CharUtils.valueOf(f);
                if(charBuffer.length()==0){
                    val=CharUtils.suffixZeroize(val,value.length-numberEndIndex+number.length-dEndIndex,'0');
                    charBuffer.append(val);
                }else{
                    int sbStartIndex=numberStartIndex+dStartIndex;//进位i*baseLen+j*baseLen;
                    int fLen=Number.NumberSize(f);
                    int sbEndIndex=(fLen+sbStartIndex>charBuffer.length())?charBuffer.length():fLen+sbStartIndex;
                    long e=Number.parseLong(charBuffer.substring(sbStartIndex,sbEndIndex));
                    long ef=e+f;
                    int eLen=sbEndIndex-sbStartIndex;
                    char[] efs=CharUtils.valueOf(ef);
                    charBuffer.replace(CharUtils.subChars(efs,efs.length-eLen,efs.length),sbStartIndex,sbEndIndex);
                    if(efs.length>eLen){
                        f=Number.parseLong(CharUtils.subChars(efs,0,efs.length-eLen));
                        int z=0;
                        do{
                            int startIndex=sbStartIndex-(z+1)*baseLen;
                            if(startIndex<0){
                                startIndex=0;
                            }
                            int endIndex=sbStartIndex-z*baseLen;
                            eLen=endIndex-startIndex;
                            e=Number.parseLong(CharUtils.subChars(charBuffer.getValue(),startIndex,endIndex));//取高位
                            ef=e+f;
                            efs=CharUtils.valueOf(ef);
                            if(startIndex==0){
                                char[] back=charBuffer.substring(endIndex);//可能补位的零
                                charBuffer.setLength(0);
                                charBuffer.append(efs);
                                charBuffer.append(back);
                                break;
                            }else{
                                charBuffer.replace(CharUtils.subChars(efs,efs.length-eLen,efs.length),startIndex,endIndex);
                            }
                            if(efs.length==eLen){//长度一致
                                break;
                            }
                        }while (true);
                    }
                }
            }
        }
        return charBuffer.getValue();
    }
    @Override
    public String toString() {
        return new String(this.value);
    }
    public Number clone(){
        return new Number(this.value.clone());
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Number){
            char[] temp=((Number) obj).value;
            return this.equals(temp);
        }else if(obj instanceof Byte||obj instanceof Character||obj instanceof Short || obj instanceof Integer || obj instanceof Long){
            return this.equals(obj.toString().toCharArray());
        }else if(obj instanceof String){
            return this.equals(((String) obj).toCharArray());
        }
        return super.equals(obj);
    }
    public boolean equals(char[] chars){
        if(chars.length!=this.value.length){
            return false;
        }else{
            for(int i=0;i<chars.length;i++){
                if(chars[i]!=this.value[i]){
                    return false;
                }
            }
            return true;
        }
    }

    public final static long [] INTEGER_SIZE = { 1,10, 100, 1000, 10000, 100000, 1000000, 10000000,
            100000000, 1000000000 };
    public final static long[] LONG_SIZE =new long[]{1,10, 100, 1000, 10000, 100000, 1000000, 10000000,
            100000000, 1000000000,10000000000L,100000000000L,1000000000000L,10000000000000L,
            100000000000000L,1000000000000000L,10000000000000000L,100000000000000000L,
            1000000000000000000L
    };
    public static long parseLong(char[] chars){
        long l=0;
        for(int i=0;i<chars.length;i++){
            l+=(chars[i]-48)* Number.LONG_SIZE[chars.length-i-1];
        }
        return l;
    }
    public static int NumberSize(long x) {
        long p = 10;
        for (int i=1; i<19; i++) {
            if (x < p)
                return i;
            p = 10*p;
        }
        return 19;
    }
    /**
     * 16进制内任意进制转换
     * @param num
     * @param srcBase
     * @param destBase
     * @return
     */
    public static String baseNum(String num,int srcBase,int destBase){
        if(srcBase == destBase){
            return num;
        }
        char[] chars = num.toCharArray();
        int len = chars.length;
        if(destBase != 10){//目标进制不是十进制 先转化为十进制
            num = baseNum(num,srcBase,10);
        }else{
            int n = 0;
            for(int i = len - 1; i >=0; i--){
                int val=(int)(chars[i]);
                if(val<58){
                    n+=(val-48)*Math.pow(srcBase, len - i - 1);
                }else if(val>64&&val<91){
                    n+=(val-65+10)*Math.pow(srcBase, len - i - 1);
                }
            }
            return n + "";
        }
        return baseString(Integer.valueOf(num),destBase);
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
     * @param off
     *            开始位置
     * @return int整数
     */
    public static int byte4ToInt(ByteSet bytes, int off) {
        int b0 = bytes.get(off) & 0xFF;
        int b1 = bytes.get(off + 1) & 0xFF;
        int b2 = bytes.get(off + 2) & 0xFF;
        int b3 = bytes.get(off + 3) & 0xFF;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }
    public static long bytesToLong(ByteSet bytes) {
        ByteBuffer buffer= ByteBuffer.allocate(8);
        buffer.put(bytes.getByteSet(), 0, bytes.length());
        buffer.flip();//need flip
        return buffer.getLong();
    }
    /**
     * 将数转为任意进制
     * @param num
     * @param base
     * @return
     */
    public static String baseString(int num,int base){
        StringBuffer str = new StringBuffer("");
        Stack<Character> s = new Stack<Character>();
        while(num != 0){
            int val=num%base;
            if(val<10) {
                s.push((char)(val+48));
            }else{
                s.push((char)(val+65-10));
            }
            num/=base;
        }
        while(!s.isEmpty()){
            str.append(s.pop());
        }
        return str.toString();
    }
    public static int longToInt(long val){
        if(val<=Integer.MAX_VALUE){
            return (int) val;
        }else{
            return (int) -(Integer.MAX_VALUE+Integer.MAX_VALUE-val);
        }
    }
}
