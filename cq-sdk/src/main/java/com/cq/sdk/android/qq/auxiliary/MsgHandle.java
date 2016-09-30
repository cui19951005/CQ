package com.cq.sdk.android.qq.auxiliary;

import com.cq.sdk.android.qq.impl.CommonServiceImpl;
import com.cq.sdk.android.qq.CommonService;
import com.cq.sdk.potential.annotation.Autowired;
import com.cq.sdk.potential.annotation.Service;

/**
 * Created by CuiYaLei on 2016/8/27.
 */
@Service
public class MsgHandle extends Thread {
    public CommonService commonService;
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
