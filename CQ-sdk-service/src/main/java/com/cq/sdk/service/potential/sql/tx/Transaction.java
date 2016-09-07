package com.cq.sdk.service.potential.sql.tx;

/**
 * Created by admin on 2016/9/7.
 */
public interface Transaction {
    void begin();
    void commit();
    void rollback();
}
