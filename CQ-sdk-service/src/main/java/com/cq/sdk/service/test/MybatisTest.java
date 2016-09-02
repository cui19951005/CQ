package com.cq.sdk.service.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Property;
import com.cq.sdk.service.potential.mybatis.MybatisTrusteeship;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

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

    public Object sqlSessionFactory(DataSource dataSource) {//XMLConfigBuilder范例
        Configuration configuration=new Configuration();
        TransactionFactory transactionFactory=new JdbcTransactionFactory();//事物Configuration构造方法
        Environment environment=new Environment("jdbc",transactionFactory,dataSource);
        configuration.setEnvironment(environment);
        configuration.addMappers("com.cq.sdk.service.test.dao");
        return new SqlSessionFactoryBuilder().build(configuration);
    }

    public Object sqlSession(Object sqlSessionFactory) {
        return ((SqlSessionFactory)sqlSessionFactory).openSession(true);
    }

    public String mapperLocation() {
        return "com.cq.sdk.service.test.dao.*";
    }
}
