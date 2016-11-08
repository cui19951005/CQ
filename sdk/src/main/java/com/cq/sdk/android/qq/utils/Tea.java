package com.cq.sdk.android.qq.utils;

import com.cq.sdk.android.qq.struct.TeaStruct;
import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Logger;
import com.cq.sdk.utils.Number;
import com.cq.sdk.utils.Random;

/**
 * Created by CuiYaLei on 2016/8/12.
 */
public class Tea {
    private static TeaStruct TEA_STRUCT= TeaStruct.getTeaStruct();
    /**
     * 加密
     * @param content
     * @param key
     * @return
     */
    public static final ByteSet encryption(ByteSet content, ByteSet key){
        Tea.TEA_STRUCT=TeaStruct.getTeaStruct();
        return hashTea(content,key,0,true);
    }

    /**
     * 解密
     * @param content
     * @param key
     * @return
     */
    public static final ByteSet decrypt(ByteSet content,ByteSet key){
        Tea.TEA_STRUCT=TeaStruct.getTeaStruct();
        return unHashTea(content,key,0);
    }
    public static final ByteSet hashTea(ByteSet binFrom,ByteSet binKey,int offset,boolean is16Rounds){
        Tea.TEA_STRUCT.mHeader=true;
        Tea.TEA_STRUCT.mKey=binKey;
        Tea.TEA_STRUCT.mPos=0;
        Tea.TEA_STRUCT.mPadding=0;
        Tea.TEA_STRUCT.mCrypt=0;
        Tea.TEA_STRUCT.mPreCrypt=0;
        Tea.TEA_STRUCT.mPos=(binFrom.length()+10)%8;
        if(Tea.TEA_STRUCT.mPos!=0){
            Tea.TEA_STRUCT.mPos=8-Tea.TEA_STRUCT.mPos;
        }
        Tea.TEA_STRUCT.mOut=new ByteSet((int)(Tea.TEA_STRUCT.mPos+binFrom.length()+9+1));
        Tea.TEA_STRUCT.mPlain=new ByteSet(8);
        Tea.TEA_STRUCT.mPlain.set(0,(byte) LongXOR(LongAND(Random.random(1000,5000),248), Tea.TEA_STRUCT.mPos));
        for(int i=0;i<Tea.TEA_STRUCT.mPos;i++){
            Tea.TEA_STRUCT.mPlain.set(i+1,(byte) LongAND(Random.random(1000,5000),255));
        }
        Tea.TEA_STRUCT.mPrePlain=ByteSet.empty(8);
        Tea.TEA_STRUCT.mPos++;
        Tea.TEA_STRUCT.mPadding=1;
        do{
            if(Tea.TEA_STRUCT.mPos<8){
                Tea.TEA_STRUCT.mPlain.set((int)(Tea.TEA_STRUCT.mPos),(byte) LongAND(Random.random(1000,5000),255));
                Tea.TEA_STRUCT.mPos++;
                Tea.TEA_STRUCT.mPadding++;
            }else{
                encrypt8Bytes(is16Rounds);
            }
        }while (Tea.TEA_STRUCT.mPadding<3);
        int i=offset;
        int intLen=binFrom.length();
        while (intLen>0){
            if(Tea.TEA_STRUCT.mPos<8){
                Tea.TEA_STRUCT.mPlain.set((int)(Tea.TEA_STRUCT.mPos),binFrom.get(i));
                Tea.TEA_STRUCT.mPos++;
                intLen--;
                i++;
            }else{
                encrypt8Bytes(is16Rounds);
            }
        }
        Tea.TEA_STRUCT.mPadding=1;
        while (Tea.TEA_STRUCT.mPadding<8){
            if(Tea.TEA_STRUCT.mPos<8){
                Tea.TEA_STRUCT.mPlain.set((int)Tea.TEA_STRUCT.mPos, (byte) 0);
                Tea.TEA_STRUCT.mPadding++;
                Tea.TEA_STRUCT.mPos++;
            }
            if(Tea.TEA_STRUCT.mPos==8){
                encrypt8Bytes(is16Rounds);
            }
        }
        return Tea.TEA_STRUCT.mOut;
    }
    public static final void encrypt8Bytes(boolean is16Rounds){
        Tea.TEA_STRUCT.mPos=1;
        for(int i=0;i<8;i++){
            if(Tea.TEA_STRUCT.mHeader){
                Tea.TEA_STRUCT.mPlain.set(i,(byte) LongXOR((long)Tea.TEA_STRUCT.mPlain.get(i),(long)Tea.TEA_STRUCT.mPrePlain.get(i)));
            }else {
                if (Tea.TEA_STRUCT.mPreCrypt + i > Tea.TEA_STRUCT.mOut.length()) {
                    return;
                }
                Tea.TEA_STRUCT.mPlain.set(i,(byte) LongXOR((long)Tea.TEA_STRUCT.mPlain.get(i),(long)Tea.TEA_STRUCT.mOut.get(Tea.TEA_STRUCT.mPreCrypt+i)));
            }
        }
        ByteSet crypted=encipher(Tea.TEA_STRUCT.mPlain,is16Rounds);
        for(int i=0;i<crypted.length();i++){
            if(Tea.TEA_STRUCT.mCrypt+i>Tea.TEA_STRUCT.mOut.length()){
                return;
            }
            Tea.TEA_STRUCT.mOut.set(Tea.TEA_STRUCT.mCrypt+i,crypted.get(i));
        }
        for(int i=0;i<8;i++){
            if(Tea.TEA_STRUCT.mCrypt+i>Tea.TEA_STRUCT.mOut.length()){
                return ;
            }
            Tea.TEA_STRUCT.mOut.set(Tea.TEA_STRUCT.mCrypt+i,(byte) LongXOR((long)Tea.TEA_STRUCT.mOut.get(Tea.TEA_STRUCT.mCrypt+i),(long) Tea.TEA_STRUCT.mPrePlain.get(i)));
        }
        for(int i=0;i<Tea.TEA_STRUCT.mPlain.length();i++){
            if(i>Tea.TEA_STRUCT.mPrePlain.length()){
                return ;
            }
            Tea.TEA_STRUCT.mPrePlain.set(i,Tea.TEA_STRUCT.mPlain.get(i));
        }
        Tea.TEA_STRUCT.mPreCrypt=Tea.TEA_STRUCT.mCrypt;
        Tea.TEA_STRUCT.mCrypt+=8;
        Tea.TEA_STRUCT.mPos=0;
        Tea.TEA_STRUCT.mHeader=false;
    }
    public static final ByteSet encipher(ByteSet binInput,boolean is16Rounds){
        long y=getUInt(binInput,0,4);
        long z=getUInt(binInput,4,4);
        long a=getUInt(Tea.TEA_STRUCT.mKey,0,4);
        long b=getUInt(Tea.TEA_STRUCT.mKey,4,4);
        long c=getUInt(Tea.TEA_STRUCT.mKey,8,4);
        long d=getUInt(Tea.TEA_STRUCT.mKey,12,4);
        int rounds;
        if(is16Rounds){
            rounds=16;
        }else{
            rounds=32;
        }
        long sum=0;
        for(int i=0;i<rounds;i++){
            sum=LongAND(sum,4294967295L);
            sum+=2654435769L;
            z=LongAND(z,4294967295L);
            y=y+LongXOR(LongXOR(LongLeft(z,4)+a,z+sum),LongRight(z,5)+b);
            y=LongAND(y,4294967295L);
            z=z+LongXOR(LongXOR(LongLeft(y,4)+c,y+sum),LongRight(y,5)+d);
        }
        return toBytes(y,z);
    }
    public static final boolean decrypt8Bytes(ByteSet input,int offset,int intLen){
        for(int i=0;i<8;i++){
            if(Tea.TEA_STRUCT.mContextStart+i+1>intLen){
                return true;
            }
            if(offset+Tea.TEA_STRUCT.mCrypt+i+1>input.length()){
                return false;
            }
            Tea.TEA_STRUCT.mPrePlain.set(i,(byte) (Tea.TEA_STRUCT.mPrePlain.get(i)^input.get(offset+ Tea.TEA_STRUCT.mCrypt+i)));
        }
        Tea.TEA_STRUCT.mPrePlain=decipher(Tea.TEA_STRUCT.mPrePlain,true);
        if(Tea.TEA_STRUCT.mPrePlain.length()==0){
            return false;
        }
        Tea.TEA_STRUCT.mContextStart+=8;
        Tea.TEA_STRUCT.mCrypt+=8;
        Tea.TEA_STRUCT.mPos=0;
        return true;
    }
    public static final ByteSet decipher(ByteSet binInput,boolean is16Rounds){
        long sum=3816266640L;
        long y=getUInt(binInput,0,4);
        long z=getUInt(binInput,4,4);
        long a=getUInt(Tea.TEA_STRUCT.mKey,0,4);
        long b=getUInt(Tea.TEA_STRUCT.mKey,4,4);
        long c=getUInt(Tea.TEA_STRUCT.mKey,8,4);
        long d=getUInt(Tea.TEA_STRUCT.mKey,12,4);
        int rounds;
        if(is16Rounds){
            rounds=16;
        }else{
            rounds=32;
        }
        for(int i=0;i<rounds;i++){
            long temp=LongXOR(LongXOR(LongLeft(y,4)+c,y+sum),LongRight(y,5)+d);
            z=z-temp;
            z=LongAND(z,4294967295L);
            temp=LongXOR(LongXOR(LongLeft(z,4)+a,z+sum),LongRight(z,5)+b);
            y=y-temp;
            y=LongAND(y,4294967295L);
            sum-=2654435769L;
            sum=LongAND(sum,4294967295L);
        }
        return toBytes(y,z);
    }
    public static final ByteSet unHashTea(ByteSet binFrom,ByteSet key,int offset){
        Tea.TEA_STRUCT.mCrypt=0;
        Tea.TEA_STRUCT.mPreCrypt=0;
        Tea.TEA_STRUCT.mKey=key;
        ByteSet om=new ByteSet(offset+8);
        int intLen=binFrom.length();
        Tea.TEA_STRUCT.mPrePlain=decipher(binFrom,true);
        Tea.TEA_STRUCT.mPos=Tea.TEA_STRUCT.mPrePlain.get(0)&7;
        int count=(int)(intLen-Tea.TEA_STRUCT.mPos-10);
        Tea.TEA_STRUCT.mOut=new ByteSet(count);
        Tea.TEA_STRUCT.mPreCrypt=0;
        Tea.TEA_STRUCT.mCrypt=8;
        Tea.TEA_STRUCT.mContextStart=8;
        Tea.TEA_STRUCT.mPos++;
        Tea.TEA_STRUCT.mPadding=1;
        while (Tea.TEA_STRUCT.mPadding<=2){
            if(Tea.TEA_STRUCT.mPos<8){
                Tea.TEA_STRUCT.mPos++;
                Tea.TEA_STRUCT.mPadding++;
            }
            if(Tea.TEA_STRUCT.mPos==8){
                om=binFrom;
                decrypt8Bytes(binFrom,offset,intLen);
            }
        }
        int ic=0;
        while (count!=0){
            if(Tea.TEA_STRUCT.mPos<8){
                if(ic<=Tea.TEA_STRUCT.mOut.length()){
                    if(Tea.TEA_STRUCT.mPos+1<=Tea.TEA_STRUCT.mPrePlain.length()){
                        if(offset+Tea.TEA_STRUCT.mPreCrypt+Tea.TEA_STRUCT.mPos<=om.length()){
                            Tea.TEA_STRUCT.mOut.set(ic,(byte) (om.get((int)(offset+Tea.TEA_STRUCT.mPreCrypt+Tea.TEA_STRUCT.mPos)) ^ Tea.TEA_STRUCT.mPrePlain.get((int) Tea.TEA_STRUCT.mPos)));
                        }else{
                            return null;
                        }
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
                ic++;
                count--;
                Tea.TEA_STRUCT.mPos++;
            }
            if(Tea.TEA_STRUCT.mPos==8){
                om=binFrom;
                Tea.TEA_STRUCT.mPreCrypt=Tea.TEA_STRUCT.mCrypt-8;
                decrypt8Bytes(binFrom,offset,intLen);
            }
        }
        for(int i=0;i<7;i++){
            if(Tea.TEA_STRUCT.mPos<8){
                Tea.TEA_STRUCT.mPos++;
                if(Tea.TEA_STRUCT.mPos==8){
                    om=binFrom;
                    decrypt8Bytes(binFrom,offset,intLen);
                }
            }
        }
        return Tea.TEA_STRUCT.mOut;

    }
    public static final ByteSet toBytes(long a,long b){
        ByteSet bytes=new ByteSet(8);
        bytes.set(0, (byte) ((a>>24)&255));
        bytes.set(1, (byte) ((a>>16)& 255));
        bytes.set(2, (byte) ((a>>8)&255));
        bytes.set(3, (byte) (a&255));
        bytes.set(4, (byte) ((b>>24)&255));
        bytes.set(5, (byte) ((b>>16)&255));
        bytes .set(6, (byte) ((b>>8)&255));
        bytes.set(7, (byte) (b&255));
        return bytes;
    }
    public static final long getUInt(ByteSet input,int iOffset,int intLength){
        int lend;
        if(intLength>4){
            lend=iOffset+5;
        }else {
            lend=iOffset+intLength+1;
        }
        int ret=0;
        for(int i=iOffset;i<lend-1;i++){
            ret= ret<<8;
            ret=ret|ByteSet.byteToUByte(input.get(i));
        }
        return unsignedLong(ret);
    }
    public static final long LongXOR(Long x,Long y){
        ByteSet xb= Number.longToByte8(x);
        ByteSet yb= Number.longToByte8(y);
        ByteSet emptyB=new ByteSet(8);
        for(int i=0;i<emptyB.length();i++){
            emptyB.set(i,(byte) (xb.get(i)^yb.get(i)));
        }
        return Number.bytesToLong(emptyB);
    }
    public static final long LongAND(long x,long y){
        ByteSet xb= Number.longToByte8(x);
        ByteSet yb= Number.longToByte8(y);
        ByteSet emptyB=new ByteSet(8);
        for(int i=0;i<emptyB.length();i++){
            emptyB.set(i,(byte) (xb.get(i)&yb.get(i)));
        }
        return Number.bytesToLong(emptyB);
    }
    public static final long unsignedLong(long value){
        if(value>=0){
            return value;
        }else {
            return value & Integer.MAX_VALUE | 0x80000000L;
        }
    }
    public static final long LongLeft(long x,int n){
        for(int i=0;i<n;i++){
            x=x*2;
        }
        return x;
    }
    public static final long LongRight(long x,int n){
        for(int i=0;i<n;i++){
            x=x/2;
        }
        return x;
    }
}