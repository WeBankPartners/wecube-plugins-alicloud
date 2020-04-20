package com.webank.wecube.plugins.alicloud.support.resourceSeeker;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeAvailableResourceRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeAvailableResourceResponse;
import com.google.common.collect.Iterables;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
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
        // cloud
        cloud,
        // efficiency cloud disk
        cloud_efficiency,
        // cloud ssd
        cloud_ssd,
        // ephermeral ssd
        ephemeral_ssd,
        // cloud essd
        cloud_essd
    }


    private static final Logger logger = LoggerFactory.getLogger(ECSResourceSeeker.class);

    private final AcsClientStub acsClientStub;

    @Autowired
    public ECSResourceSeeker(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }


    public String findAvailableInstance(IAcsClient client, String regionId, String zoneId, String instanceChargeType, String coreAndMemoryString) throws PluginException, AliCloudException {

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

    public String findAvailableDataDisk(IAcsClient client, String regionId, String zoneId, String dataDiskCategory) throws PluginException, AliCloudException {

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

    /**
     * Find last available resource according to the search result
     *
     * @param client  AliCloud client
     * @param request AliCloud's search resource request
     * @return found resource type string
     * @throws PluginException   plugin exception
     * @throws AliCloudException alicloud exception
     */
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
