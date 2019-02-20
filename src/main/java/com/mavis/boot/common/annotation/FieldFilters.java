package com.mavis.boot.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于controller层方法上，过滤返回对象中的属性
 *
 * @author xuyongchen
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldFilters {

    FieldFilter[] value();
}