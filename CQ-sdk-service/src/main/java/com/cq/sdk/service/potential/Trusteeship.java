package com.cq.sdk.service.potential;

import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Component;
import com.cq.sdk.service.potential.annotation.Repository;
import com.cq.sdk.service.potential.annotation.Service;
import com.cq.sdk.service.potential.utils.ClassObj;
import com.cq.sdk.service.potential.utils.InjectionType;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 托管类
 * Created by admin on 2016/9/1.
 */
public class Trusteeship {
    private String packagePath;
    private String mainMethod;
    private List<ClassObj> classList=new ArrayList<ClassObj>();
    private String rootDir;
    private Class mainClass;
    private InjectionType injectionType=InjectionType.AutoAll;//默认采用全部注入

    public Trusteeship(String packagePath, String mainMethod) {
        this.packagePath = packagePath;
        this.mainMethod = mainMethod;
    }

    public Trusteeship(String packagePath, String mainMethod, InjectionType injectionType) {
        this.packagePath = packagePath;
        this.mainMethod = mainMethod;
        this.injectionType=injectionType;
    }
    public void init(){
        try {
            this.rootDir = this.getClass().getResource("/").getFile().substring(1);
            String absPath=this.rootDir+packagePath.replace(".","/").replace("*","");
            this.loadClass(absPath);
            this.mainClass=Class.forName(this.mainMethod.substring(0,this.mainMethod.lastIndexOf(".")));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void loadClass(String path) throws ClassNotFoundException {
        File file = new File(path);
        for (File item : file.listFiles()) {
            if (item.isFile()) {
                String clazz=item.getAbsolutePath().substring(this.rootDir.length()).replace("\\","/").replace("/",".");
                clazz=clazz.substring(0,clazz.length()-6);
                ClassObj classObj=new ClassObj(Class.forName(clazz));
                Repository repository= (Repository) classObj.getClazz().getAnnotation(Repository.class);
                Service service= (Service) classObj.getClazz().getAnnotation(Service.class);
                Component component= (Component) classObj.getClazz().getAnnotation(Component.class);
                if((!classObj.getClazz().isInterface() && (repository!=null || service !=null || component !=null)) || classObj.getClazz().isInterface()) {
                    this.classList.add(classObj);
                }
            }else if(item.isDirectory()){
                this.loadClass(item.getAbsolutePath());
            }
        }
    }
    public void start(){
        try {
            int index=this.mainMethod.lastIndexOf(".");
            String method=this.mainMethod.substring(index+1);
            Object object=this.injection(this.mainClass);
            this.mainClass.getMethod(method).invoke(object);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * 自动注入
     * @param clazz 被注入的类
     * @return 返回注入后的对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private Object injection(Class clazz) throws IllegalAccessException, InstantiationException {
        Object object;
        if(clazz.isInterface()){
            ClassObj classObj=this.impl(clazz);
            if(classObj !=null &&classObj.getObject()==null){
                classObj.setObject(this.injection(classObj.getClazz()));
            }else{
                return null;
            }
            object=classObj.getObject();
        }else{
            object=clazz.newInstance();
        }
        Field[] fields= clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            if((this.injectionType==InjectionType.Annotation&&field.isAnnotationPresent(Autowired.class)) || this.injectionType==InjectionType.AutoAll) {
                Class type = field.getType();
                ClassObj classObj = this.exists(type,field);
                if (classObj != null) {
                    if (classObj.getObject() == null) {
                        classObj.setObject(this.injection(classObj.getClazz()));
                    }
                    if(classObj.getObject()!=null) {
                        field.set(object, classObj.getObject());
                    }
                }
            }
        }
        return object;
    }

    /**
     * 类型是否存在总库
     * @param clazz1
     * @return
     */
    private ClassObj exists(Class clazz1,Field field){
        for(ClassObj classObj :this.classList){
            if(this.injectionType==InjectionType.Annotation && !field.getAnnotation(Autowired.class).value()){
                if(this.isClassName(classObj.getClazz(),field.getName())){
                    return classObj;
                }
            }else {
                if (classObj.getClazz() == clazz1) {
                    return classObj;
                }
            }
        }
        return null;
    }

    /**
     * 根据名字比较是否一样
     * @param clazz
     * @param name
     * @return
     */
    private boolean isClassName(Class clazz,String name){
        String clazzName=clazz.getName();
        clazzName=clazzName.substring(clazzName.lastIndexOf(".")+1);
        if(clazzName.equals(name)){
            return true;
        }else if(clazzName.equals(name.substring(0,1).toUpperCase()+name.substring(1))){
            return true;
        }
        return false;
    }
    /**
     * 查找实现类
     * @param clazz1
     * @return
     */
    private ClassObj impl(Class clazz1){
        for(ClassObj classObj : this.classList){
            if(!classObj.getClazz().isInterface() && this.isImplClass(classObj.getClazz(),clazz1)){
                return classObj;
            }
        }
        return null;
    }

    /**
     * 是否实现接口
     * @param clazz1 实体类
     * @param clazz2 接口
     * @return 实现true否则false
     */
    private boolean isImplClass(Class clazz1,Class clazz2){
        for(Class c : clazz1.getInterfaces()){
           if(c.isAssignableFrom(clazz2)){
               return true;
           }
        }
        return false;
    }

}