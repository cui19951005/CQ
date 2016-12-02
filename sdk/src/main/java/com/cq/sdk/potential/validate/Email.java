package com.cq.sdk.potential.validate;

import java.lang.annotation.*;

/**
 * Created by admin on 2016/11/17.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Email {
    String message() default "";
}
