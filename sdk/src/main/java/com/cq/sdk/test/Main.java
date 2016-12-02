package com.cq.sdk.test;
import com.cq.sdk.android.qq.UserService;
import com.cq.sdk.potential.annotation.*;
import com.cq.sdk.potential.utils.InjectionType;
import com.cq.sdk.potential.validate.Validator;
import com.cq.sdk.utils.*;
import com.cq.sdk.utils.Date;
import java.text.SimpleDateFormat;
import java.util.*;


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
    private static String dateToString(java.util.Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
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
        File file=new File("D:\\data\\teach-edu-admin\\app");
        Logger.info(file.getParentFile());
    }
}