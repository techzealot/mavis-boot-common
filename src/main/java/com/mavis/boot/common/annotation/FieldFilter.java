package com.mavis.boot.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(FieldFilters.class)   // 让方法支持多重@FieldFilter 注解
public @interface FieldFilter {
    Class<?> type();
    String[] includes() default {};
    String[] excludes() default {};
}
