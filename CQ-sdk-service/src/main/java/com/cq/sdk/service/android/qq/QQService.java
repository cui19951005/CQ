package com.cq.sdk.service.android.qq;

import com.cq.sdk.service.android.qq.struct.StructGroupMsg;

/**
 * Created by CuiYaLei on 2016/8/21.
 */
public interface QQService {
    void groupMessage(long qq, StructGroupMsg struct);
    void nearbyPeopleEnd(long qq);
    void addFriendsResult(long qq,boolean suc,String msg);

    /**
     * 添加好友获取设置返回
     * @param qq
     * @param friendUin
     * @param verifyType
     * @param question
     */
    void addFriendsGetSetResult(long qq,long friendUin,int verifyType,String question);
}
