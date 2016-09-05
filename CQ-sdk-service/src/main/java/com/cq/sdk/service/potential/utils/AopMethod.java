package com.cq.sdk.service.potential.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by admin on 2016/9/5.
 */
public class AopMethod {
    private Annotation value;
    private Object object;
    private Method method;

    public AopMethod(Annotation value, Object object, Method method) {
        this.value = value;
        this.object = object;
        this.method = method;
    }

    public Annotation getValue() {
        return value;
    }

    public void setValue(Annotation value) {
        this.value = value;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
