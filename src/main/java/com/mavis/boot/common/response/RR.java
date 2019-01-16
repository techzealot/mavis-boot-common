package com.mavis.boot.common.response;

import java.util.HashMap;

public class RR extends HashMap<String, Object> {

  private RR() {

  }

  public RR data(Object data) {
    this.put("data", data);
    return this;
  }

  public RR status(Integer status) {
    this.put("status", status);
    return this;
  }

  public RR message(String message) {
    this.put("message", message);
    return this;
  }

  public static RR ok(String message, Object data) {
    return RR.of(message, 200, data);
  }

  public static RR ok(Object data) {
    return RR.of("ok", 200, data);
  }

  public static RR error(String message, Object data) {
    return RR.of(message, 500, data);
  }

  public static RR error(Object data) {
    return RR.of("error", 500, data);
  }

  public static RR of(String message, Integer status, Object data) {
    RR r = new RR();
    r.message(message);
    r.data(data);
    r.status(status);
    return r;
  }

  public RR attach(String key,Object value){
    this.put(key,value);
    return this;
  }
}
