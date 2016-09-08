package com.cq.sdk.service.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Property;
import com.cq.sdk.service.potential.sql.tx.TransactionManager;
import com.cq.sdk.service.potential.sql.utils.TransactionMethod;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
public class MybatisTest {//implements MybatisTrusteeship {
    @Autowired
    @Property("jdbc.")
    DruidDataSource druidDataSource;
    public DataSource dataSource() {
        druidDataSource.setDefaultAutoCommit(false);
        return druidDataSource;
    }

    public TransactionManager transactionManager() {
        TransactionManager transactionManager=new TransactionManager();
        transactionManager.setPackName("com.cq.sdk.service.test.*");
        List<TransactionMethod> transactionMethodList=new ArrayList<>();
        TransactionMethod transactionMethod=new TransactionMethod();
        transactionMethod.setName("update*");
        transactionMethodList.add(transactionMethod);
        transactionManager.setTransactionMethodList(transactionMethodList);
        return transactionManager;
    }

    public Object configuration(Object configuration, DataSource dataSource) {
        return configuration;
    }

    public String mappers() {
        return "classpath*:mapper/*.xml";
    }

    public String mapperLocation() {
        return "com.cq.sdk.service.test.dao.entity.*";
    }
}
