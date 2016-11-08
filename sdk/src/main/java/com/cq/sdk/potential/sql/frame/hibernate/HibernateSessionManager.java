package com.cq.sdk.potential.sql.frame.hibernate;

import com.cq.sdk.potential.SynchronizationManager;
import com.cq.sdk.potential.utils.SynchronizationType;
import com.cq.sdk.utils.Logger;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.Proxy;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by admin on 2016/9/12.
 */
public class HibernateSessionManager implements InvocationHandler {

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Class[] params=new Class[objects.length];
        for(int i=0;i<objects.length;i++){
            params[i]=objects[i].getClass();
        }
       return this.getClass().getMethod(method.getName(),params).invoke(this,objects);
    }
    public Object currentSession() {
        return SynchronizationManager.get(SynchronizationType.HibernateSession);
    }


}
