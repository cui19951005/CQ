package com.cq.sdk.service.potential.sql.frame;

import com.cq.sdk.service.potential.sql.frame.BaseTrusteeship;

import javax.sql.DataSource;

/**
 * Created by admin on 2016/9/7.
 */
public interface HibernateTrusteeship extends BaseTrusteeship {
    /**
     * 创建configuration
     * @param dataSource
     * @return
     */
    Object configuration(Object configuration, DataSource dataSource);
}
