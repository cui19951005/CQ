package com.cq.sdk.service.potential.utils;

import com.cq.sdk.service.utils.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by CuiYaLei on 2016/9/4.
 */
public class Tool {
    public static final boolean checkAopMethod(String pointcut,Method method){
        pointcut= StringUtils.replaceSpace(pointcut);
        String[] array=pointcut.split(" ");//修饰符 返回类型 方法全限定名 (参数)
        if(array.length==4){
            if(array.equals("*")){
                if(!Tool.checkPointcutStr(array[0],Modifier.toString(method.getModifiers()))){
                    return false;
                }

            }
        }
        return false;
    }
    public static final boolean checkPointcutStr(String str,String modifier){
        String[] modifiers=modifier.split(" ");
        String[] strings=str.split("&");
        for(String strItem : strings){
            boolean isExists = false;
            for(String modifierItem : modifiers){
                String[] array=strItem.split("\\*");
                if(array.length==1){
                    if(strItem.equals(modifierItem)){
                        isExists=true;
                        break;
                    }else{
                        isExists=false;
                    }
                }else {
                    isExists=true;
                    int index = -1;
                    for (String ar : array) {
                        if (modifierItem.indexOf(ar, index) == -1) {
                            isExists = false;
                            break;
                        }
                    }
                    if(isExists){
                        break;
                    }
                }
            }
            if(!isExists){
                return false;
            }
        }
        return true;
    }
}
