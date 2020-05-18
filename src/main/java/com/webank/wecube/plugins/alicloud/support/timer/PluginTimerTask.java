package com.webank.wecube.plugins.alicloud.support.timer;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author howechen
 */
public class PluginTimerTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PluginTimerTask.class);

    private int DELAY_TIME = 5;
    private final Function<?, Boolean> func;


    public PluginTimerTask(Function<?, Boolean> func, int delayTime) {
        this.DELAY_TIME = delayTime;
        this.func = func;
    }

    public PluginTimerTask(Function<?, Boolean> func) {
        this.func = func;
    }


    @Override
    public void run() throws PluginException {
        int times = 0;
        logger.info(String.format("Running plugin timer task... Retry times: [%d]", times));
        while (!this.func.apply(null)) {
            times += 1;
            logger.info(String.format("Running plugin timer task... Retry times: [%d]", times));

            try {
                TimeUnit.SECONDS.sleep(this.DELAY_TIME);
            } catch (InterruptedException e) {
                throw new PluginException(e.getMessage());
            }
        }

        logger.info("Plugin timer task completed.");
    }
}
