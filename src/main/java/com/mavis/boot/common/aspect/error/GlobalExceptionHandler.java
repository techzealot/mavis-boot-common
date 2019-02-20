package com.mavis.boot.common.aspect.error;

import com.mavis.boot.common.exception.BusinessException;
import com.mavis.boot.common.exception.DecodeAccessTokenException;
import com.mavis.boot.common.exception.NoPermissionException;
import com.mavis.boot.common.response.R;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * 统一异常处理 在Controller中抛出的异常，GlobalExceptionHandler中定义的处理方法可以起作用 其他的业务层异常也可以单独处理
 *
 * @author hfb
 * @date 2017/9/18
 */
@ControllerAdvice
@Slf4j
//todo 优化返回错误消息
public class GlobalExceptionHandler {

    @Autowired
    private ErrorAttributes errorAttributes;

    /**
     * 默认异常处理
     */
    @ExceptionHandler(value = {Exception.class})
    public R handleException(Exception ex, WebRequest request) {
        Map<String, Object> errorAttributes = this.errorAttributes
            .getErrorAttributes(request, false);
        Map<String, Object> attachment = new HashMap<>(4);
        attachment.put("path", errorAttributes.getOrDefault("path", ""));
        attachment.put("timestamp", errorAttributes.getOrDefault("timestamp", new Date()));
        return R.error(errorAttributes.getOrDefault("message", ex.getMessage()).toString(),
            Collections.emptyMap())
            .withAttachment(attachment);
    }

    /**
     * 自定义业务异常处理
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public R businessExceptionHandler(HttpServletRequest req, Exception e) {
        // 记录日志
        log.error(e.getMessage() + " url:" + req.getRequestURI(), e);
        return R.error(e.getMessage(), Collections.emptyMap());
    }

    /**
     * 处理validation异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public R validationExceptionHandler(HttpServletRequest req,
        ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage()).append(" ");
        }
        log.error(strBuilder.toString() + " url:" + req.getRequestURI(), e);
        return R.error(strBuilder.toString(), Collections.emptyMap());
    }

    /**
     * 处理Methodvalidation异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public R methodValidationExceptionHandler(MethodArgumentNotValidException e) {
        StringBuilder stringBuilder = new StringBuilder();
        String str = null;
        if (null != e.getBindingResult()) {
            List<FieldError> errorList = e.getBindingResult().getFieldErrors();
            for (FieldError fieldError : errorList) {
                stringBuilder.append(fieldError.getDefaultMessage()).append("|");
            }
            str = stringBuilder.substring(0, stringBuilder.length() - 1);
        }
        return R.error(str, Collections.emptyMap());
    }

    /**
     * 处理没有权限异常
     */
    @ExceptionHandler(value = NoPermissionException.class)
    @ResponseBody
    public R noPermissionExceptionHandler(HttpServletRequest req,
        NoPermissionException e) {
        // 记录日志
        log.error(e.getMessage() + " url:" + req.getRequestURI(), e);
        return R.noAuthority(Collections.emptyMap());
    }

    /**
     * 功能描述: 处理token解析异常 <br>
     */
    @ExceptionHandler(value = DecodeAccessTokenException.class)
    @ResponseBody
    public R decodeAccessTokenException(HttpServletRequest req,
        DecodeAccessTokenException e) {
        // 记录日志
        log.error(e.getMessage() + " url:" + req.getRequestURI(), e);
        return R.error(e.getMessage(), Collections.emptyMap());
    }
}
