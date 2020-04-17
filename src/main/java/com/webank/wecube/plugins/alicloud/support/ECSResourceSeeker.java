package com.webank.wecube.plugins.alicloud.support;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeAvailableResourceRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeAvailableResourceResponse;
import com.google.common.collect.Iterables;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author howechen
 */
@Component
public class ECSResourceSeeker {

    private enum DestinationResource {
        // instance
        INSTANCE("InstanceType"),
        // system disk
        SYSTEM_DISK("SystemDisk"),
        // data disk
        DATA_DISK("DataDisk");

        String instanceType;

        DestinationResource(String instanceType) {
            this.instanceType = instanceType;
        }

        String getInstanceType() {
            return this.instanceType;
        }
    }

    private enum ResourceType {
        // instance
        INSTANCE("instance"),
        // disk
        DISK("disk");

        String resourceType;

        ResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        String getResourceType() {
            return this.resourceType;
        }
    }

    private enum DataDiskCategory {
        cloud,
        cloud_efficiency,
        cloud_ssd,
        ephemeral_ssd,
        cloud_essd
    }


    private static final Logger logger = LoggerFactory.getLogger(ECSResourceSeeker.class);

    private final AcsClientStub acsClientStub;

    @Autowired
    public ECSResourceSeeker(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }


    private String findAvailableInstance(IAcsClient client, String regionId, String zoneId, String instanceChargeType, String coreAndMemoryString) throws PluginException, AliCloudException {

        final Pair<String, String> coreMemoryPair = PluginStringUtils.splitCoreAndMemory(coreAndMemoryString);

        DescribeAvailableResourceRequest request = new DescribeAvailableResourceRequest();

        request.setRegionId(regionId);
        request.setZoneId(zoneId);
        request.setInstanceChargeType(instanceChargeType);
        request.setCores(Integer.valueOf(coreMemoryPair.getLeft()));
        request.setMemory(Float.valueOf(coreMemoryPair.getRight()));
        request.setDestinationResource(DestinationResource.INSTANCE.getInstanceType());
        request.setResourceType(ResourceType.INSTANCE.getResourceType());

        return getLastResource(client, request);
    }

    private String findAvailableDataDisk(IAcsClient client, String regionId, String zoneId, String dataDiskCategory) throws PluginException, AliCloudException {

        if (!EnumUtils.isValidEnumIgnoreCase(DataDiskCategory.class, dataDiskCategory)) {
            throw new PluginException("Invalid dataDiskCategory");
        }

        DescribeAvailableResourceRequest request = new DescribeAvailableResourceRequest();
        request.setRegionId(regionId);
        request.setZoneId(zoneId);
        request.setDataDiskCategory(dataDiskCategory);
        request.setDestinationResource(DestinationResource.DATA_DISK.getInstanceType());
        request.setResourceType(ResourceType.DISK.getResourceType());

        return getLastResource(client, request);
    }

    private String getLastResource(IAcsClient client, DescribeAvailableResourceRequest request) throws PluginException, AliCloudException {
        final DescribeAvailableResourceResponse response = this.acsClientStub.request(client, request);

        if (response.getAvailableZones().get(0).getAvailableResources().isEmpty()) {
            throw new PluginException("Cannot find relevant instance resource.");
        }

        final List<DescribeAvailableResourceResponse.AvailableZone.AvailableResource.SupportedResource> supportedResources = response.getAvailableZones().get(0).getAvailableResources().get(0).getSupportedResources();
        final DescribeAvailableResourceResponse.AvailableZone.AvailableResource.SupportedResource last = Iterables.getLast(supportedResources);
        return last.getValue();
    }
}
