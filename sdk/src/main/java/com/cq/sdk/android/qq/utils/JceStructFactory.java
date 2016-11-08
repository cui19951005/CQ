package com.cq.sdk.android.qq.utils;

import com.cq.sdk.android.qq.struct.*;
import com.cq.sdk.utils.*;
import com.cq.sdk.utils.Number;
import com.sun.prism.impl.BaseMesh;

import java.util.ArrayList;
import java.util.List;

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
    public static final void readEncounterSvcRespGetEncounterV2(ByteSet bin,List<RespEncounterInfo> respEncounterInfo){
        Logger.info("正在写入附近人数据");
        JceInputStream in=new JceInputStream();
        in.wrap(bin);
        in.readByte(0);
        in.readInt(0);
        int type=in.readType();
        RespEncounterInfo info=new RespEncounterInfo();
        if(type==Constants.TYPE_STRUCT_BEGIN){
            in.readLong(0);
            in.readLong(1);
            in.readLong(2);
            in.readLong(3);
            in.readLong(4);
            info.province=in.readString(5);
            ByteSet temp=in.readSimpleList(6);
        }
        in.readType();
        type=in.readType();
        if(type==Constants.TYPE_LIST){
            int count=in.readShort(0);
            for(int i=0;i<count;i++){
                in.readByte(0);
                info.num=in.readLong(0);
                in.readShort(1);
                in.readInt(2);
                info.strDescription=in.readString(3);
                in.readShort(4);
                info.gender=in.readShort(5);
                info.age= String.valueOf(in.readShort(6));
                info.strNick=in.readString(7);
                in.readByte(8);
                respEncounterInfo.add(info);
                in.skipToEnd();
            }
        }
    }
    public static final void readGroupMngRes2(QQ qq,ByteSet bin){
        String text=StringUtils.subString(bin.toStringHex(),"2C 3C 4D","59 00");
        ByteSet group=ByteSet.parse("00"+ text.substring(text.length()-6));
        Logger.info(group.toStringHex());

        ByteSet topBin=ByteSet.parse(StringUtils.left(bin.toStringHex(),"0B 69")+"0B");
        text=StringUtils.subString(bin.toStringHex().replace(" ",""),Hex.baseNum(String.valueOf(qq.qq),10,16),"2C3C4D");
        int groupCount= Integer.parseInt(Hex.baseNum(text.substring(text.length()-2),16,10));
        bin=topBin.replace(1,StringUtils.left(topBin.toStringHex(),"59 00").length()/2,ByteSet.empty());
        bin=bin.replace(1,3,ByteSet.empty());
        JceInputStream in=new JceInputStream();
        in.wrap(bin);
        List<Group> groupList=new ArrayList<>();
        for(int i=0;i<groupCount;i++){
            in.readByte(0);
            long code=in.readLong(0);
            long uin=in.readLong(1);
            in.readInt(2);
            in.readInt(3);
            String name=in.readString(4);
            if(Number.NumberSize(uin)>1){
                Group g=new Group();
                g.code= code;
                g.id= uin;
                g.name=name;
            }
            in.readString(5);
            in.readLong(6);
            in.readInt(7);
            in.readInt(8);
            in.readInt(9);
            in.readInt(10);
            in.readInt(11);
            in.readInt(12);
            in.readInt(13);
            in.readInt(14);
            in.readInt(15);
            in.readInt(16);
            in.readInt(17);
            in.skipToEnd();
        }
        qq.groupList=groupList;
    }
    public static final void readGroupMemberList(QQ qq,ByteSet bin){
        JceInputStream in=new JceInputStream();
        in.wrap(bin);
        in.readByte(0);
        JceStructGroupMemberList groupMember=new JceStructGroupMemberList();
        groupMember.qq =in.readLong(0);
        groupMember.group =in.readLong(1);
        groupMember.c=in.readLong(2);
        in.readToTag(3);
        int count=in.readByte(0);
        List<GroupMember> groupMemberList=new ArrayList<>();
        for(int i=0;i<count;i++){
            in.readByte(0);
            GroupMember gm=new GroupMember();
            in.readInt(1);
            gm.qq=in.readLong(0);
            gm.age=in.readShort(2);
            gm.sex=in.readShort(3);
            gm.name=in.readString(4);
            in.readShort (5);
            in.readString (6);
            in.readString (8);
            in.readShort (9);
            in.readString (10);
            in.readString (11);
            in.readString (12);
            String name2 = in.readString (13);
            in.readShort (14);
            in.readLong (15);
            in.readLong (16);
            in.readShort (17);
            in.readShort (18);
            in.readShort (19);
            in.readShort (20);
            in.readShort (21);
            in.readShort (22);
            in.readString (23);
            in.readShort (24);
            in.readString (25);
            in.readShort (4);
            in.readShort (5);
            in.readShort (6);
            in.readShort (7);
            in.skipToEnd ();
            groupMemberList.add(gm);
        }
        qq.groupList.stream().filter(g->g.id==groupMember.group).forEach(g->g.groupMemberList=groupMemberList);
    }
    public static final void readOnlineFriendList(QQ qq,ByteSet bin){
        JceInputStream in=new JceInputStream();
        in.wrap(bin);
        in.readByte(0);
        in.readLong(0);
        in.readToTag(1);
        int count=in.readByte(0);
        List<Friend> friendList=qq.friendList;
        for(int i=0;i<count;i++){
            in.readByte(0);
            long uin=in.readLong(0);
            Friend friend=null;
            for(Friend item : friendList){
                if(item.qq==uin){
                    friend=item;
                    break;
                }
            }
            in.readInt (1);
            in.readInt (2);
            in.readInt (3);
            in.readInt (4);
            in.readInt (5);
            in.readInt (6);
            in.readString (7);
            in.readInt (8);
            in.readInt (9);
            in.readInt (10);
            in.readInt (11);
            in.readInt (12);
            in.readInt (13);
            friend.state=in.readString(14);
            in.skipToEnd();
        }
    }
    public static final void ReadAFRESP(QQ qq,ByteSet bin){
        JceInputStream in=new JceInputStream();
        in.wrap(bin);
        in.readByte(0);
        in.readLong(0);
        in.readByte(1);
        in.readByte(2);
        in.readShort(3);
        in.readShort(4);
        in.readShort(6);
        int type=in.readShort(7);
        AddFriendResult addFriendResult=new AddFriendResult();
        addFriendResult.message=in.readString(7);
        if(type==0){
            addFriendResult.suc=true;
        }
        qq.addFriendResult=addFriendResult;
    }
    public static final void ReadGroupMngRes(QQ qq,ByteSet bin){
        String hex=bin.toStringHex();
        String pack=StringUtils.left(hex,"69 00");
        String mainPack=pack.replace(StringUtils.subString(pack,"0A 02 0C","12 00"),"");
        mainPack=mainPack.replace(StringUtils.subString(mainPack,"2C 31","46"),"");
        return;//未完
    }
}
