package com.cq.sdk.service.potential.annotation;

import com.cq.sdk.service.potential.utils.InjectionType;

import java.lang.annotation.*;

/**
 * Created by admin on 2016/9/2.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Entrance {
    String value() default "";
    String method() default "";
    InjectionType injectionType() default InjectionType.AutoAll;
}
