package com.cq.sdk.android.qq.utils;

import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Number;

/**
 * Created by CuiYaLei on 2016/8/14.
 */
public class Pack {
    private ByteSet mBin=new ByteSet();
    public void empty(){
        this.mBin=ByteSet.empty();
    }
    public ByteSet getAll(){
        return this.mBin.clone();
    }
    public int length(){
        return this.mBin.length();
    }
    public void setBin(ByteSet bin){
        this.mBin.append(bin);
    }
    public void setByte(byte b){
        this.mBin.append(Bin.byte2Bin(b));
    }
    public void setData(ByteSet bytes){
        this.mBin=bytes;
    }
    public void setHex(String hex){
        this.mBin.append( Bin.hex2Bin(hex));
    }
    public void setInt(int val){
        this.mBin.append(Bin.int2Bin(val));
    }
    public void setShort(short val){
        this.mBin.append(Bin.short2Bin(val));
    }
    public void setLong(long val){
        this.mBin.append(Bin.long2Bin(val));
    }
    public void setUInt(int uint){
        this.setBin(Number.longToByte8(MathUtils.toULong(uint)));
    }
    public void setStr(String str){
        this.mBin.append(str.getBytes());
    }
    public void setToken(ByteSet token){
        setShort((short) token.length());
        setBin(token);
    }
}
