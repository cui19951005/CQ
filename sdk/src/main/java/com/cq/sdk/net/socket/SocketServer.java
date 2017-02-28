package com.cq.sdk.net.socket;

import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Logger;
import com.cq.sdk.utils.Timer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by admin on 2016/10/31.
 */
public class SocketServer extends java.net.ServerSocket {
    private Vector<Socket> socketVector=new Vector<>();
    private String encoding="utf-8";

    public SocketServer(int port) throws IOException {
        this(port,50,null);
    }


    private SocketServer(int port, int backlog, InetAddress bindAddr) throws IOException {
        super(port, backlog, bindAddr);

    }
    public void startup(SocketReceiveData receiveData){
        Timer.open(new Timer.TimerTask() {
            @Override
            public void execute(int id) {
            try {
                Socket socket=SocketServer.this.accept();
                SocketServer.this.socketVector.add(socket);
                SocketSession socketSession=new SocketSession(socket,new HashMap());
                receiveData.connection(socketSession);
                Timer.open(new Timer.TimerTask() {
                    @Override
                    public void execute(int streamId) {
                    try {
                        InputStream inputStream=socket.getInputStream();
                        byte[] bytes=new byte[8192];
                        ByteSet byteSet=new ByteSet();
                        while (true){
                            int length=inputStream.read(bytes);
                            if(length<=0){
                                break;
                            }else{
                                byteSet.append(bytes,0,length);
                                if(length!=bytes.length){
                                    break;
                                }
                            }
                        }
                        if(byteSet.length()>0) {
                            receiveData.receive(socketSession, byteSet, socket.getLocalAddress().getHostAddress(), socket.getPort());
                            if(socketSession.getSocket().isClosed()){
                                Timer.close(streamId);
                            }
                        }
                    } catch (Exception e) {
                        Logger.error(socket.getLocalAddress().getHostAddress(),e);
                        try {
                            socket.close();
                            Timer.close(streamId);
                            SocketServer.this.socketVector.remove(socket);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }},0);
            } catch (IOException e) {
                Logger.error("server accept fail",e);
            }
        }},0);
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void send(Socket socket, ByteSet byteSet){
        try {
            socket.getOutputStream().write(byteSet.getByteSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendText(Socket socket,String text){
        try {
            this.send(socket,ByteSet.parse(text.getBytes(this.encoding)));
        } catch (UnsupportedEncodingException e) {
            Logger.error("encoding:{0} not exists",e,this.encoding);
        }
    }
}
