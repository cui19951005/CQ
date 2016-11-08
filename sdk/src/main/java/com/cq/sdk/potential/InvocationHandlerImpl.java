package com.cq.sdk.potential;

import com.cq.sdk.potential.aop.JoinPoint;
import com.cq.sdk.potential.aop.ProceedingJoinPoint;
import com.cq.sdk.potential.utils.AopClass;
import com.cq.sdk.utils.Logger;
import com.cq.sdk.utils.ObjectUtils;
import javassist.*;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by CuiYaLei on 2016/9/4.
 */
public class InvocationHandlerImpl implements MethodInterceptor {
    private Object object;
    private Collection<AopClass> aopClassList;
    public InvocationHandlerImpl(Collection<AopClass> aopClassList) {
        this.aopClassList = aopClassList;
    }

    public Object bind(Object obj){
        if((obj.getClass().getModifiers() & Modifier.FINAL)==Modifier.FINAL || obj.getClass().getName().indexOf(obj.getClass().getSuperclass().getName())!=-1){
            return obj;//final类或本身
        }
        this.object=obj;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.object.getClass());
        enhancer.setCallback(this);
        enhancer.setClassLoader(this.object.getClass().getClassLoader());
        Object object = enhancer.create();
        Stream.of(this.object.getClass().getDeclaredFields()).filter(f->(f.getModifiers() & Modifier.FINAL)==0 && (f.getModifiers() & Modifier.STATIC)==0).forEach(f->{
            try {
                Field field=object.getClass().getSuperclass().getDeclaredField(f.getName());
                f.setAccessible(true);
                field.setAccessible(true);
                field.set(object,f.get(this.object));
            } catch (NoSuchFieldException e) {
                Logger.error("no find field",e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return object;
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object object=null;
        List<AopClass> aopClassList = null;
        try {
            StringBuilder sb=new StringBuilder();
            sb.append(Modifier.toString(method.getModifiers()));
            sb.append(" ");
            sb.append(this.object.getClass().getName());
            sb.append(".");
            sb.append(method.getName());
            sb.append("(");
            boolean isTrue=false;
            for(Object obj : objects){
                if(obj!=null){
                    sb.append(obj.getClass().getName());
                    sb.append(",");
                    isTrue=true;
                }
            }
            if(isTrue){
                sb=new StringBuilder(sb.substring(0,sb.length()-1));
            }
            sb.append(")");
            aopClassList=this.exists(sb.toString());
            List<AopClass> aroundList=new ArrayList<>();
            for(AopClass aopClass : aopClassList){
                if(aopClass.getBefore()!=null){
                    aopClass.getBefore().getMethod().invoke(aopClass.getBefore().getObject(),this.createParams(this.object,aopClass.getBefore().getMethod(),method,objects));
                }
                if(aopClass.getRound()!=null){
                    aroundList.add(aopClass);
                }
            }
            if(aroundList.size()>0) {
                object=aroundList.get(0).getRound().getMethod().invoke(aroundList.get(0).getRound().getObject(), createAround(aroundList, 0, method, objects));
            }else{
                object= method.invoke(this.object,objects);
            }
            for(AopClass aopClass : aopClassList){
                if(aopClass.getReturning()!=null) {
                    aopClass.getReturning().getMethod().invoke(aopClass.getObject(), this.createParams(this.object, aopClass.getReturning().getMethod(), method, objects));
                }
            }
        }catch (InvocationTargetException e){
            e.getCause().printStackTrace();
            this.exception(method,objects,e);
        }finally {
            for(AopClass aopClass : aopClassList){
                if(aopClass.getAfter()!=null) {
                    aopClass.getAfter().getMethod().invoke(aopClass.getAfter().getObject(), this.createParams(this.object, aopClass.getAfter().getMethod(), method, objects));
                }
            }
        }
        return object;
    }
    private void exception(Method method,Object[] objects,Exception ex) throws InvocationTargetException, IllegalAccessException {
        for(AopClass aopClass : aopClassList) {
            if (aopClass.getThrowing() != null) {
                Object[] params = new Object[aopClass.getThrowing().getMethod().getParameters().length];
                for (int i = 0; i < aopClass.getThrowing().getMethod().getParameterTypes().length; i++) {
                    if (aopClass.getThrowing().getMethod().getParameterTypes()[i].isAssignableFrom(ex.getClass())) {
                        params[i] = ex;
                    } else if (aopClass.getThrowing().getMethod().getParameterTypes()[i].isAssignableFrom(ProceedingJoinPoint.class)) {
                        params[i] = new JoinPoint() {
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
        }
    }
    private List<AopClass> exists(String name){
        List<AopClass> aopClassList= this.aopClassList.stream().filter(aopClass -> aopClass.getPointcut().matcher(name).find()).collect(Collectors.toList());
        return aopClassList;
    }
    private Object[] createParams(Object object,Method method,Method paramsMethod,Object[] objects){
        return this.createParams(object,new Object[method.getParameters().length],method,paramsMethod,objects);
    }
    private Object[] createParams(Object object,Object[] params,Method method,Method paramsMethod,Object[] objects){
        for(int i=0;i<params.length;i++){
            if(method.getParameters()[i].getType().isAssignableFrom(JoinPoint.class)){
                params[i]=new JoinPoint() {
                    @Override
                    public Object getThis() {
                        return object;
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
                        return paramsMethod.invoke(object, objects);
                    }

                    @Override
                    public Object proceed(Object[] args) throws InvocationTargetException, IllegalAccessException {
                        return paramsMethod.invoke(object,args);
                    }

                    @Override
                    public Object getThis() {
                        return object;
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
    private Object[] createAround(List<AopClass> aopClassList,int level,Method method,Object[] params){
        if(level+1==aopClassList.size()){
            return this.createParams(this.object,aopClassList.get(level).getRound().getMethod(),method,params);
        }else {
            return this.createParams(aopClassList.get(level+1).getRound().getObject(), aopClassList.get(level).getRound().getMethod(), aopClassList.get(level + 1).getRound().getMethod(), createAround(aopClassList, ++level,method,params));
        }
    }
}
