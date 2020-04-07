package com.webank.wecube.plugins.alicloud.support.ssh2.impl;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.support.ssh2.PoolableRemoteCommandExecutor;
import com.webank.wecube.plugins.alicloud.support.ssh2.RemoteCommand;
import com.webank.wecube.plugins.alicloud.support.ssh2.RemoteCommandExecutorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.*;

/**
 * @author howechen
 */
public class PooledRemoteCommandExecutor implements PoolableRemoteCommandExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PooledRemoteCommandExecutor.class);
    private Session sshSession;
    private Connection sshConn;
    private ExecutorService executor;
    private RemoteCommandExecutorConfig config;

    @Override
    public String execute(RemoteCommand cmd) {
        Future<String> f = this.executor.submit(new PooledRemoteCommandTask(cmd));

        try {
            return f.get();
        } catch (InterruptedException | ExecutionException e1) {
            LOGGER.error("execution error", e1);
            return null;
        }
    }

    @Override
    public void execute(RemoteCommand cmd, boolean asynchorously) {
        this.executor.submit(new PooledRemoteCommandTask(cmd));
    }

    @Override
    public void init(RemoteCommandExecutorConfig config) throws PluginException {
        this.executor = Executors.newFixedThreadPool(3);
        this.config = config;

        try {
            buildConnection();
            buildSession();
        } catch (IOException e) {
            LOGGER.error("failed to init executor", e);
            this.destroy();
            throw new PluginException("failed to init executor");
        }
    }

    @Override
    public void destroy() {
        if (this.executor != null) {
            this.executor.shutdown();
        }

        if (this.sshSession != null) {
            this.sshSession.close();
        }

        if (this.sshConn != null) {
            this.sshConn.close();
        }
    }

    protected void buildConnection() throws IOException {
        Connection conn = new Connection(getConfig().getRemoteHost(), getConfig().getPort());
        conn.connect();
        boolean isAuthenticated = conn.authenticateWithPassword(getConfig().getUser(), getConfig().getPsword());
        if (!isAuthenticated) {
            if (conn != null) {
                conn.close();
            }

            throw new IOException("authentication failed");
        }

        this.sshConn = conn;
    }

    protected RemoteCommandExecutorConfig getConfig() {
        return this.config;
    }

    protected void buildSession() throws IOException {
        this.sshSession = this.sshConn.openSession();
    }

    private Session getSession() {
        return sshSession;
    }

    private class PooledRemoteCommandTask implements Callable<String> {
        private RemoteCommand cmd;

        public PooledRemoteCommandTask(RemoteCommand cmd) {
            super();
            this.cmd = cmd;
        }

        @Override
        public String call() throws Exception {
            try {
                String shellCmd = "sh -l -c '" + cmd.getCommand() + "'";
                getSession().execCommand(shellCmd);
                getSession().waitForCondition(ChannelCondition.TIMEOUT, 1000L * 60 * 5);
            } catch (Exception e) {
                LOGGER.error("errors while exec command", e);
                throw e;
            }

            InputStream stdout = new StreamGobbler(getSession().getStdout());
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            StringBuilder result = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            if (getSession().getExitStatus() != null && getSession().getExitStatus() != 0) {
                throw new Exception("exec failed with code " + getSession().getExitStatus());
            }
            return result.toString();
        }

    }

}
