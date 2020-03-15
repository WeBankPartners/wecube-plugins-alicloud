package com.webank.wecube.plugins.alicloud.service.rds;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.rds.model.v20140815.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreCreateBackupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreCreateBackupResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreDeleteBackupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreDeleteBackupResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreCreateDBInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreCreateDBInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreDeleteDBInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreDeleteDBInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityIP.CoreModifySecurityIPsRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityIP.CoreModifySecurityIPsResponseDto;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.PluginSdkBridge;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author howechen
 */
@Service
public class RDSServiceImpl implements RDSService {

    private static Logger logger = LoggerFactory.getLogger(RDSService.class);

    private AcsClientStub acsClientStub;

    @Autowired
    public RDSServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateDBInstanceResponseDto> createDB(List<CoreCreateDBInstanceRequestDto> requestDtoList) throws PluginException {
        List<CoreCreateDBInstanceResponseDto> resultList = new ArrayList<>();
        for (CoreCreateDBInstanceRequestDto requestDto : requestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            if (StringUtils.isNotEmpty(requestDto.getDBInstanceId())) {
                final String instanceId = requestDto.getDBInstanceId();
                final DescribeDBInstancesResponse retrieveDBInstance = this.retrieveDBInstance(client, regionId, instanceId);
                if (1 == retrieveDBInstance.getTotalRecordCount()) {
                    final DescribeDBInstancesResponse.DBInstance dbInstance = retrieveDBInstance.getItems().get(0);
                    CoreCreateDBInstanceResponseDto result = PluginSdkBridge.fromSdk(dbInstance, CoreCreateDBInstanceResponseDto.class);
                    result.setGuid(requestDto.getGuid());
                    result.setCallbackParameter(requestDto.getCallbackParameter());
                    resultList.add(result);
                }

            } else {
                logger.info("Creating DB instance....");

                final CreateDBInstanceRequest createDBInstanceRequest = PluginSdkBridge.toSdk(requestDto, CreateDBInstanceRequest.class);
                CreateDBInstanceResponse response;
                try {
                    response = this.acsClientStub.request(client, createDBInstanceRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                CoreCreateDBInstanceResponseDto result = PluginSdkBridge.fromSdk(response, CoreCreateDBInstanceResponseDto.class);
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());

                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreDeleteDBInstanceResponseDto> deleteDB(List<CoreDeleteDBInstanceRequestDto> requestDtoList) throws PluginException {
        List<CoreDeleteDBInstanceResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteDBInstanceRequestDto requestDto : requestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String regionId = cloudParamDto.getRegionId();

            final String dbInstanceId = requestDto.getDBInstanceId();

            if (StringUtils.isAnyEmpty(regionId, dbInstanceId)) {
                throw new PluginException("Either the regionId or dbInstanceID cannot be empty or null.");
            }

            DescribeDBInstancesResponse describeDBInstancesResponse = this.retrieveDBInstance(client, regionId, dbInstanceId);
            if (0 == describeDBInstancesResponse.getTotalRecordCount()) {
                throw new PluginException("The given db instance has already been released...");
            }

            logger.info("Deleting DB instance...");

            requestDto.setRegionId(regionId);
            final DeleteDBInstanceRequest deleteDBInstanceRequest = PluginSdkBridge.toSdk(requestDto, DeleteDBInstanceRequest.class);
            DeleteDBInstanceResponse response;
            try {
                response = this.acsClientStub.request(client, deleteDBInstanceRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }

            describeDBInstancesResponse = this.retrieveDBInstance(client, regionId, dbInstanceId);
            if (1 == describeDBInstancesResponse.getTotalRecordCount()) {
                throw new PluginException("The given db instance cannot be released");
            }

            CoreDeleteDBInstanceResponseDto result = PluginSdkBridge.fromSdk(response, CoreDeleteDBInstanceResponseDto.class);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<CoreModifySecurityIPsResponseDto> modifySecurityIPs(List<CoreModifySecurityIPsRequestDto> requestDtoList) throws PluginException {
        List<CoreModifySecurityIPsResponseDto> resultList = new ArrayList<>();
        for (CoreModifySecurityIPsRequestDto requestDto : requestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String regionId = cloudParamDto.getRegionId();


            if (StringUtils.isAnyEmpty(regionId, requestDto.getSecurityIps())) {
                throw new PluginException("Either regionId or security IP cannot be null or empty.");
            }
            requestDto.setRegionId(regionId);
            final ModifySecurityIpsRequest request = PluginSdkBridge.toSdk(requestDto, ModifySecurityIpsRequest.class);
            ModifySecurityIpsResponse response;
            try {
                response = this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }

            CoreModifySecurityIPsResponseDto result = PluginSdkBridge.fromSdk(response, CoreModifySecurityIPsResponseDto.class);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<CoreCreateBackupResponseDto> createBackup(List<CoreCreateBackupRequestDto> requestDtoList) throws PluginException {
        List<CoreCreateBackupResponseDto> resultList = new ArrayList<>();
        for (CoreCreateBackupRequestDto requestDto : requestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            if (StringUtils.isNotEmpty(requestDto.getBackupJobId())) {
                final String backupJobId = requestDto.getBackupJobId();
                final DescribeBackupsResponse describeBackupsResponse = this.retrieveBackups(client, regionId, requestDto.getDBInstanceId(), backupJobId);
                if (StringUtils.isNotEmpty(describeBackupsResponse.getTotalRecordCount())) {
                    final DescribeBackupsResponse.Backup foundBackup = describeBackupsResponse.getItems().get(0);
                    CoreCreateBackupResponseDto result = PluginSdkBridge.fromSdk(foundBackup, CoreCreateBackupResponseDto.class);
                    result.setGuid(requestDto.getGuid());
                    result.setCallbackParameter(requestDto.getCallbackParameter());
                    resultList.add(result);
                }

            } else {
                logger.info("Creating backup....");

                final CreateBackupRequest createDBInstanceRequest = PluginSdkBridge.toSdk(requestDto, CreateBackupRequest.class);
                CreateBackupResponse response;
                try {
                    response = this.acsClientStub.request(client, createDBInstanceRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                CoreCreateBackupResponseDto result = PluginSdkBridge.fromSdk(response, CoreCreateBackupResponseDto.class);
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());

                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreDeleteBackupResponseDto> deleteBackup(List<CoreDeleteBackupRequestDto> requestDtoList) throws PluginException {
        List<CoreDeleteBackupResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteBackupRequestDto requestDto : requestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String regionId = cloudParamDto.getRegionId();

            final String dbInstanceId = requestDto.getDBInstanceId();
            final String backupId = requestDto.getBackupId();

            if (StringUtils.isAnyEmpty(regionId, dbInstanceId, backupId)) {
                throw new PluginException("Either the regionId, dbInstanceId or backupId cannot be empty or null.");
            }

            DescribeBackupsResponse describeBackupsResponse = this.retrieveBackups(client, regionId, dbInstanceId, backupId);
            if (StringUtils.isEmpty(describeBackupsResponse.getTotalRecordCount())) {
                throw new PluginException("The given db instance has already been released...");
            }

            logger.info("Deleting backup...");

            requestDto.setRegionId(regionId);
            final DeleteBackupRequest deleteBackupRequest = PluginSdkBridge.toSdk(requestDto, DeleteBackupRequest.class);
            DeleteBackupResponse response;
            try {
                response = this.acsClientStub.request(client, deleteBackupRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }

            describeBackupsResponse = this.retrieveBackups(client, regionId, dbInstanceId, backupId);
            if (StringUtils.isNotEmpty(describeBackupsResponse.getTotalRecordCount())) {
                throw new PluginException("The given db instance cannot be released");
            }

            CoreDeleteBackupResponseDto result = PluginSdkBridge.fromSdk(response, CoreDeleteBackupResponseDto.class);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    private DescribeDBInstancesResponse retrieveDBInstance(IAcsClient client, String regionId, String dbInstanceId) throws PluginException {
        if (StringUtils.isAnyEmpty(regionId, dbInstanceId)) {
            throw new PluginException("Either regionId or dbInstanceID cannot be null or empty");
        }

        logger.info("Retrieving dbInstance info...");

        DescribeDBInstancesRequest request = new DescribeDBInstancesRequest();
        request.setRegionId(regionId);
        request.setDBInstanceId(dbInstanceId);

        DescribeDBInstancesResponse response;
        try {
            response = this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }
        return response;
    }

    private DescribeBackupsResponse retrieveBackups(IAcsClient client, String regionId, String dbInstanceId, String backupId) throws PluginException {
        if (StringUtils.isAnyEmpty(regionId, dbInstanceId, backupId)) {
            throw new PluginException("Either regionId, dbInstanceID or backupId cannot be null or empty");
        }

        logger.info("Retrieving backup info...");

        DescribeBackupsRequest request = new DescribeBackupsRequest();
        request.setRegionId(regionId);
        request.setDBInstanceId(dbInstanceId);
        request.setBackupId(backupId);

        DescribeBackupsResponse response;
        try {
            response = this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }
        return response;
    }
}
