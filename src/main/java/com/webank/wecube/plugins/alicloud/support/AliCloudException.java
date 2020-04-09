package com.webank.wecube.plugins.alicloud.support;

public class AliCloudException extends RuntimeException {
    public AliCloudException(String message) {
        super(message);
    }

    public AliCloudException(String message, Throwable cause) {
        super(message, cause);
    }
}
