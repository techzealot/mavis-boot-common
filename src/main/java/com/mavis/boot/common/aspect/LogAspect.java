package com.mavis.boot.common.aspect;

import com.mavis.boot.common.annotation.Log;
import com.mavis.boot.common.util.AopLogUtil;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 非spring web环境日志切面，不会获取servlet request相关信息(可能没有servlet相关类)
 */
@Component
@Slf4j
@Aspect
@ConditionalOnNotWebApplication
public class LogAspect {

  //方法执行耗时触发告警的时间上限，单位：毫秒
  public static final int warnLimit = 1000;

  /**
   * 切入点,记录log注解标记的方法的类名、方法名、执行时间、返回值、参数、request等详细信息
   */
  @Pointcut("@annotation(com.mavis.boot.common.annotation.WebLog)")
  public void log() {
  }

  @Around(value = "log()")
  public Object doBefore(ProceedingJoinPoint pjp) {
    MDC.put("TRACE_ID", UUID.randomUUID().toString());
    StopWatch stopWatch = new StopWatch();
    //获取方法签名
    Signature signature = pjp.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();
    String methodName = method.getName();
    //获取注解信息
    Log log = null;
    if (method.isAnnotationPresent(Log.class)) {
      log = method.getAnnotation(Log.class);
    }
    //开始计时
    stopWatch.start(methodName);
    Object result = null;
    try {
      result = pjp.proceed();
    } catch (Throwable throwable) {
      LogAspect.log.error(methodName, throwable);
    } finally {
      stopWatch.stop();
      long totalTimeMillis = stopWatch.getTotalTimeMillis();
      //记录日志
      StringBuilder sb = new StringBuilder(200);
      if (Objects.nonNull(log)) {
        sb.append(MessageFormat
            .format("Log info > value:{0},description:{1} \n\t", log.value(), log.description()));
      }
      sb.append(AopLogUtil.extractMothodInfo(pjp));
      sb.append(MessageFormat.format("return value:{} \n\t", result));
      sb.append(stopWatch.prettyPrint());
      //如果执行时间超过限制,输出warn日志，否则输出info日志
      if (totalTimeMillis > warnLimit) {
        LogAspect.log.warn("{}", sb);
      } else {
        LogAspect.log.info("{}", sb);
      }
      MDC.clear();
    }
    return result;
  }
}
