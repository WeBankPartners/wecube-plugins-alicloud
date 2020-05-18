package com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author howechen
 */
public class CoreMemorySpec {

    public static final Comparator<CoreMemorySpec> COMPARATOR = Comparator.comparing(CoreMemorySpec::getCore).thenComparing(CoreMemorySpec::getMemory);

    private int core;
    private int memory;

    public CoreMemorySpec() {
    }

    public CoreMemorySpec(int core, int memory) {
        this.core = core;
        this.memory = memory;
    }

    public CoreMemorySpec(String coreMemoryStr) {
        final Pair<String, String> coreMemoryPair = PluginStringUtils.splitCoreAndMemory(coreMemoryStr);
        try {
            this.core = Integer.parseInt(coreMemoryPair.getKey());
            this.memory = Integer.parseInt(coreMemoryPair.getValue());
        } catch (NumberFormatException ex) {
            throw new PluginException(String.format("Cannot transfer coreMemoryStr: [%s]to integer", coreMemoryStr));
        }
    }

    public static Predicate<? super Map.Entry<String, CoreMemorySpec>> greaterThan(CoreMemorySpec target) {
        return entry -> {
            final int thisCore = entry.getValue().getCore();
            final int thisMemory = entry.getValue().getMemory();
            final int thatCore = target.getCore();
            final int thatMemory = target.getMemory();

            if (thisCore == thatCore) {
                return thisMemory >= thatMemory;
            } else {
                return thisCore >= thatCore;
            }
        };
    }

    public int getCore() {
        return core;
    }

    public void setCore(int core) {
        this.core = core;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("core", core)
                .append("memory", memory)
                .toString();
    }


}
