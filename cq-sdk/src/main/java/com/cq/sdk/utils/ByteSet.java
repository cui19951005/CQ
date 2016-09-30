package com.cq.sdk.utils;
import java.io.Serializable;
import java.lang.*;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by admin on 2016/8/17.
 */
public class ByteSet implements Iterable<Byte>,Cloneable,Serializable {
    private byte[] byteSet=new byte[0];

    public ByteSet() {
        this(0);
    }
    public ByteSet(int length){
        this(new byte[length]);
    }
    public ByteSet(byte[] byteSet) {
        this.byteSet = byteSet.clone();
    }
    public ByteSet(byte[] byteSet,int offset,int length){
        this.byteSet= Arrays.copyOfRange(byteSet,offset,length);
    }
    public ByteSet(String hex){
        hex = hex.replace(" ", "");
        if(hex.indexOf("{")==-1) {
            this.byteSet = new byte[hex.length() / 2];
            for (int i = 0; i < this.length(); i++) {
                this.byteSet[i] = ByteSet.uByteToByte(Integer.valueOf(Number.baseNum(hex.substring(i * 2, (i + 1) * 2), 16, 10)));
            }
        }else{
            String content=hex.substring(hex.indexOf("{")+1,hex.indexOf("}"));
            String[] strArray=content.split(",");
            this.byteSet=new byte[strArray.length];
            int i=0;
            for(String str:strArray){
                this.byteSet[i++]= ByteSet.uByteToByte(Integer.valueOf(str));
            }
        }
    }
    public ByteSet(byte b){
        this(new byte[]{b});
    }
    public ByteSet subByteSet(int startIndex, int length){
        ByteSet byteSet=new ByteSet(new byte[length]);
        System.arraycopy(this.byteSet,startIndex,byteSet.byteSet,0,length);
        return byteSet;
    }
    public ByteSet subByteSet(int startIndex){
        return this.subByteSet(startIndex,this.length()-startIndex);
    }
    public ByteSet getLeft(int length){
        if(this.length()<length){
            return null;
        }
        ByteSet result=new ByteSet(length);
        System.arraycopy(this.byteSet,0,result.byteSet,0,length);
        return result;
    }
    public final ByteSet getRight(int length){
        if(length>this.length()){
            return null;
        }
        ByteSet result= new ByteSet(length);
        System.arraycopy(this.byteSet,this.length()-length,result.byteSet,0,length);
        return result;
    }
    public int indexOf(byte[] bytes){
        return this.indexOf(bytes,0);
    }
    public int indexOf(byte[] bytes,int startIndex){
        if(startIndex>this.length()-1){
            return -1;
        }
        for(int i=startIndex;i<this.length();i++){
            boolean isTrue=true;
            for(int j=0;j<bytes.length;j++){
                if(i+j<this.length()&&this.byteSet[i+j]!=bytes[j]){
                    isTrue=false;
                }
            }
            if(isTrue){
                return i;
            }
        }
        return -1;
    }
    public int indexOf(ByteSet bytes){
        return this.indexOf(bytes,0);
    }
    public int indexOf(ByteSet bytes,int startIndex){
        return this.indexOf(bytes.byteSet,startIndex);
    }
    public ByteSet append(byte[]... bytes){
        for(int i=0;i<bytes.length;i++){
            if(bytes[i].length==0){
                continue;
            }
            ByteSet temp=this.clone();
            this.byteSet=new byte[this.length()+bytes[i].length];
            System.arraycopy(temp.byteSet,0,this.byteSet,0,temp.length());
            System.arraycopy(bytes[i],0,this.byteSet,temp.length(),bytes[i].length);
        }
        return this;
    }
    public ByteSet append(byte... bytes){
        return this.append(bytes,new byte[0]);
    }
    public ByteSet append(String... hexs){
        byte[][] bytes=new byte[hexs.length][0];
        for(int i=0;i<hexs.length;i++){
            bytes[i]= ByteSet.parse(hexs[i]).getByteSet();
        }
        return this.append(bytes);
    }
    public ByteSet append(ByteSet... byteSets){
        for(ByteSet bytes : byteSets) {
            this.append(bytes.getByteSet());
        }
        return this;
    }
    public ByteSet append(byte[] bytes,int startIndex,int endIndex){
        return this.append(new ByteSet(bytes).subByteSet(startIndex,endIndex));
    }
    public byte[] getByteSet() {
        return byteSet.clone();
    }
    public int length(){
        return this.byteSet.length;
    }
    public byte get(int index){return this.byteSet[index];}
    public void set(int index,byte b){
        this.byteSet[index]=b;
    }
    @Override
    public ByteSet clone()  {
        return new ByteSet(this.byteSet);
    }
    @Override
    public String toString() {
        /*StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(this.length());
        stringBuffer.append("{");
        for(int i=0;i<this.length();i++){
            stringBuffer.append(this.byteSet[i]);
            if(i!=this.length()-1){
                stringBuffer.append(",");
            }
        }
        stringBuffer.append("}");
        return stringBuffer.toString();*/
        return toStringUInt();
    }
    public String toStringUInt(){
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(this.length());
        stringBuffer.append("{");
        for(int i=0;i<this.length();i++){
            stringBuffer.append(ByteSet.byteToUByte(this.byteSet[i]));
            if(i!=this.length()-1){
                stringBuffer.append(",");
            }
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
    public String toStringHex(){
        StringBuffer stringBuffer=new StringBuffer();
        for(int i=0;i<this.length();i++){
            String b= Number.baseString(ByteSet.byteToUByte(this.get(i)),16);
            if(b.length()<2){
                stringBuffer.append("0");
            }
            stringBuffer.append(b);
            if(i<this.length()-1) {
                stringBuffer.append(" ");
            }
        }
        return stringBuffer.toString();
    }
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {
            private int index =-1;
            public boolean hasNext() {
                return ByteSet.this.length()!= index+1;
            }
            public Byte next() {
                this.index++;
                return ByteSet.this.byteSet[this.index];
            }
            public void remove() {
                ByteSet byteSet= ByteSet.this.clone();
                ByteSet.this.byteSet=new byte[ByteSet.this.length()-1];
                System.arraycopy(byteSet.byteSet,0,ByteSet.this.byteSet,0,this.index);
                System.arraycopy(byteSet.byteSet,this.index+1,ByteSet.this.byteSet,this.index,ByteSet.this.length()-this.index);
                this.index--;
            }

        };
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ByteSet){
            return this.equals(((ByteSet)obj).byteSet);
        }else if(obj instanceof Byte[]){
            Byte[] bytes=(Byte[]) obj;
            byte[] temp=new byte[bytes.length];
            for(int i=0;i<temp.length;i++){
                temp[i]=(byte) (bytes[i]==null?0:bytes[i]);
            }
            return this.equals(temp);
        }else if(obj instanceof byte[]){
            return this.equals((byte[])obj);
        }else if(obj instanceof String){
            try {
                return this.equals(ByteSet.parse(obj.toString()).byteSet);
            }catch (Exception ex){
                return super.equals(obj);
            }
        } else{
            return super.equals(obj);
        }
    }
    public boolean equals(byte[] byteSet){
        if(byteSet==null || byteSet.length!=this.length()){
            return false;
        }
        for(int i=0;i<byteSet.length;i++){
            if(byteSet[i]!=this.byteSet[i]){
                return false;
            }
        }
        return true;
    }

    /**
     * 将字节数组转换字节集
     * @param byteSet
     * @return
     */
    public static ByteSet parse(byte[] byteSet){
        return new ByteSet(byteSet);
    }
    public static ByteSet parse(byte[] byteSet,int offset,int length){
        return new ByteSet(byteSet,offset,length);
    }
    /**
     * 将16进制字节文本转换为字节集
     * @param hex
     * @return
     */
    public static ByteSet parse(String hex){
        return new ByteSet(hex);
    }

    /**
     * 返回空字节集
     * @return
     */
    public static ByteSet empty(){
        return ByteSet.empty(0);
    }

    /**
     * 返回指定长度空的字节集
     * @param length
     * @return
     */
    public static ByteSet empty(int length){
        ByteSet bytes= ByteSet.parse(new byte[length]);
        for(int i=0;i<bytes.length();i++){
            bytes.byteSet[i]=0;
        }
        return bytes;
    }
    /**
     * 到ip地址
     * @param ip
     * @return
     */
    public static final String toIP(byte[] ip){
        if(ip.length!=6 && ip.length!=4){
            return null;
        }
        StringBuffer stringBuffer=new StringBuffer();
        for(int i=0;i<ip.length;i++){
            stringBuffer.append(ByteSet.byteToUByte(ip[i]));
            if(i<ip.length-1){
                stringBuffer.append(".");
            }
        }
        return stringBuffer.toString();
    }
    public static final String toIP(ByteSet bytes){
        return ByteSet.toIP(bytes.getByteSet());
    }
    /**
     *  字节有符号到无符号
     * @param b
     * @return
     */
    public static final int byteToUByte(byte b){
        if(b<0){
            return (int)b+256;
        }else  {
            return (int) b;
        }
    }

    /**
     * 无符号到有符号
     * @param val
     * @return
     */
    public static final byte uByteToByte(int val){
        if(val>127){
            return (byte)(val-256);
        }else{
            return (byte) val;
        }
    }
}
