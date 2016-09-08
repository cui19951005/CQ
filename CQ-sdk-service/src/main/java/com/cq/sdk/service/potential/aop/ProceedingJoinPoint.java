package com.cq.sdk.service.potential.aop;


/**
 * Created by admin on 2016/9/6.
 */
public interface ProceedingJoinPoint extends JoinPoint {
    Object proceed();
    Object proceed(Object[] args);
}
