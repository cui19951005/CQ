package com.cq.sdk.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/10/24.
 */
public class ObjectUtils {
    public static Object copy(Object old,Object newObj) {
        try {
            Field[] fields = old.getClass().getDeclaredFields();
            for (Field field : fields) {
                Field[] newFields = newObj.getClass().getDeclaredFields();
                for (Field newField : newFields) {
                    if (field.getName().equals(newField.getName())) {
                        newField.setAccessible(true);
                        field.setAccessible(true);
                        newField.set(newObj, field.get(old));
                        break;
                    }
                }
            }
            return newObj;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static Map<String,Object> copyMap(Object object){
        try {
            Map<String, Object> map = new HashMap<>();
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(object));
            }
            return map;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
