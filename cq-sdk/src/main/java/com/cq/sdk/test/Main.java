package com.cq.sdk.test;

import com.cq.sdk.android.qq.CommonService;
import com.cq.sdk.android.qq.UserService;
import com.cq.sdk.android.qq.struct.QQ;
import com.cq.sdk.android.qq.utils.*;
import com.cq.sdk.hibernate.LoginAccountEntity;
import com.cq.sdk.net.NetObject;
import com.cq.sdk.net.udp.ReceiveData;
import com.cq.sdk.net.udp.UDP;
import com.cq.sdk.potential.Trusteeship;
import com.cq.sdk.potential.annotation.*;
import com.cq.sdk.potential.sql.frame.hibernate.HibernateSessionManager;
import com.cq.sdk.potential.utils.FileUtils;
import com.cq.sdk.potential.utils.InjectionType;
import com.cq.sdk.test.dao.LoginaccountMapper;
import com.cq.sdk.utils.*;
import com.cq.sdk.utils.Number;
import com.cq.sdk.utils.Timer;
import org.hibernate.*;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;

import java.io.File;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.android.qq.*",injectionType = InjectionType.Annotation)
@LoadProperties({"test.properties"})
//@NetAddress(port=2020,value = "localhost:2021")
public class Main {
    @Autowired
    Main main;
    @Autowired
    static SessionFactory sessionFactory;
    @Autowired
    static LoginaccountMapper loginaccountMapper;
    @Autowired
    UserService userService;
    @Autowired
    CommonService commonService;
    public static void main(String[] args) throws Exception {
        /*NetObject netObject=new NetObject(2021,"localhost",2020);
        netObject.messageHandle(null);*/
        //Trusteeship trusteeship=new Trusteeship(Main.class);
        //trusteeship.add(TestAop.class);
        //trusteeship.get(UserService.class).login("2534549160","sj17839969220");
        /*Semaphore semaphore=new Semaphore(3);
        for(int i=0;i<100;i++) {
            new Thread(new Test(semaphore,i)).start();
        }*/

    }
    private static class Test implements Runnable{
        public Semaphore semaphore;
        public int index;

        public Test(Semaphore semaphore, int index) {
            this.semaphore = semaphore;
            this.index = index;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                Logger.info("线程{0}开始处理",index);
                Thread.sleep(1000);
                Logger.info("线程{0}处理完毕",index);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }
        }
    }
    private static class Test1 implements Callable{

        @Override
        public Object call() throws Exception {
            return "hello work";
        }
    }

}