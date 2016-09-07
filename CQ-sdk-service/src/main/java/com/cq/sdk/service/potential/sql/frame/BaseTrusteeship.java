package com.cq.sdk.service.potential.sql.frame;

import com.cq.sdk.service.potential.sql.tx.TransactionManager;

import javax.sql.DataSource;

/** Mybatis托管
 * Created by admin on 2016/9/2.
 */
public interface BaseTrusteeship {
    /**
     * 创建dataSource
     * @return DataSource
     */
    DataSource dataSource();

    /**
     * 创建TransactionFactory
     * @return
     */
    TransactionManager transactionManager();

}
