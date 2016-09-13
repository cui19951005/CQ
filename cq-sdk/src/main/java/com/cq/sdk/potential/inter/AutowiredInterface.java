package com.cq.sdk.potential.inter;

import com.cq.sdk.potential.utils.ClassObj;
import com.cq.sdk.potential.sql.tx.TransactionManager;

import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
public interface AutowiredInterface  {
    TransactionManager transactionManager();
    List<ClassObj> beanList();
}
