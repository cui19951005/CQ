package com.cq.sdk.service.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Property;
import com.cq.sdk.service.potential.sql.TransactionManager;
import com.cq.sdk.service.potential.sql.mybatis.MybatisTrusteeship;
import com.cq.sdk.service.potential.sql.utils.TransactionMethod;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
public class MybatisTest implements MybatisTrusteeship {
    @Autowired
    @Property("jdbc.")
    DruidDataSource druidDataSource;
    public DataSource dataSource() {
        return druidDataSource;
    }

    public TransactionManager transactionManager(DataSource dataSource) {
        TransactionManager transactionManager=new TransactionManager();
        transactionManager.setDataSource(dataSource);
        transactionManager.setPackName("com.cq.sdk.service.test.*");
        List<TransactionMethod> transactionMethodList=new ArrayList<>();
        TransactionMethod transactionMethod=new TransactionMethod();
        transactionMethod.setName("update*");
        transactionMethodList.add(transactionMethod);
        transactionManager.setTransactionMethodList(transactionMethodList);
        return transactionManager;
    }

    public String mappers() {
        return "classpath*:mapper/*.xml";
    }

    public String mapperLocation() {
        return "com.cq.sdk.service.test.dao.entity.*";
    }
}
