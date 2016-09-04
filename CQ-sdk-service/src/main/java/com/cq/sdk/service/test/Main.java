package com.cq.sdk.service.test;

import com.cq.sdk.service.potential.Trusteeship;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Entrance;
import com.cq.sdk.service.potential.annotation.Execute;
import com.cq.sdk.service.potential.annotation.LoadProperties;
import com.cq.sdk.service.potential.utils.InjectionType;
import com.cq.sdk.service.potential.utils.Tool;
import com.cq.sdk.service.test.dao.entity.UserMapper;
import com.cq.sdk.service.utils.Logger;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.service.test.*",injectionType = InjectionType.Annotation)
@LoadProperties({"test.properties"})
public class Main implements MethodInterceptor {
    @Autowired
    UserMapper userMapper;
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
      // new Trusteeship(Main.class);
       /* Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(a.class);
        enhancer.setCallback(new Main());
        enhancer.setClassLoader(a.class.getClassLoader());
        a= (com.cq.sdk.service.test.a) enhancer.create();
        a.println();*/
        Logger.info(Tool.checkPointcutStr("static&fina","public static final"));
    }
    @Execute
    public  final void main() {
        Logger.info(this.userMapper.selectByPrimaryKey("1000000000040"));
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object object=null;
        object=method.invoke(new a());
        return object;
    }
}
final class a{
    public void println(){
      System.out.println("the is a class");
    }
}