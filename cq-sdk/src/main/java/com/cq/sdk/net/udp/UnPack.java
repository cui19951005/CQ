package com.cq.sdk.net.udp;

import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Number;
/**
 * 数据包
 * Created by admin on 2016/9/14.
 */
public class UnPack {
    private ByteSet md5;
    private int size;
    private ByteSet data;
    private ByteSet surplusData;
    public UnPack(ByteSet byteSet){
        this.md5=byteSet.subByteSet(0,16);
        this.size=Number.byte4ToInt(byteSet.subByteSet(16,4));
        int head=20;
        this.data=byteSet.subByteSet(head, size>byteSet.length()-head?byteSet.length():size);
        this.surplusData=byteSet.subByteSet(this.data.length()+head);
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
