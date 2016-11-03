package com.cq.sdk.net.socket;

import java.net.Socket;
import java.util.Map;

/**
 * Created by admin on 2016/10/31.
 */
public class SocketSession {
    private Socket socket;
    private Map<String,Object> attribute;

    public SocketSession(Socket socket, Map<String, Object> attribute) {
        this.socket = socket;
        this.attribute = attribute;
    }

    public Object getAttribute(String key) {
        return attribute.get(key);
    }

    public void setAttribute(String key,Object value) {
        this.attribute.put(key,value);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
