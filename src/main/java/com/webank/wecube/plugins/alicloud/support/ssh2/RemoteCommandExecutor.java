package com.webank.wecube.plugins.alicloud.support.ssh2;

/**
 * @author howechen
 */
public interface RemoteCommandExecutor {
	String execute(RemoteCommand cmd);

	void execute(RemoteCommand cmd, boolean asynchorously);
}
