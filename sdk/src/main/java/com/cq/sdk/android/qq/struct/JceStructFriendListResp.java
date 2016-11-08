package com.cq.sdk.android.qq.struct;

import com.cq.sdk.utils.ByteSet;

/**
 * Created by CuiYaLei on 2016/8/21.
 */
public class JceStructFriendListResp {
    public int reqtype;
    public int ifReflush;
    public long uin;
    public int startIndex;
    public int getfriendCount;
    public int totoal_friend_count;
    public int friend_count;
    public int vecFriendInfo;
    public int groupid;
    public int ifGetGroupInfo;
    public int groupstartIndex;
    public int getgroupCount;
    public int totoal_group_count;
    public int group_count;
    public int vecGroupInfo;
    public int result;
    public int errorCode;
    public int online_friend_count;
    public int serverTime;
    public int sqqOnLine_count;
    public ByteSet cache_vecFriendInfo;
    public ByteSet cache_vecGroupInfo;

}
