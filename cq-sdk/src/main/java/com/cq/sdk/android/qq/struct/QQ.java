package com.cq.sdk.android.qq.struct;

import com.cq.sdk.android.qq.inter.MessageHandle;
import com.cq.sdk.android.qq.utils.DataType;
import com.cq.sdk.utils.ByteSet;

import java.util.ArrayList;
import java.util.List;

/**
 * QQ登录信息
 * Created by CuiYaLei on 2016/8/13.
 */
public class QQ {
    public long account;
    public long qq;
    public ByteSet user;
    public ByteSet caption;
    public String pass;
    public ByteSet md5;
    public ByteSet md52;
    public ByteSet time;
    public ByteSet key;
    public String nick;
    public ByteSet token002C;
    public ByteSet token004C;
    public ByteSet token0058;
    public ByteSet tgtKey;
    public ByteSet shareKey;
    public ByteSet pubKey;
    public ByteSet ksid;
    public ByteSet randKey;
    public ByteSet mST1Key;
    public String stweb;
    public ByteSet sKey;
    public ByteSet psKey;
    public ByteSet superKey;
    public ByteSet vKey;
    public ByteSet sId;
    public ByteSet sessionKey;
    public DataType.UserState loginState;
    public ByteSet vieryToken1;
    public ByteSet vieryToken2;
    public ByteSet viery;
    public List<Group> groupList=new ArrayList<>();
    public List<Friend> friendList=new ArrayList<>();
    public AddFriendResult addFriendResult=new AddFriendResult();
    public MessageHandle messageHandle;
}
