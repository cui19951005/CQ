package com.cq.sdk.service.android.qq.impl;

import com.cq.sdk.service.android.qq.CommonService;
import com.cq.sdk.service.android.qq.NetworkService;
import com.cq.sdk.service.android.qq.QQService;
import com.cq.sdk.service.android.qq.struct.StructGroupMsg;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Service;
import com.cq.sdk.service.utils.ByteSet;
import com.cq.sdk.service.utils.Logger;
import com.sun.deploy.ref.AppRef;

/**
 * Created by CuiYaLei on 2016/8/21.
 */
@Service
public class QQServiceImpl implements QQService {
    @Autowired
    private NetworkService networkService;
    public void groupMessage(long qq, StructGroupMsg struct) {
        String name=new String(struct.sendName.getByteSet());
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<struct.arr.length;i++){
            if(struct.arr[i].type==0){
                break;
            }
            if(struct.arr[i].type!=1){
                continue;
            }
            ByteSet bytes;
            bytes=struct.arr[i].msg;
            bytes=bytes.getLeft(struct.arr[i].msgLen);
            sb.append(new String(bytes.getByteSet()));
        }
        Logger.info(sb.toString());
    }

    public void nearbyPeopleEnd(long qq) {

    }

    public void addFriendsResult(long qq, boolean suc, String msg) {

    }

    public void addFriendsGetSetResult(long qq, long friendUin, int verifyType, String question) {

    }
    public void addFriendsSendVlidate(long friendUin,int type,String msg){
        //this.networkService.send(this.commonService.packAddFriendReq(friendUin,type,msg ));
    }
}
