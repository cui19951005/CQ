package com.cq.sdk.utils;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * json转换
 * Created by CuiYaLei on 2016/8/27.
 */
public final class Json {
    public static final String toJson(Object object){
        StringBuilder sb=new StringBuilder();
        sb.append(field(null,object.getClass(),object));
        return sb.toString();
    }
    private static final String object(Object object){
        try {
            StringBuilder sb=new StringBuilder();
            sb.append("{");
            Class objClass = object.getClass();
            Field[] fields = objClass.getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                field.setAccessible(true);
                Class type = field.getType();
                Object value = field.get(object);
                sb.append(field(field.getName(), type, value));
                if (j + 1 != fields.length) {
                    sb.append(",");
                }
            }
            sb.append("}");
            return sb.toString();
        }catch (Exception e){
            return "{}";
        }
    }
    private static final String field(Object name,Class type,Object value){
        StringBuilder sb=new StringBuilder();
        if(name!=null) {
            sb.append("\"");
            sb.append(name);
            sb.append("\":");
        }
        if (value instanceof String
                || value instanceof Character
                ||  type.getName().equals("char")
                ) {
            sb.append("\"");
            sb.append(value);
            sb.append("\"");
        }else if(value instanceof Byte
                || value instanceof Short
                || value instanceof Integer
                || value instanceof Long
                || value instanceof Boolean
                || value instanceof Float
                || value instanceof Double
                || type.getName().equals("int") || type.getName().equals("byte") || type.getName().equals("short")
                || type.getName().equals("long") || type.getName().equals("float")
                || type.getName().equals("double")){
            sb.append(value);
        }
        else if(value instanceof java.util.Date){
            sb.append("\"");
            sb.append(new Date((java.util.Date)value));
            sb.append("\"");
        } else if (type.isArray()){
            sb.append(baseTypeArray(type,value));
        }else if (value instanceof Map){
            sb.append("{");
            Map<Object,Object> map= (Map<Object,Object>) value;
            if(map.size()>0) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    sb.append(format(entry.getKey(), toJson(entry.getValue()),true));
                    sb.append(",");
                }
                return sb.substring(0, sb.length() - 1)+"}";
            }
            return sb.toString()+"}";
        }else if (value instanceof List) {
            List list = (List) value;
            sb.append("[");
            sb.append(arrayObj(list.toArray(new Object[list.size()])));
            sb.append("]");
        }else if(value instanceof Enum) {
            sb.append("\"");
            sb.append(value.toString());
            sb.append("\"");
        }else{
            sb.append(object(value));
        }
        return sb.toString();
    }
    private static final String format(Object name,Object value,boolean isJson){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("\"");
        stringBuilder.append(name);
        stringBuilder.append("\":");
        if(isJson) {
            stringBuilder.append(value);
        }else{
            stringBuilder.append("\"");
            stringBuilder.append(value);
            stringBuilder.append("\"");
        }
        return stringBuilder.toString();
    }
    private static final String baseTypeArray(Class type,Object val) {
        StringBuilder sb = new StringBuilder("[");
        Object[] objArray = null;
        if (val instanceof byte[]) {
            byte[] bytes = (byte[]) val;
            for (int i = 0; i < bytes.length; i++) {
                sb.append( bytes[i]);
                if (bytes.length != i + 1) {
                    sb.append(",");
                }
            }
        } else if (val instanceof short[]) {
            short[] shorts = (short[]) val;
            for (int i = 0; i < shorts.length; i++) {
                sb.append(shorts[i]);
                if (shorts.length != i + 1) {
                    sb.append(",");
                }
            }
        } else if (val instanceof int[]) {
            int[] ints = (int[]) val;
            for (int i = 0; i < ints.length; i++) {
                sb.append(ints[i]);
                if (ints.length != i + 1) {
                    sb.append(",");
                }
            }
        } else if (val instanceof long[]) {
            long[] longs = (long[]) val;
            for (int i = 0; i < longs.length; i++) {
                sb.append(longs[i]);
                if (longs.length != i + 1) {
                    sb.append(",");
                }
            }
        } else if (val instanceof float[]) {
            float[] floats = (float[]) val;
            for (int i = 0; i < floats.length; i++) {
                sb.append(floats[i]);
                if (floats.length != i + 1) {
                    sb.append(",");
                }
            }
        } else if (val instanceof double[]) {
            double[] doubles = (double[]) val;
            for (int i = 0; i < doubles.length; i++) {
                sb.append(doubles[i]);
                if (doubles.length != i + 1) {
                    sb.append(",");
                }
            }
        } else if (val instanceof char[]) {
            char[] chars = (char[]) val;
            for (int i = 0; i < chars.length; i++) {
                sb.append( chars[i]);
                if (chars.length != i + 1) {
                    sb.append(",");
                }
            }
        } else if (val instanceof boolean[]) {
            boolean[] booleans = (boolean[]) val;
            for (int i = 0; i < booleans.length; i++) {
                sb.append(booleans[i]);
                if (booleans.length != i + 1) {
                    sb.append(",");
                }
            }
        } else if (val instanceof String[]
                || val instanceof Byte[]
                || val instanceof Short[]
                || val instanceof Integer[]
                || val instanceof Long[]
                || val instanceof Boolean[]
                || val instanceof Character[]
                || val instanceof Float[]
                || val instanceof Double[]
                ) {
            objArray = (Object[]) val;
        }else if(type.isArray()){
            Object[] objects= (Object[]) val;
            sb.append(arrayObj(objects));
        }
        if(objArray!=null){
            Object[] objects= (Object[]) val;
            for(int i=0;i<objects.length;i++){
                sb.append(objects[i]);
                if(objects.length!=i+1){
                    sb.append(",");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }
    private static final String arrayObj(Object[] objects){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<objects.length;i++){
            sb.append(Json.toJson(objects[i]));
            if(objects.length!=i+1){
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static final <T> T fromJson(String json,Class<T> type){
        Map<String, Object> map = new HashMap<>();
        Json.analysis(json, 0, "", map);
        return (T)field(map.get(Json.emptyName+"0"),null,new ClassSuper(type),new ArrayList<>());
    }
    private static <T> T field(Object json,Field field,ClassSuper type,List<String> name){
        try {
            if (type.getClazz().isAssignableFrom(String.class)
                    || type.getClazz().isAssignableFrom(Byte.class)
                    || type.getClazz().isAssignableFrom(Short.class)
                    || type.getClazz().isAssignableFrom(Integer.class)
                    || type.getClazz().isAssignableFrom(Long.class)
                    || type.getClazz().isAssignableFrom(Float.class)
                    || type.getClazz().isAssignableFrom(Double.class)
                    || type.getClazz().isAssignableFrom(Boolean.class)
                    || type.getClazz().isAssignableFrom(Character.class)
                    || type.getName().equals("int") || type.getName().equals("byte") || type.getName().equals("short")
                    || type.getName().equals("long") || type.getName().equals("char") || type.getName().equals("float")
                    || type.getName().equals("double") || type.getName().equals("boolean")
                    ) {
                if (name == null) {
                    if (json instanceof Map) {
                        for(Map.Entry<String,Object> entry: ((Map<String,Object>)json).entrySet()) {
                            return (T) Json.convert(json, type, entry.getValue());
                        }
                    }else{
                        return (T) Json.convert(json,type,json);
                    }
                }else{
                    if (json instanceof Map) {
                        Map<String,Object> map=(Map<String,Object>)json;
                        return (T) Json.convert(json, type,Json.exists(map,name,0));
                    }
                }
            } else if (type.getClazz().isArray()) {
                List list = null;
                if(name==null) {
                    if(json instanceof Map) {
                        for (Map.Entry<String, Object> entry : ((Map<String, Object>)json).entrySet()) {
                            if (entry.getValue() instanceof List) {
                                list = (List) entry.getValue();
                                break;
                            }
                        }
                    }
                }else{
                    if(json instanceof Map) {
                        Object value=Json.exists((Map<String, Object>)json,name,0);
                        if(value instanceof List){
                            list= (List) value;
                        }else if(value==null){
                            list=new ArrayList<>();
                        }
                    }
                }
                if (type.getClazz().isAssignableFrom(byte[].class)) {
                    byte[] temp = new byte[list.size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = Byte.valueOf(list.get(i).toString());
                    }
                    return (T) temp;
                } else if (type.getClazz().isAssignableFrom(short[].class)) {
                    short[] temp = new short[list.size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = Short.valueOf(list.get(i).toString());
                    }
                    return (T) temp;
                } else if (type.getClazz().isAssignableFrom(int[].class)) {
                    int[] temp = new int[list.size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = Integer.valueOf(list.get(i).toString());
                    }
                    return (T) temp;
                } else if (type.getClazz().isAssignableFrom(long[].class)) {
                    long[] temp = new long[list.size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = Long.valueOf(list.get(i).toString());
                    }
                    return (T) temp;
                } else if (type.getClazz().isAssignableFrom(boolean[].class)) {
                    boolean[] temp = new boolean[list.size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = Boolean.valueOf(list.get(i).toString());
                    }
                    return (T) temp;
                } else if (type.getClazz().isAssignableFrom(char[].class)) {
                    char[] temp = new char[list.size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = list.get(i).toString().charAt(0);
                    }
                    return (T) temp;
                } else if (type.getClazz().isAssignableFrom(float[].class)) {
                    float[] temp = new float[list.size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = Float.valueOf(list.get(i).toString());
                    }
                    return (T) temp;
                } else if (type.getClazz().isAssignableFrom(double[].class)) {
                    double[] temp = new double[list.size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = Double.valueOf(list.get(i).toString());
                    }
                    return (T) temp;
                } else if (type.getClazz().isAssignableFrom(Byte[].class)
                        || type.getClazz().isAssignableFrom(Short[].class)
                        || type.getClazz().isAssignableFrom(Integer[].class)
                        || type.getClazz().isAssignableFrom(Long[].class)
                        || type.getClazz().isAssignableFrom(Boolean[].class)
                        || type.getClazz().isAssignableFrom(Float[].class)
                        || type.getClazz().isAssignableFrom(Double[].class)
                        || type.getClazz().isAssignableFrom(String[].class)
                        ) {
                    try {
                        Object object = Class.forName(type.getName().substring(2, type.getName().length() - 1));
                        Method method = type.getClazz().getMethod("valueOf", String.class);
                        Object[] temp = (Object[]) Array.newInstance(object.getClass(), list.size());
                        for (int i = 0; i < temp.length; i++) {
                            temp[i] = method.invoke(object, list.get(i));
                        }
                        return (T) temp;
                    } catch (Exception e) {
                        return null;
                    }
                } else if (type.getClazz().isAssignableFrom(Character[].class)) {
                    Character[] temp = new Character[list.size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = list.get(i).toString().charAt(0);
                    }
                    return (T) temp;
                } else {
                    Class objType = Class.forName(type.getName().substring(2, type.getName().length() - 1));
                    Object[] temp = (Object[]) Array.newInstance(objType, list.size());
                    for (int i = 0; i < list.size(); i++) {
                        temp[i] = Json.field(json, objType, null);
                    }
                    return (T) temp;
                }
            } else if (Json.isAchieve(type.getClazz(), List.class)) {
                List list=null;
                if(json instanceof Map) {
                    if(name==null) {
                        for (Map.Entry<String, Object> entry : ((Map<String, Object>) (json)).entrySet()) {
                            if (entry.getValue() instanceof List && entry.getKey().indexOf(Json.emptyName) != -1) {
                                list = (List) entry.getValue();
                            }
                        }
                    }else{
                        list = (List)Json.exists((Map<String, Object>)json,name,0);
                    }
                }else if(json instanceof List){
                    list= (List) json;
                }
                if(field==null){
                    return (T) list;
                }else{
                    Class clazz=Class.forName(field.getType().getName());//java.util.List<java.lang.String>
                    List objList;
                    if(clazz.isInterface()){
                        objList=new ArrayList(list.size());
                    }else{
                        objList=(List) clazz.newInstance();
                    }
                    ClassSuper listClazz=null;
                    Type fieldType=field.getGenericType();
                    if(fieldType instanceof ParameterizedTypeImpl) {//泛型有参
                        ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) fieldType;
                        for (Type itemType : parameterizedType.getActualTypeArguments()) {
                            if(itemType instanceof ParameterizedTypeImpl) {//泛型嵌套
                                ParameterizedTypeImpl temp = (ParameterizedTypeImpl) itemType;
                                listClazz = new ClassSuper(temp.getRawType());
                                listClazz.setParams(temp.getActualTypeArguments());
                            }else{//无嵌套
                                listClazz = new ClassSuper(Class.forName(itemType.toString()));
                            }
                        }
                    }else if(fieldType instanceof  Class){//泛型无参数
                        listClazz=new ClassSuper(Object.class);
                    }
                    for(int i=0;i<list.size();i++){
                        objList.add(Json.convert(json,listClazz==null?new ClassSuper(String.class):listClazz,list.get(i)));
                    }
                    return (T) objList;
                }
            }else if(Json.isAchieve(type.getClazz(),Map.class)){

                if(field==null){
                    Map map;
                    if (type.getClazz().isInterface()) {
                        map = new HashMap();
                    } else {
                        map = (Map) type.getClazz().newInstance();
                    }
                    if(json instanceof Map) {
                        if(type.getParams()==null) {//无泛型参数指定
                            for (Map.Entry<String, Object> entry : ((Map<String, Object>) json).entrySet()) {
                                map.put(entry.getKey(),entry.getValue());
                            }
                            return (T) map;
                        }else{//有参数指定类型
                            for (Map.Entry<String, Object> entry : ((Map<String, Object>) json).entrySet()) {//将数据转换为指定类型
                                Object key=null;
                                if(type.getParams()[0] instanceof ParameterizedTypeImpl){//泛型套泛型
                                    ParameterizedTypeImpl parameterizedType= (ParameterizedTypeImpl) type.getParams()[0];
                                    ClassSuper classSuper=new ClassSuper(parameterizedType.getRawType());
                                    classSuper.setParams(parameterizedType.getActualTypeArguments());
                                    key=Json.convert(json,classSuper,entry.getValue());
                                }else{
                                    key=Json.convert(json,new ClassSuper(type.getParams()[0].toString()),entry.getKey());
                                }
                                Object value;
                                if(type.getParams()[1] instanceof ParameterizedTypeImpl){//泛型套泛型
                                    ParameterizedTypeImpl parameterizedType= (ParameterizedTypeImpl) type.getParams()[1];
                                    ClassSuper classSuper=new ClassSuper(parameterizedType.getRawType());
                                    classSuper.setParams(parameterizedType.getActualTypeArguments());
                                    value=Json.convert(json,classSuper,entry.getValue());
                                }else{
                                    value=Json.convert(json,new ClassSuper(type.getParams()[1].toString()),entry.getValue());
                                }
                                map.put(key,value);
                            }
                            return (T) map;
                        }
                    }
                }else{
                    Type mapType=field.getGenericType();
                    if(mapType instanceof Class) {//泛型无类型
                        ClassSuper classSuper=new ClassSuper((Class) mapType);
                        return Json.field(Json.exists((Map<String, Object>) json,name,0),null,classSuper,null);
                    }else if(mapType instanceof ParameterizedTypeImpl){//泛型嵌套
                        ParameterizedTypeImpl parameterizedType= (ParameterizedTypeImpl) mapType;
                        ClassSuper classSuper=new ClassSuper(parameterizedType.getRawType());
                        classSuper.setParams(parameterizedType.getActualTypeArguments());
                        return Json.field(Json.exists((Map<String, Object>) json,name,0),null,classSuper,null);
                    }
                }
            }else if(type.getClazz().isEnum()){
                if(json instanceof Map){
                    String value= (String) Json.exists((Map<String, Object>) json,name,0);
                    return (T) Enum.valueOf(type.getClazz(),value);
                }else{
                    return (T) Enum.valueOf(type.getClazz(), (String) json);
                }
            }
            else {
                Object obj=type.getClazz().newInstance();
                for(Field f : type.getClazz().getDeclaredFields()){
                    f.setAccessible(true);
                    name.add(f.getName());
                    f.set(obj,Json.field(json,f,new ClassSuper(f.getType()),name));
                    name.remove(name.size()-1);
                }
                return (T) obj;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 转换为目的类型
     * @param json 回传json对象
     * @param type 目的类型
     * @param obj 当前数据对象
     * @return
     */
    private static final Object convert(Object json,ClassSuper type,Object obj){
        try {
            if(type.getClazz().isAssignableFrom(Object.class)){
                return obj;
            }
            else if (type.getClazz().isAssignableFrom(String.class)) {
                return obj.toString();
            } else if (type.getClazz().isAssignableFrom(Byte.class) || type.getName().equals("byte")) {
                return Byte.valueOf(obj.toString());
            } else if (type.getClazz().isAssignableFrom(Short.class) || type.getName().equals("short")) {
                return Short.valueOf(obj.toString());
            } else if (type.getClazz().isAssignableFrom(Integer.class) || type.getName().equals("int")) {
                return Integer.valueOf(obj.toString());
            } else if (type.getClazz().isAssignableFrom(Long.class) || type.getName().equals("long")) {
                return Long.valueOf(obj.toString());
            } else if (type.getClazz().isAssignableFrom(Float.class) || type.getName().equals("float")) {
                return Float.valueOf(obj.toString());
            } else if (type.getClazz().isAssignableFrom(Double.class) || type.getName().equals("double")) {
                return Double.valueOf(obj.toString());
            } else if (type.getClazz().isAssignableFrom(Boolean.class) || type.getName().equals("boolean")) {
                return Boolean.valueOf(obj.toString());
            } else if (type.getClazz().isAssignableFrom(Character.class) || type.getName().equals("char")) {
                return obj.toString().charAt(0);
            }  else {
                return Json.field(obj, null, type, null);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    private static final boolean isAchieve(Class type,Class inter){
        if(type.isAssignableFrom(inter)){
            return true;
        }
        for(Class c :type.getInterfaces()){
            if(c==inter){
                return true;
            }
        }
        return false;
    }
    private static final String quotes="\"";
    private static final String emptyName="!@#$%^&*";
    private static void analysis(String json,int baseIndex,String name,Map<String,Object> map){
        if(baseIndex<0 || json.length()<baseIndex+1)return;
        ifType(json,json.substring(baseIndex,baseIndex+1),name,baseIndex,map,true);
    }
    private static void disp(String json,int baseIndex,Map<String,Object> map){
        int leftIndex=json.indexOf(quotes,baseIndex)+1;
        int rightIndex=json.indexOf(quotes,leftIndex+1);
        String name=json.substring(leftIndex,rightIndex);
        baseIndex=leftIndex-1;//到引号前
        leftIndex=rightIndex+1;//过引号
        if(leftIndex+1<json.length()&&json.substring(leftIndex,leftIndex+1).equals(":")){
            leftIndex++;
            String type=json.substring(leftIndex,leftIndex+1);
            if(type.equals(quotes)){//内容过"其他无需过符号
                leftIndex++;
            }
            ifType(json,type,name,leftIndex,map,false);
        }else{
            //取"qq"
            ifType(json,json.substring(baseIndex,rightIndex+1),"",baseIndex,map,false);
        }

    }
    private static void ifType(String json,String type,String name,int baseIndex,Map<String,Object> map,boolean parent){
        int rightIndex ;
        if(type.equals(quotes)){//普通类型
            if(parent){
                disp(json,baseIndex,map);
            }else {
                rightIndex = json.indexOf(quotes, baseIndex);
                String value = json.substring(baseIndex, rightIndex);
                map.put(name, value);
                int index=json.indexOf(",",rightIndex + 1);
                if(index==-1)return;
                Json.analysis(json, index+1,"", map);//继续下一个
            }
        }else if(type.equals("[")) {//数组类型
            List<Object> list=new ArrayList<Object>();
            rightIndex=Json.findRight(json,baseIndex+1,"[","]")+1;//+1到]
            String arrayText=json.substring(baseIndex,rightIndex);
            Pattern pattern=Pattern.compile("\\[(\\d|\"\\w\"|,|-)+\\]");
            Matcher matcher=pattern.matcher(arrayText);
            if(matcher.find()&&matcher.group().equals(arrayText)){
                String[] array=arrayText.substring(1,arrayText.length()-1).split(",");
                for(String val : array){
                    list.add(val);
                }
                if(name.length()==0){
                    name=emptyName+baseIndex;
                }
                map.put(name,list);
                Json.analysis(json,rightIndex+1,"",map);//+1 ,到"
            }else {
                Map<String,Object> temp=new HashMap<String,Object>();
                Json.analysis(arrayText.substring(1,arrayText.length()-1), 0,name, temp);
                for(Map.Entry<String,Object> entry : temp.entrySet()){
                    list.add(entry.getValue());
                }
                if(name.length()==0){
                    name=emptyName+baseIndex;
                }
                map.put(name,list);
            }
        }else if(type.equals("{")){//对象
            if(name.length()==0){
                name=Json.emptyName+baseIndex;
            }
            Map<String, Object> temp = new HashMap<>();
            rightIndex = Json.findRight(json, baseIndex+1, "{", "}");
            Json.analysis(json.substring(baseIndex+1, rightIndex), 0, "", temp);//解析对象baseIndex没过{第一次
            map.put(name,temp);//将解析的对象添加到属性
            Json.analysis(json,rightIndex+2,"",map);//继续下一个+1过}+1过,
        }else{
            char[] text=json.substring(baseIndex).toCharArray();
            rightIndex=text.length;
            for(int i=0;i<text.length;i++){
                if(text[i]=='}' || text[i]==','){
                    rightIndex=i;
                    break;
                }
            }
            String value=json.substring(baseIndex,baseIndex+rightIndex);
            if(name.length()==0) {
                map.put(emptyName + baseIndex, value);
            }else{
                map.put(name,value);
            }
            Json.analysis(json,baseIndex+value.length()+1,"",map);
        }
    }
    /**
     * 递归找出对应的字符，解决嵌套问题
     * @param text 文本
     * @param baseIndex 基础位置要+1过匹配的符号[123,123]位置1
     * @param left 左边文本
     * @param right 右边文本
     * @return
     */
    private static int findRight(String text,int baseIndex,String left,String right,int count){
        int rightIndex;
        rightIndex = text.indexOf(right, baseIndex);
        if(rightIndex!=-1) {
            count--;
        }
        String temp=text.substring(baseIndex,rightIndex);
        int index=0;
        do{
            index=temp.indexOf(left,index+1);
            if(index!=-1){
                count++;
            }else{
                break;
            }
        }while (true);
        index=0;
        do{
            index=temp.indexOf(right,index+1);
            if(index!=-1){
                count--;
            }else{
                break;
            }
        }while (true);
        if(count==0){
            return rightIndex;
        }else{
            return findRight(text,rightIndex+1,left,right,count);
        }
    }
    private static final int findRight(String text,int baseIndex,String left,String right){
        int index=text.indexOf(left, baseIndex);
        int rightIndex=text.indexOf(right,baseIndex);
        if(index>rightIndex){
            return rightIndex;
        }
        if(index!=-1){
            return  findRight(text,index,left,right,2);
        }else{
            return  findRight(text,baseIndex,left,right,1);
        }

    }
    private static final Object exists(Map<String,Object> map,List<String> name,int level){
        for(Map.Entry<String,Object> entry : map.entrySet()){
            if(entry.getKey().equals(name.get(level))){
                if(level+1==name.size()) {
                    return entry.getValue();
                }else if(entry.getValue() instanceof Map){
                    return exists((Map<String, Object>) entry.getValue(), name, ++level);
                }else{
                    return null;
                }
            }
        }
        return null;
    }
    private static class ClassSuper{
        /**
         * 名字(没什么用)
         */
        private String name;
        /**
         * 类
         */
        private Class clazz;
        /**
         * 用于泛型数据类型
         */
        private Type[] params;
        public ClassSuper(Class clazz){
            this.name=clazz.getName();
            this.clazz =clazz;
        }
        public ClassSuper(String name) throws ClassNotFoundException {
            this.name=name;
            this.clazz=Class.forName(name);
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public Type[] getParams() {
            return params;
        }

        public void setParams(Type[] params) {
            this.params = params;
        }
    }
}
