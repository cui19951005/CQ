package com.cq.sdk.service.android.qq.utils;

import com.cq.sdk.service.android.qq.struct.*;
import com.cq.sdk.service.utils.ByteSet;

/**
 * Created by CuiYaLei on 2016/8/20.
 */
public class JceStructFactory {
    public  static final void readRequestPacket(JceInputStream jceInputStream, JceStructRequestPacket jceStructRequestPacket){
        jceStructRequestPacket.iversion=jceInputStream.readShort(1);
        jceStructRequestPacket.cPacketType = jceInputStream.readShort (2);
        jceStructRequestPacket.iMessageType = jceInputStream.readShort (3);
        jceStructRequestPacket.iRequestId = jceInputStream.readInt (4);
        jceStructRequestPacket.sServantName = jceInputStream.readString (5);
        jceStructRequestPacket.sFuncName = jceInputStream.readString (6);
        jceStructRequestPacket.sBuffer = jceInputStream.readSimpleList (7);
        jceStructRequestPacket.iTimeout = jceInputStream.readInt (8);
        jceStructRequestPacket.context=jceInputStream.readMap(9);
        jceStructRequestPacket.status=jceInputStream.readMap(10);
    }
    public static final void writeRequestPacket(JceOutputStream jceOutputStream, JceStructRequestPacket jceStructRequestPacket){
        jceOutputStream.writeShort (jceStructRequestPacket.iversion, 1);
        jceOutputStream.writeShort (jceStructRequestPacket.cPacketType, 2);
        jceOutputStream.writeShort (jceStructRequestPacket.iMessageType, 3);
        jceOutputStream.writeInt (jceStructRequestPacket.iRequestId, 4);
        jceOutputStream.writeStringByte (jceStructRequestPacket.sServantName, 5);
        jceOutputStream.writeStringByte (jceStructRequestPacket.sFuncName, 6);
        jceOutputStream.writeSimpleList (jceStructRequestPacket.sBuffer, 7);
        jceOutputStream.writeInt (jceStructRequestPacket.iTimeout, 8);
        jceOutputStream.writeMap (jceStructRequestPacket.context, 9);
        jceOutputStream.writeMap (jceStructRequestPacket.status, 10);

    }
    public static final void readSvcRegister(JceInputStream in,JceStructSvcReqRegister jceStructSvcReqRegister){

        jceStructSvcReqRegister.lUin = in.readLong (0);
        jceStructSvcReqRegister.lBid = in.readLong (1);
        jceStructSvcReqRegister.cConnType = in.readByte (2);
        jceStructSvcReqRegister.sOther = in.readString (3);
        jceStructSvcReqRegister.iStatus = in.readInt (4);
        jceStructSvcReqRegister.bOnlinePush = in.readByte (5);
        jceStructSvcReqRegister.bIsOnline = in.readByte (6);
        jceStructSvcReqRegister.bIsShowOnline = in.readByte (7);
        jceStructSvcReqRegister.bKikPC = in.readByte (8);
        jceStructSvcReqRegister.bKikWeak = in.readByte (9);
        jceStructSvcReqRegister.timeStamp = in.readLong (10);
        ;
        jceStructSvcReqRegister._11 = in.readByte (11);
        jceStructSvcReqRegister._12 = in.readByte (12);
        jceStructSvcReqRegister._13 = in.readString (13);
        jceStructSvcReqRegister._14 = in.readByte (14);
        //jceStructSvcReqRegister._imei_ =  in.readSimpleList (16).toStringHex();
        jceStructSvcReqRegister._17 = in.readShort (17);
        jceStructSvcReqRegister._18 = in.readByte (18);
        jceStructSvcReqRegister._19_device = in.readString (19);
        jceStructSvcReqRegister._20_device = in.readString (20);
        jceStructSvcReqRegister._21_sys_ver = in.readString (21);

    }
    public static final void writeSvcReqRegister(JceOutputStream out,JceStructSvcReqRegister struct){
        out.writeLong (struct.lUin, 0);
        out.writeLong (struct.lBid, 1);
        out.writeByte (struct.cConnType, 2);
        out.writeStringByte (struct.sOther, 3);
        out.writeInt (struct.iStatus, 4);
        out.writeByte (struct.bOnlinePush, 5);
        out.writeByte (struct.bIsOnline, 6);
        out.writeByte (struct.bIsShowOnline, 7);
        out.writeByte (struct.bKikPC, 8);
        out.writeByte (struct.bKikWeak, 9);
        out.writeLong (struct.timeStamp, 10);
        out.writeByte (struct._11, 11);
        out.writeByte (struct._12, 12);
        out.writeStringByte (struct._13, 13);
        out.writeByte (struct._14, 14);
        out.writeSimpleList (struct._imei_, 16);
        out.writeShort (struct._17, 17);
        out.writeByte (struct._18, 18);
        out.writeStringByte (struct._19_device, 19);
        out.writeStringByte (struct._20_device, 20);
        out.writeStringByte (struct._21_sys_ver, 21);
    }
    public static final void readGetSimpleOnlineFriendInfoReq(){
        JceInputStream in=new JceInputStream();
        JceStructFSOLREQ struct=new JceStructFSOLREQ();
        struct.luin = in.readLong (0);
        struct._1 = in.readShort (1);
        struct._2 = in.readShort (2);
        struct._3 = in.readShort (3);
        struct._4 = in.readShort (4);
        struct._5 = in.readShort (5);
        struct._6 = in.readShort (6);

    }
    public static final void writeGetSimpleOnlineFriendInfoReq(JceOutputStream out,JceStructFSOLREQ struct){
        out.writeLong (struct.luin, 0);
        out.writeShort (struct._1, 1);
        out.writeShort (struct._2, 2);
        out.writeShort (struct._3, 3);
        out.writeShort (struct._4, 4);
        out.writeShort (struct._5, 5);
        out.writeShort (struct._6, 6);

    }
    public static final void readFL(JceInputStream in,JceStructFL struct){
        struct._0_reqtype = in.readShort (0);
        struct._1_ifReflush = in.readShort (1);
        struct.luin = in.readLong (2);
        struct._3_startIndex = in.readShort (3);
        struct._4_getfriendCount = in.readShort (4);
        struct._5_totoal_friend_count = in.readShort (5);
        struct._6_friend_count = in.readShort (6);
        struct._7 = in.readShort (7);
        struct._8 = in.readShort (8);
        struct._9 = in.readShort (9);
        struct._10 = in.readShort (10);
        struct._11 = in.readShort (11);
    }
    public static final void writeFL(JceOutputStream out,JceStructFL struct){
        out.writeShort (struct._0_reqtype, 0);
        out.writeShort (struct._1_ifReflush, 1);
        out.writeLong (struct.luin, 2);
        out.writeShort (struct._3_startIndex, 3);
        out.writeShort (struct._4_getfriendCount, 4);
        out.writeShort (struct._5_totoal_friend_count, 5);
        out.writeShort (struct._6_friend_count, 6);
        out.writeShort (struct._7, 7);
    }
    public static final JceStructFriendInfo[] readFLRESP(ByteSet bin, JceStructFriendListResp struct){
        JceInputStream in=new JceInputStream();
        in.wrap(bin);
        in.readByte(0);
        struct.reqtype = in.readByte (0);
        struct.ifReflush = in.readByte (1);
        struct.uin = in.readLong (2);
        struct.startIndex = in.readShort (3);
        struct.getfriendCount = in.readShort (4);
        struct.totoal_friend_count = in.readShort (5);
        struct.friend_count = in.readShort (6);
        int type=in.readToTag(7);
        JceStructFriendInfo flinfo=new JceStructFriendInfo();
        if(type!= Constants.TYPE_ZERO_TAG){
            if(type==Constants.TYPE_LIST){
                int count=in.readShort(0);
                JceStructFriendInfo[] friends=new JceStructFriendInfo[count];
                for(int i=0;i<count;i++){
                    in.readByte(0);
                    flinfo.friendUin = in.readLong (0);
                    flinfo.groupId = in.readByte (1);
                    flinfo.faceId = in.readShort (2);
                    flinfo.name = in.readString (3);
                    flinfo.sqqtype = in.readByte (4);
                    flinfo.status = in.readByte (5);
                    flinfo.memberLevel = in.readByte (6);
                    flinfo.isMqqOnLine = in.readByte (7);
                    flinfo.sqqOnLineState = in.readByte (8);
                    flinfo.isIphoneOnline = in.readByte (9);
                    flinfo.detalStatusFlag = in.readByte (10);
                    flinfo.sqqOnLineStateV2 = in.readByte (11);
                    flinfo.sShowName = in.readString (12);
                    in.readShort (13);
                    flinfo.nick = in.readString (14);
                    friends[i]=flinfo;
                    in.skipToEnd();
                }
                return friends;
            }
        }
        return null;
    }
    public static final FSRESPStruts readFSRESP(ByteSet bin){
        JceInputStream in=new JceInputStream();
        in.wrap(bin);
        in.readByte(0);
        in.readLong(0);
        FSRESPStruts fsrespStruts=new FSRESPStruts();
        fsrespStruts.friendUint=in.readLong(1);
        fsrespStruts.verifyType=in.readByte(2);
        int type=in.readType();
        if(type==Constants.TYPE_ZERO_TAG){

        }else if(type==Constants.TYPE_LIST){
            int count=in.readShort(0);
            if(count>0){
                fsrespStruts.question=in.readString(0);
            }
        }
        return fsrespStruts;
    }
}
