package com.cq.sdk.net.udp;

import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Number;
/**
 * 数据包
 * Created by admin on 2016/9/14.
 */
public class UnPack {
    private ByteSet head;
    private ByteSet md5;
    private int size;
    private ByteSet data;
    private ByteSet surplusData;
    public UnPack(ByteSet byteSet){
        this.head=byteSet.subByteSet(0,16+8);//16包md5 8长度
        this.md5=this.head.subByteSet(0,16);
        this.size=Number.byte4ToInt(this.head.subByteSet(16,8));
        this.data=byteSet.subByteSet(this.head.length(), size>byteSet.length()-this.head.length()?byteSet.length():size);
        this.surplusData=byteSet.subByteSet(this.data.length()+this.head.length());
    }
    public boolean isNotEnd(){
        return this.size!=this.data.length();
    }
    public void add(ByteSet byteSet){
        if(this.isNotEnd()){
            int surplus=this.size-this.data.length();
            this.data.append(byteSet.subByteSet(0,surplus>byteSet.length()?byteSet.length():surplus));
            this.surplusData=byteSet.subByteSet(this.data.length());
        }
    }

    public ByteSet getMd5() {
        return md5;
    }

    public int getSize() {
        return size;
    }

    public ByteSet getData() {
        return data;
    }

    public ByteSet getSurplusData() {
        return surplusData;
    }
}
