package com.webank.wecube.plugins.alicloud.support.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * @author howechen
 */
public class PluginTimerScheduleTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PluginTimerScheduleTask.class);

    private final Function<?, Boolean> func;


    public PluginTimerScheduleTask(Function<?, Boolean> func) {
        this.func = func;
    }


    @Override
    public void run() throws InterruptTaskException {
        if (func != null) {
            logger.info("Running plugin timer task.");
            if (func.apply(null)) {
                throw new InterruptTaskException("The task has finished");
            }
        }
    }

}
