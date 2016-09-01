package com.cq.sdk.service.potential.annotation;

import java.lang.annotation.*;

/**
 * Created by admin on 2016/9/1.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
