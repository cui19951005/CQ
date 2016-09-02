package com.cq.sdk.service.potential.annotation;

import java.lang.annotation.*;

/**
 * Created by admin on 2016/9/2.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoadProperties {
    /**
     * 属性文件
     * @return
     */
    String[] value();
}
