package com.cq.sdk.android.qq.impl;
import com.cq.sdk.android.qq.NetworkService;
import com.cq.sdk.android.qq.inter.NetworkReceive;
import com.cq.sdk.android.qq.struct.IPAddress;
import com.cq.sdk.android.qq.utils.Constants;
import com.cq.sdk.android.qq.utils.Global;
import com.cq.sdk.potential.annotation.Service;
import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by CuiYaLei on 2016/8/20.
 */
@Service
public class NetworkServiceImpl implements NetworkService {
    private Socket clicent;
    public boolean connection(IPAddress ipAddress) {
        try {
            this.clicent=new Socket(Constants.IP_ADDRESS.host, Constants.IP_ADDRESS.port);
            return true;
        } catch (IOException e) {
            Logger.error("连接QQ服务器失败",e);
            return false;
        }
    }

    public boolean send(ByteSet bytes) {
       return this.send(bytes,null);
    }

    public boolean send(ByteSet bytes, NetworkReceive networkReceive) {
        Global.increaseSsoSeq();
        Logger.info(bytes);
        try {
            OutputStream outputStream=this.clicent.getOutputStream();
            outputStream.write(bytes.getByteSet());
            if(networkReceive!=null) {
                InputStream inputStream = this.clicent.getInputStream();
                byte[] data = new byte[1024];
                ByteSet result = new ByteSet();
                while (true) {
                    int length = inputStream.read(data, 0, data.length);
                    if(length<=0){
                        break;
                    }
                    result.append(data,0,length);
                    if(length!=data.length){
                        break;
                    }
                }
                networkReceive.receive(result);
            }
            return true;
        } catch (IOException e) {
            Logger.error("发送数据失败",e);
            return false;
        }
    }

    public ByteSet receive() {
        try {
            InputStream inputStream=this.clicent.getInputStream();
            ByteSet data = new ByteSet(1024);
            ByteSet result = new ByteSet();
            while (true) {
                int length = inputStream.read(data.getByteSet(), 0, data.length());
                result.append(data.subByteSet(0, length));
                if(length!=data.length()){
                    break;
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("接收数据错误",e);
            return null;
        }
    }
}
