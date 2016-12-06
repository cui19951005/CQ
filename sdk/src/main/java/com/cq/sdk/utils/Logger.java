package com.cq.sdk.utils;

import java.io.FileOutputStream;

/**
 * Created by CuiYaLei on 2016/8/20.
 */
public final class Logger {
    static FileOutputStream outputStream=null;
    public static final void error(Object object) {
        error(object,null);
    }

    public static final void error(Object object, Exception e) {
        String info= formatMsg(object);
        System.err.println(info);
        if(e!=null) {
            e.printStackTrace();
        }
    }

    public static final void info(Object object) {
        String info= formatMsg(object);
        System.out.println(info);
    }
    private static final String formatMsg(Object object){
        try {
            StackTraceElement nowClass = null;
            for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                if (!stackTraceElement.getClassName().equals(Thread.class.getName()) && !stackTraceElement.getClassName().equals(Logger.class.getName())) {
                    nowClass = stackTraceElement;
                    break;
                }
            }
            String prefix=Str.concat(new Time().toString("yyyy-MM-dd HH:mm:ss EEE"),
                    ":",nowClass.getClassName(),
                    ".",nowClass.getMethodName(),
                    "(",nowClass.getFileName(),
                    ":",String.valueOf(nowClass.getLineNumber()),
                    "):",msg(object)
                    );
            if (outputStream == null) {
                outputStream = new FileOutputStream(System.getProperty("user.dir")+"/"+new Time().toString("yyyy-MM-dd")+".log");
            }
            outputStream.write((Str.concat(prefix,"\r\n").getBytes("utf-8")));
            return prefix;
        }catch (Exception e){
            Logger.error(e);
            return null;
        }
    }
    public static final void error(String format,Exception e,Object... objects){
        Logger.error(Logger.format(format,objects),e);
    }
    public static final void error(String format,Object... objects){
        String message=Logger.format(format,objects);
        Logger.error(message,new Exception(message));
    }
    public static final void info(String format,Object... objects){
        Logger.info(Logger.format(format,objects));
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
        }
        return object.toString();
    }
}
