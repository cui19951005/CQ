package com.cq.sdk.test;

import com.cq.sdk.android.qq.UserService;
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
import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Logger;
import com.cq.sdk.utils.Number;
import org.hibernate.*;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.android.qq.*",injectionType = InjectionType.Annotation)
@LoadProperties({"test.properties"})
@NetAddress(port=2020,value = "localhost:2021")
public class Main {
    @Autowired
    Main main;
    @Autowired
    static SessionFactory sessionFactory;
    @Autowired
    LoginaccountMapper loginaccountMapper;
    @Autowired
    UserService userService;
    public static void main(String[] args) throws Exception {
        NetObject netObject=new NetObject(2021,"localhost",2020);
        netObject.messageHandle(null);
        new Trusteeship(Main.class);
    }
    @Execute
    public void main(){
        Logger.info(this.userService.login("111111111","211111111"));
    }
}