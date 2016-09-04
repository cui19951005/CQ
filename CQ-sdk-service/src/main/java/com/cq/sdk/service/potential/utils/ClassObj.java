package com.cq.sdk.service.potential.utils;

/**
 * Created by admin on 2016/9/1.
 */
public class ClassObj {
    private Class clazz;
    private Object object;
    private Object invocationObj;
    public ClassObj(Class clazz) {
        this.clazz = clazz;
    }
    public ClassObj(Object object){
        this.clazz=object.getClass();
        this.object=object;
    }
    public ClassObj(Object object,Object invocationObj){
        this.clazz=object.getClass();
        this.object=object;
        this.invocationObj=invocationObj;
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

    public Object getInvocationObj() {
        return invocationObj;
    }

    public void setInvocationObj(Object invocationObj) {
        this.invocationObj = invocationObj;
    }
}
