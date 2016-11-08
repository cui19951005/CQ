package com.cq.sdk.android.qq;

import com.cq.sdk.android.qq.struct.CardData;
import com.cq.sdk.android.qq.struct.QQ;
import com.cq.sdk.android.qq.utils.JceInputStream;
import com.cq.sdk.utils.ByteSet;

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

    /**
     * 解包
     * @param jceInputStream
     * @param cardData
     */
    void unPackMap(JceInputStream jceInputStream, CardData cardData);
    ByteSet packAddFriendReq(long friendUin,int type,String msg);
    ByteSet makeSendSsoMsg(String serviceCmd,ByteSet wupBuffer);
    ByteSet makeSendSsoMsgSimple(String serviceCmd, short version, int iRequestId, String sServantName, String sFuncName, String mapKey, ByteSet wupBuffer);
    ByteSet packSendSsoMsgSimple(short version,int iRequestId,String sServantName,String sFuncName,String mapKey,ByteSet wupBuffer);
    boolean unPackLogin(ByteSet bin);
    ByteSet unPackLoginPc(ByteSet bin);
    void unPackVieryImage(ByteSet bin);
    void unPackErrMsg(ByteSet bin);
    void unTlv(ByteSet bin);
    void tlvGet(String cmd,ByteSet bin);
    ByteSet packOidbSvc0x7a20();
    ByteSet packStatSvcRegisterOnline();
    ByteSet packStatSvcRegister(long lBid,int iStatus,long timeStamp);

    /**
     * 上线
     */
    void onLine();

    /**
     * 等待数据
     */
    void keep();
    /**
     * 心跳数据
     */
    void heartbeat();
    ByteSet packStatSvcGet();

    /**
     * 发送qq消息
     * @param account 目标帐号
     * @param message 消息
     * @return
     */
    ByteSet packMessageSvcOfflinemsg(long account,String message);

    /**
     * 获取好友列表
     * @param startIndex
     * @param getFriendCount
     */
    void funSendGetFriendList(int startIndex,int getFriendCount);
    ByteSet packFriendListGetFriendGroupList(int startIndex, int getFriendCount);

    /**
     * 传入qq对象
     * @param qq
     */
    void setQQ(QQ qq);
}
