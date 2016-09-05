package com.cq.sdk.service.potential;

import com.cq.sdk.service.potential.annotation.AfterThrowing;
import com.cq.sdk.service.potential.utils.AopClass;
import com.cq.sdk.service.potential.utils.ClassObj;
import com.cq.sdk.service.utils.Logger;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.*;
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
        Object object=enhancer.create();
        return object;
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object object=null;
        AopClass aopClass=null;
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
            aopClass=this.exists(sb.toString());
            if(aopClass!=null){
                if(aopClass.getBefore()!=null){
                    aopClass.getBefore().getMethod().invoke(aopClass.getBefore().getObject());
                }
                if(aopClass.getRound()!=null){
                    object=aopClass.getRound().getMethod().invoke(aopClass.getRound().getObject());//参数暂时不写
                }else{
                    object= method.invoke(this.object,objects);
                }
            }else{
                object= method.invoke(this.object,objects);
            }
            if(aopClass!=null && aopClass.getReturning()!=null){
                aopClass.getReturning().getMethod().invoke(aopClass.getObject());
            }
        }catch (Exception ex){
            if(aopClass!=null && aopClass.getThrowing()!=null) {
                AfterThrowing afterThrowing = (AfterThrowing) aopClass.getThrowing().getValue();
                if (afterThrowing.throwing() != null) {
                    Object[] params = new Object[aopClass.getThrowing().getMethod().getParameters().length];
                    for (int i = 0; i < aopClass.getThrowing().getMethod().getParameters().length; i++) {
                        if (aopClass.getThrowing().getMethod().getParameters()[i].getName().equals(afterThrowing.throwing())) {
                            params[i] = ex;
                            aopClass.getThrowing().getMethod().invoke(aopClass.getThrowing().getObject(), params);
                        }
                    }
                } else {
                    aopClass.getThrowing().getMethod().invoke(aopClass.getThrowing());
                }
            }
        }finally {
            if(aopClass!=null && aopClass.getAfter()!=null){
                aopClass.getAfter().getMethod().invoke(aopClass.getAfter().getObject());
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
}
