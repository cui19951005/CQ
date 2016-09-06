package com.cq.sdk.service.test;

import com.cq.sdk.service.potential.Trusteeship;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Entrance;
import com.cq.sdk.service.potential.annotation.Execute;
import com.cq.sdk.service.potential.annotation.LoadProperties;
import com.cq.sdk.service.potential.utils.InjectionType;
import com.cq.sdk.service.test.dao.Loginaccount;
import com.cq.sdk.service.test.dao.entity.LoginaccountMapper;
import com.cq.sdk.service.utils.Logger;
import org.apache.ibatis.session.Configuration;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.service.test.*",injectionType = InjectionType.Annotation)
@LoadProperties({"test.properties"})
public class Main {
    @Autowired
    Main main;
    @Autowired
    LoginaccountMapper loginaccountMapper;
    static long time=0;
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        time=System.currentTimeMillis();
         new Trusteeship(Main.class);
    }
    @Execute
    public void main() throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
       /* List list=new ArrayList();
        org.mybatis.generator.config.Configuration configuration= new ConfigurationParser(list).parseConfiguration(new File(Thread.currentThread().getClass().getResource("/mybatisGeneratorConfig.xml").getFile()));
        MyBatisGenerator myBatisGenerator=new MyBatisGenerator(configuration,null,list);
        myBatisGenerator.generate(null);*/
        Logger.info(this.loginaccountMapper.selectByPrimaryKey(12L));
    }
}