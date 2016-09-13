package com.cq.sdk.potential.sql.frame;

import com.cq.sdk.potential.sql.tx.TransactionManager;

import javax.sql.DataSource;

/** Mybatis托管
 * Created by admin on 2016/9/2.
 */
public interface BaseTrusteeship {
    /**
     * create dataSource
     * @return DataSource
     */
    DataSource dataSource();

    /**
     * create TransactionFactory
     * @return
     */
    TransactionManager transactionManager();

    /**
     * create configuration
     * @param dataSource
     * @return
     */
    Object configuration(Object configuration, DataSource dataSource);

}
