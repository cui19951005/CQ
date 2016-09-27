package com.cq.sdk.net.udp;

import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Encryption;
import com.cq.sdk.utils.Number;

/**
 * Created by admin on 2016/9/14.
 */
public class Pack {
    private ByteSet data;

    public ByteSet getData() {
        return data;
    }

    public Pack(int size, ByteSet byteSet){
        ByteSet sum=Number.intToByte4(size).append(byteSet);
        ByteSet md5= Encryption.MD5(sum);
        this.data=md5.append(sum);
    }
}
