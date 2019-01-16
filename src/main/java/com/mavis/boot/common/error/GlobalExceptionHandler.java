package com.mavis.boot.common.error;

import com.mavis.boot.common.response.RR;
import java.util.Collections;
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
  public RR handleException(Exception ex, WebRequest request) {
    Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, false);
    return RR.error(errorAttributes.getOrDefault("message", ex.getMessage()).toString(),
        Collections.emptyMap())
        .attach("path", errorAttributes.getOrDefault("path", ""))
        .attach("timestamp", errorAttributes.getOrDefault("timestamp", 0));
  }
}
