package com.cq.sdk.test;
import com.cq.sdk.android.qq.UserService;
import com.cq.sdk.android.qq.impl.UserServiceImpl;
import com.cq.sdk.net.ftp.FtpServer;
import com.cq.sdk.net.ftp.FtpUser;
import com.cq.sdk.potential.Trusteeship;
import com.cq.sdk.potential.annotation.*;
import com.cq.sdk.potential.utils.InjectionType;


/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.android.qq.*",injectionType = InjectionType.Annotation)
@LoadProperties({"jdbc.properties"})
//@NetAddress(port=2020,value = "localhost:2021")
public class Main {
    @Property("jdbc.password")
    public Integer value=1;
    @Autowired
    private UserService userService;

    public static void main(String[] args) throws Exception {
        /*UserService userService=trusteeship.get(UserService.class);
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
        frame.setResizable(true);
        frame.setGame(new Game(
                 new Size(20,20)
                ,new Point((frame.getWidth()-frame.getContentPane().getWidth())/2,frame.getHeight()-frame.getContentPane().getHeight())
                ,new Rectangle(new Point(),frame.getContentPane().getSize())
        ));
        frame.getGame().start((id)->{
            frame.repaint();
            frame.setTitle("积分:"+frame.getGame().getIntegral());
        },500);*/
    }
}