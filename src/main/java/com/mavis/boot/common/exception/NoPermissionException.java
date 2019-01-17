package com.mavis.boot.common.exception;

import java.security.PrivilegedActionException;

/**
 * 业务层异常-没有权限访问
 * @author mavis
 * @date 2018/4/20
 */
public class NoPermissionException extends BusinessException{

    public NoPermissionException() {
    }

    public NoPermissionException(String message) {
        super(message);
    }

    public NoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionException(Throwable cause) {
        super(cause);
    }

    public NoPermissionException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
