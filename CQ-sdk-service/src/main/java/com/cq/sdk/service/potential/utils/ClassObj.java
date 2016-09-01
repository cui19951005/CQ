package com.cq.sdk.service.potential.utils;

/**
 * Created by admin on 2016/9/1.
 */
public class ClassObj {
    private Class clazz;
    private Object object;

    public ClassObj(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
