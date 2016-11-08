package com.cq.sdk.android.qq.utils;

import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Hex;
import com.cq.sdk.utils.Number;
import com.cq.sdk.utils.Random;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CuiYaLei on 2016/8/12.
 */
public class Bin {
    public ByteSet midBin(ByteSet content, ByteSet left, ByteSet right, boolean isLoop, List<ByteSet> result){
        result=new ArrayList<ByteSet>();
       if(isLoop){
           if(left.length()==0 || right.length()==0){
               return null;
           }else{
               int leftIndex=content.indexOf(left);
               while (leftIndex!=-1){
                   int tmpIndex=leftIndex+1;
                   int rightIndex=content.indexOf(right,leftIndex);
                   result.add(content.subByteSet(leftIndex+left.length(),rightIndex-leftIndex-left.length()));
                   leftIndex=content.indexOf(left,tmpIndex);
               }
               if(result.size()>0) {
                   return result.get(0);
               }else{
                   return null;
               }
           }
       }else{
           if(right.length()==0 || left.length()==0){
               return null;
           }else if(right.length()==0){
               int leftIndex=content.indexOf(left);
               if(leftIndex>-1){
                   int tmpIndex=leftIndex+left.length();
                   ByteSet tmpByte=content.subByteSet(tmpIndex,content.length()-tmpIndex);
                   return tmpByte;
               }else{
                   return null;
               }
           }else if(left.length()==0){
               int rightIndex=content.indexOf(right);
               if(rightIndex>-1){
                   int tmpIndex=rightIndex-1;
                   ByteSet tmpByte=content.getLeft(tmpIndex);
                   return tmpByte;
               }else{
                   return null;
               }
           }else{
               int leftIndex=content.indexOf(left)+left.length();
               ByteSet tmpByte=content.subByteSet(leftIndex,content.length()-leftIndex);
               int rightIndex=tmpByte.indexOf(right);
               tmpByte=tmpByte.subByteSet(0,rightIndex);
               return tmpByte;
           }
       }
    }

    /**
     * 字节反转
     * @param bytes
     * @return
     */
    public static final ByteSet flip(ByteSet bytes){
        ByteSet result=new ByteSet(bytes.length());
        for(int i=bytes.length()-1;i>=0;i--){
            result.set(bytes.length()-i-1,bytes.get(i));
        }
        return result;
    }
    public static final int bin2Int(ByteSet bytes){
        return Number.byte4ToInt(flip(bytes));
    }
    public static final ByteSet int2Bin(int value){
        return flip(Number.intToByte4(value));
    }
    public static final ByteSet long2Bin(long value){
        return flip(Number.longToByte8(value));
    }
    public static final long bin2Long(ByteSet bytes){
        return Number.bytesToLong(bytes);
    }
    public static final ByteSet short2Bin(short value){
       return flip(Number.unsignedShortToByte2(value));
    }
    public static final short bin2Short(ByteSet bytes){
        return Number.byte2ToUnsignedShort(bytes);
    }
    public static final int bin2Byte(ByteSet bytes){
        return bytes.get(0);
    }
    public static final ByteSet byte2Bin(byte b){
        return flip(new ByteSet(b));
    }
    public static final String binToString(ByteSet bytes){
        return bytes.toStringUInt();
    }
    public static final String toIp(ByteSet ip){
       return ByteSet.toIP(ip);
    }
    public static final ByteSet bin2Array(ByteSet bytes){
        return bytes;
    }
    public static final String bin2Hex(ByteSet bin){
        StringBuffer stringBuffer=new StringBuffer();
        for(int i=0;i<bin.length();i++){
            String b= Hex.baseString(ByteSet.byteToUByte(bin.get(i)),16);
            if(b.length()<2){
                stringBuffer.append("0");
            }
            stringBuffer.append(b);
            if(i<bin.length()-1) {
                stringBuffer.append(" ");
            }
        }
        return stringBuffer.toString();
    }
    public static final ByteSet hex2Bin(String value){
        return ByteSet.parse(value);
    }
    public static final ByteSet getRandomBin(int len){
        ByteSet bytes=new ByteSet(len);
        for(int i=0;i<len;i++){
            bytes.set(i, (byte) Random.random(0,255));
        }
        return bytes;
    }
}
