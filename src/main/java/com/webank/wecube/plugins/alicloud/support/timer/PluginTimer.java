package com.webank.wecube.plugins.alicloud.support.timer;

import com.webank.wecube.plugins.alicloud.common.PluginException;

import java.util.concurrent.*;

/**
 * @author howechen
 */
public interface PluginTimer {
    int TIMEOUT = 15;

    /**
     * Run task with default timeout time.
     *
     * @param task
     */
    static void runTask(Runnable task) {
        runTask(task, TIMEOUT);
    }

    static void runTask(Runnable task, int timeout) throws PluginException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        final Future<?> scheduledFuture = service.submit(task);
        try {
            scheduledFuture.get(timeout, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new PluginException(String.format("The running task thread has been interrupted. The error is: %s", e.getMessage()));
        } catch (ExecutionException e) {
            throw new PluginException(String.format("Encounter error while running the timer task. The error is: %s", e.getMessage()));
        } catch (TimeoutException e) {
            throw new PluginException("The request is timeout.");
        }
    }
}
