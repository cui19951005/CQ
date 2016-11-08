package com.cq.sdk.potential.sql.frame.hibernate;

import com.cq.sdk.utils.Logger;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * Created by admin on 2016/9/13.
 */
public class HibernateProxy {
    protected InvocationHandler h=new HibernateSessionManager();
    private static final CallbackFilter BAD_OBJECT_METHOD_FILTER = new CallbackFilter() {
        public int accept(Method method) {
            if(method.getDeclaringClass().getName().equals("java.lang.Object")) {
                String name = method.getName();
                if(!name.equals("hashCode") && !name.equals("equals") && !name.equals("toString")) {
                    return 1;
                }
            }

            return 0;
        }
    };

    public HibernateProxy() {
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
        Enhancer.registerCallbacks(this.getClass(), new Callback[]{this.h, null});
    }

    public static InvocationHandler getInvocationHandler(Object proxy) {
        if(!(proxy instanceof HibernateProxyImpl)) {
            throw new IllegalArgumentException("Object is not qq proxy");
        } else {
            return ((HibernateProxy)proxy).h;
        }
    }

    public static Class getProxyClass(ClassLoader loader, Class[] interfaces) {
        try {
            Enhancer e = new Enhancer();
            e.setSuperclass(Class.forName(HibernateProxyImpl.HibernateProxyImpl));
            e.setInterfaces(interfaces);
            e.setCallbackTypes(new Class[]{InvocationHandler.class, NoOp.class});
            e.setCallbackFilter(BAD_OBJECT_METHOD_FILTER);
            e.setUseFactory(false);
            return e.createClass();
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean isProxyClass(Class cl) {
        return cl.getSuperclass().equals(HibernateProxyImpl.class);
    }

    public static Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h) {
        try {
            Class e = getProxyClass(loader, interfaces);
            return e.getConstructor(new Class[]{InvocationHandler.class}).newInstance(new Object[]{h});
        } catch (RuntimeException var4) {
            throw var4;
        } catch (Exception var5) {
            throw new CodeGenerationException(var5);
        }
    }

}
