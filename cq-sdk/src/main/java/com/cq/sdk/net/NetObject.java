package com.cq.sdk.net;

import com.cq.sdk.net.udp.UdpReceiveData;
import com.cq.sdk.net.udp.UDP;
import com.cq.sdk.net.uitls.NetClass;
import com.cq.sdk.potential.utils.FileUtils;
import com.cq.sdk.potential.utils.PackUtils;
import com.cq.sdk.utils.*;
import com.cq.sdk.utils.Number;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/9/14.
 */
public class NetObject {
    private String host;
    private int port;
    private UDP udp;
    private int localPort;
    private static final String METHOD_INVOKE="method.invoke";
    private static final String CLASS_ANNOTATION_LIST="class.annotation.list";
    private int overtime=3000;
    private MessageHandle messageHandle;
    public NetObject(int localPort,String host, int port) {
        this.host = host;
        this.port = port;
        this.localPort=localPort;
    }

    public Object invoke(String method, Object[] params){
        ReceiveObject receiveObject=new ReceiveObject();
        this.udp=new UDP(host, port, receiveObject);
        ByteArrayOutputStream outputStream=null;
        try {
            outputStream=new ByteArrayOutputStream();
            this.writeObject(outputStream,METHOD_INVOKE);
            this.writeObject(outputStream,method);
            this.writeObject(outputStream,params);
            this.udp.send(this.udp.getHost(),this.udp.getPort(),ByteSet.parse(outputStream.toByteArray()),this.overtime);
        } finally {
            try {
                outputStream.close();
                this.udp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return receiveObject.getObject();
    }
    public List<NetClass> getList(String packName, List<Class> annotationList){
        ReceiveObject receiveObject=new ReceiveObject();
        this.udp=new UDP(host, port,receiveObject);
        ByteArrayOutputStream outputStream=null;
        try {
            outputStream=new ByteArrayOutputStream();
            this.writeObject(outputStream,CLASS_ANNOTATION_LIST);
            this.writeObject(outputStream,packName);
            this.writeObject(outputStream,annotationList);
            this.udp.send(this.udp.getHost(),this.udp.getPort(),ByteSet.parse(outputStream.toByteArray()),this.overtime);
        } finally {
            try {
                outputStream.close();
                this.udp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (List<NetClass>) receiveObject.getObject();
    }
    private static byte[] getObject(Object object){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            return outputStream.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void writeObject(OutputStream outputStream,Object object){
        String str=null;
        byte[] bytes;
        if(object.getClass().isPrimitive() || object instanceof String){
            str=object.toString();
        }
        if(str!=null){
            bytes=str.getBytes();
        }else{
            bytes=this.getObject(object);
        }
        try {
            outputStream.write(Number.intToByte4(bytes.length).getByteSet());
            outputStream.write(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static <T>T readObject(ByteSet byteSet,Class<T> type){
        if(type.isAssignableFrom(String.class)){
            return (T) new String(byteSet.getByteSet());
        }else {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteSet.getByteSet());
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                return (T) objectInputStream.readObject();
            } catch (Exception e) {
                return null;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void messageHandle(Map<Class,Object> objectMap){
        messageHandle=new MessageHandle();
        messageHandle.netObject=this;
        messageHandle.objectMap=objectMap;
        messageHandle.start();
    }

    private static class  MessageHandle extends Thread{
        private NetObject netObject;
        private Map<Class,Object> objectMap;
        @Override
        public void run() {
            this.netObject.udp=new UDP(this.netObject.localPort,this.netObject.host,this.netObject.port, new UdpReceiveData() {
                @Override
                public void receive(UDP udp, ByteSet byteSet, String host,int port) {
                    int headLen=Number.byte4ToInt(byteSet.subByteSet(0,4));
                    String head=new String(byteSet.subByteSet(4,headLen).getByteSet());
                    byteSet=byteSet.subByteSet(4+headLen);
                    if(METHOD_INVOKE.equals(head))
                    {
                        MethodInvoke(udp,byteSet,host,port);
                    }else if(CLASS_ANNOTATION_LIST.equals(head)){
                        annotationClassList(udp,byteSet,host,port);
                    }
                }

                private void annotationClassList(UDP udp,ByteSet byteSet,String host,int port) {
                    int size= Number.byte4ToInt(byteSet.subByteSet(0,4));
                    String packName=  NetObject.readObject(byteSet.subByteSet(4,size),String.class);
                    byteSet=byteSet.subByteSet(4+size);
                    size=Number.byte4ToInt(byteSet.subByteSet(0,4));
                    List<Class> annotationList=  NetObject.readObject(byteSet.subByteSet(4,size),List.class);
                    List<File> fileList= FileUtils.loadClass(packName+"*");
                    List<NetClass> classList=new ArrayList<>();
                    fileList.stream().forEach(file -> {
                        try {
                            Class clazz = Class.forName(PackUtils.filePathToPack(file, packName));
                            annotationList.stream().filter(s->clazz.isAnnotationPresent(s)).limit(1).forEach(
                                    s-> {
                                        NetClass netClass=new NetClass();
                                        netClass.setName(clazz.getName());
                                        String[] interfaceList=new String[clazz.getInterfaces().length];
                                        for(int i=0;i<interfaceList.length;i++){
                                            interfaceList[i]=clazz.getInterfaces()[i].getName();
                                        }
                                        netClass.setInterfaceName(interfaceList);
                                        String[] classAnnotationList=new String[clazz.getAnnotations().length];
                                        for(int i=0;i<classAnnotationList.length;i++){
                                            classAnnotationList[i]=clazz.getAnnotations()[i].annotationType().getName();
                                        }
                                        netClass.setAnnotationList(classAnnotationList);
                                        classList.add(netClass);
                                    }
                            );
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
                    ByteSet bytes= ByteSet.parse(NetObject.getObject(classList));
                    ByteSet length=Number.intToByte4(bytes.length());
                    bytes=length.append(bytes);
                    udp.send(host,port,bytes);
                }

                private void MethodInvoke(UDP udp,ByteSet byteSet,String host,int port) {
                    int methodLen= Number.byte4ToInt(byteSet.subByteSet(0,4));
                    String methodAllName=new String(byteSet.subByteSet(4,methodLen).getByteSet());
                    byteSet=byteSet.subByteSet(4+methodLen);
                    int clazzIndex=methodAllName.lastIndexOf(".");
                    String clazzName=methodAllName.substring(0,clazzIndex);
                    String methodName=methodAllName.substring(clazzIndex+1);
                    Object[] params=new Object[0];
                    Class[] paramsType=new Class[0];
                    try {
                        if(byteSet.length()>0) {
                            int length=Number.byte4ToInt(byteSet.subByteSet(0,4));
                            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteSet.subByteSet(4,length).getByteSet()));
                            params = (Object[]) objectInputStream.readObject();
                            objectInputStream.close();
                            paramsType = new Class[params.length];
                            for (int i = 0; i < params.length; i++) {
                                paramsType[i] = params[i].getClass();
                            }
                        }
                        Object object=null;
                        if(objectMap!=null) {
                            object = objectMap.get(Class.forName(clazzName));
                        }else{
                            try {
                                Class clazz = Class.forName(clazzName);
                                Constructor[] constructors = clazz.getConstructors();
                                if (constructors.length == 0) {
                                    object= clazz.newInstance();
                                }
                                for (Constructor constructor : constructors) {
                                    if (constructor.getParameterCount() == 0) {
                                        object= clazz.newInstance();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        Method method=object.getClass().getMethod(methodName,paramsType);
                        method.setAccessible(true);
                        Object result=method.invoke(object,params);
                        if(result==null) {
                            udp.send(host,port,new ByteSet(4));//int4字节
                        }else{
                            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                            ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(result);
                            ByteSet bytes=ByteSet.parse(outputStream.toByteArray());
                            ByteSet length=Number.intToByte4(bytes.length());
                            udp.send(host,port,length.append(bytes));
                            objectOutputStream.close();
                        }
                    }catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        Logger.error("{0}对象不存在",e,clazzName);
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        Logger.error("{0}方法不存在",e,methodAllName);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private static class ReceiveObject implements UdpReceiveData {
        private Object object;
        @Override
        public void receive(UDP udp, ByteSet byteSet, String host,int port) {
            try {
                if(byteSet.length()>0) {
                    int length=Number.byte4ToInt(byteSet.subByteSet(0,4));
                    ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(byteSet.subByteSet(4,length).getByteSet()));
                    Object obj = inputStream.readObject();
                    this.object = obj;
                }else {
                    this.object=null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally {
                udp.close();
            }
        }

        public Object getObject() {
            return object;
        }
    }
}
