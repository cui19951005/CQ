package com.cq.sdk.net.udp;

import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Encryption;
import com.cq.sdk.utils.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by admin on 2016/9/14.
 */
public class UDP {
    private DatagramSocket datagramSocket;
    private ReceiveData receiveData;
    private int port;
    private String host;
    public UDP(int port,ReceiveData receiveData){
        try {
            this.datagramSocket=new DatagramSocket(port);
            Receive receive=new Receive();
            receive.udp=this;
            receive.start();
        } catch (SocketException e) {
            e.printStackTrace();
            Logger.error("startup listen port fail");
        }
    }
    public UDP(int port,String host,ReceiveData receiveData){
        try {
            this.port = port;
            this.host = host;
            this.receiveData = receiveData;
            this.datagramSocket = new DatagramSocket();
            Receive receive = new Receive();
            receive.udp = this;
            receive.start();
        }catch (Exception e){
            e.printStackTrace();
            Logger.error("connection server fail");
        }
    }
    public void send(ByteSet byteSet){
        try {
            Pack data = new Pack(Encryption.MD5(byteSet), byteSet.length(), byteSet);
            DatagramPacket datagramPacket = new DatagramPacket(data.getData().getByteSet(), data.getData().length());
            this.datagramSocket.send(datagramPacket);
        }catch (Exception ex){
            ex.printStackTrace();
            Logger.error("udp send data fail",ex);
        }
    }
    private static class Receive extends Thread{
        private UDP udp;
        private DatagramPacket datagramPacket;
        private ByteSet lastData=new ByteSet();
        @Override
        public void run() {
            try {
                while (true) {
                    ByteSet byteSet=this.lastData.append(this.getData());
                    UnPack pack=new UnPack(byteSet);
                    while (pack.isNotEnd()){
                        pack.add(this.getData());
                    }
                    if(pack.getMd5().equals(Encryption.MD5(byteSet))) {
                        this.udp.receiveData.receive(this.udp, pack.getData(), this.datagramPacket.getAddress());
                        this.lastData=pack.getSurplusData();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger.error("udp receive data fail",e);
            }
        }
        private ByteSet getData() throws IOException {
            this.datagramPacket=new DatagramPacket(new byte[1024],1024);
            this.udp.datagramSocket.receive(this.datagramPacket);
            return ByteSet.parse(this.datagramPacket.getData(), this.datagramPacket.getOffset(), this.datagramPacket.getLength());
        }
    }
}
