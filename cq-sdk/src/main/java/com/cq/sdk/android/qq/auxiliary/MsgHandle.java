package com.cq.sdk.android.qq.auxiliary;
import com.cq.sdk.android.qq.CommonService;

/**
 * Created by CuiYaLei on 2016/8/27.
 */
public class MsgHandle extends Thread {
    public CommonService commonService;
    @Override
    public void run() {
        this.commonService.heartbeat();
      while (true){
        this.commonService.keep();
          try {
              Thread.sleep(200);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
    }
}
