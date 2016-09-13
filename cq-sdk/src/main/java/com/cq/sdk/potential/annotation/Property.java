package com.cq.sdk.potential.annotation;

import java.lang.annotation.*;

/**
 * Created by CuiYaLei on 2016/9/2.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Property {
    /**
     * 配置文件中前缀jdbc.
     * @return
     */
    String value() default "";


}
