package com.cq.sdk.test;

import com.cq.sdk.potential.annotation.*;
import com.cq.sdk.potential.aop.JoinPoint;
import com.cq.sdk.potential.aop.ProceedingJoinPoint;
import com.cq.sdk.utils.Logger;

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
    public void before(JoinPoint joinPoint){
        Logger.info("前置通知:"+joinPoint.getMethod().getName());
    }
    @AfterThrowing(value = "pointcut()")
    public void afterThrowing(Exception ex){
        ex.printStackTrace();
        Logger.error("异常通知",ex);
    }
    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws InvocationTargetException, IllegalAccessException {
        Logger.info("环绕前");
           Object object= proceedingJoinPoint.proceed();
        Logger.info("环绕后");
        return object;
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
