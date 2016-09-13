package com.cq.sdk.test;

import com.cq.sdk.android.qq.UserService;
import com.cq.sdk.hibernate.LoginAccountEntity;
import com.cq.sdk.potential.Trusteeship;
import com.cq.sdk.potential.annotation.Autowired;
import com.cq.sdk.potential.annotation.Entrance;
import com.cq.sdk.potential.annotation.Execute;
import com.cq.sdk.potential.annotation.LoadProperties;
import com.cq.sdk.potential.sql.frame.hibernate.HibernateSessionManager;
import com.cq.sdk.potential.utils.InjectionType;
import com.cq.sdk.test.dao.LoginaccountMapper;
import com.cq.sdk.utils.Logger;
import org.hibernate.*;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.android.qq.*",injectionType = InjectionType.Annotation)
@LoadProperties({"test.properties"})
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
        new Trusteeship(Main.class);
        /*List list=new ArrayList();
        Configuration configuration=new  ConfigurationParser(list).parseConfiguration(new File(Main.class.getResource("/mybatisGeneratorConfig.xml").getFile()));
        MyBatisGenerator myBatisGenerator=new MyBatisGenerator(configuration,null,list);
        myBatisGenerator.generate(null);*/
    }
    @Execute
    public void main(){
       /* Session session=this.sessionFactory.getCurrentSession();
        LoginAccountEntity loginAccountEntity=session.get(LoginAccountEntity.class,1L);
        loginAccountEntity.setLockStatus("0");
        session.save(loginAccountEntity);
        Logger.info(loginaccountMapper.selectByPrimaryKey(1L));*/
       this.userService.login("2534549160","sj17839969220");
    }
}