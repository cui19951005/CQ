package com.cq.sdk.test;
import com.cq.sdk.android.qq.UserService;
import com.cq.sdk.potential.annotation.*;
import com.cq.sdk.potential.utils.InjectionType;
import com.sun.glass.ui.Size;

import java.awt.*;


/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.android.qq.*",injectionType = InjectionType.Annotation)
@LoadProperties({"test.properties"})
//@NetAddress(port=2020,value = "localhost:2021")
public class Main {
    public static void main(String[] args) throws Exception {
        /*Trusteeship trusteeship=new Trusteeship(Main.class);
        UserService userService=trusteeship.get(UserService.class);
        userService.login("2534549160", "sj17839969220", (msgType,qq) -> {
            if(msgType== DataType.MsgType.LoginEnd){
                userService.friendList();
                while (true){
                    Scanner scanner=new Scanner(System.in);
                    Logger.info("请输入好友QQ:");
                    String account=scanner.next();
                    Logger.info("请输入发送消息:");
                    String message=scanner.next();
                    userService.sendMessage(account,message);
                }
            }
            Logger.info(msgType);
        });*/
        Window frame=new Window();
        frame.setTitle("my title");
        frame.setLocation(0,0);
        frame.setSize(300,500);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setGame(new Game(frame,new Size(20,20),new Point((frame.getWidth()-frame.getContentPane().getWidth())/2,frame.getHeight()-frame.getContentPane().getHeight())));
        frame.getGame().start();
    }
}