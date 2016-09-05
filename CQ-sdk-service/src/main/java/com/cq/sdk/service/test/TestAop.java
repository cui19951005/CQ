package com.cq.sdk.service.test;

import com.cq.sdk.service.potential.annotation.*;
import com.cq.sdk.service.utils.Logger;

/**
 * Created by admin on 2016/9/5.
 */
@Aspect
public class TestAop {
    @Pointcut("* *(..)")
    public void pointcut(){
        Logger.info("切入点");
    }
    @Before("pointcut()")
    public void before(){
        Logger.info("前置通知");
    }
    @AfterThrowing(value = "pointcut()",throwing = "ex")
    public void afterThrowing(Exception ex){
        Logger.info("异常通知",ex);
    }
    @AfterReturning
    public void afterReturning(){
        Logger.info("后置通知");
    }
    @After
    public void after(){
        Logger.info("最终通知");
    }
}
