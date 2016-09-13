package com.cq.sdk.utils;

import java.lang.reflect.Method;

/**
 * Created by CuiYaLei on 2016/8/20.
 */
public class Logger {
    static StringBuffer stringBuffer=new StringBuffer();
    public static final void error(Object object) {
        error(object,null);
    }

    public static final void error(Object object, Exception ex) {
        String info= formatMsg(object);
        System.err.println(info);
    }

    public static final void info(Object object) {
        String info= formatMsg(object);
        System.out.println(info);
    }
    private static final String formatMsg(Object object){
        StringBuilder stringBuilder=new StringBuilder();
        StackTraceElement nowClass=null;
        for(StackTraceElement stackTraceElement :Thread.currentThread().getStackTrace() ){
            if(!stackTraceElement.getClassName().equals(Thread.class.getName()) && !stackTraceElement.getClassName().equals(Logger.class.getName())){
                nowClass=stackTraceElement;
                break;
            }
        }
        stringBuilder.append(new Date().toString("yyyy-MM-dd HH:mm:ss EEE"));
        stringBuilder.append(":");
        stringBuilder.append(nowClass.getClassName());
        stringBuilder.append(".");
        stringBuilder.append(nowClass.getMethodName());
        stringBuilder.append("(");
        /*stringBuilder.append(nowClass.getClassName());
        stringBuilder.append(":");*/
        //太长了
        stringBuilder.append(nowClass.getLineNumber());
        stringBuilder.append(")");
        stringBuilder.append(":");
        stringBuilder.append(msg(object));
        stringBuffer.append(stringBuilder.toString());
        return stringBuilder.toString();
    }
    public static final void info(String format,Object... objects){
        Logger.info(Logger.format(format,objects));
    }
    public static final void error(String format,Object... objects){
        Logger.error(Logger.format(format,objects));
    }
    private static final String format(String format,Object... objects){
        for(int i=0;i<objects.length;i++){
            String chars="{"+i+"}";
            if(format.indexOf(chars)!=-1) {
                format=format.replace(chars, msg(objects[i]));
            }else {
                Logger.error(format+"占位符{"+i+"}不存在");
                return null;
            }
        }
        return format;
    }
    /**
     * 对象处理
     * @param object
     * @return
     */
    private static String msg(Object object){
        if(object == null){
          return "null";
        } else if(object instanceof byte[]){
            return ByteSet.parse((byte[]) object).toStringUInt();
        }else if(object instanceof ByteSet){
            return ((ByteSet) object).toStringUInt();
        }else if(object instanceof char[]){
            char[] chars= (char[]) object;
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<chars.length;i++){
                sb.append(chars[i]);
                if(i+1!=chars.length){
                    sb.append(",");
                }
            }
            return sb.toString();
        }else if(object.getClass().isArray()){
            Object[] array= (Object[]) object;
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<array.length;i++){
                if(array[i] !=null) {
                    sb.append(array[i].toString());
                }else{
                    sb.append("null");
                }
                if(i+1!=array.length){
                    sb.append(",");
                }
            }
            return sb.toString();
        }else if(object.getClass().isPrimitive()){
            return object.toString();
        }else{
            try {
                Method method=object.getClass().getMethod("toString");
                if(method.isAnnotationPresent(Override.class)){
                    return object.toString();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return object.toString();
    }
}
