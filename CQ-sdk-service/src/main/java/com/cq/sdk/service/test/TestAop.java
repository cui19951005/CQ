package com.cq.sdk.service.test;

import com.cq.sdk.service.potential.annotation.*;
import com.cq.sdk.service.potential.aop.JoinPoint;
import com.cq.sdk.service.potential.aop.ProceedingJoinPoint;
import com.cq.sdk.service.utils.Logger;

import java.lang.reflect.InvocationTargetException;

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
    @AfterThrowing(value = "pointcut()")
    public void afterThrowing(Exception ex){
        ex.printStackTrace();
        Logger.error("异常通知",ex);
    }
   // @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws InvocationTargetException, IllegalAccessException {
           return proceedingJoinPoint.proceed();
    }
    @AfterReturning
    public void afterReturning(JoinPoint joinPoint){
        Logger.info("后置通知");
    }
    @After
    public void after(JoinPoint joinPoint){
        Logger.info("最终通知");
    }
}
