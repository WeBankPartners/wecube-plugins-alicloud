package com.webank.wecube.plugins.alicloud.support;


import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.support.ssh2.RemoteCommand;
import com.webank.wecube.plugins.alicloud.support.ssh2.RemoteCommandExecutorConfig;
import com.webank.wecube.plugins.alicloud.support.ssh2.impl.PooledRemoteCommandExecutor;
import com.webank.wecube.plugins.alicloud.support.ssh2.impl.SimpleRemoteCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author howechen
 */
@Component
public class PluginSshdClient {
    public static String DEFAULT_USER = "root";
    public final static int PORT = 22;

    private static final Logger logger = LoggerFactory.getLogger(PluginSshdClient.class);


    public void run(String host, String user, String password, Integer port, String command)
            throws PluginException {
        runWithReturn(host, user, password, port, command);
    }

    public String runWithReturn(String host, String user, String password, Integer port, String command)
            throws PluginException {
        RemoteCommandExecutorConfig config = new RemoteCommandExecutorConfig();
        config.setRemoteHost(host);
        config.setUser(user);
        config.setPsword(password);
        config.setPort(port);

        RemoteCommand cmd = new SimpleRemoteCommand(command);
        PooledRemoteCommandExecutor executor = new PooledRemoteCommandExecutor();
        executor.init(config);

        String result;
        try {
            logger.info("Sending command: [{}] to target machine: [{}]", command, host);
            result = executor.execute(cmd);
        } finally {
            executor.destroy();
        }
        logger.info("result is: " + result);
        return result;
    }
}
