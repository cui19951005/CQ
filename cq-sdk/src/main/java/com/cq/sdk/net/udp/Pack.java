package com.cq.sdk.net.udp;

import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Number;

/**
 * Created by admin on 2016/9/14.
 */
public class Pack {
    private ByteSet data;

    public ByteSet getData() {
        return data;
    }

    public Pack(ByteSet md5, int size, ByteSet byteSet){
        this.data=md5.append(Number.intToByte4(size),byteSet);
    }
}
