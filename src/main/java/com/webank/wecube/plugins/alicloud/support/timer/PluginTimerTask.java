package com.webank.wecube.plugins.alicloud.support.timer;

import com.webank.wecube.plugins.alicloud.common.PluginException;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author howechen
 */
public class PluginTimerTask implements Runnable {

    private int DELAY_TIME = 5;
    private Function<?, Boolean> func;


    public PluginTimerTask(Function<?, Boolean> func, int delayTime) {
        this.DELAY_TIME = delayTime;
        this.func = func;
    }

    public PluginTimerTask(Function<?, Boolean> func) {
        this.func = func;
    }


    @Override
    public void run() throws PluginException {
        while (!this.func.apply(null)) {
            try {
                TimeUnit.SECONDS.sleep(this.DELAY_TIME);
            } catch (InterruptedException e) {
                throw new PluginException(e.getMessage());
            }
        }
    }
}
