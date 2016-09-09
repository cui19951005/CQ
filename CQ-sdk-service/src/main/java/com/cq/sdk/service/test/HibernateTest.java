package com.cq.sdk.service.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.cq.sdk.service.hibernate.LoginAccountEntity;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Property;
import com.cq.sdk.service.potential.sql.frame.HibernateTrusteeship;
import com.cq.sdk.service.potential.sql.tx.TransactionManager;
import com.cq.sdk.service.potential.sql.utils.TransactionMethod;
import org.hibernate.cfg.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by admin on 2016/9/7.
 */
public class HibernateTest implements HibernateTrusteeship {
    @Property("jdbc.")
    @Autowired
    DruidDataSource druidDataSource;
    @Override
    public DataSource dataSource() {
        this.druidDataSource.setDefaultAutoCommit(false);
        return this.druidDataSource;
    }

    @Override
    public TransactionManager transactionManager() {
        TransactionManager transactionManager=new TransactionManager();
        transactionManager.setPackName("com.cq.sdk.service.test.*");
        List<TransactionMethod> transactionMethodList=new ArrayList<>();
        TransactionMethod transactionMethod=new TransactionMethod();
        transactionMethod.setName("*");
        transactionMethodList.add(transactionMethod);
        transactionManager.setTransactionMethodList(transactionMethodList);
        return transactionManager;
    }

    @Override
    public Object configuration(Object configuration, DataSource dataSource) {
        /*Configuration c= (Configuration) configuration;
        c.configure(new File(LoginAccountEntity.class.getResource("/hibernate.cfg.xml").getFile()));
        return c;*/
        return configuration;
    }

    @Override
    public Properties properties(Properties properties) {
        return properties;
    }

    @Override
    public String mapping() {
        return "com.cq.sdk.service.hibernate.*";
    }
}
