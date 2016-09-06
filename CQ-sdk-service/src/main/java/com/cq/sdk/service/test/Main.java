package com.cq.sdk.service.test;

import com.cq.sdk.service.potential.Trusteeship;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Entrance;
import com.cq.sdk.service.potential.annotation.Execute;
import com.cq.sdk.service.potential.annotation.LoadProperties;
import com.cq.sdk.service.potential.utils.InjectionType;
import com.cq.sdk.service.test.dao.entity.UserMapper;
import com.cq.sdk.service.utils.Logger;
/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.service.test.*",injectionType = InjectionType.Annotation)
@LoadProperties({"test.properties"})
public class Main {
    @Autowired
    UserMapper userMapper;
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
         new Trusteeship(Main.class);
       /* Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(a.class);
        enhancer.setCallback(new Main());
        enhancer.setClassLoader(a.class.getClassLoader());
        a= (com.cq.sdk.service.test.a) enhancer.create();
        a.println();*/
    }
    @Execute
    public void main() {
        Logger.info(this.userMapper.selectByPrimaryKey("1000000000040"));
    }
}