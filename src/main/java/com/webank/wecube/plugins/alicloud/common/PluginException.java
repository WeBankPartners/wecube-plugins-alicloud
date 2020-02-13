package com.webank.wecube.plugins.alicloud.common;

/**
 * @author howechen
 */
public class PluginException extends RuntimeException {

    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
