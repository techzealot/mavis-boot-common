package com.mavis.boot.common.util;

import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: mavis
 * @date: 2019-01-17 14:22
 */
public class CookieUtil {

  /**
   * 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
   */
  public static Cookie getCookie(String name, HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null || cookies.length < 1) {
      return null;
    }
    for (Cookie c : cookies) {
      if (Objects.equals(c.getName(), name)) {
        return c;
      }
    }
    return null;
  }

  /**
   * 根据Cookie名称直接得到Cookie值
   */
  public static String getCookieValue(String name, HttpServletRequest request) {
    Cookie cookie = getCookie(name, request);
    if (cookie != null) {
      return cookie.getValue();
    }
    return "";
  }


}
