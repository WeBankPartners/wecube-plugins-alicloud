package com.webank.wecube.plugins.alicloud.support.password;

/**
 * @author howechen
 */
public class CryptoException extends RuntimeException {

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }
}
