package com.cq.sdk.service.potential.sql.tx;

/**
 * 事件
 * Created by admin on 2016/9/7.
 */
public interface Transaction {
    /**
     * 事物开始
     */
    void begin();

    /**
     * 提交
     */
    void commit();

    /**
     * 回滚
     */
    void rollback();
}
