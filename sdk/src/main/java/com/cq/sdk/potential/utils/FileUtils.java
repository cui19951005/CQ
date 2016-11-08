package com.cq.sdk.potential.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/9/2.
 */
public class FileUtils {
    /**
     * 通配符支持com.cq.**(表示通配多级目录).qq.*(通配一级目录)
     * @param packName
     * @return
     */
    public static final List<File> findFileList(String packName){
        String[] array;
        if((packName.length()>11&&packName.substring(0,11).equals("classpath*:")) || (packName.length()>10&&packName.substring(0,10).equals("classpath:"))) {
            packName = packName.replace("classpath*:", "").replace("classpath:", "");
            array=packName.split("/");
        }else{
            array=packName.split("\\.");
        }
        StringBuilder sb=new StringBuilder("/");
        for(int i=0;i<array.length;i++){
            if(array[i].indexOf("*")==-1){
                sb.append(array[i]);
                sb.append("/");
            }else{
                array=Arrays.copyOfRange(array,i,array.length);
                break;
            }
        }
        URL url=Thread.currentThread().getClass().getResource(sb.toString());
        if(url!=null) {
            File file=new File(url.getFile());
            return FileUtils.fileList(new ArrayList(),file , array, 0, true);
        }else{
            return new ArrayList<>();
        }
    }
    private static final List<File> fileList(List<File> fileList,File path,String[] array,int level,boolean fileMode){
        String nowStr=array[level];
        int type=wildcard(nowStr);
        File[] files=path.listFiles();
        if(files!=null) {
            for (File file : files) {
                if (type == 0 || type == 2 || type == 3 || type == 4 || type == 5) {
                    if (level + 1 == array.length) {
                        addList(fileMode, file, type, nowStr, fileList);
                    } else if (file.isDirectory() && wildcard(type, nowStr, file.getName())) {
                        fileList(fileList, file, array, ++level, fileMode);
                    }
                } else if (type == 1) {
                    if (level + 1 == array.length) {
                        loadFile(file, fileList);
                    } else {
                        if (file.isDirectory() && wildcard(wildcard(array[level + 1]), array[level + 1], file.getName())) {
                            if (level + 2 == array.length) {
                                loadFile(file, fileList);
                            } else {
                                fileList(fileList, file, array, level + 2, fileMode);
                            }
                        } else if (file.isDirectory()) {
                            fileList(fileList, file, array, level, fileMode);
                        }
                    }
                }
            }
        }
        return fileList;
    }
    private static final void addList(boolean fileMode,File file,int type,String nowStr,List<File> fileList){
        if(fileMode){
            if (file.isFile()&&wildcard(type,nowStr, file.getName())) {
                fileList.add(file);
            }
        }else if(file.isDirectory() &&wildcard(type,nowStr, file.getName())){
            for(File item : file.listFiles()){
                if(item.isFile()){
                    fileList.add(item);
                }
            }
        }
    }
    private static final boolean wildcard(int type,String str,String name){
        if(type==0 || type==1){//星号通配任何都通过
            return true;
        }else if(type==2 && name.lastIndexOf(str.substring(1,str.length()))==name.length()-str.length()+1){
            return true;
        }else if(type==3 && name.indexOf(str.substring(1))==0){
            return true;
        }else if(type==4 ){
            String[] array=str.split("\\*");
            if(name.indexOf(array[0])==0 && name.lastIndexOf(array[1])==name.length()-array[1].length()){
                return true;
            }
        }else if(type==5 && str.equals(name)){
            return true;
        }
        return false;
    }
    private static final int wildcard(String string){
        Pattern pattern=Pattern.compile("\\*\\*+");
        Matcher matcher=pattern.matcher(string);
        int type=-1;
        if(string.equals("*")){
            type=0;
        }else if(matcher.find() && matcher.group().equals(string)){
            type=1;
        }else if(string.substring(0,1).equals("*")) {
            type=2;
        }else if(string.substring(string.length()-1).equals("*")){
            type=3;
        }else if(string.indexOf("*")!=-1){
            type=4;
        }else{//没有通配
            type=5;
        }
        return type;
    }
    private static final void loadFile(File path,List<File> fileList){
        for(File file : path.listFiles()){
            if(file.isFile()){
                fileList.add(file);
            }else if(file.isDirectory()){
                loadFile(file,fileList);
            }
        }
    }
    public static final List<File> loadClass(String packName){
        StringBuilder sb=new StringBuilder("/");
        String[] array=packName.split("\\.");
        int index=-1;
        for(int i=0;i<array.length;i++){
            if(array[i].indexOf("*")==-1 && array[i].indexOf("?")==-1) {
                sb.append(array[i]);
            }else{
                break;
            }
            sb.append("/");
            index+=array[i].length()+1;
        }
        packName=packName.substring(index);
        List<File> fileList=new ArrayList<>();
        FileUtils.findList(new File(Thread.currentThread().getClass().getResource(sb.substring(0,sb.length()-1)).getFile()),fileList,PackUtils.generateFilePattern(packName),null);
        return fileList;
    }
    private static final void findList(File base,List<File> list,Pattern match,File now){
        if(now==null)now=base;
        File[] files=now.listFiles();
        if(files==null)return;
        for(File file : files){
            String path=file.getAbsolutePath();
            path=path.substring(base.getAbsolutePath().length());
            int index=path.indexOf(".");
            path=path.substring(0,index==-1?path.length():index);
            if(match.matcher(path.replace("\\","/")).find()&&file.isFile()){
                list.add(file);
            }else if(file.isDirectory()){
                FileUtils.findList(base,list,match,file);
            }
        }
    }
}
