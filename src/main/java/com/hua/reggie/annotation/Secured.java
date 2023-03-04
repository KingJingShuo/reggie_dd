package com.hua.reggie.annotation;

/**
 * @author : hua
 * @date : 2023/3/2 22:54
 */
public @interface Secured {

    String value() default "";

    boolean update() default false;

    int code() default 0;

}
