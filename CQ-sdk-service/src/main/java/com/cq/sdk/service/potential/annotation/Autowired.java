package com.cq.sdk.service.potential.annotation;

import java.lang.annotation.*;

/**
 * Created by admin on 2016/9/1.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    boolean value() default true;
}
