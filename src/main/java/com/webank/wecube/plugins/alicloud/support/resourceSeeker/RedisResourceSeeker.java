package com.webank.wecube.plugins.alicloud.support.resourceSeeker;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.r_kvstore.model.v20150101.DescribeAvailableResourceRequest;
import com.aliyuncs.r_kvstore.model.v20150101.DescribeAvailableResourceResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author howechen
 */
@Component
public class RedisResourceSeeker {

    public enum Engine {
        // redis
        REDIS("redis"),
        // memcache
        MEMCACHE("memcache");

        String engine;

        Engine(String engine) {
            this.engine = engine;
        }

        public String getEngine() {
            return engine;
        }
    }

    public enum EditionType {
        // community
        COMMUNITY("Community"),
        // enterprise
        ENTERPRISE("Enterprise");

        String editionType;

        EditionType(String editionType) {
            this.editionType = editionType;
        }

        public String getEditionType() {
            return editionType;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(RedisResourceSeeker.class);

    private final AcsClientStub acsClientStub;

    @Autowired
    public RedisResourceSeeker(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    public String findAvailableResource(IAcsClient client,
                                        String regionId,
                                        String zoneId,
                                        String instanceChargeType,
                                        String engine,
                                        String editionType,
                                        String seriesType,
                                        String version,
                                        String architecture,
                                        String shardNumber,
                                        String nodeType,
                                        String capacity) throws PluginException {


        if (!EnumUtils.isValidEnumIgnoreCase(Engine.class, engine)) {
            final String msg = String.format("The given engine type: %s is not supported.", engine);
            logger.error(msg);
            throw new PluginException(msg);
        }

        DescribeAvailableResourceRequest request = new DescribeAvailableResourceRequest();
        request.setRegionId(regionId);
        request.setZoneId(zoneId);
        request.setInstanceChargeType(instanceChargeType);
        request.setEngine(engine);
        final DescribeAvailableResourceResponse response = this.acsClientStub.request(client, request);

        return filterResponse(response, editionType, seriesType, version, architecture, shardNumber, nodeType, capacity);

    }

    private String filterResponse(DescribeAvailableResourceResponse response,
                                  String editionType,
                                  String seriesType,
                                  String version,
                                  String architecture,
                                  String shardNumber,
                                  String nodeType,
                                  String capacity) throws PluginException {
        final List<DescribeAvailableResourceResponse.AvailableZone.SupportedEngine.SupportedEditionType> supportedEditionTypes = response.getAvailableZones().get(0).getSupportedEngines().get(0).getSupportedEditionTypes();
        final Optional<DescribeAvailableResourceResponse.AvailableZone.SupportedEngine.SupportedEditionType> matchEditionTypeOpt = supportedEditionTypes.stream().filter(supportedEditionType -> StringUtils.equals(editionType, supportedEditionType.getEditionType())).findAny();
        final Optional<DescribeAvailableResourceResponse.AvailableZone.SupportedEngine.SupportedEditionType.SupportedSeriesType> matchSeriesTypeOpt;
        if (matchEditionTypeOpt.isPresent()) {
            matchSeriesTypeOpt = matchEditionTypeOpt.get().getSupportedSeriesTypes().stream().filter(supportedSeriesType -> StringUtils.equals(seriesType, supportedSeriesType.getSeriesType())).findAny();
        } else {
            throw new PluginException("Cannot find available resource according to given editionType.");
        }

        final Optional<DescribeAvailableResourceResponse.AvailableZone.SupportedEngine.SupportedEditionType.SupportedSeriesType.SupportedEngineVersion> matchedVersionOpt;
        if (matchSeriesTypeOpt.isPresent()) {
            matchedVersionOpt = matchSeriesTypeOpt.get().getSupportedEngineVersions().stream().filter(supportedEngineVersion -> StringUtils.equals(version, supportedEngineVersion.getVersion())).findAny();
        } else {
            throw new PluginException("Cannot find available resource according to given seriesType.");
        }

        final Optional<DescribeAvailableResourceResponse.AvailableZone.SupportedEngine.SupportedEditionType.SupportedSeriesType.SupportedEngineVersion.SupportedArchitectureType> matchedArchitectureOpt;
        if (matchedVersionOpt.isPresent()) {
            matchedArchitectureOpt = matchedVersionOpt.get().getSupportedArchitectureTypes().stream().filter(supportedArchitectureType -> StringUtils.equals(architecture, supportedArchitectureType.getArchitecture())).findAny();
        } else {
            throw new PluginException("Cannot find available resource according to given version.");
        }

        final Optional<DescribeAvailableResourceResponse.AvailableZone.SupportedEngine.SupportedEditionType.SupportedSeriesType.SupportedEngineVersion.SupportedArchitectureType.SupportedShardNumber> matchedShardOpt;
        if (matchedArchitectureOpt.isPresent()) {
            matchedShardOpt = matchedArchitectureOpt.get().getSupportedShardNumbers().stream().filter(supportedShardNumber -> StringUtils.equals(shardNumber, supportedShardNumber.getShardNumber())).findAny();
        } else {
            throw new PluginException("Cannot find available resource according to given architecture.");
        }

        final Optional<DescribeAvailableResourceResponse.AvailableZone.SupportedEngine.SupportedEditionType.SupportedSeriesType.SupportedEngineVersion.SupportedArchitectureType.SupportedShardNumber.SupportedNodeType> matchedNodeTypeOpt;
        if (matchedShardOpt.isPresent()) {
            matchedNodeTypeOpt = matchedShardOpt.get().getSupportedNodeTypes().stream().filter(supportedNodeType -> StringUtils.equals(nodeType, supportedNodeType.getSupportedNodeType())).findAny();
        } else {
            throw new PluginException("Cannot find available resource according to given shard number.");
        }

        // AliCloud support mb sized capacity, transfer this to larger unit
        String filterCapacity = PluginStringUtils.kbToLargerUnit(Long.parseLong(capacity) * 1024 * 1024);
        final Optional<DescribeAvailableResourceResponse.AvailableZone.SupportedEngine.SupportedEditionType.SupportedSeriesType.SupportedEngineVersion.SupportedArchitectureType.SupportedShardNumber.SupportedNodeType.AvailableResource> matchedCapacityOpt;
        if (matchedNodeTypeOpt.isPresent()) {
            matchedCapacityOpt = matchedNodeTypeOpt.get().getAvailableResources().stream().filter(availableResource -> availableResource.getInstanceClassRemark().contains(filterCapacity)).findFirst();
        } else {
            throw new PluginException("Cannot find available resource according to given editionType.");
        }
        String result;
        if (matchedCapacityOpt.isPresent()) {
            result = matchedCapacityOpt.get().getInstanceClass();
        } else {
            throw new PluginException("Cannot find available resource according to given capacity.");
        }

        return result;

    }


}
