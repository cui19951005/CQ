package com.cq.sdk.android.qq.struct;

import com.cq.sdk.utils.ByteSet;

/**
 * Created by CuiYaLei on 2016/8/12.
 */
public class TeaStruct {
    public ByteSet mPlain=new ByteSet(8);
    public ByteSet mOut=new ByteSet(8);
    public ByteSet mPrePlain=new ByteSet(8);
    public int mCrypt;
    public int mPreCrypt;
    public long mPos;
    public long mPadding;
    public ByteSet mKey=new ByteSet(16);
    public boolean mHeader;
    public long mContextStart;
    private static final TeaStruct TEA_STRUCT=new TeaStruct();
    public static final TeaStruct getTeaStruct(){
        TEA_STRUCT.mPos=0;
        TEA_STRUCT.mPlain=new ByteSet(TEA_STRUCT.mPlain.length());
        TEA_STRUCT.mOut=new ByteSet(TEA_STRUCT.mOut.length());
        TEA_STRUCT.mPrePlain=new ByteSet(TEA_STRUCT.mPrePlain.length());
        TEA_STRUCT.mCrypt=0;
        TEA_STRUCT.mPreCrypt=0;
        TEA_STRUCT.mPadding=0;
        TEA_STRUCT.mKey=new ByteSet(TEA_STRUCT.mKey.length());
        TEA_STRUCT.mHeader=false;
        TEA_STRUCT.mContextStart=0;
        return TEA_STRUCT;
    }
    private TeaStruct(){}
}
