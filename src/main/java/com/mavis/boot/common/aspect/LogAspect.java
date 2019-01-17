package com.mavis.boot.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
@ConditionalOnNotWebApplication
public class LogAspect {

}
