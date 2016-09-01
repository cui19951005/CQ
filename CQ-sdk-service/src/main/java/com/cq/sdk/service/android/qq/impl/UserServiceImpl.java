package com.cq.sdk.service.android.qq.impl;

import com.cq.sdk.service.android.qq.CommonService;
import com.cq.sdk.service.android.qq.NetworkService;
import com.cq.sdk.service.android.qq.UserService;
import com.cq.sdk.service.android.qq.auxiliary.MsgHandle;
import com.cq.sdk.service.android.qq.inter.NetworkReceive;
import com.cq.sdk.service.android.qq.utils.*;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Service;
import com.cq.sdk.service.utils.*;
import com.cq.sdk.service.utils.Number;

/**
 * Created by CuiYaLei on 2016/8/13.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private CommonService commonService;
    @Autowired
    private NetworkService networkService;
    public int login(String user, String password) {
        if (!Validate.qqAccount(user)) {
            return ErrorCode.User.USER_ACCOUNTFORMAT.getCode();
        }
        Global.qq.account = user;
        long userVal = Long.valueOf(user);
        if (userVal > Integer.MAX_VALUE) {
            userVal = (int) -(Integer.MAX_VALUE + Integer.MAX_VALUE - userVal);
        }
        if (userVal > Integer.MAX_VALUE) {
            Global.qq.user = Bin.flip(Number.longToByte8(userVal)).getRight(4);
        } else {
            Global.qq.user = Bin.flip(Number.intToByte4((int) userVal));
        }
        Global.qq.qq = userVal;
        Global.qq.caption = ByteSet.parse(user.getBytes());
        Global.qq.pass = password;
        Global.qq.md5 = Hash.MD5_B(ByteSet.parse(password.getBytes()));
        Global.qq.md52 = Hash.MD5_B(new ByteSet().append(Global.qq.md5, ByteSet.empty(4), Global.qq.user));
        Global.qq.ksid = Bin.hex2Bin("93 AC 68 93 96 D5 7E 5F 94 96 B8 15 36 AA FE 91");
        Global.qq.token002C = new ByteSet();
        Global.qq.token004C = new ByteSet();
        Global.qq.token0058 = new ByteSet();
        Global.qq.key = ByteSet.empty(16);
        Global.qq.loginState = DataType.UserState.Login;
        Logger.info(Constants.IP_ADDRESS);
        if(!this.networkService.connection(Constants.IP_ADDRESS)){
            Global.qq.loginState = DataType.UserState.OffLine;
            return ErrorCode.System.SERVER_CONNECTION_FAILED.getCode();
        }
        Global.qq.shareKey = ByteSet.parse("957C3AAFBF6FAF1D2C2F19A5EA04E51C");
        Global.qq.pubKey = ByteSet.parse("02244B79F2239755E73C73FF583D4EC5625C19BF8095446DE1");
        Global.qq.tgtKey = Bin.getRandomBin(16);
        Global.qq.time = Bin.flip(Number.longToByte8(Other.timeStamp(false)).getLeft(4));
        Global.qq.randKey = Bin.getRandomBin(16);
        Pack pack = new Pack();
        pack.setHex("00 09");
        pack.setShort((short) 19);
        pack.setBin(Tlv.tlv18(Global.qq.user));
        pack.setBin(Tlv.tlv1(Global.qq.user, Global.qq.time));
        pack.setBin(Tlv.tlv106(Global.qq.user, Global.qq.md5, Global.qq.md52, Global.qq.tgtKey, Global.qqGlobal.imei_, Global.qq.time, Global.qqGlobal.appId));
        pack.setBin(Tlv.tlv116());
        pack.setBin(Tlv.tlv100(Global.qqGlobal.appId));
        pack.setBin(Tlv.tlv107());
        Global.qq.ksid=new ByteSet(0);
        pack.setBin(Tlv.tlv108(Global.qq.ksid));
        ByteSet tlv109 = Tlv.tlv109(Global.qqGlobal.imei_);
        ByteSet tlv124 = Tlv.tlv124(Global.qqGlobal.osType, Global.qqGlobal.osVersion, Global.qqGlobal.networkType, Global.qqGlobal.apn);
        ByteSet tlv128 = Tlv.tlv128(Global.qqGlobal.device, Global.qqGlobal.imei_);
        ByteSet tlv16e = Tlv.tlv16e(Global.qqGlobal.device);
        pack.setBin(Tlv.tlv144(Global.qq.tgtKey, tlv109, tlv124, tlv128, tlv16e));
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
        buffer = this.commonService.packPc("08 10", Hash.QQTea(buffer, Global.qq.shareKey), Global.qq.randKey, Global.qq.pubKey);
        if (!this.networkService.send(this.commonService.makeLoginSendSsoMsg("wtlogin.login", buffer, new ByteSet(), Global.qqGlobal.imei, Global.qq.ksid, Global.qqGlobal.ver, true),new Receive())) {
            Global.qq.loginState = DataType.UserState.OffLine;
            return ErrorCode.User.USER_LOGINFAILED.getCode();
        }
        if(Global.qq.loginState.getState()== DataType.UserState.Success.getState()){
            Logger.info("登录成功");
            this.commonService.onLine();
            Global.qq.loginState= DataType.UserState.OnLine;
            return 0;
        }else if(Global.qq.loginState.getState()==DataType.UserState.Verification.getState()){
            Logger.info("需要验证码");
        }
        return -1;
    }
    private
    class Receive implements NetworkReceive{
        private CommonService commonService=new CommonServiceImpl();
        public void receive(ByteSet bytes) {
            this.commonService.receive(bytes);
        }
    }

}