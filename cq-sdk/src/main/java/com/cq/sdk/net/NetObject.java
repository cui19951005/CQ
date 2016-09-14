package com.cq.sdk.net;

import com.cq.sdk.net.udp.UDP;
import com.cq.sdk.utils.ByteSet;

import java.io.*;

/**
 * Created by admin on 2016/9/14.
 */
public class NetObject {
    private String host;
    private int port;
    private UDP udp;
    public Object send(String method,Object[] params){
        try {
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(params);
            this.udp.send(ByteSet.parse(outputStream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
