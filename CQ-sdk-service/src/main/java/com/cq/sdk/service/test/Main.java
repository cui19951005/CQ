package com.cq.sdk.service.test;

import com.cq.sdk.service.potential.Trusteeship;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Entrance;
import com.cq.sdk.service.potential.annotation.Execute;
import com.cq.sdk.service.potential.annotation.LoadProperties;
import com.cq.sdk.service.potential.utils.InjectionType;
import com.cq.sdk.service.utils.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.cq.sdk.service.hibernate.LoginAccountEntity;
import org.hibernate.jdbc.Work;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.service.test.*",injectionType = InjectionType.Annotation)
@LoadProperties({"test.properties"})
public class Main {
    @Autowired
    Main main;
    @Autowired
    SessionFactory sessionFactory;
    public static ThreadLocal<List<StringBuilder>> sb=new ThreadLocal<>();
    public static List<StringBuilder> list=new ArrayList<>();
    static long time=0;
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        time=System.currentTimeMillis();
        new Trusteeship(Main.class);
    }
    @Execute
    public void main(){
       /* List list=new ArrayList();
        org.frame.generator.config.Configuration configuration= new ConfigurationParser(list).parseConfiguration(new File(Thread.currentThread().getClass().getResource("/mybatisGeneratorConfig.xml").getFile()));
        MyBatisGenerator myBatisGenerator=new MyBatisGenerator(configuration,null,list);
        myBatisGenerator.generate(null);*/
        /*Configuration configuration=new Configuration().configure(new File(LoginAccountEntity.class.getResource("/hibernate.cfg.xml").getFile()));
        SessionFactory sessionFactory=configuration.buildSessionFactory();
        Session session=sessionFactory.openSession();
        Properties properties=new Properties();
        properties.put("datasource","my is test datasource");
        configuration.addProperties(properties);*/
        Logger.info(sessionFactory.openSession().get(LoginAccountEntity.class,2L).getAccountName());
       /* ConnectionProvider connectionProvider= (ConnectionProvider) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[]{ConnectionProvider.class},new InvocationHandlerClass());
        Logger.info(connectionProvider);*/
    }
}