package com.cq.sdk.potential.sql.frame.hibernate;

import com.cq.sdk.potential.sql.frame.BaseTrusteeship;

import java.util.Properties;

/**
 * Created by admin on 2016/9/7.
 */
public interface HibernateTrusteeship extends BaseTrusteeship {
    /**
     * hibernate properties
     * @param properties
     * @return
     */
    Properties properties(Properties properties);

    /**
     * hibernate mapping file path
     * @return
     */
    String mapping();
}
