package com.cq.sdk.android.qq;


import com.cq.sdk.android.qq.inter.MessageHandle;
import com.cq.sdk.android.qq.struct.QQ;

/**
 * Created by CuiYaLei on 2016/8/12.
 */
public interface UserService {
    int login(String user, String password, MessageHandle messageHandle);
    void sendMessage(String account,String message);
    void friendList();
}
