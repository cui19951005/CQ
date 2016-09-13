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

    /**
     * 动态创建类
     */
    static {
        try {
            String clazz = HibernateProxyImpl.class.getName();
            StringBuilder sb=new StringBuilder();
            Class session = Class.forName(HibernateClassName.SessionFactoryImplementor.getClassName());
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.get(clazz);
            classPool.clearImportedPackages();
            CtField ctField=new CtField(classPool.get("java.lang.Object"),"sessionFactoryImplementor",ctClass);
            ctClass.addField(ctField);
            CtConstructor ctConstructor = new CtConstructor(new CtClass[]{classPool.get(session.getName())}, ctClass);
            sb.append("{this.sessionFactoryImplementor=$1;}");
            ctConstructor.setBody(sb.toString());
            ctClass.addConstructor(ctConstructor);
            ctClass.setName(HibernateProxyImpl.HibernateProxyImpl);
            ctClass.toClass();
        }catch (Exception ex){
            ex.printStackTrace();
            Logger.error("HibernateProxy init fail",ex);
        }
    }
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Class[] params=new Class[objects.length];
        for(int i=0;i<objects.length;i++){
            params[i]=objects[i].getClass();
        }
       return HibernateSessionManager.class.getMethod(method.getName(),params).invoke(this,objects);
    }
    public Object currentSession() {
        return SynchronizationManager.get(SynchronizationType.HibernateSession);
    }


}
