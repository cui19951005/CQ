package com.cq.sdk.test;
import com.cq.sdk.potential.annotation.*;
import com.cq.sdk.potential.utils.InjectionType;
import com.cq.sdk.utils.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.android.qq.*",injectionType = InjectionType.Annotation)
@LoadProperties({"jdbc.properties"})
//@NetAddress(port=2020,value = "localhost:2021")
public class Main {
    public static void main(String[] args) throws Exception{
        Timer.open(new Timer.TimerTask() {
            @Override
            public void execute(int id) {
                File file=new File("e:/123.apk");
                Http.download("http://121.42.139.147/app/10008/4/?f=118&r=118-1487483792-634805-4954&m=10d192ea02493481a275a673bad98c91&p=3054651401&d=http://imtt.dd.qq.com/16891/0B558C168BF35B5C2E37090CFDAFDD93.apk",file.getAbsolutePath(),null);
                Logger.info(file.length());
            }
        },-1);

    }
}