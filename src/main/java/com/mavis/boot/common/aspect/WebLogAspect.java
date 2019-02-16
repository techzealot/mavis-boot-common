package com.mavis.boot.common.aspect;

import com.mavis.boot.common.annotation.WebLog;
import com.mavis.boot.common.util.AopLogUtil;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * spring WEB层日志切面,用来记录请求信息
 *
 * @author mavis
 */
@Aspect
@Component
@ConditionalOnWebApplication
@Slf4j
public class WebLogAspect {

  //方法执行耗时触发告警的时间上限，单位：毫秒
  public static final int warnLimit = 1000;

  /**
   * 切入点,记录controller和WebLog注解标记的方法的类名、方法名、执行时间、返回值、参数、request等详细信息
   */
  @Pointcut("@annotation(org.springframework.stereotype.Controller)||@annotation(com.mavis.boot.common.annotation.WebLog)")
  public void webLog() {
  }

  @Around(value = "webLog()")
  public Object doBefore(ProceedingJoinPoint pjp) {
    MDC.put("TRACE_ID", UUID.randomUUID().toString());
    StopWatch stopWatch = new StopWatch();
    //获取方法签名
    MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    Method method = methodSignature.getMethod();
    String methodName = method.getName();
    //开始计时
    stopWatch.start(methodName);
    Object result = null;
    try {
      result = pjp.proceed();
      return result;
    } catch (Throwable throwable) {
      log.error(methodName, throwable);
      return null;
    } finally {
      stopWatch.stop();
      long totalTimeMillis = stopWatch.getTotalTimeMillis();
      //记录日志
      StringBuilder sb = new StringBuilder(128);
      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes();
      HttpServletRequest request = null;
      if (attributes != null) {
        request = attributes.getRequest();
      }
      sb.append(MessageFormat.format("request info:{0} ;;", request));
      //记录方法详细信息
      sb.append(AopLogUtil.extractTargetInfo(pjp, WebLog.class));
      //记录返回值详细信息
      sb.append(MessageFormat.format("return value:{} ;;", result));
      //记录执行时间
      sb.append(stopWatch.toString());
      //如果执行时间超过限制,输出warn日志，否则输出info日志
      if (totalTimeMillis > warnLimit) {
        log.warn("{}", sb);
      } else {
        log.info("{}", sb);
      }
      MDC.clear();
    }
  }
}
