package com.webank.wecube.plugins.alicloud.support;


import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.support.ssh2.RemoteCommand;
import com.webank.wecube.plugins.alicloud.support.ssh2.RemoteCommandExecutorConfig;
import com.webank.wecube.plugins.alicloud.support.ssh2.impl.PooledRemoteCommandExecutor;
import com.webank.wecube.plugins.alicloud.support.ssh2.impl.SimpleRemoteCommand;
import org.apache.commons.lang3.StringUtils;
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


    public String run(String host, String user, String password, Integer port, String command)
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
            result = executor.execute(cmd);
        } finally {
            executor.destroy();
        }

        logger.info("result is: " + result);
        return result;
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
            result = executor.execute(cmd);
            logger.info("result is: " + result);
            if (StringUtils.isEmpty(result)) {
                throw new PluginException("return is empty, please check !");
            }
        } finally {
            executor.destroy();
        }
        return result;

    }
}
