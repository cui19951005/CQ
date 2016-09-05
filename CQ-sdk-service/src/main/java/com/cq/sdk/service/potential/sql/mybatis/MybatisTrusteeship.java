package com.cq.sdk.service.potential.sql.mybatis;

import com.cq.sdk.service.potential.sql.TransactionManager;

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
    TransactionManager transactionManager(DataSource dataSource);

    /**
     * 创建mapper.xml文件包
     */
    String mappers();

    /**
     * mapper 接口位置
     * @return
     */
    String mapperLocation();

}
