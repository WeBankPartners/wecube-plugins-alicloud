package com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static void main(String[] args) {
        CoreMemorySpec spec1 = new CoreMemorySpec(1, 2);
        CoreMemorySpec spec3 = new CoreMemorySpec(2, 2);
        CoreMemorySpec spec4 = new CoreMemorySpec(2, 4);
        CoreMemorySpec spec2 = new CoreMemorySpec(1, 4);

        List<CoreMemorySpec> test = Lists.newArrayList(spec1, spec2, spec3, spec4);
        Collections.shuffle(test);
        test.sort(CoreMemorySpec.COMPARATOR);
        System.out.println(test);

        List<String> keyFilterList = Lists.newArrayList("1", "2");
        CoreMemorySpec pivot = new CoreMemorySpec(1, 3);

        Map<String, CoreMemorySpec> result = new HashMap<>();
        result.put("1", spec4);
        result.put("3", spec2);
        result.put("4", spec1);
        result.put("2", spec3);
        final List<Map.Entry<String, CoreMemorySpec>> resultList = result.entrySet().stream().filter(CoreMemorySpec.greaterThan(pivot)).sorted(Map.Entry.comparingByValue(CoreMemorySpec.COMPARATOR)).collect(Collectors.toList());
        System.out.println(resultList);

        final Map<String, CoreMemorySpec> stringCoreMemorySpecMap = Maps.filterKeys(result, keyFilterList::contains);
        System.out.println(stringCoreMemorySpecMap);
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
