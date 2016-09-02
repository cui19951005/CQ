package com.cq.sdk.service.potential.mybatis.utils;

import com.cq.sdk.service.potential.inter.AutowiredInterface;
import com.cq.sdk.service.potential.mybatis.MybatisTrusteeship;
import com.cq.sdk.service.potential.utils.ClassObj;
import com.cq.sdk.service.utils.FileUtils;
import com.cq.sdk.service.utils.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
public class MybatisGenerateBean {
    public static final AutowiredInterface trusteeship(MybatisTrusteeship mybatisTrusteeship){
        try {
            DataSource dataSource = mybatisTrusteeship.dataSource();
            Object sqlSessionFactory = mybatisTrusteeship.sqlSessionFactory(dataSource);
            Object sqlSession = mybatisTrusteeship.sqlSession(sqlSessionFactory);
            List<File> fileList = FileUtils.findFileList(mybatisTrusteeship.mapperLocation());
            final List<ClassObj> objectList = new ArrayList<ClassObj>();
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
        }
        return null;
    }
}
