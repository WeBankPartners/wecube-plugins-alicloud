package com.webank.wecube.plugins.alicloud.support.resourceSeeker;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeAvailableResourceRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeAvailableResourceResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse;
import com.google.common.collect.Maps;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs.CoreMemorySpec;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs.SpecInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    public SpecInfo findAvailableInstance(IAcsClient client, String regionId, String zoneId, String instanceChargeType, String coreAndMemoryString, String instanceFamily) throws PluginException, AliCloudException {
        final String locInstanceFamily = handleInstanceFamily(instanceFamily);

        final DescribeAvailableResourceResponse response = queryAvailableResources(client, regionId, zoneId, instanceChargeType);

        final List<DescribeInstanceTypesResponse.InstanceType> foundInstanceTypes = queryTypes(client, locInstanceFamily);

        return filterAvailableResource(response, foundInstanceTypes, locInstanceFamily, coreAndMemoryString);
    }

    private DescribeAvailableResourceResponse queryAvailableResources(IAcsClient client, String regionId, String zoneId, String instanceChargeType) {
        DescribeAvailableResourceRequest request = new DescribeAvailableResourceRequest();

        request.setZoneId(zoneId);
        request.setInstanceChargeType(instanceChargeType);
        request.setDestinationResource(DestinationResource.INSTANCE.getInstanceType());
        request.setResourceType(ResourceType.INSTANCE.getResourceType());

        return this.acsClientStub.request(client, request, regionId);
    }

    /**
     * Filter out available resource according to the given instanceFamily
     *
     * @param response         AliCloud's search resource response
     * @param allInstanceTypes already found instance types including spec info
     * @return found resource spec info
     * @throws PluginException   plugin exception
     * @throws AliCloudException alicloud exception
     */
    private SpecInfo filterAvailableResource(DescribeAvailableResourceResponse response, List<DescribeInstanceTypesResponse.InstanceType> allInstanceTypes, String instanceFamily, String coreAndMemoryString) throws PluginException, AliCloudException {

        final List<String> availableTypes;
        try {
            if (response.getAvailableZones().get(0).getAvailableResources().isEmpty()) {
                throw new PluginException("Cannot find relevant instance resource.");
            }
            availableTypes = response.getAvailableZones().get(0).getAvailableResources().get(0).getSupportedResources().stream()
                    .filter(supportedResource ->
                            StringUtils.equalsIgnoreCase(RESOURCE_STATUS.Available.toString(), supportedResource.getStatus())
                                    && StringUtils.equalsIgnoreCase(STATUS_CATEGORY.WithStock.toString(), supportedResource.getStatusCategory()))
                    .filter(supportedResource -> StringUtils.containsIgnoreCase(supportedResource.getValue(), instanceFamily))
                    .map(DescribeAvailableResourceResponse.AvailableZone.AvailableResource.SupportedResource::getValue)
                    .collect(Collectors.toList());
        } catch (IndexOutOfBoundsException ex) {
            throw new PluginException("Cannot find relevant instance resource.");
        }

        if (availableTypes.isEmpty()) {
            throw new PluginException(String.format("There is no available and with-stock resource that match the given instanceFamily: [%s].", instanceFamily));
        }

        final List<Map.Entry<String, CoreMemorySpec>> sortedFitTypeList = allInstanceTypes.stream()
                .filter(instanceType -> availableTypes.contains(instanceType.getInstanceTypeId()))
                .map(instanceType -> Maps.immutableEntry(instanceType.getInstanceTypeId(), new CoreMemorySpec(instanceType.getCpuCoreCount(), Math.round(instanceType.getMemorySize()))))
                .filter(CoreMemorySpec.greaterThan(new CoreMemorySpec(coreAndMemoryString)))
                .sorted(Map.Entry.comparingByValue(CoreMemorySpec.COMPARATOR))
                .collect(Collectors.toList());

        if (sortedFitTypeList.isEmpty()) {
            throw new PluginException(String.format("Cannot find relevant instance resource according to the given instance spec: [%s]", coreAndMemoryString));
        }

        final Map.Entry<String, CoreMemorySpec> fitType = sortedFitTypeList.get(0);

        return new SpecInfo(fitType.getKey(), fitType.getValue());
    }

    private List<DescribeInstanceTypesResponse.InstanceType> queryTypes(IAcsClient client, String instanceTypeFamily) throws PluginException, AliCloudException {
        DescribeInstanceTypesRequest request = new DescribeInstanceTypesRequest();
        request.setInstanceTypeFamily(instanceTypeFamily);
        final DescribeInstanceTypesResponse response = acsClientStub.request(client, request);
        if (response.getInstanceTypes().isEmpty()) {
            throw new PluginException(String.format("Cannot find instance types by given instanceTypeFamily: [%s]", instanceTypeFamily));
        }

        return response.getInstanceTypes();
    }

    private String handleInstanceFamily(String rawStr) {
        if (StringUtils.containsIgnoreCase(rawStr, "ecs.")) {
            return rawStr;
        } else {
            return "ecs." + rawStr;
        }
    }

    private enum RESOURCE_STATUS {
        // available
        Available,
        // sold out
        SoldOut
    }

    private enum STATUS_CATEGORY {
        // with stock
        WithStock,
        // closed with stock
        ClosedWithStock,
        // without stock
        WithoutStock,
        // closed without stock
        ClosedWithoutStock
    }
}
