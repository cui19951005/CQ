package com.cq.sdk.service.potential.sql.frame;

import javax.sql.DataSource;

/** Mybatis托管
 * Created by admin on 2016/9/2.
 */
public interface MybatisTrusteeship extends BaseTrusteeship {


    /**
     * get mapper*.xml resource path
     */
    String mappers();

    /**
     * get mapper interface package path
     * @return
     */
    String mapperLocation();

}
