package com.cq.sdk.potential.annotation;

import java.lang.annotation.*;

/**
 * Created by admin on 2016/9/18.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented()
public @interface NetAddress {
    int port() default -1;
    String[] value() default "";
}
