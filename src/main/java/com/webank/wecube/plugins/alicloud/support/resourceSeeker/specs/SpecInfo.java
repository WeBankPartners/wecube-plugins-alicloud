package com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs;

/**
 * @author howechen
 */
public class SpecInfo {
    private String resourceClass;
    private CoreMemorySpec coreMemorySpec;

    public SpecInfo() {
    }

    public SpecInfo(String resourceClass, CoreMemorySpec coreMemorySpec) {
        this.resourceClass = resourceClass;
        this.coreMemorySpec = coreMemorySpec;
    }

    public String getResourceClass() {
        return resourceClass;
    }

    public void setResourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
    }

    public CoreMemorySpec getCoreMemorySpec() {
        return coreMemorySpec;
    }

    public void setCoreMemorySpec(CoreMemorySpec coreMemorySpec) {
        this.coreMemorySpec = coreMemorySpec;
    }
}
