package com.cq.sdk.android.qq.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/10/14.
 */
public class Group {
    public long id;
    public long code;
    public String name;
    public List<GroupMember> groupMemberList=new ArrayList<>();
}
