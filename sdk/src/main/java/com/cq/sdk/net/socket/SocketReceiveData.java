package com.cq.sdk.net.socket;
import com.cq.sdk.utils.ByteSet;

import java.net.Socket;

/**
 * Created by admin on 2016/9/14.
 */
public interface SocketReceiveData {
    void connection(SocketSession socketSession);
    void receive(SocketSession socketSession, ByteSet byteSet, String host, int port);
}
