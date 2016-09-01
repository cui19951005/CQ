package com.cq.sdk.service.android.qq;

import com.cq.sdk.service.android.qq.inter.NetworkReceive;
import com.cq.sdk.service.android.qq.struct.IPAddress;
import com.cq.sdk.service.utils.ByteSet;

/**
 * Created by CuiYaLei on 2016/8/20.
 */
public interface NetworkService {
    boolean connection(IPAddress ipAddress);
    boolean send(ByteSet bytes);
    boolean send(ByteSet bytes,NetworkReceive networkReceive);
    ByteSet receive();
}
