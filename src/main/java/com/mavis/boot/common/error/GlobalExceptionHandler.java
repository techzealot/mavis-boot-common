package com.mavis.boot.common.error;

import com.mavis.boot.common.response.R;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @Autowired
  private ErrorAttributes errorAttributes;

  @ExceptionHandler(value = {Exception.class})
  public R handleException(Exception ex, WebRequest request) {
    Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, false);
    Map<String,Object> attachment=new HashMap<>(4);
    attachment.put("path", errorAttributes.getOrDefault("path", ""));
    attachment.put("timestamp", errorAttributes.getOrDefault("timestamp", new Date()));
    return R.error(errorAttributes.getOrDefault("message", ex.getMessage()).toString(),
        Collections.emptyMap())
        .withAttachment(attachment);
  }
}
