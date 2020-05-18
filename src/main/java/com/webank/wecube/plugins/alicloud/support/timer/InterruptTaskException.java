package com.webank.wecube.plugins.alicloud.support.timer;

/**
 * @author howechen
 */
public class InterruptTaskException extends RuntimeException {

    public InterruptTaskException() {
    }

    public InterruptTaskException(String message) {
        super(message);
    }

    public InterruptTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterruptTaskException(Throwable cause) {
        super(cause);
    }

    public InterruptTaskException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
