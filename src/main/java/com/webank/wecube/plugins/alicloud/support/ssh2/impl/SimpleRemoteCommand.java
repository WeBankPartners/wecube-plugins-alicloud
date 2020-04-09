package com.webank.wecube.plugins.alicloud.support.ssh2.impl;


import com.webank.wecube.plugins.alicloud.support.ssh2.RemoteCommand;

/**
 * @author howechen
 */
public class SimpleRemoteCommand implements RemoteCommand {
	private String simpleCommand;

	public SimpleRemoteCommand(String simpleCommand) {
		super();
		this.simpleCommand = simpleCommand;
	}

	@Override
	public String getCommand() {
		return this.simpleCommand;
	}

}
