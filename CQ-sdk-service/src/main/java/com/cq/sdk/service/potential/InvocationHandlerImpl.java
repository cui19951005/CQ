package com.cq.sdk.service.potential;

import com.cq.sdk.service.potential.aop.JoinPoint;
import com.cq.sdk.service.potential.aop.ProceedingJoinPoint;
import com.cq.sdk.service.potential.sql.tx.Transaction;
import com.cq.sdk.service.potential.sql.tx.TransactionManager;
import com.cq.sdk.service.potential.utils.AopClass;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.*;
import java.sql.Connection;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by CuiYaLei on 2016/9/4.
 */
public class InvocationHandlerImpl implements MethodInterceptor {
    private Object object;
    private List<AopClass> aopClassList;
    private TransactionManager transactionManager;
    public InvocationHandlerImpl(List<AopClass> aopClassList,TransactionManager transactionManager) {
        this.aopClassList = aopClassList;
        this.transactionManager=transactionManager;
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
        Object object=enhancer.create();
        return object;
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object object=null;
        AopClass aopClass=null;
        Transaction transaction=null;
        try {
            StringBuilder sb=new StringBuilder();
            sb.append(Modifier.toString(method.getModifiers()));
            sb.append(" ");
            sb.append(this.object.getClass().getName());
            sb.append(".");
            sb.append(method.getName());
            sb.append("(");
            for(int i=0;i<objects.length;i++){
                sb.append(objects[i].getClass().getName());
                if(i+1!=objects.length){
                    sb.append(",");
                }
            }
            sb.append(")");
            String methodName=sb.toString();
            if(this.transactionManager!=null) {
                Matcher matcher = this.transactionManager.getPackPattern().matcher(methodName);
                if (matcher.find()) {
                    transaction=this.transactionManager.getTransaction();
                    transaction.begin();
                }
            }
            aopClass=this.exists(sb.toString());
            if(aopClass!=null){
                if(aopClass.getBefore()!=null){
                    aopClass.getBefore().getMethod().invoke(aopClass.getBefore().getObject(),this.createParams(aopClass.getBefore().getMethod(),method,objects));
                }
                if(aopClass.getRound()!=null){
                    object=aopClass.getRound().getMethod().invoke(aopClass.getRound().getObject(),this.createParams(aopClass.getRound().getMethod(),method,objects));//参数暂时不写
                }else{
                    object= method.invoke(this.object,objects);
                }
            }else{
                object= method.invoke(this.object,objects);
            }
            if(aopClass!=null && aopClass.getReturning()!=null){
                aopClass.getReturning().getMethod().invoke(aopClass.getObject(),this.createParams(aopClass.getReturning().getMethod(),method,objects));
            }
            if(transaction!=null){
                transaction.commit();
            }
        }catch (Exception ex){
            if(aopClass!=null && aopClass.getThrowing()!=null) {
                Object[] params = new Object[aopClass.getThrowing().getMethod().getParameters().length];
                for (int i = 0; i < aopClass.getThrowing().getMethod().getParameterTypes().length; i++) {
                    if(aopClass.getThrowing().getMethod().getParameterTypes()[i].isAssignableFrom(ex.getClass())) {
                        params[i] = ex;
                    }else if(aopClass.getThrowing().getMethod().getParameterTypes()[i].isAssignableFrom(ProceedingJoinPoint.class)){
                        params[i]=new JoinPoint() {
                            @Override
                            public Object getThis() {
                                return InvocationHandlerImpl.this.object;
                            }

                            @Override
                            public Method getMethod() {
                                return method;
                            }

                            @Override
                            public Object[] getArgs() {
                                return objects;
                            }
                        };
                    }
                }
                aopClass.getThrowing().getMethod().invoke(aopClass.getThrowing().getObject(), params);
            }
            if(transaction!=null){
                transaction.rollback();
            }
        }finally {
            if(aopClass!=null && aopClass.getAfter()!=null){
                aopClass.getAfter().getMethod().invoke(aopClass.getAfter().getObject(),this.createParams(aopClass.getAfter().getMethod(),method,objects));
            }
        }
        return object;
    }
    private AopClass exists(String name){
        for(AopClass aopClass : this.aopClassList){
            if(aopClass.getPointcut().matcher(name).find()){
                return aopClass;
            }
        }
        return null;
    }
    private Object[] createParams(Method method,Method paramsMethod,Object[] objects){
        return this.createParams(new Object[method.getParameters().length],method,paramsMethod,objects);
    }
    private Object[] createParams(Object[] params,Method method,Method paramsMethod,Object[] objects){
        for(int i=0;i<params.length;i++){
            if(method.getParameters()[i].getType().isAssignableFrom(JoinPoint.class)){
                params[i]=new JoinPoint() {
                    @Override
                    public Object getThis() {
                        return InvocationHandlerImpl.this.object;
                    }

                    @Override
                    public Method getMethod() {
                        return paramsMethod;
                    }

                    @Override
                    public Object[] getArgs() {
                        return objects;
                    }
                };
            }else if(method.getParameters()[i].getType().isAssignableFrom(ProceedingJoinPoint.class)){
                params[i]=new ProceedingJoinPoint() {
                    @Override
                    public Object proceed() throws InvocationTargetException, IllegalAccessException {
                        return paramsMethod.invoke(InvocationHandlerImpl.this.object,objects);
                    }

                    @Override
                    public Object proceed(Object[] args) throws InvocationTargetException, IllegalAccessException {
                        return paramsMethod.invoke(InvocationHandlerImpl.this.object,args);
                    }

                    @Override
                    public Object getThis() {
                        return InvocationHandlerImpl.this.object;
                    }

                    @Override
                    public Method getMethod() {
                        return paramsMethod;
                    }

                    @Override
                    public Object[] getArgs() {
                        return objects;
                    }
                };
            }
        }
        return params;
    }
}
