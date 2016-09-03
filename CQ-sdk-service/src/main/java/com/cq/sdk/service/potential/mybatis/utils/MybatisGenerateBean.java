package com.cq.sdk.service.potential.mybatis.utils;

import com.cq.sdk.service.potential.inter.AutowiredInterface;
import com.cq.sdk.service.potential.mybatis.MybatisTrusteeship;
import com.cq.sdk.service.potential.utils.ClassObj;
import com.cq.sdk.service.utils.FileUtils;
import com.cq.sdk.service.utils.Logger;
import com.cq.sdk.service.utils.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/9/2.
 */
public class MybatisGenerateBean {
    public static final AutowiredInterface trusteeship(MybatisTrusteeship mybatisTrusteeship){
        try {
            DataSource dataSource = mybatisTrusteeship.dataSource();
            Object transactionFactory=mybatisTrusteeship.transactionFactory();
            String mapper=mybatisTrusteeship.mappers();
            Class configClass=Class.forName("org.apache.ibatis.session.Configuration");
            Class environment=Class.forName("org.apache.ibatis.mapping.Environment");
            Class transactionFactoryClass=Class.forName("org.apache.ibatis.transaction.TransactionFactory");
            Constructor constructor=environment.getConstructor(String.class,transactionFactoryClass,DataSource.class);
            Object envObj=constructor.newInstance("mybatis",transactionFactory,dataSource);
            Object configObj=configClass.newInstance();
            Class xMLMapperBuilder=Class.forName("org.apache.ibatis.builder.xml.XMLMapperBuilder");
            List<File> fileList=FileUtils.findResources(mybatisTrusteeship.getClass(),mapper);
            for(File file :  fileList){//解析xml文件
                Constructor xMLMapperBuilderCon=xMLMapperBuilder.getConstructor(InputStream.class,configClass,String.class, Map.class);
                Object xMLMapperBuilderObj=xMLMapperBuilderCon.newInstance(new FileInputStream(file),configObj,file.getAbsolutePath(),configClass.getMethod("getSqlFragments").invoke(configObj));
                xMLMapperBuilder.getMethod("parse").invoke(xMLMapperBuilderObj);
            }

            configClass.getMethod("setEnvironment",environment).invoke(configObj,envObj);
            Object sqlSessionFactory = mybatisTrusteeship.sqlSessionFactory(configObj);
            Object sqlSession = mybatisTrusteeship.sqlSession(sqlSessionFactory);
            final List<ClassObj> objectList = new ArrayList<ClassObj>();
            fileList = FileUtils.findFileList(mybatisTrusteeship.mapperLocation());
            for (File file : fileList) {
                ClassObj classObj=new ClassObj(sqlSession.getClass().getMethod("getMapper",Class.class).invoke(sqlSession,Class.forName(StringUtils.filePathConvertPack(file))));
                objectList.add(classObj);
            }
            return new AutowiredInterface() {
                public List<ClassObj> getBeanList() {
                    return objectList;
                }
            };
        }catch (Exception ex){
            ex.printStackTrace();
            Logger.error("mybatis加入托管失败",ex);
        }
        return null;
    }
}
