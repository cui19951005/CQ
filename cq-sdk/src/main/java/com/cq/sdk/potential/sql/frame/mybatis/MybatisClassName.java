package com.cq.sdk.potential.sql.frame.mybatis;

/**
 * Created by admin on 2016/9/13.
 */
public enum MybatisClassName {
    Configuration("org.apache.ibatis.session.Configuration"),
    Environment("org.apache.ibatis.mapping.Environment"),
    TransactionFactory("org.apache.ibatis.transaction.TransactionFactory"),
    JdbcTransactionFactory("org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory"),
    XMLMapperBuilder("org.apache.ibatis.builder.xml.XMLMapperBuilder"),
    SqlSessionFactoryBuilder("org.apache.ibatis.session.SqlSessionFactoryBuilder");
    private String className;
    MybatisClassName(String className){
        this.className=className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
