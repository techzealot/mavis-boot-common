package com.mavis.boot.common.aspect;

import com.mavis.boot.common.annotation.NotAuthrnticated;
import com.mavis.boot.common.util.CookieUtil;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 接口调用权限校验
 *
 * @date 2018/4/20
 */
@Component
public class AccessTokenVerifyInterceptor extends HandlerInterceptorAdapter {

  public static final String TOKEN_NAME = "access_token";

  /**
   * 请求调用前处理
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    if (handler instanceof HandlerMethod) {
      // 加上这个注解不校验接口权限
      NotAuthrnticated notAuthrnticated = ((HandlerMethod) handler).getMethod().getAnnotation(
          NotAuthrnticated.class);
      if (notAuthrnticated != null) {
        return true;
      }
      String token = getToken(request, TOKEN_NAME);
      return verifyToken(token);
    }
    return true;
  }

  private String getToken(HttpServletRequest request, String tokenKey) {
    String token = request.getParameter(tokenKey);
    if (StringUtils.hasText(token)) {
      return token;
    }
    token = Optional.ofNullable(request.getAttribute(tokenKey)).map(Objects::toString).orElse("");
    if (StringUtils.hasText(token)) {
      return token;
    }
    token = Optional.ofNullable(CookieUtil.getCookieValue(tokenKey, request)).map(Objects::toString)
        .orElse("");
    if (StringUtils.hasText(token)) {
      return token;
    }
    return "";
  }

  private boolean verifyToken(String token) {
    //todo token验证,默认返回true
    return true;
  }
}
