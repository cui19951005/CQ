package com.cq.sdk.potential.aop;

import java.lang.annotation.*;

/**
 * Created by CuiYaLei on 2016/9/3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aspect {
    String value() default "";
}
