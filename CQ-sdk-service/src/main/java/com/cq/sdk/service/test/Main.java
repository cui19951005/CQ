package com.cq.sdk.service.test;

import com.cq.sdk.service.android.qq.UserService;
import com.cq.sdk.service.potential.Trusteeship;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Entrance;
import com.cq.sdk.service.potential.annotation.Execute;
import com.cq.sdk.service.potential.utils.InjectionType;
import com.cq.sdk.service.utils.Logger;

/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.service.*",injectionType = InjectionType.Annotation)
public class Main {
    @Autowired
    UserService userService;
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
       new Trusteeship(Main.class);
    }
    @Execute
    public void main(){
        Logger.info(userService);
    }
}
