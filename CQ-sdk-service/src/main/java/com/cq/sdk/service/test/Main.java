package com.cq.sdk.service.test;

import com.cq.sdk.service.potential.Trusteeship;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Entrance;
import com.cq.sdk.service.potential.annotation.Execute;
import com.cq.sdk.service.potential.annotation.LoadProperties;
import com.cq.sdk.service.potential.utils.InjectionType;
import com.cq.sdk.service.utils.FileUtils;
import com.cq.sdk.service.utils.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.cq.sdk.service.hibernate.LoginAccountEntity;
import org.hibernate.Transaction;
import org.hibernate.boot.cfgxml.spi.MappingReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
       time=System.currentTimeMillis();
        new Trusteeship(Main.class);
        /*Object hibernateConnectionProvider=  Proxy.newProxyInstance(HibernateConnectionProvider.class.getClassLoader(),HibernateConnectionProvider.class.getInterfaces(),new HibernateConnectionProvider());
        Logger.info(hibernateConnectionProvider.getClass().getName());
        Class c=Class.forName(hibernateConnectionProvider.getClass().getName());
        Logger.info(c.newInstance());*/
        //ApplicationContext applicationContext=new ClassPathXmlApplicationContext("/applicationContext.xml");
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
        Session session=sessionFactory.getCurrentSession();
        LoginAccountEntity loginAccountEntity=session.get(LoginAccountEntity.class,16L);
        loginAccountEntity.setAccountName("123456");
        session.save(loginAccountEntity);
       /* ConnectionProvider connectionProvider= (ConnectionProvider) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[]{ConnectionProvider.class},new InvocationHandlerClass());
        Logger.info(connectionProvider);*/
    }
}