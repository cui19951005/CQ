package com.cq.sdk.potential.validate;

import java.lang.annotation.*;

/**
 * Created by admin on 2016/11/17.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Size {
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    String message() default "";
}
