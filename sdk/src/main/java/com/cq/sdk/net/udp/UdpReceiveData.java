package com.cq.sdk.net.udp;
import com.cq.sdk.utils.ByteSet;

/**
 * Created by admin on 2016/9/14.
 */
public interface UdpReceiveData {
    void receive(UDP udp, ByteSet byteSet, String host, int port);
}
