package com.cq.sdk.service.potential;

import com.cq.sdk.service.potential.utils.AopClass;
import com.cq.sdk.service.potential.utils.ClassObj;
import com.cq.sdk.service.utils.Logger;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by CuiYaLei on 2016/9/4.
 */
public class InvocationHandlerImpl implements MethodInterceptor {
    private Object object;
    private List<AopClass> aopClassList;

    public InvocationHandlerImpl(List<AopClass> aopClassList) {
        this.aopClassList = aopClassList;
    }

    public Object bind(Object obj){
        if(obj.getClass().getModifiers()>=16){
            return obj;//final类
        }
        this.object=obj;
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(obj.getClass());
        enhancer.setCallback(this);
        enhancer.setClassLoader(obj.getClass().getClassLoader());
        return enhancer.create();
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object object=null;
        try {

            object= method.invoke(this.object,objects);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return object;
    }
}
