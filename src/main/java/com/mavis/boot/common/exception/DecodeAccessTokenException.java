package com.mavis.boot.common.exception;

/**
 * @description:
 * @author: mavis
 * @date: 2019-01-22 21:09
 */
public class DecodeAccessTokenException extends RuntimeException {

  public DecodeAccessTokenException() {
  }

  public DecodeAccessTokenException(String message) {
    super(message);
  }

  public DecodeAccessTokenException(String message, Throwable cause) {
    super(message, cause);
  }

  public DecodeAccessTokenException(Throwable cause) {
    super(cause);
  }

  public DecodeAccessTokenException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
