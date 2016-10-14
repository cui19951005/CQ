package com.cq.sdk.android.qq.impl;

import com.cq.sdk.android.qq.CommonService;
import com.cq.sdk.android.qq.NetworkService;
import com.cq.sdk.android.qq.UserService;
import com.cq.sdk.android.qq.inter.MessageHandle;
import com.cq.sdk.android.qq.inter.NetworkReceive;
import com.cq.sdk.android.qq.struct.Friend;
import com.cq.sdk.android.qq.struct.QQ;
import com.cq.sdk.android.qq.utils.*;
import com.cq.sdk.potential.annotation.Autowired;
import com.cq.sdk.potential.annotation.Service;
import com.cq.sdk.utils.Number;
import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Logger;

import java.util.List;

/**
 * Created by CuiYaLei on 2016/8/13.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private CommonService commonService;
    @Autowired
    private NetworkService networkService;
    private QQ qq=new QQ();
    public int login(String user, String password, MessageHandle messageHandle) {
        try {
            if (!Validate.account(user)) {
                return ErrorCode.User.USER_ACCOUNTFORMAT.getCode();
            }
            qq.messageHandle=messageHandle;
            this.commonService.setQQ(qq);
            this.qq.account = Long.valueOf(user);
            long userVal = Long.valueOf(user);
            if (userVal > Integer.MAX_VALUE) {
                this.qq.user = Number.longToByte8(userVal).getRight(4);
            } else {
                this.qq.user = Number.intToByte4((int) userVal);
            }
            this.qq.qq = userVal;
            this.qq.caption = ByteSet.parse(user.getBytes("gbk"));
            this.qq.pass = password;
            this.qq.md5 = Hash.MD5_B(ByteSet.parse(password.getBytes("gbk")));
            this.qq.md52 = Hash.MD5_B(new ByteSet().append(this.qq.md5, ByteSet.empty(4), this.qq.user));
            this.qq.ksid = Bin.hex2Bin("93 AC 68 93 96 D5 7E 5F 94 96 B8 15 36 AA FE 91");
            this.qq.token002C = new ByteSet();
            this.qq.token004C = new ByteSet();
            this.qq.token0058 = new ByteSet();
            this.qq.key = ByteSet.empty(16);
            this.qq.loginState = DataType.UserState.Login;
            if (!this.networkService.connection(Constants.IP_ADDRESS)) {
                this.qq.loginState = DataType.UserState.OffLine;
                return ErrorCode.System.SERVER_CONNECTION_FAILED.getCode();
            }
            this.qq.shareKey = ByteSet.parse("957C3AAFBF6FAF1D2C2F19A5EA04E51C");
            this.qq.pubKey = ByteSet.parse("02244B79F2239755E73C73FF583D4EC5625C19BF8095446DE1");
            this.qq.tgtKey = new ByteSet("{ 40, 81, 21, 92, 70, 136, 123, 20, 15, 44, 58, 216, 60, 152, 108, 30 }");//Bin.getRandomBin(16);
            this.qq.time = Number.longToByte8(Other.timeStamp(false)).getRight(4);//java语法不同
            this.qq.randKey = new ByteSet("{ 40, 81, 21, 92, 70, 136, 123, 20, 15, 44, 58, 216, 60, 152, 108, 30 }"); //Bin.getRandomBin(16);
            Pack pack = new Pack();
            pack.setHex("00 09");
            pack.setShort((short) 19);
            pack.setBin(Tlv.tlv18(this.qq.user));
            pack.setBin(Tlv.tlv1(this.qq.user, this.qq.time));
            pack.setBin(Tlv.tlv106(this.qq.user, this.qq.md5, this.qq.md52, this.qq.tgtKey, Global.qqGlobal.imei_, this.qq.time, Global.qqGlobal.appId));
            pack.setBin(Tlv.tlv116());
            pack.setBin(Tlv.tlv100(Global.qqGlobal.appId));
            pack.setBin(Tlv.tlv107());
            this.qq.ksid = new ByteSet(0);
            pack.setBin(Tlv.tlv108(this.qq.ksid));
            ByteSet tlv109 = Tlv.tlv109(Global.qqGlobal.imei_);
            ByteSet tlv124 = Tlv.tlv124(Global.qqGlobal.osType, Global.qqGlobal.osVersion, Global.qqGlobal.networkType, Global.qqGlobal.apn);
            ByteSet tlv128 = Tlv.tlv128(Global.qqGlobal.device, Global.qqGlobal.imei_);
            ByteSet tlv16e = Tlv.tlv16e(Global.qqGlobal.device);
            pack.setBin(Tlv.tlv144(this.qq.tgtKey, tlv109, tlv124, tlv128, tlv16e));
            pack.setBin(Tlv.tlv142(Global.qqGlobal.apkId));
            pack.setBin(Tlv.tlv145(Global.qqGlobal.imei_));
            pack.setBin(Tlv.tlv154(Global.requestId));
            pack.setBin(Tlv.tlv141(Global.qqGlobal.networkType, Global.qqGlobal.apn));
            pack.setBin(Tlv.tlv8());
            pack.setBin(Tlv.tlv16b());
            pack.setBin(Tlv.tlv147(Global.qqGlobal.apkV, Global.qqGlobal.apkSig));
            pack.setBin(Tlv.tlv177());
            pack.setBin(Tlv.tlv187());
            pack.setBin(Tlv.tlv188());
            pack.setBin(Tlv.tlv191());
            ByteSet buffer = pack.getAll();
            buffer = Hash.QQTea(buffer, this.qq.shareKey);
            Logger.info(buffer);
            buffer = this.commonService.packPc("08 10", buffer, this.qq.randKey, this.qq.pubKey);
            buffer = this.commonService.makeLoginSendSsoMsg("wtlogin.login", buffer, new ByteSet(), Global.qqGlobal.imei, this.qq.ksid, Global.qqGlobal.ver, true);
            if (!this.networkService.send(buffer, new Receive())) {
                this.qq.loginState = DataType.UserState.OffLine;
                return ErrorCode.User.USER_LOGINFAILED.getCode();
            }
            if (this.qq.loginState.getState() == DataType.UserState.Success.getState()) {
                Logger.info("登录成功");
                this.commonService.onLine();
                this.qq.loginState = DataType.UserState.OnLine;
                return 0;
            } else if (this.qq.loginState.getState() == DataType.UserState.Verification.getState()) {
                Logger.info("需要验证码");
            }
            return -1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void sendMessage(String account, String message) {
        this.networkService.send(this.commonService.packMessageSvcOfflinemsg(Long.parseLong(account),message));
    }

    @Override
    public void friendList() {
        this.commonService.funSendGetFriendList(0,20);
    }

    @Override
    public QQ getQQ() {
        return this.qq;
    }

    private class Receive implements NetworkReceive {
        private CommonService commonService=UserServiceImpl.this.commonService;
        public void receive(ByteSet bytes) {
            this.commonService.receive(bytes);
        }
    }

}