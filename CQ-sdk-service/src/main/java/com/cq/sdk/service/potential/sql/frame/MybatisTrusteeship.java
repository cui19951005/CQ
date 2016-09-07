package com.cq.sdk.service.potential.sql.frame;

import javax.sql.DataSource;

/** Mybatis托管
 * Created by admin on 2016/9/2.
 */
public interface MybatisTrusteeship extends BaseTrusteeship {

    /**
     * 创建configuration
     * @param dataSource
     * @return
     */
    Object configuration(Object configuration, DataSource dataSource);
    /**
     * 创建mapper.xml文件包
     */
    String mappers();

    /**
     * mapper 接口位置
     * @return
     */
    String mapperLocation();

}
