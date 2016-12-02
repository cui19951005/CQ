package com.cq.sdk.potential.aop;

import java.lang.annotation.*;

/**
 * Created by CuiYaLei on 2016/9/3.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented()
public @interface AfterReturning {
    String value() default "";
}
