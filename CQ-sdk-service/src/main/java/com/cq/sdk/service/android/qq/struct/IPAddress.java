package com.cq.sdk.service.android.qq.struct;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by CuiYaLei on 2016/8/13.
 */
public class IPAddress {
    public String host;
    public int port;

    public IPAddress(String host, int port) {
        try {
            this.host = InetAddress.getByName(host).getHostAddress().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
    }
}
