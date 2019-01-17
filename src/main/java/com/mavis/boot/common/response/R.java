package com.mavis.boot.common.response;

import java.io.Serializable;
import java.util.Collections;
import lombok.Data;
import lombok.experimental.Wither;

@Data
@Wither
public class R<T> implements Serializable {

  private static final long serialVersionUID = 1234343454L;
  public static final int SUCCESS = 200;
  public static final int INTERNAL_ERROR = 500;
  public static final int NO_AUTHORITY = 401;
  public static final int BAD_ARGS = 400;
  public static final String MSG_SUCCESS = "success";
  public static final String MSG_INTERNAL_ERROR = "server error";
  public static final String MSG_NO_AUTHORITY = "no authority";
  public static final String MSG_BAD_ARGS = "bad args";
  private Integer status;
  private String message;
  private T data;
  private Object attachment;

  private R(Integer status, String message, T data, Object attachment) {
    this.status = status;
    this.message = message;
    this.data = data;
    this.attachment = attachment;
  }

  private R() {
  }

  public static <T> R of(Integer status, String message, T data, Object attachment) {
    return new R(status, message, data, attachment);
  }

  public static <T> R success(String message, T data, Object attachment) {
    return R.of(SUCCESS, message, data, attachment);
  }

  public static <T> R success(String message, T data) {
    return R.success(message, data, Collections.emptyMap());
  }

  public static <T> R success(T data) {
    return R.success(MSG_SUCCESS, data);
  }

  public static <T> R error(String message, T data, Object attachment) {
    return R.of(INTERNAL_ERROR, message, data, attachment);
  }

  public static <T> R error(String message, T data) {
    return R.error(message, data, Collections.emptyMap());
  }

  public static <T> R error(T data) {
    return R.error(MSG_INTERNAL_ERROR, data);
  }

  public static <T> R noAuthority(String message, T data, Object attachment) {
    return R.of(NO_AUTHORITY, message, data, attachment);
  }

  public static <T> R noAuthority(String message, T data) {
    return R.noAuthority(message, data, Collections.emptyMap());
  }

  public static <T> R noAuthority(T data) {
    return R.noAuthority(MSG_NO_AUTHORITY, data);
  }

  public static <T> R badArgs(String message, T data, Object attachment) {
    return R.of(BAD_ARGS, message, data, attachment);
  }

  public static <T> R badArgs(String message, T data) {
    return R.badArgs(message, data, Collections.emptyMap());
  }

  public static <T> R badArgs(T data) {
    return R.badArgs(MSG_BAD_ARGS, data);
  }


}
