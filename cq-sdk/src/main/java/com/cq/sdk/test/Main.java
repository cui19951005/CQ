package com.cq.sdk.test;

import com.cq.sdk.android.qq.CommonService;
import com.cq.sdk.android.qq.UserService;
import com.cq.sdk.android.qq.inter.MessageHandle;
import com.cq.sdk.android.qq.utils.DataType;
import com.cq.sdk.android.qq.utils.Global;
import com.cq.sdk.potential.Trusteeship;
import com.cq.sdk.potential.annotation.*;
import com.cq.sdk.potential.utils.InjectionType;
import com.cq.sdk.test.dao.LoginaccountMapper;
import com.cq.sdk.utils.*;
import org.hibernate.*;

import java.util.Scanner;
import java.util.concurrent.*;

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
        Trusteeship trusteeship=new Trusteeship(Main.class);
        //trusteeship.add(TestAop.class);
        UserService userService=trusteeship.get(UserService.class);
        userService.login("2534549160", "sj17839969220", new MessageHandle() {
            @Override
            public void message(DataType.MsgType msgType) {
                if(msgType== DataType.MsgType.LoginEnd){
                    userService.friendList();
                }
                Logger.info(msgType);
            }
        });
        Logger.info("欢迎登录QQ您的帐号:{0} 昵称:{1}", userService.getQQ().account,userService.getQQ().nick);
        while (true){
            Scanner scanner=new Scanner(System.in);
            Logger.info("请输入好友QQ:");
            String qq=scanner.next();
            Logger.info("请输入发送消息:");
            String message=scanner.next();
            userService.sendMessage(qq,message);
        }

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