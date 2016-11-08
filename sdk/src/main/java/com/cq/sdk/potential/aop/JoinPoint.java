package com.cq.sdk.potential.aop;

import java.lang.reflect.Method;

/**
 * Created by admin on 2016/9/6.
 */
public interface JoinPoint {
    Object getThis();
    Method getMethod();
    Object[] getArgs();
}
