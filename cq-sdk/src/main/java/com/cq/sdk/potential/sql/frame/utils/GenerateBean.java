package com.cq.sdk.potential.sql.frame.utils;

import com.cq.sdk.potential.SynchronizationManager;
import com.cq.sdk.potential.annotation.Autowired;
import com.cq.sdk.potential.inter.AutowiredInterface;
import com.cq.sdk.potential.sql.frame.hibernate.HibernateProxy;
import com.cq.sdk.potential.sql.tx.Transaction;
import com.cq.sdk.potential.sql.tx.TransactionManager;
import com.cq.sdk.potential.sql.frame.hibernate.HibernateTrusteeship;
import com.cq.sdk.potential.sql.frame.MybatisTrusteeship;
import com.cq.sdk.potential.sql.frame.hibernate.HibernateSessionManager;
import com.cq.sdk.potential.utils.ClassObj;
import com.cq.sdk.potential.utils.SynchronizationType;
import com.cq.sdk.utils.FileUtils;
import com.cq.sdk.utils.Logger;
import com.cq.sdk.utils.StringUtils;
import org.hibernate.context.spi.CurrentSessionContext;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by admin on 2016/9/2.
 */
public class GenerateBean {
    public static final AutowiredInterface mybatis(MybatisTrusteeship mybatisTrusteeship){
        try {
            DataSource dataSource = mybatisTrusteeship.dataSource();
            String mapper=mybatisTrusteeship.mappers();
            Class configClass=Class.forName("org.apache.ibatis.session.Configuration");
            Class environment=Class.forName("org.apache.ibatis.mapping.Environment");
            Class transactionFactoryClass=Class.forName("org.apache.ibatis.transaction.TransactionFactory");
            Constructor constructor=environment.getConstructor(String.class,transactionFactoryClass,DataSource.class);
            Object envObj=constructor.newInstance("frame",Class.forName("org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory").newInstance(),dataSource);
            Object configObj=configClass.newInstance();
            Class xMLMapperBuilder=Class.forName("org.apache.ibatis.builder.xml.XMLMapperBuilder");
            List<File> fileList=FileUtils.findFileList(mapper);
            for(File file :  fileList){//解析xml文件
                Constructor xMLMapperBuilderCon=xMLMapperBuilder.getConstructor(InputStream.class,configClass,String.class, Map.class);
                Object xMLMapperBuilderObj=xMLMapperBuilderCon.newInstance(new FileInputStream(file),configObj,file.getAbsolutePath(),configClass.getMethod("getSqlFragments").invoke(configObj));
                xMLMapperBuilder.getMethod("parse").invoke(xMLMapperBuilderObj);
            }
            configClass.getMethod("setEnvironment",environment).invoke(configObj,envObj);
            configObj=mybatisTrusteeship.configuration(configObj,dataSource);
            Class sqlSessionFactoryBuilder=Class.forName("org.apache.ibatis.session.SqlSessionFactoryBuilder");
            Object sqlSessionFactoryBuilderObj=sqlSessionFactoryBuilder.newInstance();
            Object sqlSessionFactory = sqlSessionFactoryBuilder.getMethod("build",configClass).invoke(sqlSessionFactoryBuilderObj,configObj);
            Object sqlSession = sqlSessionFactory.getClass().getMethod("openSession").invoke(sqlSessionFactory);
            final List<ClassObj> objectList = new ArrayList<ClassObj>();
            fileList = FileUtils.findFileList(mybatisTrusteeship.mapperInterface());
            for (File file : fileList) {
                ClassObj classObj=new ClassObj(sqlSession.getClass().getMethod("getMapper",Class.class).invoke(sqlSession,Class.forName(StringUtils.filePathConvertPack(file,mybatisTrusteeship.mapperInterface()))));
                objectList.add(classObj);
            }
            objectList.add(new ClassObj(dataSource));
            TransactionManager transactionManager=mybatisTrusteeship.transactionManager();
            Field field=transactionManager.getClass().getDeclaredField("transaction");
            field.setAccessible(true);
            field.set(transactionManager, new Transaction() {
                @Autowired
                DataSource dataSource;
                Connection connection;
                @Override
                public void begin() {
                    try {
                         this.connection=dataSource.getConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void commit() {
                    try {
                        this.connection.commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void rollback() {
                    try {
                        this.connection.rollback();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return new AutowiredInterface() {
                @Override
                public TransactionManager transactionManager() {
                    return transactionManager;
                }

                public List<ClassObj> beanList() {
                    return objectList;
                }
            };
        }catch (Exception ex){
            ex.printStackTrace();
            Logger.error("mybatis join trusteeship fail",ex);
        }
        return null;
    }
    public static final AutowiredInterface hibernate(HibernateTrusteeship hibernateTrusteeship){
        try {
            DataSource dataSource = hibernateTrusteeship.dataSource();
            Object configuration = Class.forName("org.hibernate.cfg.Configuration").newInstance();
            Field field=configuration.getClass().getDeclaredField("properties");
            field.setAccessible(true);
            Properties properties = (Properties) field.get(configuration);
            properties.put("hibernate.cache.use_query_cache",false);
            properties.put("hibernate.cache.use_second_level_cache",true);
            properties.put("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
            properties=hibernateTrusteeship.properties(properties);
            configuration.getClass().getMethod("setProperties",Properties.class).invoke(configuration,properties);
            configuration=hibernateTrusteeship.configuration(configuration,dataSource);
            if(dataSource!=null) {
                properties.put("hibernate.connection.datasource",dataSource);
            }
            properties.put("hibernate.current_session_context_class", HibernateProxy.getProxyClass(HibernateSessionManager.class.getClassLoader(),new Class[]{CurrentSessionContext.class}).getName());
            configuration.getClass().getMethod("setProperties",Properties.class).invoke(configuration,properties);
            field=configuration.getClass().getDeclaredField("standardServiceRegistryBuilder");
            field.setAccessible(true);
            Object standardServiceRegistryBuilder=field.get(configuration);
            field=standardServiceRegistryBuilder.getClass().getDeclaredField("aggregatedCfgXml");
            field.setAccessible(true);
            Object aggregatedCfgXml=field.get(standardServiceRegistryBuilder);
            Field mappingReferencesField=aggregatedCfgXml.getClass().getDeclaredField("mappingReferences");
            mappingReferencesField.setAccessible(true);
            List mappingReferences= (List) mappingReferencesField.get(aggregatedCfgXml);
            if(mappingReferences==null){
                mappingReferences=new ArrayList();
            }
            List<File> mappingFile=FileUtils.findFileList(hibernateTrusteeship.mapping());
            Class mappingReference$Type=Class.forName("org.hibernate.boot.cfgxml.spi.MappingReference$Type");
            for(File file : mappingFile){
                Object mappingReference=Class.forName("org.hibernate.boot.cfgxml.spi.MappingReference").getConstructor(mappingReference$Type,String.class).newInstance(mappingReference$Type.getEnumConstants()[1],StringUtils.filePathConvertPack(file,hibernateTrusteeship.mapping()));
                mappingReferences.add(mappingReference);
            }
            mappingReferencesField.set(aggregatedCfgXml,mappingReferences);
            Object sessionFactory=configuration.getClass().getMethod("buildSessionFactory").invoke(configuration);
            TransactionManager transactionManager = hibernateTrusteeship.transactionManager();
            field=transactionManager.getClass().getDeclaredField("transaction");
            field.setAccessible(true);
            field.set(transactionManager, new Transaction() {
                @Autowired(type = "org.hibernate.SessionFactory")
                Object sessionFactory;
                Object session;
                Object transaction;
                @Override
                public void begin() {
                    try {
                        this.session=sessionFactory.getClass().getMethod("openSession").invoke(sessionFactory);
                        SynchronizationManager.bind(SynchronizationType.HibernateSession,this.session);
                        this.transaction=session.getClass().getMethod("getTransaction").invoke(session);
                        if(!(Boolean) this.transaction.getClass().getMethod("isActive").invoke(this.transaction)) {
                            this.transaction.getClass().getMethod("begin").invoke(this.transaction);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void commit() {
                    try {
                        this.transaction.getClass().getMethod("commit").invoke(this.transaction);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void rollback() {
                    try {
                        this.transaction.getClass().getMethod("rollback").invoke(this.transaction);
                        this.session.getClass().getMethod("close").invoke(this.transaction);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            List<ClassObj> classObjList=new ArrayList<>();
            classObjList.add(new ClassObj(sessionFactory));
            classObjList.add(new ClassObj(transactionManager));
            return new AutowiredInterface() {
                @Override
                public TransactionManager transactionManager() {
                    return transactionManager;
                }

                @Override
                public List<ClassObj> beanList() {
                    return classObjList;
                }
            };
        }catch (Exception ex){
            ex.printStackTrace();
            Logger.info("hibernate join trusteeship fail");
        }
        return null;
    }
}
