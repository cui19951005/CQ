package com.cq.sdk.test;
import com.cq.sdk.net.ftp.FtpServer;
import com.cq.sdk.net.ftp.FtpUser;
import com.cq.sdk.potential.annotation.*;
import com.cq.sdk.potential.utils.InjectionType;
import com.cq.sdk.utils.Date;
import com.cq.sdk.utils.Logger;

import java.util.Arrays;
import java.util.Locale;


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


        /*Window frame=new Window();
        frame.setTitle("my title");
        frame.setLocation(0,0);
        frame.setSize(300,500);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setGame(new Game(
                 new Size(20,20)
                ,new Point((frame.getWidth()-frame.getContentPane().getWidth())/2,frame.getHeight()-frame.getContentPane().getHeight())
                ,new Rectangle(new Point(),frame.getContentPane().getSize())
        ));
        frame.getGame().start(()->{
            frame.repaint();
            frame.setTitle("积分:"+frame.getGame().getIntegral());
        },500);*/
        FtpServer ftpServer=new FtpServer(21);
        ftpServer.setFtpUser(new FtpUser() {
            @Override
            public boolean isAnonymous() {
                return true;
            }

            @Override
            public int login(String user, String password) {
                return 0;
            }

            @Override
            public int passiveModePort() {
                return 4321;
            }

            @Override
            public boolean checkFileVisible(String user, String path) {
                return true;
            }

            @Override
            public boolean checkFileDownload(String user, String path) {
                return true;
            }

            @Override
            public boolean checkFileUpload(String user, String path) {
                return true;
            }

            @Override
            public boolean fileRename(String user, String path) {
                return true;
            }

            @Override
            public boolean createFolder(String user, String path) {
                return true;
            }

            @Override
            public boolean deleteFolder(String user, String path) {
                return true;
            }

        });
        ftpServer.setEncoding("gbk");
        ftpServer.setBasePath("D:/");
        ftpServer.startup();
    }
}