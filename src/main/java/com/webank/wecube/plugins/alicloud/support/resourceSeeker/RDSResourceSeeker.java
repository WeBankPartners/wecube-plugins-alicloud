package com.webank.wecube.plugins.alicloud.support.resourceSeeker;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.rds.model.v20140815.DescribeAvailableResourceRequest;
import com.aliyuncs.rds.model.v20140815.DescribeAvailableResourceResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs.CoreMemorySpec;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs.RDSMariaDBSpec;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs.RDSMySQLSpec;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs.SpecInfo;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
@Component
public class RDSResourceSeeker {
    public enum RDSEngine {
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

    public SpecInfo findAvailableResource(IAcsClient client, String engine, String coreMemoryString, String regionId, String zoneId, String engineVersion, String instanceChargeType, String dBInstanceStorageType, String category) {
        SpecInfo result;
        switch (EnumUtils.getEnumIgnoreCase(RDSEngine.class, engine)) {
            case MYSQL:
                result = findAvailableMySQLResource(client, coreMemoryString, regionId, zoneId, engineVersion, instanceChargeType, dBInstanceStorageType, category);
                break;
            case MARIADB:
                result = findAvailableMariaDBResource(client, coreMemoryString, regionId, zoneId, engineVersion, instanceChargeType, dBInstanceStorageType, category);
                break;
            default:
                String msg = String.format("The plugin doesn't support the given RDS engine: [%s]", engine);
                logger.error(msg);
                throw new PluginException(msg);
        }

        return result;

    }

    private SpecInfo findAvailableMySQLResource(IAcsClient client, String coreMemoryString, String regionId, String zoneId, String engineVersion, String instanceChargeType, String dBInstanceStorageType, String category) {
        DescribeAvailableResourceRequest request = new DescribeAvailableResourceRequest();
        request.setZoneId(zoneId);
        request.setEngine(RDSEngine.MYSQL.getEngine());
        request.setEngineVersion(engineVersion);
        request.setInstanceChargeType(instanceChargeType);
        request.setDBInstanceStorageType(dBInstanceStorageType);
        request.setCategory(category);

        final DescribeAvailableResourceResponse response = acsClientStub.request(client, request, regionId);

        return matchResourceStringFromList(RDSEngine.MYSQL, coreMemoryString, response);
    }


    private SpecInfo findAvailableMariaDBResource(IAcsClient client, String coreMemoryString, String regionId, String zoneId, String engineVersion, String instanceChargeType, String dBInstanceStorageType, String category) {
        DescribeAvailableResourceRequest request = new DescribeAvailableResourceRequest();
        request.setZoneId(zoneId);
        request.setEngine(RDSEngine.MARIADB.getEngine());
        request.setEngineVersion(engineVersion);
        request.setInstanceChargeType(instanceChargeType);
        request.setDBInstanceStorageType(dBInstanceStorageType);
        request.setCategory(category);

        final DescribeAvailableResourceResponse response = acsClientStub.request(client, request, regionId);

        return matchResourceStringFromList(RDSEngine.MARIADB, coreMemoryString, response);
    }


    private SpecInfo matchResourceStringFromList(RDSEngine engine, String coreMemoryString, DescribeAvailableResourceResponse response) throws PluginException {

        List<DescribeAvailableResourceResponse.AvailableZone.SupportedEngine.SupportedEngineVersion.SupportedCategory.SupportedStorageType.AvailableResource> foundResourceList;
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
        List<Map.Entry<String, CoreMemorySpec>> matchResult = new ArrayList<>();
        switch (engine) {
            case MYSQL:
                matchResult = RDSMySQLSpec.matchResource(dBInstanceClassStr, coreMemoryString);
                break;
            case MARIADB:
                matchResult = RDSMariaDBSpec.matchResource(dBInstanceClassStr, coreMemoryString);
                break;
            default:
                break;
        }

        if (matchResult.isEmpty()) {
            throw new PluginException("The given coreMemoryString doesn't match all available resources.");
        }

        return new SpecInfo(matchResult.get(0).getKey(), matchResult.get(0).getValue());

    }


}
