package com.cq.sdk.test;

import com.cq.sdk.android.qq.CommonService;
import com.cq.sdk.android.qq.UserService;
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
import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Json;
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
        trusteeship.get(UserService.class).login("2534549160","sj17839969220");
      //trusteeship.get(CommonService.class).receive(new ByteSet("{0,0,3,240,0,0,0,8,2,0,0,0,0,14,50,53,51,52,53,52,57,49,54,48,23,143,229,99,103,87,252,91,151,230,60,167,53,98,157,14,206,255,209,231,207,241,90,0,131,194,45,90,122,83,56,180,24,60,167,198,78,146,47,171,74,20,27,113,238,195,0,22,0,5,32,148,0,209,165,30,204,75,233,132,227,134,219,202,1,91,156,18,176,151,43,120,47,111,35,232,172,40,65,20,171,184,186,187,61,210,23,157,212,161,18,229,155,214,78,177,107,131,74,90,52,164,213,234,33,197,167,170,212,180,35,1,28,105,12,136,177,255,18,239,103,79,7,202,65,63,144,155,140,240,208,104,177,177,100,161,160,238,10,36,199,26,102,129,207,37,23,86,53,165,247,111,170,154,89,69,72,34,50,247,23,84,134,111,87,24,162,204,54,226,255,130,17,220,223,4,137,146,227,244,83,72,41,229,36,113,210,12,237,16,123,249,39,77,59,139,207,53,91,254,240,181,83,83,9,200,89,225,95,195,202,225,139,113,178,74,92,153,147,174,205,172,69,42,39,22,95,183,180,209,27,208,121,143,73,209,133,77,191,52,177,93,138,217,121,107,95,100,175,201,177,243,23,238,127,83,218,238,201,130,54,182,8,45,231,145,180,3,210,78,142,59,227,65,207,229,5,96,67,205,232,44,204,72,53,147,236,19,193,90,117,220,125,122,175,252,213,0,5,187,147,224,134,99,221,15,176,202,75,175,54,134,7,147,112,4,155,29,241,18,157,96,114,127,165,60,216,52,166,186,177,203,158,23,213,162,96,247,252,54,134,235,4,136,200,137,132,125,83,153,221,101,6,59,32,168,137,14,150,228,186,76,36,170,227,38,152,79,113,95,229,64,79,249,90,177,244,183,94,172,139,244,22,134,143,14,190,96,58,206,107,112,193,207,24,154,252,95,111,73,218,114,91,144,59,93,169,224,119,251,24,243,74,59,71,9,240,230,251,73,54,92,103,132,224,130,142,219,171,213,47,100,75,202,162,86,4,194,238,11,163,58,113,200,96,131,124,244,115,165,190,50,76,241,208,40,242,160,154,144,20,92,50,49,49,169,174,230,146,72,29,183,33,81,160,11,152,112,77,25,107,214,67,138,173,254,225,174,213,8,168,122,96,239,59,21,92,193,147,167,91,74,221,17,216,147,172,189,241,67,96,169,225,171,131,198,172,38,37,246,180,159,17,253,109,121,183,28,90,197,39,244,149,150,105,63,107,87,82,131,2,200,217,106,23,171,65,172,214,42,27,63,100,150,3,80,182,194,215,163,240,71,65,227,68,198,180,17,220,178,235,228,200,255,47,87,25,160,172,203,0,183,39,235,189,145,11,51,151,62,38,134,226,84,117,178,244,45,2,154,102,129,92,36,18,214,50,5,164,111,66,241,40,18,121,174,246,56,10,66,19,68,49,144,192,37,49,180,139,112,191,43,236,237,179,76,223,169,132,11,76,194,111,163,52,115,217,44,151,225,30,198,243,44,237,88,7,142,110,126,49,225,150,69,119,21,30,46,197,17,19,100,69,242,61,208,171,15,17,152,43,62,94,248,17,228,36,194,233,160,244,61,159,181,181,198,152,140,219,49,175,2,99,28,241,119,242,21,94,65,200,202,141,19,190,128,145,64,232,19,232,36,235,39,79,206,101,112,74,58,239,59,44,168,1,160,167,222,182,114,154,240,206,48,79,34,136,173,66,82,247,24,254,242,183,102,232,183,248,180,144,231,92,16,215,69,20,219,46,221,122,168,53,206,221,230,163,22,10,214,160,117,8,235,152,59,216,212,15,15,81,180,147,72,241,164,229,253,190,47,8,255,162,69,86,169,45,6,18,77,3,119,227,154,237,254,146,151,74,2,84,50,208,56,240,224,62,233,112,224,8,245,255,234,190,60,52,61,144,191,18,46,175,65,182,16,101,137,64,78,196,51,227,199,100,22,178,156,250,164,123,194,112,2,213,118,153,156,139,43,200,162,63,248,154,181,96,189,16,176,252,86,10,146,10,115,14,5,86,242,206,187,177,172,53,82,27,225,247,203,147,62,90,252,176,204,187,182,48,179,150,173,236,109,236,165,96,39,101,136,80,192,206,105,247,224,8,238,83,214,76,72,138,246,51,204,50,130,223,195,138,24,253,192,38,79,143,65,52,23,4,70,57,51,152,180,187,191,242,160,32,219,109,100,35,68,252,187,12,140,110,48,35,221,86,178,39,137,183,122,11,186,162,183}"));

    }
}