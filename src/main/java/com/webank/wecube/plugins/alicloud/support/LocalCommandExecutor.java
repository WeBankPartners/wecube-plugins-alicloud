package com.webank.wecube.plugins.alicloud.support;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author howechen
 */
@Component
public class LocalCommandExecutor {

    private static final Logger logger = LoggerFactory.getLogger(LocalCommandExecutor.class);
    private ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
    private PluginExecuteResultHandler resultHandler = new PluginExecuteResultHandler();


    public LocalCommandExecutor() {
    }

    public String runWithReturn(String command) throws PluginException {
        StringBuilder result = new StringBuilder();
        Process pro;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            throw new PluginException("Create runtime false!");
        }
        try {
            pro = runTime.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result.append(line).append("\n");
            }
            input.close();
            output.close();
            pro.destroy();
        } catch (IOException e) {
            logger.error("Execute '{}' command failed.{}", command, e);
        }
        logger.info("Execute '{}' command successful", command);
        return result.toString();
    }

    public void run(String command) throws PluginException {
        run(command, false);
    }

    public void run(CommandLine commandLine) throws PluginException {
        run(commandLine, false);
    }

    public void run(String command, boolean oneAsExit) throws PluginException {
        CommandLine commandLine = handleCommand(command);
        run(commandLine, oneAsExit);
    }

    public void run(CommandLine commandLine, boolean oneAsExit) throws PluginException {
        DefaultExecutor executor = new DefaultExecutor();
        if (oneAsExit) {
            executor.setExitValue(1);
        }
        executor.setWatchdog(watchdog);
        try {
            executor.execute(commandLine);
        } catch (IOException e) {
            throw new PluginException(e.getMessage());
        }
    }

    public PluginExecuteResultHandler runAsync(String command) throws PluginException {
        return runAsync(command, false);
    }

    public PluginExecuteResultHandler runAsync(CommandLine commandLine) throws PluginException {
        return runAsync(commandLine, false);
    }

    public PluginExecuteResultHandler runAsync(String command, boolean oneAsExit) throws PluginException {

        CommandLine commandLine = handleCommand(command);
        return runAsync(commandLine, oneAsExit);
    }

    public PluginExecuteResultHandler runAsync(CommandLine commandLine, boolean oneAsExit) throws PluginException {

        DefaultExecutor executor = new DefaultExecutor();
        if (oneAsExit) {
            executor.setExitValue(1);
        }
        LogOutputStream stream = new LogOutputStream() {
            @Override
            protected void processLine(String s, int i) {
                System.out.println(s);
            }
        };
        executor.setWatchdog(watchdog);
        executor.setStreamHandler(new PumpStreamHandler(stream));
        try {
            executor.execute(commandLine, resultHandler);
        } catch (IOException e) {
            throw new PluginException(e.getMessage());
        }
        return resultHandler;
    }

    private CommandLine handleCommand(String command) {
        return CommandLine.parse(command);
    }

    public static class PluginExecuteResultHandler extends DefaultExecuteResultHandler {
        public PluginExecuteResultHandler() {
        }

        @Override
        public void onProcessComplete(int exitValue) {
            super.onProcessComplete(exitValue);
            logger.info("Run command success");
        }

        @Override
        public void onProcessFailed(ExecuteException e) throws PluginException {
            super.onProcessFailed(e);
            logger.error("Run command failed");
            throw new PluginException(e.getMessage());
        }
    }
}
