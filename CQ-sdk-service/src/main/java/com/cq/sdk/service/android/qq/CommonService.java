package com.cq.sdk.service.android.qq;

import com.cq.sdk.service.android.qq.struct.CardData;
import com.cq.sdk.service.android.qq.utils.JceInputStream;
import com.cq.sdk.service.utils.ByteSet;

/**
 * Created by CuiYaLei on 2016/8/20.
 */

public interface CommonService {
    void receive(ByteSet bytes);
    ByteSet pack(ByteSet bin,int type);
    ByteSet makeLoginSendSsoMsg(String serviceCmd,ByteSet buffer,ByteSet extBin,String imei,ByteSet ksId,ByteSet ver,boolean isLogin);
    ByteSet packPc(String cmd,ByteSet bin,ByteSet extKey,ByteSet extBin);
    ByteSet unPack(ByteSet bytes,boolean bool);
    void msgHandle(int ssoSeq,String serviceCmd,ByteSet bin);
    void unPackRequestPacket(ByteSet bin, CardData cardData);
    void unPackMap(JceInputStream jceInputStream,CardData cardData);
    ByteSet packAddFriendReq(long friendUin,int type,String msg);
    ByteSet makeSendSsoMsg(String serviceCmd,ByteSet wupBuffer);
    ByteSet makeSendSsoMsgSimple(String serviceCmd, short iversion, int iRequestId, String sServantName, String sFuncName, String mapKey, ByteSet wupBuffer);
    ByteSet packSendSsoMsgSimple(short iversion,int iRequestId,String sServantName,String sFuncName,String mapKey,ByteSet wupBuffer);
    boolean unPackLogin(ByteSet bin);
    ByteSet unPackLoginPc(ByteSet bin);
    void unPackVieryImage(ByteSet bin);
    void unPackErrMsg(ByteSet bin);
    void unTlv(ByteSet bin);
    void tlvGet(String cmd,ByteSet bin);
    ByteSet packOidbSvc0x7a20();
    ByteSet packStatSvcRegisterOnline();
    ByteSet packStatSvcRegister(long lBid,int iStatus,long timeStamp);
    void onLine();
    void keep();
    /**
     * 心跳
     */
    void heartbeat();
    ByteSet packStatSvcGet();
}
