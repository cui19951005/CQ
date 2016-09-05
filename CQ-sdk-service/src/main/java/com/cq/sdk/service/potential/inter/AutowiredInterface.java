package com.cq.sdk.service.potential.inter;

import com.cq.sdk.service.potential.sql.TransactionManager;
import com.cq.sdk.service.potential.utils.ClassObj;

import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
public interface AutowiredInterface  {
    TransactionManager transactionManager();
    List<ClassObj> beanList();
}
