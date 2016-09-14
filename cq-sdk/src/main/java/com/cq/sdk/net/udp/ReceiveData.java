package com.cq.sdk.net.udp;

import com.cq.sdk.utils.ByteSet;

import java.net.InetAddress;

/**
 * Created by admin on 2016/9/14.
 */
public interface ReceiveData {
    void receive(UDP udp, ByteSet byteSet, InetAddress inetAddress);
}
