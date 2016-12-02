package com.cq.sdk.utils;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/11/10.
 */
public final class File extends java.io.File {
    public File(String pathname) {
        super(pathname);
    }

    public File(String parent, String child) {
        super(parent, child);
    }

    public File(java.io.File parent, String child) {
        super(parent, child);
    }

    public File(URI uri) {
        super(uri);
    }

    public String readText(String encoding){
        FileInputStream inputStream=null;
        try {
            if(!this.exists()){
                return null;
            }
            inputStream = new FileInputStream(this);
            int length;
            byte[] bytes=new byte[8192];
            StringBuilder sb=new StringBuilder();
            while ((length=inputStream.read(bytes))>0){
                sb.append(new String(bytes,0,length,encoding));
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            Logger.error("file not exists",e);
        } catch (IOException e) {
            Logger.error("file readText error",e);
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public ByteSet readBytes(){
        FileInputStream inputStream=null;
        try {
            if(!this.exists()){
                return null;
            }
            inputStream = new FileInputStream(this);
            int length;
            byte[] bytes=new byte[8192];
            ByteSet byteSet=new ByteSet();
            while ((length=inputStream.read(bytes))>0){
                byteSet.append(bytes,0,length);
            }
            return byteSet;
        } catch (FileNotFoundException e) {
            Logger.error("file not exists",e);
        } catch (IOException e) {
            Logger.error("file readText error",e);
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public void writeText(String encoding, String text){
        try {
            this.writeBytes(ByteSet.parse(text.getBytes(encoding)));
        } catch (UnsupportedEncodingException e) {
            Logger.error("encoding not find",e);
        }
    }
    public void writeBytes(ByteSet byteSet){
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(this);
            outputStream.write(byteSet.getByteSet());
        } catch (FileNotFoundException e) {
            Logger.error("file not find",e);
        } catch (UnsupportedEncodingException e) {
            Logger.error("encoding not exists",e);
        } catch (IOException e) {
            Logger.error("stream error",e);
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public File getAbsoluteFile() {
        return new File(super.getAbsolutePath());
    }

    @Override
    public File getCanonicalFile() throws IOException {
        return new File(super.getCanonicalFile().getAbsolutePath());
    }

    @Override
    public File[] listFiles(FileFilter filter) {
        String[] fileList=super.list();
        List<File> files=new ArrayList<>();
        for(int i=0;i<fileList.length;i++){
            File file=new File(fileList[i]);
            if(filter==null || filter.accept(file)) {
                files.add(file);
            }
        }
        return files.toArray(new File[files.size()]);
    }

    @Override
    public File[] listFiles() {
        return this.listFiles(pathname -> true);
    }

    @Override
    public File[] listFiles(FilenameFilter filter) {
        String[] fileList=super.list();
        List<File> files=new ArrayList<>();
        for(int i=0;i<fileList.length;i++){
            File file=new File(fileList[i]);
            if(filter==null || filter.accept(this,fileList[i])) {
                files.add(file);
            }
        }
        return files.toArray(new File[files.size()]);
    }

    @Override
    public File getParentFile() {
        return new File(super.getParentFile().getAbsolutePath());
    }
}
