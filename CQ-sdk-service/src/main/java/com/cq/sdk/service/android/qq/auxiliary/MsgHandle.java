package com.cq.sdk.service.android.qq.auxiliary;

import com.cq.sdk.service.android.qq.CommonService;
import com.cq.sdk.service.android.qq.impl.CommonServiceImpl;
import com.cq.sdk.service.potential.annotation.Service;

/**
 * Created by CuiYaLei on 2016/8/27.
 */
@Service
public class MsgHandle extends Thread {
    private CommonService commonService=new CommonServiceImpl();
    @Override
    public void run() {
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
