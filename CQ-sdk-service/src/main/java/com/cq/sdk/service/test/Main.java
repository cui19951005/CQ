package com.cq.sdk.service.test;

import com.cq.sdk.service.android.qq.UserService;
import com.cq.sdk.service.potential.Trusteeship;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Entrance;
import com.cq.sdk.service.potential.annotation.Execute;
import com.cq.sdk.service.potential.annotation.LoadProperties;
import com.cq.sdk.service.potential.utils.InjectionType;
import com.cq.sdk.service.utils.FileUtils;
import com.cq.sdk.service.utils.Json;
import com.cq.sdk.service.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.service.*",injectionType = InjectionType.Annotation)
@LoadProperties({"test.properties"})
public class Main {
    @Autowired
    UserService userService;
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
       new Trusteeship(Main.class);
    }
    @Execute
    public void main(){
        Logger.info("hell world");
    }

}