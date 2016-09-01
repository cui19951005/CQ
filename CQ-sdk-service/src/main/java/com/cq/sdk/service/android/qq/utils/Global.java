package com.cq.sdk.service.android.qq.utils;

import com.cq.sdk.service.android.qq.struct.JceStructFriendInfo;
import com.cq.sdk.service.android.qq.struct.QQ;
import com.cq.sdk.service.android.qq.struct.QQGlobal;
import com.cq.sdk.service.utils.ByteSet;

import java.net.Socket;

/**
 * Created by CuiYaLei on 2016/8/14.
 */
public class Global {
    public static int requestId=1000;
    public static int pcSubCmd=0;
    public static QQGlobal qqGlobal=new QQGlobal();
    public static QQ qq=new QQ();
    public static JceStructFriendInfo[] friends=new JceStructFriendInfo[0];
    /**
     * QQ设备信息
     */
    static {
        Global.qqGlobal.imei = "866819027236658";
        Global.qqGlobal.ver = ByteSet.parse("5.8.0.157158".getBytes());
        Global.qqGlobal.appId = 537042771;
        Global.qqGlobal.pcVer = "1F 41";
        Global.qqGlobal.osType = "android";
        Global.qqGlobal.osVersion = "4.4.4";
        Global.qqGlobal.networkType = 2;
        Global.qqGlobal.apn = "wifi";
        Global.qqGlobal.device = "vivo X5Max+";
        Global.qqGlobal.apkId = "com.tencent.mobileqq";
        Global.qqGlobal.apkV = "5.8.0.157158";
        Global.qqGlobal.apkSig = Bin.hex2Bin("A6 B7 45 BF 24 A2 C2 77 52 77 16 F6 F3 6E B6 8D");
        Global.qqGlobal.imei_ = Bin.hex2Bin("38 36 36 38 31 39 30 32 37 32 33 36 36 35 38");
    }
    public static final int getSubCmd() {
        if(Global.pcSubCmd>=Integer.MAX_VALUE){
            Global.pcSubCmd=0;
        }
        Global.pcSubCmd++;
        return Global.pcSubCmd;
    }

    public static final int increaseSsoSeq() {
        if(Global.requestId>=Integer.MAX_VALUE){
            Global.requestId=1000;
        }
        Global.requestId++;
        return Global.requestId;
    }
}
