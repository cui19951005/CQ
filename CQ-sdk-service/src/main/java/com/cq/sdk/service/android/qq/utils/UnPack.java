package com.cq.sdk.service.android.qq.utils;

import com.cq.sdk.service.android.qq.utils.Bin;
import com.cq.sdk.service.utils.ByteSet;

/**
 * Created by CuiYaLei on 2016/8/14.
 */
public class UnPack {
    private ByteSet mBin=new ByteSet();
    public ByteSet getAll(){
        return this.mBin;
    }
    public String getAllHex(){
        return Bin.bin2Hex(this.mBin);
    }
    public ByteSet getBin(int length){
        ByteSet bin=this.mBin.getLeft(length);
        this.mBin=this.mBin.getRight(this.mBin.length()-length);
        return bin;
    }
    public byte getByte(){
        ByteSet bin=this.mBin.getLeft(1);
        this.mBin=this.mBin.getRight(this.mBin.length()-1);
        return (byte) Bin.bin2Byte(bin);
    }
    public int getInt(){
        ByteSet bin=this.mBin.getLeft(4);
        this.mBin=this.mBin.getRight(this.mBin.length()-4);
        return Bin.bin2Int(bin);
    }
    public long getLong(){
        ByteSet bin=this.mBin.getLeft(8);
        this.mBin=this.mBin.getRight(this.mBin.length()-8);
        return Bin.bin2Long(bin);
    }
    public short getShort(){
        ByteSet bin=this.mBin.getLeft(2);
        this.mBin=this.mBin.getRight(this.mBin.length()-2);
        return Bin.bin2Short(bin);
    }
    public ByteSet getToken(){
        return this.getBin(getShort());
    }
    public int length(){
        return this.mBin.length();
    }
    public void setData(ByteSet data){
        this.mBin=data;
    }
    public void setDataHex(String hex){
        this.mBin= Bin.hex2Bin(hex);
    }
}
