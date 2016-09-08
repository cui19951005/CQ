package com.cq.sdk.service.test;

import com.cq.sdk.service.hibernate.LoginAccountEntity;
import com.cq.sdk.service.potential.sql.frame.HibernateTrusteeship;
import com.cq.sdk.service.potential.sql.tx.TransactionManager;
import com.cq.sdk.service.potential.sql.utils.TransactionMethod;
import org.hibernate.cfg.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/7.
 */
public class HibernateTest implements HibernateTrusteeship {
    @Override
    public DataSource dataSource() {
        return null;
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
        Configuration c= (Configuration) configuration;
        c.configure(new File(LoginAccountEntity.class.getResource("/hibernate.cfg.xml").getFile()));
        return c;
    }
}
