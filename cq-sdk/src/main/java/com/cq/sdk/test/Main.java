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
import java.util.*;
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
        //Trusteeship trusteeship=new Trusteeship(Main.class);
        //trusteeship.add(TestAop.class);
        //trusteeship.get(UserService.class).login("2534549160","sj17839969220");
        QQ qq=Json.fromJson("{\"account\":\"2534549160\",\"qq\":\"2534549160\",\"user\":{\"byteSet\":[-105,18,38,-88]},\"caption\":{\"byteSet\":[50,53,51,52,53,52,57,49,54,48]},\"pass\":\"sj17839969220\",\"md5\":{\"byteSet\":[-71,118,49,-60,0,43,-80,-122,61,114,127,-45,22,48,-74,-102]},\"md52\":{\"byteSet\":[-48,111,-97,12,-67,-118,-120,-54,-22,72,6,7,-25,74,-34,-37]},\"time\":{\"byteSet\":[87,-5,2,-1]},\"key\":{\"byteSet\":[70,52,115,64,110,97,125,88,103,80,59,113,126,112,64,56]},\"nick\":\"勿忘\",\"token002C\":{\"byteSet\":[-103,-112,-110,117,26,79,111,1,-69,-106,-120,-22,93,-105,98,-117,7,-46,-118,86,111,19,-43,-49,26,27,-81,104,-97,-19,110,94,-97,35,-16,78,75,-3,-82,116,10,-88,-69,-16,17,-58,46,-77,65,-24,-51,-84,-35,-63,-102,-49,83,68,24,-126,21,29,111,50]},\"token004C\":{\"byteSet\":[15,-38,33,92,41,-5,38,-12,127,-95,106,-81,87,123,61,66,1,78,73,46,-70,-74,-85,127,-122,113,-22,-38,122,-72,53,-103,103,1,-114,-32,-119,-35,-48,-78,-122,-3,112,127,-103,-40,77,-12,-18,102,69,5,48,17,-120,-123,-106,83,-31,-80,-69,68,-117,-89,27,-82,-67,99,-95,-45,21,-43]},\"token0058\":{\"byteSet\":[41,-24,54,22,-8,-72,-66,62,91,64,-98,-68,4,-82,107,-37,87,40,90,-128,-120,-15,-81,-92,103,99,-124,-63,-125,111,-85,46,109,-22,-108,109,-18,44,25,101,-35,-57,-27,-98,120,91,-100,-124,-92,33,-89,-1,-107,-17,-30,78,-109,-34,-127,55,-45,27,-85,103,-63,-12,122,19,126,96,-99,94,85,-72,16,-105,9,91,107,-126,-70,60,42,94,-80,79,-17,-7]},\"tgtKey\":{\"byteSet\":[40,81,21,92,70,-120,123,20,15,44,58,-40,60,-104,108,30]},\"shareKey\":{\"byteSet\":[-107,124,58,-81,-65,111,-81,29,44,47,25,-91,-22,4,-27,28]},\"pubKey\":{\"byteSet\":[2,36,75,121,-14,35,-105,85,-25,60,115,-1,88,61,78,-59,98,92,25,-65,-128,-107,68,109,-31]},\"ksid\":{\"byteSet\":[]},\"randKey\":{\"byteSet\":[40,81,21,92,70,-120,123,20,15,44,58,-40,60,-104,108,30]},\"mST1Key\":{\"byteSet\":[122,69,40,119,122,64,99,106,64,39,89,118,103,68,71,99]},\"stweb\":\"A2 DE AF 31 F9 B6 02 84 64 61 24 17 28 8C C6 06 EF 52 7E 26 AA EE 73 FB 61 5F 67 70 73 81 D1 E7 58 A9 92 C7 CE DE AA 7E\",\"sKey\":{\"byteSet\":[77,48,117,113,116,82,66,83,118,112]},\"psKey\":{},\"superKey\":{},\"vKey\":{\"byteSet\":[81,102,68,102,69,120,52,111,43,49,107,101,54,57,90,68,73,104,87,88,101,85,97,83,103,70,78,57,70,107,50,107,57,55,49,50,50,54,97,56,48,50,48,49,61,61]},\"sId\":{\"byteSet\":[65,89,85,111,69,112,121,74,72,51,85,48,71,101,99,108,103,79,115,105,67,117,69,102]},\"sessionKey\":{\"byteSet\":[70,52,115,64,110,97,125,88,103,80,59,113,126,112,64,56]},\"loginState\":\"Login\",\"vieryToken1\":{},\"vieryToken2\":{},\"viery\":{}}", QQ.class);
        Logger.info(Json.toJson(qq));
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