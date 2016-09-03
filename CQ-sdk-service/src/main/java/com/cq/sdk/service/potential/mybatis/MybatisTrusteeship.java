package com.cq.sdk.service.potential.mybatis;

import javax.sql.DataSource;

/** Mybatis托管
 * Created by admin on 2016/9/2.
 */
public interface MybatisTrusteeship {
    /**
     * 创建dataSource
     * @return DataSource
     */
    DataSource dataSource();

    /**
     * 创建TransactionFactory
     * @return
     */
    Object transactionFactory();

    /**
     * 创建mapper.xml文件包
     */
    String mappers();
    /**
     * 创建sqlSessionFactory
     * @return SqlSessionFactory
     */
    Object sqlSessionFactory(Object configuration);

    /**
     * 创建sqlSession
     * @param sqlSessionFactory sqlSessionFactory
     * @return SqlSession
     */
    Object sqlSession(Object sqlSessionFactory);

    /**
     * mapper 接口位置
     * @return
     */
    String mapperLocation();

}
