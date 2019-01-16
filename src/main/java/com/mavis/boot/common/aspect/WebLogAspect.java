package com.mavis.boot.common.aspect;

import com.jidian.utils.IpUtils;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * WEB层日志切面,用来记录请求信息
 *
 * @author hfb
 */
@Aspect
@Order(5)
@Component
@Slf4j
public class WebLogAspect {

	/**
	 * 开始时间
	 */
	ThreadLocal<Long> startTime = new ThreadLocal<>();

	/**
	 * 切入点
	 */
	@Pointcut("execution(public * com.laidian.*.controller.*.*(..))")
	public void webLog() {
	}

	@Before(value = "webLog()")
	public void doBefore(JoinPoint joinPoint) {
		startTime.set(System.currentTimeMillis());

		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		// 记录下请求内容
		log.info("Start API Request\n--URL:\t{} {}\n--ARGS:\t{}\n--IP:\t{}\n--CLASS_METHOD:{}", request.getMethod(),
				request.getRequestURI(), Arrays.toString(joinPoint.getArgs()), IpUtils.getRemoteAddr(request),
				joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
	}

	@AfterReturning(returning = "ret", pointcut = "webLog()")
	public void doAfterReturning(Object ret) {
		// 处理完请求，返回内容
		log.info("End API Request\n--Response:\t{}\n--SpendTime:\t{} ms", ret,
				System.currentTimeMillis() - startTime.get());
	}

}
