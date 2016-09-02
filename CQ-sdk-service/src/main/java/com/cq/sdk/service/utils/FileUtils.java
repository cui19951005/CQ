package com.cq.sdk.service.utils;

import java.io.File;
import java.util.ArrayList;
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
        String root=FileUtils.class.getResource("/").getFile();
        root=root.substring(1);
        String[] array=packName.split("\\.");
        return fileList(new ArrayList<File>(),new File(root),array,0,true);
    }
    private static final List<File> fileList(List<File> fileList,File path,String[] array,int level,boolean fileMode){

        String nowStr=array[level];
        int type=wildcard(nowStr);
        File[] files=path.listFiles();
        for(File file : files){
            if(type==0 || type == 2 || type ==3 || type==4 || type==5){
                if(level+1==array.length){
                    addList(fileMode,file,type,nowStr,fileList);
                }else if(file.isDirectory() && wildcard(type,nowStr,file.getName())) {
                    fileList(fileList, file, array, ++level,fileMode);
                }
            }else if(type==1){
                if(level+1==array.length){
                   loadFile(file,fileList);
                }else {
                    if(file.isDirectory() && wildcard(wildcard(array[level+1]),array[level+1],file.getName())){
                        if(level+2==array.length){
                            loadFile(file,fileList);
                        }else {
                            fileList(fileList, file, array, level + 2, fileMode);
                        }
                    }else if(file.isDirectory()){
                        fileList(fileList,file,array,level,fileMode);
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
        }else if(type==2 && name.lastIndexOf(str.substring(0,str.length()-1))==name.length()-str.length()+1){
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
}
