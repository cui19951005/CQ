package com.cq.sdk.test;

import com.cq.sdk.android.qq.UserService;
import com.cq.sdk.utils.ConcurrentObject;
import com.cq.sdk.potential.annotation.*;
import com.cq.sdk.potential.utils.InjectionType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
    UserService userService;
    static Map[] list=new HashMap[5];
    public static void main(String[] args) throws Exception {
       /*Trusteeship trusteeship=new Trusteeship(Main.class);
        UserService userService=trusteeship.get(UserService.class);
        userService.login("2534549160", "sj17839969220", msgType -> {
            if(msgType== DataType.MsgType.LoginEnd){
                userService.friendList();
                while (true){
                    Scanner scanner=new Scanner(System.in);
                    Logger.info("请输入好友QQ:");
                    String qq=scanner.next();
                    Logger.info("请输入发送消息:");
                    String message=scanner.next();
                    userService.sendMessage(qq,message);
                }
            }
            Logger.info(msgType);
        });*/
        ByteArrayInputStream inputStream=new ByteArrayInputStream(new byte[]{1,2,3,4});
        ConcurrentObject<InputStream> concurrentObject=new ConcurrentObject<>(inputStream);
        concurrentObject.acquire();
        concurrentObject.release();

    }
}