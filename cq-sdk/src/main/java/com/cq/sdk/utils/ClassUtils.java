package com.cq.sdk.utils;

/**
 * Created by admin on 2016/11/4.
 */
public class ClassUtils {
    public static Class isPrimitive(Class clazz){
        if (clazz.isAssignableFrom(Integer.class) || clazz.getName().equals("int")) {
            return Integer.class;
        } else if (clazz.isAssignableFrom(Byte.class) || clazz.getName().equals("byte")) {
            return Byte.class;
        } else if (clazz.isAssignableFrom(Short.class) || clazz.getName().equals("short")) {
            return Short.class;
        } else if (clazz.isAssignableFrom(Long.class) || clazz.getName().equals("long")) {
            return Long.class;
        } else if (clazz.isAssignableFrom(Boolean.class) || clazz.getName().equals("boolean")) {
            return Boolean.class;
        } else if (clazz.isAssignableFrom(Float.class) || clazz.getName().equals("float")) {
            return Float.class;
        } else if (clazz.isAssignableFrom(Double.class) || clazz.getName().equals("double")) {
            return Double.class;
        } else if (clazz.isAssignableFrom(Character.class) || clazz.getName().equals("char")) {
            return Character.class;
        } else {
            return null;
        }
    }
}
