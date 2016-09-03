package com.cq.sdk.service.test;

import com.cq.sdk.service.android.qq.UserService;
import com.cq.sdk.service.potential.Trusteeship;
import com.cq.sdk.service.potential.annotation.Autowired;
import com.cq.sdk.service.potential.annotation.Entrance;
import com.cq.sdk.service.potential.annotation.Execute;
import com.cq.sdk.service.potential.annotation.LoadProperties;
import com.cq.sdk.service.potential.utils.InjectionType;
import com.cq.sdk.service.test.dao.entity.UserMapper;
import com.cq.sdk.service.utils.FileUtils;
import com.cq.sdk.service.utils.Json;
import com.cq.sdk.service.utils.Logger;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
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
@Entrance(value = "com.cq.sdk.service.*",injectionType = InjectionType.Annotation)
@LoadProperties({"test.properties"})
public class Main {
    @Autowired
    UserMapper userMapper;
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
       new Trusteeship(Main.class);
    }
    @Execute
    public void main() throws InvalidConfigurationException, IOException, XMLParserException, SQLException, InterruptedException {
        Logger.info(this.userMapper.selectByPrimaryKey("1000000000040"));
    }

}