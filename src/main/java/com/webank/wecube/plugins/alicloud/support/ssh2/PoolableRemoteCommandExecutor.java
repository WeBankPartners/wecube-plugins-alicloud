package com.webank.wecube.plugins.alicloud.support.ssh2;

/**
 * @author howechen
 */
public interface PoolableRemoteCommandExecutor extends RemoteCommandExecutor {
	void init(RemoteCommandExecutorConfig config) throws Exception;

	void destroy();
}
