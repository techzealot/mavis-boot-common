package com.mavis.boot.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @description:
 * @author: mavis
 * @date: 2019-01-23 15:15
 */
public class AopLogUtil {

  public static StringBuilder extractTargetInfo(ProceedingJoinPoint pjp,
      Class<? extends Annotation> annotationClass) {
    //获取目标方法所在class
    Class<?> clazz = pjp.getTarget().getClass();
    String className = clazz.getName();
    Annotation classLevelAnno = null;
    if (clazz.isAnnotationPresent(annotationClass)) {
      classLevelAnno = clazz.getAnnotation(annotationClass);
    }
    //获取方法签名
    Signature signature = pjp.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();
    String methodName = method.getName();
    //抽取方法上注解
    Annotation methodLevelAnno = null;
    if (method.isAnnotationPresent(annotationClass)) {
      methodLevelAnno = method.getAnnotation(annotationClass);
    }
    StringBuilder sb = new StringBuilder(128);
    //占位值为null时，会原样输出占位符，不会抛出NPE
    sb.append(MessageFormat.format("className:{0} \n\t", className));
    if (Objects.nonNull(classLevelAnno)) {
      sb.append(MessageFormat
          .format("class annotation info > {0} \n\t", classLevelAnno));
    }
    sb.append(MessageFormat.format("methodName:{0} \n\t", methodName));
    if (Objects.nonNull(methodLevelAnno)) {
      sb.append(MessageFormat
          .format("method annotation info > {0} \n\t", methodLevelAnno));
    }
    sb.append(MessageFormat.format("method args: \n {0} \n\t", pjp.getArgs()));
    return sb;
  }

}
