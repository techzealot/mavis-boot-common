package com.mavis.boot.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: spring web环境下使用
 * @author: mavis
 * @date: 2019-01-23 10:17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface WebLog {

  String value() default "";

  String description() default "";

}
