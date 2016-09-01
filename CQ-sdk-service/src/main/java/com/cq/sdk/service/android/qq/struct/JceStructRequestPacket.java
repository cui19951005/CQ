package com.cq.sdk.service.android.qq.struct;

import com.cq.sdk.service.utils.ByteSet;

/**
 * Created by CuiYaLei on 2016/8/20.
 */
public class JceStructRequestPacket {
    public short iversion;
    public short cPacketType;
    public short iMessageType;
    public int iRequestId;
    public String sServantName;
    public String sFuncName;
    public ByteSet sBuffer;
    public int iTimeout;
    public JceMap[] context=new JceMap[1];
    public JceMap[] status=new JceMap[1];

}
