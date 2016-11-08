package com.cq.sdk.potential.sql.tx;
import com.cq.sdk.potential.annotation.*;

/**
 * 事件切面
 * Created by admin on 2016/9/8.
 */
@Component
public final class TransactionAop {
    @Autowired
    TransactionManager transactionManager;
    public void pointcut(){

    }
    @Before("pointcut()")
    public void before(){
        this.transactionManager.getTransaction().begin();
    }
    @AfterThrowing("pointcut()")
    public void throwing(Exception e){
        this.transactionManager.getTransaction().rollback();
    }
    @AfterReturning("pointcut()")
    public void returning(){
        this.transactionManager.getTransaction().commit();
    }
}
