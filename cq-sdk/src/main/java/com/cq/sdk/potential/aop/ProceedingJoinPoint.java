package com.cq.sdk.potential.aop;


import java.lang.reflect.InvocationTargetException;

/**
 * Created by admin on 2016/9/6.
 */
public interface ProceedingJoinPoint extends JoinPoint {
    Object proceed() throws InvocationTargetException, IllegalAccessException;
    Object proceed(Object[] args) throws InvocationTargetException, IllegalAccessException;
}
