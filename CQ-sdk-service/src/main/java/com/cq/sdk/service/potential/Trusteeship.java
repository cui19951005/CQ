package com.cq.sdk.service.potential;

import com.cq.sdk.service.potential.annotation.*;
import com.cq.sdk.service.potential.inter.AutowiredInterface;
import com.cq.sdk.service.potential.sql.TransactionManager;
import com.cq.sdk.service.potential.sql.mybatis.MybatisTrusteeship;
import com.cq.sdk.service.potential.sql.mybatis.utils.MybatisGenerateBean;
import com.cq.sdk.service.potential.utils.AopClass;
import com.cq.sdk.service.potential.utils.AopMethod;
import com.cq.sdk.service.potential.utils.ClassObj;
import com.cq.sdk.service.potential.utils.InjectionType;
import com.cq.sdk.service.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 托管类
 * Created by admin on 2016/9/1.
 */
public class Trusteeship {
    private String packagePath;
    private String mainMethod;
    private List<ClassObj> classList=new ArrayList();
    private List<AopClass> aopList=new ArrayList<>();
    private String rootDir;
    private Class mainClass;
    private InjectionType injectionType=InjectionType.AutoAll;//默认采用全部注入
    private List<Properties> propertiesList=new ArrayList<Properties>();
    private TransactionManager transactionManager;
    public Trusteeship(Class mainClass) {
        Entrance entrance= (Entrance) mainClass.getAnnotation(Entrance.class);
        if(entrance!=null){
            this.packagePath=entrance.value();
            this.mainClass=mainClass;
            for(Method method : mainClass.getDeclaredMethods()){
                Execute execute=method.getAnnotation(Execute.class);
                if(execute !=null){
                    this.mainMethod=mainClass.getName()+"."+method.getName();
                    break;
                }
            }
            this.injectionType=entrance.injectionType();
        }
        this.init();
        this.start();
    }

    public Trusteeship(String packagePath, String mainMethod) {
        this(packagePath,mainMethod,InjectionType.AutoAll);
    }

    public Trusteeship(String packagePath, String mainMethod, InjectionType injectionType) {
        this.packagePath = packagePath;
        this.mainMethod = mainMethod;
        this.injectionType=injectionType;
        this.init();
        this.start();
    }
    private void init(){
        try {
            this.rootDir = this.getClass().getResource("/").getFile().substring(1);
            String absPath=this.rootDir+packagePath.replace(".","/").replace("*","");
            this.mainClass=Class.forName(this.mainMethod.substring(0,this.mainMethod.lastIndexOf(".")));
            LoadProperties loadProperties= (LoadProperties) this.mainClass.getAnnotation(LoadProperties.class);
            if(loadProperties!=null){//加载属性文件
                for(String file : loadProperties.value()) {
                    Properties properties = new Properties();
                    properties.load(new FileInputStream(this.getClass().getResource("/"+file).getFile()));
                    this.propertiesList.add(properties);
                }
            }
            this.loadClass(absPath);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void loadClass(String path) throws ClassNotFoundException {
        try {
            File file = new File(path);
            for (File item : file.listFiles()) {
                if (item.isFile()) {
                    String clazzName = item.getAbsolutePath().substring(this.rootDir.length()).replace("\\", "/").replace("/", ".");
                    clazzName = clazzName.substring(0, clazzName.length() - 6);
                    Class clazz=Class.forName(clazzName);
                    if(clazz!=null && clazz.getModifiers()==1) {//0默认修饰符1public
                        Object object=this.createObj(clazz);
                        this.addAutowiredManager(clazz,object);
                        this.addAopManager(clazz,object);
                        this.addDatabaseManager(clazz,object);
                    }
                } else if (item.isDirectory()) {
                    this.loadClass(item.getAbsolutePath());
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Logger.error("加载托管类加载失败",ex);
        }
    }
    private void start(){
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
    private Object injection(Class clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object object;
        if(clazz.isInterface()){
            ClassObj classObj=this.impl(clazz);
            if(classObj !=null &&classObj.getObject()==null){
                classObj=new ClassObj(this.injection(classObj.getClazz()));
            }else{
                return null;
            }
            object=classObj.getObject();
        }else{
            object=this.createObj(clazz);
        }
        Field[] fields= clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            Class type = field.getType();
            Object obj = this.exists(type,field);
            if (obj == null) {
                obj=this.injection(type);
            }
            if(obj!=null) {
                obj =this.addAop(obj);
                injectionProperties(field,obj);
                field.set(object, obj);
            }
        }
        return this.addAop(object);
    }

    /**
     * 类型是否存在总库
     * @param clazz
     * @return
     */
    private Object exists(Class clazz,Field field) throws IllegalAccessException, InstantiationException {
        if(!clazz.isInterface()){
            return this.createObj(clazz);
        }
        for (ClassObj classObj : this.classList) {
            if(!classObj.getClazz().isInterface()) {
                if (field.isAnnotationPresent(Autowired.class)&&!field.getAnnotation(Autowired.class).value()) {//
                    if (this.isClassName(classObj.getClazz(), field.getName())) {
                        return classObj.getObject();
                    }
                } else if(!(this.injectionType==InjectionType.Annotation &&!field.isAnnotationPresent(Autowired.class))) {
                    if (this.isImplClass(classObj.getClazz(), clazz)) {//
                        return classObj.getObject();
                    }
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
    private Object createObj(Class clazz) throws InstantiationException, IllegalAccessException {
        Constructor[] constructors=clazz.getConstructors();
        if(constructors.length==0){
            return clazz.newInstance();
        }
        for(Constructor constructor : constructors){
            if(constructor.getParameterCount()==0){
                return clazz.newInstance();
            }
        }
        return null;
    }

    /**
     * 注入配置文件(set方法)
     * @param field
     * @param object
     * @throws IllegalAccessException
     */
    public void injectionProperties(Field field, Object object) throws InvocationTargetException, IllegalAccessException {
        Property property=field.getAnnotation(Property.class);
        if(property != null){
            Method[] methods=object.getClass().getMethods();
            for(Method item : methods){
                item.setAccessible(true);
                if(item.getName().length()>3&& item.getName().substring(0,3).equals("set")) {
                    for (Properties properties : propertiesList) {
                        String name=item.getName().substring(3,4).toLowerCase()+item.getName().substring(4);
                        String value = properties.getProperty(property.value() + name);
                        if(value!=null) {
                            Class params= item.getParameterTypes()[0];
                            if(params.isAssignableFrom(Integer.class) || params.getName().equals("int")){
                                item.invoke(object, Integer.valueOf(value));
                            }else if(params.isAssignableFrom(Byte.class) || params.getName().equals("byte")){
                                item.invoke(object, Byte.valueOf(value));
                            }else if(params.isAssignableFrom(Short.class) || params.getName().equals("short")){
                                item.invoke(object, Short.valueOf(value));
                            }else if(params.isAssignableFrom(Long.class) || params.getName().equals("long")){
                                item.invoke(object, Long.valueOf(value));
                            }else if(params.isAssignableFrom(Boolean.class) || params.getName().equals("boolean")){
                                item.invoke(object, Boolean.valueOf(value));
                            }else if(params.isAssignableFrom(Float.class) || params.getName().equals("float")){
                                item.invoke(object, Float.valueOf(value));
                            }else if(params.isAssignableFrom(Double.class) || params.getName().equals("double")){
                                item.invoke(object, Double.valueOf(value));
                            }else if(params.isAssignableFrom(Character.class) || params.getName().equals("char")){
                                item.invoke(object, value.charAt(0));
                            }else{
                                item.invoke(object, value);
                            }

                        }
                    }
                }
            }

        }
    }
    private Object addAop(Object object) {
        return new InvocationHandlerImpl(this.aopList,this.transactionManager).bind(object);
    }
    /**
     * 将对象注解切面表达式存入对象
     * @param object
     * @return
     */
    private AopClass analysisAopClass(Object object){
        AopClass aopClass=new AopClass();
        aopClass.setObject(object);
        for(Method method : object.getClass().getMethods()){
            Pointcut pointcut=method.getAnnotation(Pointcut.class);
            Before before=method.getAnnotation(Before.class);
            AfterReturning afterReturning=method.getAnnotation(AfterReturning.class);
            AfterThrowing afterThrowing=method.getAnnotation(AfterThrowing.class);
            After after=method.getAnnotation(After.class);
            Around around=method.getAnnotation(Around.class);
            if(pointcut!=null) {
                aopClass.setPointcut(Pattern.compile(pointcut.value()));
                aopClass.setName(method.getName());
            }else if(before!=null){
                aopClass.setBefore(new AopMethod(before,object,method));
            }else if(afterReturning != null){
                aopClass.setReturning(new AopMethod(afterReturning,object,method));
            }else if(afterThrowing != null){
                aopClass.setThrowing(new AopMethod(afterThrowing,object,method));
            }else if(after !=null){
                aopClass.setAfter(new AopMethod(after,object,method));
            }else if(around != null){
                aopClass.setRound(new AopMethod(around,object,method));
            }
        }
        return aopClass;
    }
    private void addAutowiredManager(Class clazz, Object object){
        Repository repository = (Repository) clazz.getAnnotation(Repository.class);
        Service service = (Service) clazz.getAnnotation(Service.class);
        Component component = (Component) clazz.getAnnotation(Component.class);
        if (!clazz.isInterface() && (repository != null || service != null || component != null)) {
            this.classList.add(new ClassObj(object));//不要接口
        }
    }
    private void addAopManager(Class clazz, Object object){
        Aspect aspect=(Aspect)clazz.getAnnotation(Aspect.class);
        if(aspect!=null){
            this.aopList.add(this.analysisAopClass(object));
        }
    }
    private void addDatabaseManager(Class clazz, Object object) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        if (this.isImplClass(clazz, MybatisTrusteeship.class)) {
            object=this.injection(clazz);
            AutowiredInterface autowiredInterface = MybatisGenerateBean.trusteeship((MybatisTrusteeship) object);
            this.classList.addAll(autowiredInterface.beanList());
            this.transactionManager=autowiredInterface.transactionManager();
            /*this.classList.add(new ClassObj(transactionManager));
            TransactionAop  transactionAop=new TransactionAop();
            Method method=transactionAop.getClass().getMethod("round");
            AopMethod aopMethod=new AopMethod(method.getAnnotation(Around.class),transactionAop,method);
            for(TransactionMethod transactionMethod :transactionManager.getTransactionMethodList()) {
                AopClass aopClass = new AopClass();
                aopClass.setObject(transactionAop);
                aopClass.setName("pointcut");
                StringBuilder sb = new StringBuilder();
                sb.append(".*");
                sb.append(transactionManager.getPackName());
                sb.append(transactionMethod.getName().replace("*",".*"));
                sb.append("\\(.*\\)");
                aopClass.setPointcut(Pattern.compile(sb.toString()));
                aopClass.setRound(aopMethod);
                this.aopList.add(aopClass);
            }*/
        }
    }
}