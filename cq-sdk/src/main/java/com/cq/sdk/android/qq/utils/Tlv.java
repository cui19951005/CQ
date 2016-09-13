package com.cq.sdk.android.qq.utils;

import com.cq.sdk.utils.ByteSet;

/**
 * Created by CuiYaLei on 2016/8/14.
 */
public class Tlv {
    public static final ByteSet tlv18(ByteSet user){
        Pack pack=new Pack();
        pack.setHex("00 01");
        pack.setHex("00 00 06 00");
        pack.setHex("00 00 00 10");
        pack.setHex("00 00 00 00");
        pack.setBin(user);
        pack.setHex("00 00");
        pack.setHex("00 00");
        return Tlv.tlvPack("00 18",pack.getAll());
    }
    public static final ByteSet tlv1(ByteSet user,ByteSet time){
        Pack pack=new Pack();
        pack.setHex("00 01");
        pack.setBin(Bin.getRandomBin(4));
        pack.setBin(user);
        pack.setBin(time);
        pack.setHex("00 00 00 00");
        pack.setHex("00 00");
        return Tlv.tlvPack("00 01",pack.getAll());
    }
    public static final ByteSet tlv2(String code,ByteSet vieryToken){
        Pack pack=new Pack();
        pack.setInt(code.length());
        pack.setBin(ByteSet.parse(code.getBytes()));
        pack.setShort((short) vieryToken.length());
        pack.setBin(vieryToken);
        return Tlv.tlvPack("00 02",pack.getAll());
    }
    public static final ByteSet tlv106(ByteSet user,ByteSet md5pass,ByteSet md52pass,ByteSet tgtKey,ByteSet imei_,ByteSet time, int appId){
        Pack pack=new Pack();
        pack.setHex("00 03");
        pack.setBin(Bin.getRandomBin(4));
        pack.setHex("00 00 00 05");
        pack.setHex("00 00 00 10");
        pack.setHex("00 00 00 00");
        pack.setHex("00 00 00 00");
        pack.setBin(user);
        pack.setBin(time);
        pack.setHex("00 00 00 00 01");
        pack.setBin(md5pass);
        pack.setBin(tgtKey);
        pack.setHex("00 00 00 00");
        pack.setHex("01");
        pack.setBin(imei_);
        pack.setInt(appId);
        pack.setHex("00 00 00 01");
        pack.setHex("00 00");
        return tlvPack("01 06", Hash.QQTea(pack.getAll(),md52pass));
    }
    public static final ByteSet tlv116(){
        Pack pack=new Pack();
        pack.setHex("00");
        pack.setHex("00 00 7F 7C");
        pack.setHex("00 01 04 00");
        pack.setHex("00");
        return Tlv.tlvPack("01 16",pack.getAll());
    }
    public static final ByteSet tlv100(int appId){
        Pack pack=new Pack();
        pack.setHex("00 01");
        pack.setHex("00 00 00 05");
        pack.setHex("00 00 00 10");
        pack.setInt(appId);
        pack.setHex("00 00 00 00");
        pack.setHex("00 0E 10 E0");
        return Tlv.tlvPack("01 00",pack.getAll());
    }
    public static final ByteSet tlv104(ByteSet vieryToken2){
        Pack pack=new Pack();
        pack.setBin(vieryToken2);
        return Tlv.tlvPack("01 04",pack.getAll());
    }
    public static final ByteSet tlv107(){
        Pack pack=new Pack();
        pack.setHex("00 00");
        pack.setHex("00");
        pack.setHex("00 00");
        pack.setHex("01");
        return Tlv.tlvPack("01 07",pack.getAll());
    }
    public static final ByteSet tlv108(ByteSet ksId){
        return Tlv.tlvPack("01 08",ksId);
    }
    public static final ByteSet tlv144(ByteSet tgtKey,ByteSet tlv109,ByteSet tlv124,ByteSet tlv128,ByteSet tlv16e){
        Pack pack=new Pack();
        pack.setShort((short) 4);
        pack.setBin(tlv109);
        pack.setBin(tlv124);
        pack.setBin(tlv128);
        pack.setBin(tlv16e);
        return Tlv.tlvPack("01 44", Hash.QQTea(pack.getAll(),tgtKey));
    }
    public static final ByteSet tlv109(ByteSet imei_){
        return Tlv.tlvPack("01 09",imei_);
    }
    public static final ByteSet tlv124(String osType,String osVersion,int networkType,String apn){
        Pack pack=new Pack();
        pack.setShort((short) osType.length());
        pack.setStr(osType);
        pack.setShort((short) osVersion.length());
        pack.setStr(osVersion);
        pack.setShort((short) networkType);
        pack.setHex("00 00");
        pack.setHex("00 00");
        pack.setShort((short) apn.length());
        pack.setStr(apn);
        return Tlv.tlvPack("01 24",pack.getAll());
    }
    public static final ByteSet tlv128(String device,ByteSet imei){
        Pack pack=new Pack();
        pack.setHex("00 00");
        pack.setHex("00");
        pack.setHex("01");
        pack.setHex("01");
        pack.setHex("01 00 02 00");
        pack.setShort((short) device.length());
        pack.setStr(device);
        pack.setShort((short) imei.length());
        pack.setBin(imei);
        pack.setHex("00 00");
        return Tlv.tlvPack("01 28",pack.getAll());
    }
    public static final ByteSet tlv16e(String device){
        Pack pack=new Pack();
        pack.setBin(ByteSet.parse(device.getBytes()));
        return Tlv.tlvPack("01 6E",pack.getAll());
    }
    public static final ByteSet tlv142(String apkId){
        Pack pack=new Pack();
        pack.setInt(apkId.getBytes().length);
        pack.setBin(ByteSet.parse(apkId.getBytes()));
        return Tlv.tlvPack("01 42",pack.getAll());
    }
    public static final ByteSet tlv154(int ssoSeq){
        Pack pack=new Pack();
        pack.setInt(ssoSeq);
        return Tlv.tlvPack("01 54",pack.getAll());
    }
    public static final ByteSet tlv145(ByteSet imei){
        Pack pack=new Pack();
        pack.setBin(imei);
        return Tlv.tlvPack("01 45",pack.getAll());
    }
    public static final ByteSet tlv141(int networkType,String apn){
        Pack pack=new Pack();
        pack.setHex("00 01");
        pack.setHex("00 00");
        pack.setShort((short) networkType);
        pack.setShort((short) apn.length());
        pack.setStr(apn);
        return Tlv.tlvPack("01 41",pack.getAll());
    }
    public static final ByteSet tlv8(){
        Pack pack=new Pack();
        pack.setHex("00 00");
        pack.setHex("00 00 08 04");
        pack.setHex("00 00");
        return Tlv.tlvPack("00 08",pack.getAll());
    }
    public static final ByteSet tlv16b(){
        Pack pack=new Pack();
        pack.setHex("00 01");
        pack.setHex("00 0B");
        pack.setHex("67 61 6D 65 2E 71 71 2E 63 6F 6D");
        return Tlv.tlvPack("01 6B",pack.getAll());
    }
    public static final ByteSet tlv147(String apkV,ByteSet apkSig){
        Pack pack=new Pack();
        pack.setHex("00 00 00 10");
        pack.setShort((short) apkV.length());
        pack.setStr(apkV);
        pack.setShort((short) apkSig.length());
        pack.setBin(apkSig);
        return Tlv.tlvPack("01 47",pack.getAll());
    }
    public static final ByteSet tlv177(){
        Pack pack=new Pack();
        pack.setHex("01");
        pack.setHex("53 FB 17 9B");
        pack.setHex("00 07");
        pack.setHex("35 2E 32 2E 33 2E 30");
        return Tlv.tlvPack("01 77",pack.getAll());
    }
    public static final ByteSet tlvPack(String cmd,ByteSet bin){
        Pack pack=new Pack();
        pack.setHex(cmd);
        pack.setShort((short) bin.length());
        pack.setBin(bin);
        return pack.getAll();
    }
    public static final ByteSet tlv114Get0058(ByteSet bin){
        UnPack unPack=new UnPack();
        unPack.setData(bin);
        unPack.getBin(6);
        int len=unPack.getShort();
        if(len!=88){

        }
        return unPack.getBin(len);
    }
    public static final ByteSet tlv187(){
        Pack pack=new Pack();
        pack.setHex("F8 FF 12 23 6E 0D AF 24 97 CE 7E D6 A0 7B DD 68");
        return Tlv.tlvPack("01 87",pack.getAll());
    }
    public static final ByteSet tlv188(){
        Pack pack=new Pack();
        pack.setHex("4D BF 65 33 D9 08 C2 73 63 6D E5 CD AE 83 C0 43");
        return Tlv.tlvPack("01 88",pack.getAll());
    }
    public static final ByteSet tlv191(){
        Pack pack=new Pack();
        pack.setHex("00");
        return Tlv.tlvPack("01 91",pack.getAll());
    }
}
