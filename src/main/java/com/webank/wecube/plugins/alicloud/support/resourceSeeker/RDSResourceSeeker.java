package com.webank.wecube.plugins.alicloud.support.resourceSeeker;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.rds.model.v20140815.DescribeAvailableResourceRequest;
import com.aliyuncs.rds.model.v20140815.DescribeAvailableResourceResponse;
import com.google.common.collect.Iterables;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs.RDSMariaDBSpecs;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs.RDSMySQLSpecs;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RDSResourceSeeker {
    private enum RDSEngine {
        // MySQL
        MYSQL("MySQL"),
        // MariaDB
        MARIADB("MariaDB");

        String engine;

        RDSEngine(String engine) {
            this.engine = engine;
        }

        public String getEngine() {
            return engine;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(ECSResourceSeeker.class);

    private final AcsClientStub acsClientStub;

    @Autowired
    public RDSResourceSeeker(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    public String findAvailableResource(IAcsClient client, String engine, String coreMemoryString, String regionId, String zoneId, String engineVersion, String instanceChargeType, String dBInstanceStorageType, String category) {
        String foundResourceResult;
        switch (EnumUtils.getEnumIgnoreCase(RDSEngine.class, engine)) {
            case MYSQL:
                foundResourceResult = findAvailableMySQLResource(client, coreMemoryString, regionId, zoneId, engineVersion, instanceChargeType, dBInstanceStorageType, category);
                break;
            case MARIADB:
                foundResourceResult = findAvailableMariaDBResource(client, coreMemoryString, regionId, zoneId, engineVersion, instanceChargeType, dBInstanceStorageType, category);
                break;
            default:
                String msg = String.format("The plugin doesn't support the given RDS engine: [%s]", engine);
                logger.error(msg);
                throw new PluginException(msg);
        }
        if (StringUtils.isEmpty(foundResourceResult)) {
            throw new PluginException("Cannot find available resource.");
        }
        return foundResourceResult;

    }

    private String findAvailableMySQLResource(IAcsClient client, String coreMemoryString, String regionId, String zoneId, String engineVersion, String instanceChargeType, String dBInstanceStorageType, String category) {
        DescribeAvailableResourceRequest request = new DescribeAvailableResourceRequest();
        request.setRegionId(regionId);
        request.setZoneId(zoneId);
        request.setEngine(RDSEngine.MYSQL.getEngine());
        request.setEngineVersion(engineVersion);
        request.setInstanceChargeType(instanceChargeType);
        request.setDBInstanceStorageType(dBInstanceStorageType);
        request.setCategory(category);

        final DescribeAvailableResourceResponse response = acsClientStub.request(client, request);

        return matchResourceStringFromList(RDSEngine.MYSQL, coreMemoryString, response);
    }


    private String findAvailableMariaDBResource(IAcsClient client, String coreMemoryString, String regionId, String zoneId, String engineVersion, String instanceChargeType, String dBInstanceStorageType, String category) {
        DescribeAvailableResourceRequest request = new DescribeAvailableResourceRequest();
        request.setRegionId(regionId);
        request.setZoneId(zoneId);
        request.setEngine(RDSEngine.MARIADB.getEngine());
        request.setEngineVersion(engineVersion);
        request.setInstanceChargeType(instanceChargeType);
        request.setDBInstanceStorageType(dBInstanceStorageType);
        request.setCategory(category);

        final DescribeAvailableResourceResponse response = acsClientStub.request(client, request);

        return matchResourceStringFromList(RDSEngine.MYSQL, coreMemoryString, response);
    }


    private String matchResourceStringFromList(RDSEngine engine, String coreMemoryString, DescribeAvailableResourceResponse response) throws PluginException {
        String locCoreMemoryString = coreMemoryString.toUpperCase();

        List<DescribeAvailableResourceResponse.AvailableZone.SupportedEngine.SupportedEngineVersion.SupportedCategory.SupportedStorageType.AvailableResource> foundResourceList = new ArrayList<>();
        try {
            foundResourceList = response
                    .getAvailableZones().get(0)
                    .getSupportedEngines().get(0)
                    .getSupportedEngineVersions().get(0)
                    .getSupportedCategorys().get(0)
                    .getSupportedStorageTypes().get(0)
                    .getAvailableResources();
        } catch (IndexOutOfBoundsException ex) {
            throw new PluginException("Alicloud cannot find the required resources.");
        }


        if (foundResourceList.isEmpty()) {
            throw new PluginException("Cannot find resource by given requirement.");
        }

        final List<String> dBInstanceClassStr = foundResourceList.stream().map(DescribeAvailableResourceResponse.AvailableZone.SupportedEngine.SupportedEngineVersion.SupportedCategory.SupportedStorageType.AvailableResource::getDBInstanceClass).collect(Collectors.toList());
        List<String> matchResult = new ArrayList<>();
        switch (engine) {
            case MYSQL:
                matchResult = dBInstanceClassStr.stream().filter(s -> StringUtils.equals(locCoreMemoryString, RDSMySQLSpecs.matchByInstanceClass(s))).collect(Collectors.toList());
                break;
            case MARIADB:
                matchResult = dBInstanceClassStr.stream().filter(s -> StringUtils.equals(locCoreMemoryString, RDSMariaDBSpecs.matchByInstanceClass(s))).collect(Collectors.toList());
                break;
            default:
                break;
        }

        if (matchResult.isEmpty()) {
            throw new PluginException("The given coreMemoryString doesn't match all available resources.");
        }

        return Iterables.getLast(matchResult);
    }


}
