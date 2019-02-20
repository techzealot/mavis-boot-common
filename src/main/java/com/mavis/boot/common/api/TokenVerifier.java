package com.mavis.boot.common.api;

/**
 * @description:
 * @author: mavis
 * @date: 2019-01-22 20:42
 */
public interface TokenVerifier {

    boolean verifyToken(String token);
}
