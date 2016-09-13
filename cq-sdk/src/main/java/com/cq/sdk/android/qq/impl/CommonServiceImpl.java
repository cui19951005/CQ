package com.cq.sdk.android.qq.impl;

import com.cq.sdk.android.qq.struct.*;
import com.cq.sdk.android.qq.utils.*;
import com.cq.sdk.android.qq.CommonService;
import com.cq.sdk.android.qq.NetworkService;
import com.cq.sdk.android.qq.QQService;
import com.cq.sdk.android.qq.auxiliary.MsgHandle;
import com.cq.sdk.android.qq.inter.NetworkReceive;
import com.cq.sdk.potential.annotation.Autowired;
import com.cq.sdk.potential.annotation.Service;
import com.cq.sdk.utils.*;
import com.cq.sdk.utils.Number;

/**
 * Created by CuiYaLei on 2016/8/20.
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private QQService qqService;
    @Autowired
    private NetworkService networkService;
    public void receive(ByteSet bytes) {
        if(bytes.length()==0){
            return;
        }
        Logger.info(bytes);
        bytes=this.unPack(bytes,false);
        bytes= Hash.unQQTea(bytes, Global.qq.key);
        int len=bytes.length();
        UnPack unPack=new UnPack();
        unPack.setData(bytes);
        int headLen=unPack.getInt();
        bytes=unPack.getBin(headLen-4);
        ByteSet bodyBin=unPack.getAll();
        unPack.setData(bytes);
        int ssoSeq=unPack.getInt();
        if(unPack.getBin(4).equals("{0,0,0,0}")){
            unPack.getBin(4);
        }else{
            unPack.getBin(unPack.getInt()-4);
        }
        String serviceCmd=new String(unPack.getBin(unPack.getInt()-4).getByteSet());
        if(serviceCmd.equals(Constants.WTLOGIN_LOGIN)){
            this.unPackLogin(bodyBin);
        }else{
            this.msgHandle(ssoSeq,serviceCmd,bodyBin);
        }

    }

    public ByteSet pack(ByteSet bin, int type) {
        Pack pack=new Pack();
        if(type==0){
            pack.setHex("00 00 00 08 02 00 00 00 04");
        }else if(type==1){
            pack.setHex("00 00 00 08 01 00 00");
            pack.setShort((short) (Global.qq.token002C.length()+4));
            pack.setBin(Global.qq.token002C);
        }else{
            pack.setHex("00 00 00 09 01");
            pack.setInt(Global.requestId);
        }
        pack.setHex("00 00 00");
        pack.setShort((short) (Global.qq.caption.length()+4));
        pack.setBin(Global.qq.caption);
        pack.setBin(bin);
        bin=pack.getAll();
        pack.empty();
        pack.setInt(bin.length()+4);
        pack.setBin(bin);
        bin=pack.getAll();
        return bin;
    }

    public ByteSet makeLoginSendSsoMsg(String serviceCmd, ByteSet buffer, ByteSet extBin, String imei, ByteSet ksId, ByteSet ver, boolean isLogin) {
        ByteSet cookies=new ByteSet("B6 CC 78 FC");
        Pack pack=new Pack();
        pack.setInt(Global.requestId);
        pack.setInt(Global.qqGlobal.appId);
        pack.setInt(Global.qqGlobal.appId);
        pack.setHex("01 00 00 00 00 00 00 00 00 00 00 00");
        pack.setInt(extBin.length()+4);
        pack.setBin(extBin);
        pack.setInt(serviceCmd.length()+4);
        pack.setBin(ByteSet.parse(serviceCmd.getBytes()));
        pack.setInt(cookies.length()+4);
        pack.setBin(cookies);
        pack.setInt(imei.length()+4);
        pack.setBin(ByteSet.parse(imei.getBytes()));
        pack.setInt(ksId.length()+4);
        pack.setBin(ksId);
        pack.setShort((short) (ver.length()+2));
        pack.setBin(ver);
        ByteSet bytes=pack.getAll();
        pack.empty();
        pack.setInt(bytes.length()+4);
        pack.setBin(bytes);
        bytes=pack.getAll();
        pack.empty();
        pack.setBin(bytes);
        pack.setInt(buffer.length()+4);
        pack.setBin(buffer);
        Logger.info(pack.getAll());
        return pack(Hash.QQTea(pack.getAll(),Global.qq.key),isLogin?0:1);
    }

    public ByteSet packPc(String cmd, ByteSet bin, ByteSet extKey, ByteSet extBin) {
        Pack pack=new Pack();
        pack.setHex(Global.qqGlobal.pcVer);
        pack.setHex(cmd);
        pack.setShort((short) Global.getSubCmd());
        pack.setBin(Global.qq.user);
        pack.setHex("03 07 00 00 00 00 02 00 00 00 00 00 00 00 00");
        boolean ext_bin_null;
        if(extBin!=null && extBin.length()>0){
            ext_bin_null=false;
            pack.setHex("01 01");
        }else{
            ext_bin_null=true;
            pack.setHex("01 02");
        }
        pack.setBin(extKey);
        pack.setHex("01 02");
        pack.setShort((short) extBin.length());
        if(ext_bin_null){
            pack.setHex("00 00");
        }else{
            pack.setBin(extBin);
        }
        pack.setBin(bin);
        pack.setHex("03");
        ByteSet bytes=pack.getAll();
        pack.empty();
        pack.setHex("02");
        pack.setShort((short) (bytes.length()+3));
        pack.setBin(bytes);
        return pack.getAll();
    }



    public ByteSet unPack(ByteSet bytes, boolean bool) {
        int index=bytes.indexOf(Global.qq.caption);
        bytes=bytes.getRight(bytes.length()-index-Global.qq.caption.length()+1);
        if(bool){
            index=bytes.indexOf(Global.qq.caption);
            bytes=bytes.getRight(bytes.length()-index-Global.qq.caption.length()+1);
        }
        return bytes;
    }

    public void msgHandle(int ssoSeq, String serviceCmd, ByteSet bin) {
        UnPack unPack=new UnPack();
        unPack.setData(bin);
        int len=unPack.getInt();
        bin=unPack.getBin(len);
        if (serviceCmd.equals("OidbSvc.0x7a2_0")) {

        }else if(serviceCmd.equals("friendlist.getFriendGroupList")){
            this.unPackRequestPacket(bin,null);
        }else if(serviceCmd.equals("EncounterSvc.ReqGetEncounter")){
            bin=Zlib.decompress(bin);
            this.unPackRequestPacket(bin,null);
        }else if(serviceCmd.equals("SummaryCard.ReqCondSearch")){

        }else if(serviceCmd.equals("friendlist.GetAutoInfoReq")){
            this.unPackRequestPacket(bin,null);
        }else if(serviceCmd.equals("SQQzoneSvc.getMainPage")){

        }else if(serviceCmd.equals("friendlist.addFriend")){
            this.unPackRequestPacket(bin,null);
        }else if(serviceCmd.equals("ProfileService.GroupMngReq")){
            this.unPackRequestPacket(bin,null);
        }else if(serviceCmd.equals("OnlinePush.PbPushGroupMsg")){

        }else if(serviceCmd.equals("MessageSvc.PushReaded")){
            //其他客户端查看过消息会发送此命令通知
        }else if(serviceCmd.equals("MessageSvc.PushNotify")){
            //消息通知
        }else if(serviceCmd.equals("StatSvc.get")){
            Logger.info("心跳包");
        }else if(serviceCmd.equals("SummaryCard.ReqSummaryCard")){
            if(bin.toStringHex().indexOf("78 DA")==-1){
                this.unPackRequestPacket(bin,null);
            }else{
                bin=Zlib.decompress(bin);
                this.unPackRequestPacket(bin,null);
            }
        }else if(serviceCmd.equals("ConfigPushSvc.PushReq")){

        }else if(serviceCmd.equals("OidbSvc.0x4ff_9")){
            Logger.info("修改昵称完成");
        }else if(serviceCmd.equals("QQServiceDiscussSvc.ReqGetDiscuss")){

        }else if(serviceCmd.equals("account.RequestReBindMobile")){
            Logger.info("验证码已经发送到手机");
        }else if(serviceCmd.equals("Signature.auth")){
            Logger.info("发表签名成功");
        }else if(serviceCmd.equals("SQQzoneSvc.publishmess")){
            Logger.info("留言成功");
        }else if(serviceCmd.equals("VisitorSvc.ReqFavorite")){
            Logger.info("赞名片成功");
        }else if(serviceCmd.equals("friendlist.GetSimpleOnlineFriendInfoReq")){
            if(bin.toStringHex().indexOf("78 DA")==-1){
                this.unPackRequestPacket(bin,null);
            }else {
                bin=Zlib.decompress(bin);
                this.unPackRequestPacket(bin,null);
            }
        }else if(serviceCmd.equals("FriendList.GetTroopListReqV2")){
            if(bin.toStringHex().substring(0,5).equals("78 DA")){
                bin=Zlib.decompress(bin);
                this.unPackRequestPacket(bin,null);
            }else {
                this.unPackRequestPacket(bin,null);
            }
        }else if(serviceCmd.equals("friendlist.getTroopMemberList")){
            if(bin.toStringHex().indexOf("78 DA")==-1){
                this.unPackRequestPacket(bin,null);
            }else {
                bin=Zlib.decompress(bin);
                this.unPackRequestPacket(bin,null);
            }
        }else if(serviceCmd.equals("QQServiceDiscussSvc.ReqCreateDiscuss")){

        }else if(serviceCmd.equals("QQServiceDiscussSvc.ReqAddDiscussMember")){

        }else if(serviceCmd.equals("SQQzoneSvc.getApplist")){

        }else{
            return ;
        }
        if(unPack.length()>0){
            Logger.info("消息未处理结束");
            Logger.info("CMD:{0}\nHEX:{1}",serviceCmd,unPack.getAll().toStringHex());
        }
    }

    public void unPackRequestPacket(ByteSet bin,CardData cardData) {
        JceInputStream inputStream=new JceInputStream();
        inputStream.wrap(bin);
        JceStructRequestPacket requestPacket=new JceStructRequestPacket();
        JceStructFactory.readRequestPacket(inputStream,requestPacket);
        if(requestPacket.sBuffer.length()==0){
            return ;
        }
        inputStream.wrap(requestPacket.sBuffer);
        this.unPackMap(inputStream,cardData);
    }

    public void unPackMap(JceInputStream jceInputStream, CardData cardData) {
        int type=jceInputStream.readType();
        if(type!= Constants.TYPE_MAP){
            return ;
        }
        boolean isFirendsReq=false;
        int count=jceInputStream.readShort(0);
        long friendUin = 0;
        int tVerifyType = 0;
        String question=null;
        for(int i=0;i<count;i++){
            String key=jceInputStream.readString(0);
            if(key.equals("FLRESP")){
                ByteSet bytes=jceInputStream.readSimpleList(1);
                JceStructFriendListResp friendListResp=new JceStructFriendListResp();
                Global.friends=JceStructFactory.readFLRESP(bytes,friendListResp);
                isFirendsReq=true;
            }else if(key.equals("FSRESP")){
                ByteSet bytes=jceInputStream.readSimpleList(1);
                FSRESPStruts fsrespStruts=JceStructFactory.readFSRESP(bytes);
                this.qqService.addFriendsGetSetResult(Global.qq.qq,friendUin,tVerifyType,question);
            }else if(key.equals("RespGetEncounterV2")){
                type=jceInputStream.readType();
                if(type==Constants.TYPE_MAP){
                    int j=jceInputStream.readShort(0);
                    for(int z=0;i<j;z++){
                        key=jceInputStream.readString(0);
                        ByteSet bin=jceInputStream.readSimpleList(1);
                        if(key.equals("EncounterSvc.RespGetEncounterV2")){

                        }
                    }
                }
            }
        }
    }

    public ByteSet packAddFriendReq(long friendUin, int type, String msg) {
        JceOutputStream out=new JceOutputStream();
        out.writeInt(Number.longToInt(Global.qq.qq),0);
        out.writeInt(Number.longToInt(friendUin),1);
        out.writeShort((short) type,1);
        out.writeByte((byte) 1,3);
        out.writeByte((byte) 1,4);
        out.writeByte((byte) msg.length(),5);
        out.writeStringByte(msg,6);
        out.writeByte((byte) 0,7);
        out.writeByte((byte) 1,8);
        out.writeShort((short) 3001,10);
        out.writeByte((byte) 0,11);
        out.writeByte((byte) 0,15);
        ByteSet bytes=out.toByteArray();
        return this.makeSendSsoMsgSimple(Constants.FRIENDLIST_ADDFRIEND,(short) 3, Random.random(100000,1000000),Constants.SERVANT_NAME,Constants.FUNC_NAME,Constants.MAP_KEY,bytes);
    }

    public ByteSet makeSendSsoMsg(String serviceCmd, ByteSet wupBuffer) {
        ByteSet msgCookies =ByteSet.parse("B6 CC 78 FC");
        Pack pack=new Pack();

        pack.setInt (serviceCmd.length() + 4);
        pack.setBin (ByteSet.parse(serviceCmd.getBytes()));
        pack.setInt (msgCookies.length()+4);
        pack.setBin (msgCookies);

        ByteSet temp=pack.getAll();
        pack.empty ();
        pack.setInt (temp.length()+4);
        pack.setBin (temp);
        pack.setBin (wupBuffer);
        return this.pack (Hash.QQTea (pack.getAll (), Global.qq.key), 2);

    }

    public ByteSet makeSendSsoMsgSimple(String serviceCmd, short iversion, int iRequestId, String sServantName, String sFuncName, String mapKey, ByteSet wupBuffer) {
        return this.makeSendSsoMsg(serviceCmd,this.packSendSsoMsgSimple(iversion,iRequestId,sServantName,sFuncName,mapKey,wupBuffer));
    }

    public ByteSet packSendSsoMsgSimple(short iversion, int iRequestId, String sServantName, String sFuncName, String mapKey, ByteSet wupBuffer) {
        JceOutputStream out=new JceOutputStream();
        out.writeJceStruct(wupBuffer,0);
        ByteSet bin=out.toByteArray();
        out.clear();
        JceMap[] maps=new JceMap[1];
        maps[0].keyType=Constants.TYPE_STRING1;
        maps[0].valType=Constants.TYPE_SIMPLE_LIST;
        maps[0].key= ByteSet.parse(mapKey.getBytes());
        maps[0].val=bin;
        out.writeMap(maps,0);
        bin=out.toByteArray();
        out.clear();
        JceStructRequestPacket req=new JceStructRequestPacket();
        req.iversion=iversion;
        req.iRequestId=iRequestId;
        req.sServantName=sServantName;
        req.sFuncName=sFuncName;
        req.sBuffer=bin;
        JceStructFactory.writeRequestPacket(out,req);
        bin=out.toByteArray();
        Pack pack=new Pack();
        pack.setInt(bin.length()+4);
        pack.setBin(bin);
        return pack.getAll();
    }

    public boolean unPackLogin(ByteSet bin) {
        bin=this.unPackLoginPc(bin);
        if(bin.length()==0){
            return false;
        }
        UnPack unPack=new UnPack();
        unPack.setData(bin);
        unPack.getShort();
        unPack.getByte();
        unPack.getInt();
        int len=unPack.getShort();
        bin=unPack.getBin(len);
        bin= Hash.unQQTea(bin,Global.qq.tgtKey);
        this.unTlv(bin);
        Global.qq.key=Global.qq.sessionKey;
        Logger.info(Json.toJson(Global.qq));
        Global.qq.loginState= DataType.UserState.Success;
        return true;
    }

    public ByteSet unPackLoginPc(ByteSet bin) {
        UnPack unPack=new UnPack();
        unPack.setData(bin);
        int len=unPack.getInt();
        ByteSet bytes=unPack.getAll();
        unPack.setData(bin);
        unPack.getByte();
        len=unPack.getShort();
        unPack.getBin(10);
        unPack.getBin(2);
        int result=unPack.getByte();
        bin=unPack.getBin(len-16-1);
        bin=Hash.unQQTea(bin,Global.qq.shareKey);
        if(result!=0){
            if(result==2){
                this.unPackVieryImage(bin);
                Logger.info("需要输入验证码");
                Global.qq.loginState=DataType.UserState.Verification;
                return null;
            }

        }
        this.unPackErrMsg(bin);
        Global.qq.loginState= DataType.UserState.Login;
        return bin;
    }

    public void unPackVieryImage(ByteSet bin) {
        UnPack unPack=new UnPack();
        unPack.setData(bin);
        unPack.getBin(3);
        this.unTlv(unPack.getAll());
    }

    public void unPackErrMsg(ByteSet bin) {
        UnPack unPack=new UnPack();
        unPack.getShort();
        unPack.getByte();
        unPack.getInt();
        unPack.getShort();
        int type=unPack.getInt();
        String title=new String(unPack.getBin(unPack.getShort()).getByteSet());
        String message=new String(unPack.getBin(unPack.getShort()).getByteSet());
        Logger.error("title:{0},message:{1}",title,message);
    }

    public void unTlv(ByteSet bin) {
        UnPack unPack=new UnPack();
        unPack.setData(bin);
        int count=unPack.getShort();
        for(int i=0;i<count;i++){
            ByteSet cmd=unPack.getBin(2);
            int len=unPack.getShort();
            this.tlvGet(Bin.bin2Hex(cmd),unPack.getBin(len));
        }
    }

    public void tlvGet(String cmd, ByteSet bin) {
        UnPack unPack=new UnPack();
        unPack.setData(bin);
        Logger.info(cmd);
        if(cmd.equals("01 6A")){

        }else if(cmd.equals("01 06")){

        }else if(cmd.equals("01 0C")){

        }else if(cmd.equals("01 0A")){
            Global.qq.token004C=bin.clone();
        }else if(cmd.equals("01 0D")){

        }else if(cmd.equals("01 14")){
            Global.qq.token0058= Tlv.tlv114Get0058(bin);
        }else if(cmd.equals("01 0E")){
            Global.qq.mST1Key=bin.clone();
        }else if(cmd.equals("01 03")){
            Global.qq.stweb=bin.toStringHex();
        }else if(cmd.equals("01 1F")){

        }else if(cmd.equals("01 38")){
            int len=unPack.getInt();
            for(int i=0;i<len;i++){
                int flag=unPack.getShort();
                int time=unPack.getInt();
                unPack.getInt();
               Logger.info("flag:{0},time:{1}",flag,time);
            }
        }else if(cmd.equals("01 1A")){
            short face=unPack.getShort();
            byte age=unPack.getByte();
            byte gander=unPack.getByte();
            int len=unPack.getByte();
            Global.qq.nick=new String(unPack.getBin(len).getByteSet());
            Logger.info("nickName:{0},face:{1},age:{2},gander:{3}",Global.qq.nick,face,age,gander);
        }else if(cmd.equals("01 20")){
            Global.qq.sKey=bin.clone();
        }else if(cmd.equals("01 36")){
            Global.qq.vKey=bin.clone();
        }else if(cmd.equals("01 1A")){

        }else if(cmd.equals("03 05")){
            Global.qq.sessionKey=bin.clone();
        }else if(cmd.equals("01 43")){
            Global.qq.token002C=bin.clone();
        }else if(cmd.equals("01 64")){
            Global.qq.sId=bin.clone();
        }else if(cmd.equals("01 18")){

        }else if(cmd.equals("01 30")){
            unPack.getShort();
            int time=unPack.getInt();
            String ip=ByteSet.toIP(unPack.getBin(4));
            Logger.info("time:{0},ip:{1}",time,ip);
        }else if(cmd.equals("01 05")){
            int len=unPack.getShort();
            Global.qq.vieryToken1=unPack.getBin(len);
            len=unPack.getShort();
            Global.qq.viery=unPack.getBin(len);
        }else if(cmd.equals("01 04")){
            Global.qq.vieryToken2=bin.clone();
        }else if(cmd.equals("01 65")){//需要输入验证码

        }else if(cmd.equals("01 08")){//ksid

        }else if(cmd.equals("01 6D")){
            Global.qq.superKey=bin.clone();
        }else if(cmd.equals("01 6C")){
            Global.qq.psKey=bin.clone();
        }else{
            Logger.info("未找到TLV命令:{0}",bin.toStringHex());
        }
    }

    public ByteSet packOidbSvc0x7a20() {
        ByteSet bin=ByteSet.parse("08 A2 0F 10 00 18 00 22 02 08 00");
        return this.makeLoginSendSsoMsg("OidbSvc.0x7a2_0",bin,Global.qq.token004C,Global.qqGlobal.imei,Global.qq.ksid,Global.qqGlobal.ver,false);
    }

    public ByteSet packStatSvcRegisterOnline() {
        return this.packStatSvcRegister(7,11,0);
    }

    public ByteSet packStatSvcRegister(long lBid, int iStatus, long timeStamp) {
        JceStructSvcReqRegister struct=new JceStructSvcReqRegister();
        JceOutputStream out=new JceOutputStream();
        struct.lUin=Global.qq.qq;
        struct.lBid=lBid;
        struct.iStatus=iStatus;
        struct.timeStamp=timeStamp;
        struct._11=15;
        struct._12=1;
        struct._imei_=Global.qqGlobal.imei_;
        struct._17=2052;
        struct._19_device=Global.qqGlobal.device;
        struct._20_device=Global.qqGlobal.device;
        struct._21_sys_ver=Global.qqGlobal.osVersion;
        JceStructFactory.writeSvcReqRegister(out, struct);
        ByteSet bin=out.toByteArray ();


        out.clear ();
        out.writeJceStruct (bin, 0);
        bin=out.toByteArray ();

        out.clear ();
        JceMap[] map=new JceMap[1];
        map [1].keyType=Constants.TYPE_STRING1;
        map [1].valType=Constants.TYPE_SIMPLE_LIST;
        map [1].key= ByteSet.parse("SvcReqRegister".getBytes());
        map [1].val=bin;
        out.writeMap (map, 0);
        bin=out.toByteArray ();
        JceStructRequestPacket req=new JceStructRequestPacket();
        out.clear ();
        req.iversion=3;
        req.sServantName="PushService";
        req.sFuncName="SvcReqRegister";
        req.sBuffer=bin;
        JceStructFactory.writeRequestPacket (out, req);
        bin=out.toByteArray ();
        return this.makeLoginSendSsoMsg ("StatSvc.register", bin, Global.qq.token004C, Global.qqGlobal.imei, Global.qq.ksid, Global.qqGlobal.ver,false);

    }

    public void onLine() {
        this.networkService.send(this.packOidbSvc0x7a20(), new NetworkReceive() {
            private CommonService commonService=new CommonServiceImpl();
            public void receive(ByteSet bytes) {
                this.commonService.receive(bytes);
            }
        });
        this.networkService.send(this.packStatSvcRegisterOnline(), new NetworkReceive() {
            private CommonService commonService=new CommonServiceImpl();
            public void receive(ByteSet bytes) {
                this.commonService.receive(bytes);
                if(bytes.length()<=0){

                }else{
                    MsgHandle msgHandle=new MsgHandle();
                    msgHandle.start();
                }
            }
        });
    }

    public void keep() {
        int wait=500;
        ByteSet mBin=new ByteSet();
        UnPack unPack=new UnPack();
        int len=0;
        for(int i=0;i<20;i++){
            ByteSet bin=this.networkService.receive();
            if(bin==null){
                break;
            }else if(bin.length()==0){
                continue;
            }
            mBin.append(bin);
            unPack.setData(mBin);
            while (unPack.length()>=4){
                len=unPack.getInt();
                if(len<=unPack.length()+4){
                    bin=unPack.getBin(len-4);
                    this.receive(bin);
                    mBin=unPack.getAll();
                    unPack.setData(mBin);
                }else{
                    Pack pack=new Pack();
                    pack.setInt(len);
                    pack.setBin(unPack.getAll());
                    unPack.setData(pack.getAll());
                    break;
                }
            }
        }
    }

    public void heartbeat() {
        this.networkService.send(packStatSvcGet());
    }

    public ByteSet packStatSvcGet() {
        JceOutputStream out=new JceOutputStream();
        out.writeInt (Number.longToInt(Global.qq.qq), 0);
        out.writeByte ((byte) 7, 1);
        out.writeStringByte ("", 2);
        out.writeByte ((byte)11, 3);
        out.writeByte ((byte)0, 4);
        out.writeByte ((byte)0, 5);
        out.writeByte ((byte)0, 6);
        out.writeByte ((byte)0, 7);
        out.writeByte ((byte)0, 8);
        out.writeByte ((byte)0, 9);
        out.writeByte ((byte)0, 10);
        out.writeByte ((byte)0, 11);
        ByteSet bin = out.toByteArray ();

        return this.makeSendSsoMsgSimple ("StatSvc.get", (short) 3, 1819559151, "PushService", "SvcReqGet", "SvcReqGet", bin);

    }

}