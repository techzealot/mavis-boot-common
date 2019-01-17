package com.mavis.boot.common.exception;

import java.security.PrivilegedActionException;

/**
 * 业务层异常-未登录或者登录过期
 * @author hfb
 * @date 2018/4/20
 */
public class AuthenticateErrorException extends BusinessException{

    public AuthenticateErrorException() {
    }

    public AuthenticateErrorException(String message) {
        super(message);
    }

    public AuthenticateErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticateErrorException(Throwable cause) {
        super(cause);
    }

    public AuthenticateErrorException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
