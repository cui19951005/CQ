package com.cq.sdk.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/10/24.
 */
public final class ObjectUtils {
    public static Object copy(Object old,Object newObj) {
        try {
            Field[] fields = old.getClass().getDeclaredFields();
            for (Field field : fields) {
                Field[] newFields = newObj.getClass().getDeclaredFields();
                for (Field newField : newFields) {
                    if (field.getName().equals(newField.getName()) && (field.getModifiers() & Modifier.STATIC)==0) {
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
    public static Object clone(Object object){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            ObjectOutputStream output = new ObjectOutputStream(byteArrayOutputStream);
            output.writeObject(object);
            byte[] bytes=byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            byteArrayInputStream=new ByteArrayInputStream(bytes);
            ObjectInputStream inputStream=new ObjectInputStream(byteArrayInputStream);
            object=inputStream.readObject();
            byteArrayInputStream.close();
            return object;
        }catch (Exception e){
            e.printStackTrace();
            try {
                byteArrayOutputStream.close();
                if(byteArrayInputStream!=null){
                    byteArrayInputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
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
