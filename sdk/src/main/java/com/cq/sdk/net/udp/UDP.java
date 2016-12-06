package com.cq.sdk.net.udp;

import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Encryption;
import com.cq.sdk.utils.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by admin on 2016/9/14.
 */
public class UDP {
    private DatagramSocket datagramSocket;
    private UdpReceiveData receiveData;
    private int port;
    private String host;
    Receive receive;

    public UDP(String host,int port, UdpReceiveData receive) {
        this(-1,host,port,receive);
    }

    public UDP(int localPort, String host, int port, UdpReceiveData receiveData){
        try {
            this.port = port;
            this.host = host;
            this.receiveData = receiveData;
            if(localPort>0) {
                this.datagramSocket = new DatagramSocket(localPort);
            }else{
                this.datagramSocket=new DatagramSocket();
            }
            this.receive = new Receive();
            this.receive.udp = this;
            this.receive.start();
        }catch (Exception e){
            e.printStackTrace();
            Logger.error("connection server fail");
        }
    }
    public void send(String host,int port,ByteSet byteSet,int overtime){
        try {
            Pack data = new Pack(byteSet.length(), byteSet);
            DatagramPacket datagramPacket = new DatagramPacket(data.getData().getByteSet(), data.getData().length());
            datagramPacket.setPort(port);
            datagramPacket.setAddress(InetAddress.getByName(host));
            this.datagramSocket.send(datagramPacket);
            if(overtime>0){
                this.receive.join(overtime);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Logger.error("udp send data fail",ex);
        }
    }
    public void send(String host,int port,ByteSet byteSet){
        this.send( host, port,byteSet,-1);
    }
    private static class Receive extends Thread{
        private UDP udp;
        private DatagramPacket datagramPacket;
        private ByteSet lastData=new ByteSet();
        @Override
        public void run() {
            try {
                while (!this.udp.datagramSocket.isClosed()) {
                    ByteSet data=this.getData();
                    if(data==null){
                        break;
                    }
                    ByteSet byteSet=this.lastData.append(data);
                    UnPack pack=new UnPack(byteSet);
                    while (pack.isNotEnd()){
                        pack.add(this.getData());
                    }
                    if(pack.getMd5().equals(Encryption.md5(byteSet.subByteSet(16)))) {
                        this.udp.receiveData.receive(this.udp,pack.getData(), this.datagramPacket.getAddress().getHostAddress(),this.datagramPacket.getPort());
                        this.lastData=pack.getSurplusData();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger.error("udp receive data fail",e);
            }
        }
        private ByteSet getData() throws IOException {
            if(!this.udp.datagramSocket.isClosed()) {
                this.datagramPacket = new DatagramPacket(new byte[1024], 1024);
                this.udp.datagramSocket.receive(this.datagramPacket);
                return ByteSet.parse(this.datagramPacket.getData(), this.datagramPacket.getOffset(), this.datagramPacket.getLength());
            }else{
                return null;
            }
        }
    }
    public void close(){
        if(this.datagramSocket!=null&&!this.datagramSocket.isClosed()) {
            this.datagramSocket.close();
        }
        if(this.receive!=null){
            this.receive.interrupt();
        }
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}
