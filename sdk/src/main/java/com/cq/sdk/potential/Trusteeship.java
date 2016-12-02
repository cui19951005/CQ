package com.cq.sdk.potential;

import com.cq.sdk.net.uitls.NetClass;
import com.cq.sdk.net.NetObject;
import com.cq.sdk.potential.annotation.*;
import com.cq.sdk.potential.aop.*;
import com.cq.sdk.potential.inter.AutowiredInterface;
import com.cq.sdk.potential.sql.frame.hibernate.HibernateTrusteeship;
import com.cq.sdk.potential.sql.frame.mybatis.MybatisTrusteeship;
import com.cq.sdk.potential.sql.frame.utils.GenerateBean;
import com.cq.sdk.potential.sql.tx.TransactionAop;
import com.cq.sdk.potential.sql.tx.TransactionManager;
import com.cq.sdk.potential.sql.tx.utils.TransactionMethod;
import com.cq.sdk.potential.utils.AopClass;
import com.cq.sdk.potential.utils.InjectionType;
import com.cq.sdk.potential.utils.AopMethod;
import com.cq.sdk.utils.ClassUtils;
import com.cq.sdk.utils.Logger;
import com.cq.sdk.potential.utils.PackUtils;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 托管类
 * Created by admin on 2016/9/1.
 */
public final class Trusteeship {
    private String packagePath;
    private String mainMethod;
    private Map<Class,Object> objectMap =new HashMap<>();
    private Map<String,AopClass> aopMap =new HashMap<>();
    private String rootDir;
    private Class mainClass;
    private InjectionType injectionType=InjectionType.AutoAll;//默认采用全部注入
    private List<Properties> propertiesList=new ArrayList<Properties>();
    private TransactionManager transactionManager;
    private List<NetObject> netObjectList=new ArrayList<>();
    public Trusteeship(Class mainClass) {
        Entrance entrance= (Entrance) mainClass.getAnnotation(Entrance.class);
        if(entrance!=null){
            this.packagePath=entrance.value();
            this.mainClass=mainClass;
            if(entrance.method().length()>0){
                this.mainMethod=mainClass.getName()+"."+entrance.method();
            }
            Stream.of(mainClass.getDeclaredMethods()).filter(s->s.isAnnotationPresent(Execute.class)).limit(1).forEach(method->{
                        Execute execute=method.getAnnotation(Execute.class);
                        if(execute !=null){
                            this.mainMethod=mainClass.getName()+"."+method.getName();
                        }
                    });
            this.injectionType=entrance.injectionType();
            this.addNetObject(mainClass);
            this.init();
            this.start();
        }


    }

    private void addNetObject(Class mainClass) {
        NetAddress netAddress=(NetAddress)mainClass.getAnnotation(NetAddress.class);
        if(netAddress!=null)
        {
            List<Class> annotationList=new ArrayList<>();
            annotationList.add(Service.class);
            annotationList.add(Component.class);
            annotationList.add(Repository.class);
            Stream.of(netAddress.value()).forEach(ipPort->{
                String[] array=ipPort.split(":");
                NetObject netObject=new NetObject(netAddress.port(),array[0],Integer.valueOf(array[1]));
                netObject.messageHandle(this.objectMap);
                this.netObjectList.add(netObject);
                List<NetClass> list=netObject.getList(this.packagePath,annotationList);
                ClassPool classPool=ClassPool.getDefault();
                list.stream().forEach(netClass->{
                    try {
                        CtClass ctClass = classPool.makeClass(netClass.getName()+"$");
                        CtField ctField=new CtField(classPool.get(netObject.getClass().getName()),"netObject",ctClass);
                        StringBuilder sb=new StringBuilder();
                        sb.append("new ");
                        sb.append(NetObject.class.getName());
                        sb.append("(");
                        sb.append(netAddress.port());
                        sb.append(",\"");
                        sb.append(array[0]);
                        sb.append("\",");
                        sb.append(Integer.valueOf(array[1]));
                        sb.append(");");
                        ctClass.addField(ctField,sb.toString());
                        ctField=new CtField(classPool.get(String.class.getName()),"clazz",ctClass);
                        ctClass.addField(ctField,"\""+netClass.getName()+"\"");
                        CtClass[] interfaceList = new CtClass[netClass.getInterfaceName().length];
                        for(int i=0;i<interfaceList.length;i++){
                            interfaceList[i]=classPool.get(netClass.getInterfaceName()[i]);
                            CtMethod[] ctMethods=interfaceList[i].getMethods();
                            Stream.of(ctMethods).filter(method-> method.getModifiers()==Modifier.PUBLIC || method.getModifiers()==Modifier.ABSTRACT+Modifier.PUBLIC).forEach(item->{
                                try {
                                    CtMethod ctMethod = new CtMethod(item.getReturnType(), item.getName(), item.getParameterTypes(), ctClass);
                                    sb.setLength(0);
                                    sb.append("{");
                                    sb.append("Object object=this.netObject.invoke(this.clazz+\".");
                                    sb.append(ctMethod.getName());
                                    if (ctMethod.getParameterTypes().length > 0) {
                                        sb.append("\",new Object[]{");
                                        for (int j = 0; j < ctMethod.getParameterTypes().length; j++) {
                                            sb.append("$");
                                            sb.append(j + 1);
                                            if (j != ctMethod.getParameterTypes().length - 1) {
                                                sb.append(",");
                                            }
                                        }
                                        sb.append("}");
                                    } else {
                                        sb.append("\",new Object[0]");
                                    }
                                    sb.append(");");
                                    if (!ctMethod.getReturnType().getName().equals("void")) {
                                        sb.append("return ");
                                        if(ctMethod.getReturnType().isPrimitive()){
                                            if(ctMethod.getReturnType().getName().equals("int")){
                                                sb.append("java.lang.Integer.valueOf(object.toString()).intValue();");
                                            }else if(ctMethod.getReturnType().getName().equals("byte")){
                                                sb.append("java.lang.Byte.valueOf(object.toString()).byteValue();");
                                            }else if(ctMethod.getReturnType().getName().equals("short")){
                                                sb.append("java.lang.Short.valueOf(object.toString()).shortValue();");
                                            }else if(ctMethod.getReturnType().getName().equals("char")){
                                                sb.append("object.toString().toCharArray()[0];");
                                            }else if(ctMethod.getReturnType().getName().equals("boolean")){
                                                sb.append("java.lang.Boolean.valueOf(object.toString()).booleanValue();");
                                            }else if(ctMethod.getReturnType().getName().equals("float")){
                                                sb.append("java.lang.Float.valueOf(object.toString()).floatValue();");
                                            }else if(ctMethod.getReturnType().getName().equals("double")){
                                                sb.append("java.lang.Double.valueOf(object.toString()).doubleValue();");
                                            }else if(ctMethod.getReturnType().getName().equals("long")){
                                                sb.append("java.lang.Long.valueOf(object.toString()).longValue();");
                                            }else {
                                                sb.append("object;");
                                            }
                                        }else{
                                            if(ctMethod.getReturnType().getName().equals(String.class.getName())){
                                                sb.append("object.toString();");
                                            }else {
                                                sb.append("object;");
                                            }
                                        }
                                    }
                                    sb.append("}");
                                    ctMethod.setBody(sb.toString());
                                    ctClass.addMethod(ctMethod);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            });
                        }
                        ctClass.setInterfaces(interfaceList);
                        AnnotationsAttribute annotationsAttribute=new AnnotationsAttribute(ctClass.getClassFile().getConstPool(),AnnotationsAttribute.visibleTag);
                        Stream.of(netClass.getAnnotationList()).forEach(annotation->{
                            annotationsAttribute.addAnnotation(new Annotation(annotation,ctClass.getClassFile().getConstPool()));
                        });
                        ctClass.getClassFile().addAttribute(annotationsAttribute);
                        this.addAutowiredManager(ctClass.toClass());
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            });
        }
    }
    private void init(){
        try {
            this.rootDir = Thread.currentThread().getClass().getResource("/").getFile().substring(1);
            String absPath=this.rootDir+packagePath.replace(".","/").replace("*","");
            LoadProperties loadProperties= (LoadProperties) this.mainClass.getAnnotation(LoadProperties.class);
            if(loadProperties!=null){//加载属性文件
                Stream.of(loadProperties.value()).filter(file->Thread.currentThread().getClass().getResource("/"+file)!=null).forEach(file->{
                    try {
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(this.getClass().getResource("/" + file).getFile()));
                        this.propertiesList.add(properties);
                    }catch (FileNotFoundException e) {
                        Logger.error("properties file not find",e);
                    } catch (IOException e) {
                        Logger.error("properties file stream open error",e);
                    }
                });
            }
            this.addManager(this.mainClass);
            this.loadClass(Thread.currentThread().getClass().getResource("/"+this.getClass().getPackage().getName().replace(".","/")).getFile());
            this.loadClass(absPath);
            this.objectMap.entrySet().stream().filter(item -> item.getValue() == null).forEach(item -> {
                item.setValue(this.injection(item.getKey()));
            });
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
                        this.addAutowiredManager(clazz);
                        this.addAopManager(clazz);
                        this.addDatabaseManager(clazz);
                    }
                } else if (item.isDirectory()) {
                    this.loadClass(item.getAbsolutePath());
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Logger.error("join trusteeship fail",ex);
        }
    }
    private void start(){
        if(this.mainMethod!=null) {
            try {
                int index = this.mainMethod.lastIndexOf(".");
                String method = this.mainMethod.substring(index + 1);
                Object object = this.injection(this.mainClass);
                this.mainClass.getMethod(method).invoke(object);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自动注入
     * @param clazz 被注入的类
     * @return 返回注入后的对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private <T> T injection(Class<T> clazz){
        try {
            Object object;
            if (clazz.isInterface()) {
                Class temp = this.impl(clazz);
                if (this.objectMap.get(temp)==null) {
                    object=this.addManager(this.injection(temp),temp);
                } else {
                    return (T) this.objectMap.get(temp);
                }
            } else {
                object = this.createObj(clazz);
            }
            if(object!=null) {
                return (T) this.injection(object);
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private Object injection(Object object) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (ClassUtils.isPrimitive(field.getType())!=null) {
                    this.injectionProperties(field,object);
                    continue;
                }
                Class type = field.getType();
                Object obj;
                if (type == object.getClass()) {
                    obj = object;
                } else {
                    obj = this.exists(type, field);
                }
                Map.Entry<Class,Object> temp;
                if(obj!=null && obj instanceof Map.Entry && (temp=(Map.Entry)obj).getValue()==null){
                    obj=this.addManager(this.injection(temp.getKey()),temp.getKey());
                }
                if(obj!=null && obj instanceof Map.Entry && (temp=(Map.Entry)obj).getValue()!=null){
                    temp.setValue(this.addAop(temp.getValue()));
                    injectionProperties(field,temp.getValue());
                    field.set(object,temp.getValue());
                }else if(obj != null && !(obj instanceof Map.Entry)){
                    obj = this.addAop(obj);
                    injectionProperties(field,obj);
                    field.set(object,obj);
                }
            }
            return this.addAop(object);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 类型是否存在总库
     * @param clazz
     * @return
     */
    private Map.Entry<Class,Object> exists(Class clazz, Field field) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if(field.isAnnotationPresent(Autowired.class)){
            Autowired annotation=field.getAnnotation(Autowired.class);
            if(annotation.type().length()>0){
                clazz=Class.forName(annotation.type());
            }
        }
        for (Map.Entry<Class,Object> item : this.objectMap.entrySet()) {
            if(!item.getKey().isInterface()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    if(!field.getAnnotation(Autowired.class).value()) {
                        if (this.isClassName(item.getKey(), field.getName())) {
                            return item;
                        }
                    }else if(this.isImplClass(item.getKey(), clazz)){
                        return item;
                    }
                } else if(this.injectionType==InjectionType.AutoAll&&this.isImplClass(item.getKey(), clazz)) {
                    return item;
                }
            }
        }
        if(!clazz.isInterface()&&field.isAnnotationPresent(Autowired.class)){
            Class temp=this.addManager(clazz);
            Object object=Trusteeship.this.addManager(Trusteeship.this.injection(temp),temp);
            return new Map.Entry<Class, Object>() {
                @Override
                public Class getKey() {
                    return temp;
                }
                @Override
                public Object getValue() {
                    return object;
                }
                @Override
                public Object setValue(Object value) {
                    return Trusteeship.this.addManager(value,temp);
                }
            };
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
     * @param clazz
     * @return
     */
    private Class impl(Class clazz){
        for(Class item : this.objectMap.keySet()){
            if(!item.isInterface() && this.isImplClass(item,clazz)){
                return item;
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
        if(clazz1==null){
            return false;
        }
        if(!clazz1.isInterface()&&clazz1.isAssignableFrom(clazz2)){
            return true;
        }
        for(Class c : clazz1.getInterfaces()){
            if(c==clazz2){
                return true;
            }else{
                if(isImplClass(c,clazz2)) {
                    return true;
                }
            }
        }
        return false;
    }
    private Object createObj(Class clazz) throws IllegalAccessException, InstantiationException {
        Constructor[] constructors=clazz.getConstructors();
        if(constructors.length!=0){
           if(Stream.of(constructors).filter(c->c.getParameterTypes().length==0).count()==0){
               return null;
           }
        }
        return clazz.newInstance();
    }

    /**
     * 注入配置文件(set方法)
     * @param field
     * @param object
     * @throws IllegalAccessException
     */
    private void injectionProperties(Field field, Object object) throws InvocationTargetException, IllegalAccessException {
        Property property=field.getAnnotation(Property.class);
        if(property != null) {
            if (property.value().substring(property.value().length() - 1).equals("*")) {
                Method[] methods = object.getClass().getMethods();
                for (Method method : methods) {
                    method.setAccessible(true);
                    if (method.getName().length() > 3 && method.getName().substring(0, 3).equals("set")) {
                        for (Properties properties : this.propertiesList) {
                            String name = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                            String value = properties.getProperty(property.value() + name);
                            if (value != null) {
                                Class params = method.getParameterTypes()[0];
                                method.invoke(object,this.convertBaseTypeValue(params,value));
                            }
                        }
                    }
                }
            }else{
               this.propertiesList.stream().forEach(properties -> {
                    String value=properties.getProperty(property.value());
                   if(value!=null){
                       try {
                           field.set(object,this.convertBaseTypeValue(field.getType(),value));
                       } catch (IllegalAccessException e) {
                           e.printStackTrace();
                       } catch (InvocationTargetException e) {
                           e.printStackTrace();
                       }
                   }
               });
            }
        }
    }
    private Object convertBaseTypeValue(Class params,String value) throws InvocationTargetException, IllegalAccessException {
        if (params.isAssignableFrom(Integer.class) || params.getName().equals("int")) {
            return Integer.valueOf(value);
        } else if (params.isAssignableFrom(Byte.class) || params.getName().equals("byte")) {
           return Byte.valueOf(value);
        } else if (params.isAssignableFrom(Short.class) || params.getName().equals("short")) {
            return Short.valueOf(value);
        } else if (params.isAssignableFrom(Long.class) || params.getName().equals("long")) {
            return Long.valueOf(value);
        } else if (params.isAssignableFrom(Boolean.class) || params.getName().equals("boolean")) {
            return Boolean.valueOf(value);
        } else if (params.isAssignableFrom(Float.class) || params.getName().equals("float")) {
           return Float.valueOf(value);
        } else if (params.isAssignableFrom(Double.class) || params.getName().equals("double")) {
           return Double.valueOf(value);
        } else if (params.isAssignableFrom(Character.class) || params.getName().equals("char")) {
           return value.charAt(0);
        } else {
           return value;
        }
    }
    private Object addAop(Object object) {
        return new InvocationHandlerImpl(this.aopMap.values()).bind(object);
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
                aopClass.setPointcut(PackUtils.generateNamePattern(pointcut.value()));
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
    private void addAutowiredManager(Class clazz) throws IllegalAccessException, InstantiationException {
        Repository repository = (Repository) clazz.getAnnotation(Repository.class);
        Service service = (Service) clazz.getAnnotation(Service.class);
        Component component = (Component) clazz.getAnnotation(Component.class);
        if (!clazz.isInterface() && (repository != null || service != null || component != null)) {
           this.addManager(clazz);
        }
    }
    private Class addManager(Class clazz){
        this.objectMap.put(clazz,null);//不要接口
        return clazz;
    }
    private Object addManager(Object object){
        this.objectMap.put(object.getClass(),object);
        return object;
    }
    private Object addManager(Object object,Class clazz){
        this.objectMap.put(clazz,object);
        return object;
    }
    private void addAopManager(Class clazz) {
        try {
            Aspect aspect = (Aspect) clazz.getAnnotation(Aspect.class);
            if (aspect != null) {
                Object object = this.createObj(clazz);
                this.aopMap.put(clazz.getName(), this.analysisAopClass(object));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void addDatabaseManager(Class clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException {
        AutowiredInterface autowiredInterface=null;
        if (this.isImplClass(clazz, MybatisTrusteeship.class)) {
            Object object=this.injection(clazz);
            autowiredInterface = GenerateBean.mybatis((MybatisTrusteeship) object);
        }else if(this.isImplClass(clazz, HibernateTrusteeship.class)){
            Object object=this.injection(clazz);
            autowiredInterface=GenerateBean.hibernate((HibernateTrusteeship) object);
        }
        if(autowiredInterface!=null){
            autowiredInterface.beanList().stream().forEach(item->this.addManager(item));
            setTransactionManager(autowiredInterface);
        }
    }
    private void setTransactionManager(AutowiredInterface autowiredInterface) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        this.transactionManager=autowiredInterface.transactionManager();
        this.injection(this.transactionManager.getTransaction());
        this.addManager(transactionManager);
        TransactionAop transactionAop=  this.injection(TransactionAop.class);
        AopClass baseAopClass=this.analysisAopClass(transactionAop);
        for(TransactionMethod transactionMethod :transactionManager.getTransactionMethodList()) {
            AopClass aopClass = baseAopClass.clone();
            aopClass.setObject(transactionAop);
            StringBuilder sb = new StringBuilder();
            sb.append(".*");
            sb.append(transactionManager.getPackName());
            sb.append(transactionMethod.getName().replace("*",".*"));
            sb.append("\\(.*\\)");
            aopClass.setPointcut(Pattern.compile(sb.toString()));
            this.aopMap.put(transactionAop.getClass().getName(),aopClass);
        }
    }
    public <T> T get(Class<T> clazz){
        for(Map.Entry<Class,Object> item : this.objectMap.entrySet()){
            if(this.isImplClass(item.getKey(),clazz)){
                return (T) item.getValue();
            }
        }
        return null;
    }
    public <T> T add(Class<T> clazz){
        if(clazz.isAnnotationPresent(Aspect.class)){
           this.addAopManager(clazz);
            return null;
        }else {
            return (T) this.addManager(this.injection(this.addManager(clazz)), clazz);
        }
    }

}