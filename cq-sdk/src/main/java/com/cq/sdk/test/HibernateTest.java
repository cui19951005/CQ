package com.cq.sdk.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.cq.sdk.hibernate.LoginAccountEntity;
import com.cq.sdk.potential.annotation.Property;
import com.cq.sdk.potential.sql.frame.hibernate.HibernateTrusteeship;
import com.cq.sdk.potential.sql.tx.utils.TransactionMethod;
import com.cq.sdk.potential.annotation.Autowired;
import com.cq.sdk.potential.sql.tx.TransactionManager;
import org.hibernate.cfg.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by admin on 2016/9/7.
 */
public class HibernateTest {//implements HibernateTrusteeship {
    @Property("jdbc.")
    @Autowired
    DruidDataSource druidDataSource;
    
    public DataSource dataSource() {
        this.druidDataSource.setDefaultAutoCommit(false);
        return this.druidDataSource;
    }

    
    public TransactionManager transactionManager() {
        TransactionManager transactionManager=new TransactionManager();
        transactionManager.setPackName("com.cq.sdk.test.*");
        List<TransactionMethod> transactionMethodList=new ArrayList<>();
        TransactionMethod transactionMethod=new TransactionMethod();
        transactionMethod.setName("*");
        transactionMethodList.add(transactionMethod);
        transactionManager.setTransactionMethodList(transactionMethodList);
        return transactionManager;
    }

    
    public Object configuration(Object configuration, DataSource dataSource) {
        Configuration c= (Configuration) configuration;
        c.configure(new File(LoginAccountEntity.class.getResource("/hibernate.cfg.xml").getFile()));
        return c;
        //return configuration;
    }

    
    public Properties properties(Properties properties) {
        properties.put("hibernate.show_sql","true");
        return properties;
    }

    
    public String mapping() {
        return "com.cq.sdk.hibernate.*";
    }
}
