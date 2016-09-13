package com.cq.sdk.android.qq.utils;

import com.cq.sdk.android.qq.struct.JceMap;
import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Logger;
import com.cq.sdk.utils.Number;

/**
 * Created by CuiYaLei on 2016/8/20.
 */
public class JceOutputStream {
    private Pack pack=new Pack();
    public void clear(){
        this.pack.empty();
    }
    public ByteSet toByteArray(){
        return this.pack.getAll();
    }
    public void wrap(ByteSet bytes){
        this.pack.setData(bytes);
    }
    public void writeHead(int val,int tag){
        if(tag>=15){
            val= (byte) (val | 240);
            this.pack.setByte((byte) val);
            this.pack.setByte((byte) tag);
        }else{
            val= (byte) (val | (tag<<4));
            pack.setByte((byte) val);
        }
    }
    public void writeObj(int type,ByteSet val,int tag){
        switch (type){
            case 0:
                this.writeByte((byte) Number.byte4ToInt(val,0),tag);
                break;
            case 1:
                this.writeShort((short) Number.byte4ToInt(val,0),tag);
                break;
            case 2:
                this.writeInt(Number.byte4ToInt(val,0),tag);
                break;
            case 3:
                this.writeLong(Number.bytesToLong(val),tag);
                break;
            case 13:
                this.writeSimpleList(val,tag);
                break;
            case 8:
                Logger.error("writeObj错误8");
                break;
            case 6:
                this.writeStringByte(new String(val.getByteSet()),tag);
                break;
            case 9:
                this.writeList(val,tag);
                break;
            case 7:
                this.writeStringByte(new String(val.getByteSet()),tag);
                break;
        }
    }
    public void writeByte(byte val,int tag){
        if(val==0){
            this.writeHead( 12,tag);
        }else{
            this.writeHead( 0,tag);
            this.pack.setByte(val);
        }
    }
    public void writeShort(short val,int tag){
        if(val>=Byte.MIN_VALUE && val<=Byte.MAX_VALUE){
            this.writeByte((byte) val,tag);
        }else{
            this.writeHead(1,tag);
            this.pack.setShort(val);
        }
    }
    public void writeInt(int val,int tag){
        if(val>=-Short.MIN_VALUE && val<=Short.MAX_VALUE){
            this.writeShort((short) val,tag);
        }else{
            this.writeHead(2,tag);
            this.pack.setInt(val);
        }
    }
    public void writeLong(long val,int tag){
        if(val>=Integer.MIN_VALUE && val<=Integer.MAX_VALUE){
            this.writeInt((int) val,tag);
        }else{
            this.writeHead(3,tag);
            this.pack.setBin(Bin.long2Bin(val));
        }
    }
    public void writeByteString(String val,int tag){
        ByteSet byteSet= ByteSet.parse(val);
        if(byteSet.length()>255){
            this.writeHead(7,tag);
            this.pack.setInt(byteSet.length());
            this.pack.setBin(byteSet);
        }else{
            this.writeHead(6,tag);
            this.pack.setByte((byte) byteSet.length());
            this.pack.setBin(byteSet);
        }
    }
    public void writeStringByte(String val,int tag){
        ByteSet byteSet= ByteSet.parse(val.getBytes());//默认utf-8
        if(byteSet.length()>255){
            this.writeHead(7,tag);
            this.pack.setInt(byteSet.length());
            this.pack.setBin(byteSet);
        }else{
            this.writeHead(6,tag);
            this.pack.setByte((byte) byteSet.length());
            this.pack.setBin(byteSet);
        }
    }
    public void writeJceStruct(ByteSet bytes,int tag){
        this.writeHead(10,tag);
        this.pack.setBin(bytes);
        this.writeHead(11,0);
    }
    public void writeSimpleList(ByteSet bytes,int tag){
        this.writeHead(13,tag);
        this.writeHead(0,0);
        this.writeInt(bytes.length(),0);
        this.pack.setBin(bytes);
    }
    public void writeList(ByteSet bytes,int tag){
        this.writeHead(9,tag);
        this.writeInt(bytes.length(),0);
        for(int i=0;i<bytes.length();i++){
            this.writeInt(bytes.get(i),0);
        }
    }
    public void writeMap(JceMap[] jceMaps, int tag){
        this.writeHead(8, tag);
        int len=jceMaps.length;
        this.writeShort((short) len,0);
        if(len==0){
            return ;
        }else{
            for(int i=0;i<len;i++){
                this.writeObj(jceMaps[i].keyType,jceMaps[i].key,0);
                this.writeObj(jceMaps[i].valType,jceMaps[i].val,1);
            }
        }
    }
    public void putHex(String hex){
        this.pack.setHex(hex);
    }
}
