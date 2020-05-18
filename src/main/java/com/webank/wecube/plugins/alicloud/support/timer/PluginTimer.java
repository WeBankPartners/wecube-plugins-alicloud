package com.webank.wecube.plugins.alicloud.support.timer;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author howechen
 */
public interface PluginTimer {

    Logger logger = LoggerFactory.getLogger(PluginTimer.class);
    int TIMEOUT = 15;

    /**
     * Run task with default timeout time.
     *
     * @param task runnable task
     */
    static void runTask(Runnable task) {
        runTask(task, TIMEOUT);
    }

    /**
     * Run task with default timeout time.
     *
     * @param task    the runnable task.
     * @param timeout the specified timeout.
     * @throws PluginException when handling the runnable task.
     */
    static void runTask(Runnable task, int timeout) throws PluginException {

        logger.info("The plugin is running task with the timeout: [{}]", timeout);

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
        } finally {
            if (!scheduledFuture.isCancelled()) {
                scheduledFuture.cancel(true);
            }
            service.shutdownNow();
        }
    }
}
