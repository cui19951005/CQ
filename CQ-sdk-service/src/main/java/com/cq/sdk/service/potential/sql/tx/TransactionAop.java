package com.cq.sdk.service.potential.sql.tx;
import com.cq.sdk.service.potential.annotation.Around;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Component;
import com.cq.sdk.service.potential.aop.ProceedingJoinPoint;
import com.cq.sdk.service.utils.Logger;

/**
 * 事件切面
 * Created by admin on 2016/9/8.
 */
@Component
public class TransactionAop {
    @Autowired
    TransactionManager transactionManager;
    public void pointcut(){

    }
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint){
        Logger.info("事件前");
        Object object=null;
        this.transactionManager.getTransaction().begin();
        try {
            object= proceedingJoinPoint.proceed();
            this.transactionManager.getTransaction().commit();
        }catch (Exception ex){
            ex.printStackTrace();
            this.transactionManager.getTransaction().rollback();
        }
        Logger.info("事件后");
        return object;
    }
}
