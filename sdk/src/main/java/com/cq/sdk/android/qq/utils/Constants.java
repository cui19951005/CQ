package com.cq.sdk.android.qq.utils;

import com.cq.sdk.android.qq.struct.IPAddress;

/**
 * Created by CuiYaLei on 2016/8/13.
 */
public class Constants {
    public static final boolean BYTE_VALUE=true;

    public static final IPAddress IP_ADDRESS =new IPAddress("msfwifi.3g.qq.com", 8080);

    public static final int TYPE_BYTE=0;
    public static final int TYPE_SHORT=1;
    public static final int TYPE_INT=2;
    public static final int TYPE_LONG=3;
    public static final int TYPE_SIMPLE_LIST=13;
    public static final int TYPE_MAP=8;
    public static final int TYPE_ZERO_TAG=12;
    public static final int TYPE_STRING1=6;
    public static final int TYPE_LIST=9;
    public static final int TYPE_STRING4=7;
    public static final int TYPE_STRUCT_BEGIN=10;
    public static final int TYPE_STRUCT_END=11;

    public static final String FRIENDLIST_ADDFRIEND="friendlist.addFriend";
    public static final String SERVANT_NAME="mqq.IMService.FriendListServiceServantObj";
    public static final String FUNC_NAME="AddFriendReq";
    public static final String MAP_KEY="AF";
    public static final String WTLOGIN_LOGIN="wtlogin.login";
}
