package com.webank.wecube.plugins.alicloud.service.rds;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.rds.model.v20140815.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDtoBkp;
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

            CoreCreateDBInstanceResponseDto result = new CoreCreateDBInstanceResponseDto();

            try {

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                if (StringUtils.isNotEmpty(requestDto.getDBInstanceId())) {
                    final String instanceId = requestDto.getDBInstanceId();
                    final DescribeDBInstancesResponse retrieveDBInstance = this.retrieveDBInstance(client, regionId, instanceId);
                    if (1 == retrieveDBInstance.getTotalRecordCount()) {
                        final DescribeDBInstancesResponse.DBInstance dbInstance = retrieveDBInstance.getItems().get(0);
                        result = result.fromSdkCrossLineage(dbInstance);
                        result.setRequestId(retrieveDBInstance.getRequestId());
                        continue;
                    }

                }

                logger.info("Creating DB instance....");

                final CreateDBInstanceRequest createDBInstanceRequest = requestDto.toSdk();
                createDBInstanceRequest.setRegionId(regionId);
                CreateDBInstanceResponse response;
                response = this.acsClientStub.request(client, createDBInstanceRequest);

                result = result.fromSdk(response);


            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
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

            CoreDeleteDBInstanceResponseDto result = new CoreDeleteDBInstanceResponseDto();

            try {

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
                    logger.info("The given db instance has already been released...");
                    result.setRequestId(describeDBInstancesResponse.getRequestId());
                    continue;
                }

                logger.info("Deleting DB instance...");

                final DeleteDBInstanceRequest deleteDBInstanceRequest = requestDto.toSdk();
                deleteDBInstanceRequest.setRegionId(regionId);
                DeleteDBInstanceResponse response;
                response = this.acsClientStub.request(client, deleteDBInstanceRequest);

                describeDBInstancesResponse = this.retrieveDBInstance(client, regionId, dbInstanceId);
                if (1 == describeDBInstancesResponse.getTotalRecordCount()) {
                    throw new PluginException("The given db instance cannot be released");
                }

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreModifySecurityIPsResponseDto> modifySecurityIPs(List<CoreModifySecurityIPsRequestDto> requestDtoList) throws PluginException {
        List<CoreModifySecurityIPsResponseDto> resultList = new ArrayList<>();
        for (CoreModifySecurityIPsRequestDto requestDto : requestDtoList) {

            CoreModifySecurityIPsResponseDto result = new CoreModifySecurityIPsResponseDto();
            try {

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();


                if (StringUtils.isAnyEmpty(regionId, requestDto.getSecurityIps())) {
                    throw new PluginException("Either regionId or security IP cannot be null or empty.");
                }

                final ModifySecurityIpsRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                ModifySecurityIpsResponse response;
                response = this.acsClientStub.request(client, request);

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreCreateBackupResponseDto> createBackup(List<CoreCreateBackupRequestDto> requestDtoList) throws PluginException {
        List<CoreCreateBackupResponseDto> resultList = new ArrayList<>();
        for (CoreCreateBackupRequestDto requestDto : requestDtoList) {

            CoreCreateBackupResponseDto result = new CoreCreateBackupResponseDto();

            try {

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final String dbInstanceId = requestDto.getDBInstanceId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String backupId = requestDto.getBackupId();

                if (StringUtils.isNotEmpty(backupId)) {

                    final DescribeBackupsResponse describeBackupsResponse = this.retrieveBackups(client, regionId, dbInstanceId, backupId);
                    if (StringUtils.isNotEmpty(describeBackupsResponse.getTotalRecordCount())) {
                        final DescribeBackupsResponse.Backup foundBackup = describeBackupsResponse.getItems().get(0);
                        result = result.fromSdkCrossLineage(foundBackup);
                        result.setRequestId(describeBackupsResponse.getRequestId());
                        continue;
                    }

                }

                logger.info("Creating backup....");

                final CreateBackupRequest createDBInstanceRequest = requestDto.toSdk();
                createDBInstanceRequest.setRegionId(regionId);
                CreateBackupResponse response;
                response = this.acsClientStub.request(client, createDBInstanceRequest);
                final DescribeBackupTasksResponse.BackupJob backupJob = this.retrieveBackupFromJobId(client, dbInstanceId, response.getBackupJobId());

                result = result.fromSdkCrossLineage(backupJob);
                result.setRequestId(response.getRequestId());

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreDeleteBackupResponseDto> deleteBackup(List<CoreDeleteBackupRequestDto> requestDtoList) {
        List<CoreDeleteBackupResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteBackupRequestDto requestDto : requestDtoList) {

            CoreDeleteBackupResponseDto result = new CoreDeleteBackupResponseDto();
            try {

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                final String dbInstanceId = requestDto.getDBInstanceId();
                String backupId = requestDto.getBackupId();
                final String backupJobId = requestDto.getBackupJobId();

                // if backup job exists, get backupId through job ID first
                if (StringUtils.isNotEmpty(backupJobId) && StringUtils.isEmpty(backupId)) {
                    // retrieve backupId through backupJobId
                    final DescribeBackupTasksResponse.BackupJob backup = this.retrieveBackupFromJobId(client, dbInstanceId, backupJobId);
                    backupId = backup.getBackupId();
                }

                DescribeBackupsResponse describeBackupsResponse = this.retrieveBackups(client, regionId, dbInstanceId, backupId);

                if (StringUtils.isEmpty(describeBackupsResponse.getTotalRecordCount())) {
                    result.setRequestId(describeBackupsResponse.getRequestId());
                    logger.info("The backup has already been deleted...");
                }

                logger.info("Deleting backup...");

                final DeleteBackupRequest deleteBackupRequest = requestDto.toSdk();
                deleteBackupRequest.setRegionId(regionId);
                deleteBackupRequest.setBackupId(backupId);
                DeleteBackupResponse response = this.acsClientStub.request(client, deleteBackupRequest);

                describeBackupsResponse = this.retrieveBackups(client, regionId, dbInstanceId, backupId);

                if (StringUtils.isNotEmpty(describeBackupsResponse.getTotalRecordCount())) {
                    throw new PluginException("The DB backup cannot be deleted.");
                }

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDtoBkp.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }

        }
        return resultList;
    }

    private DescribeBackupTasksResponse.BackupJob retrieveBackupFromJobId(IAcsClient client, String dbInstanceId, String backupJobId) throws PluginException, AliCloudException {
        DescribeBackupTasksRequest retrieveTasksRequest = new DescribeBackupTasksRequest();
        retrieveTasksRequest.setDBInstanceId(dbInstanceId);
        retrieveTasksRequest.setBackupJobId(backupJobId);
        DescribeBackupTasksResponse retrieveTasksRepsonse;
        retrieveTasksRepsonse = this.acsClientStub.request(client, retrieveTasksRequest);

        final List<DescribeBackupTasksResponse.BackupJob> foundBackupsList = retrieveTasksRepsonse.getItems();

        if (foundBackupsList.isEmpty()) {
            throw new PluginException("Cannot find backup through job ID");
        }

        DescribeBackupTasksResponse.BackupJob result;
        if (1 == foundBackupsList.size()) {
            result = foundBackupsList.get(0);
        } else {
            throw new PluginException(String.format("Error! Found multiple backups from one Job ID: [%s]", backupJobId));
        }

        return result;
    }

    private DescribeDBInstancesResponse retrieveDBInstance(IAcsClient client, String regionId, String dbInstanceId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, dbInstanceId)) {
            throw new PluginException("Either regionId or dbInstanceID cannot be null or empty");
        }

        logger.info("Retrieving dbInstance info...");

        DescribeDBInstancesRequest request = new DescribeDBInstancesRequest();
        request.setRegionId(regionId);
        request.setDBInstanceId(dbInstanceId);

        DescribeDBInstancesResponse response;
        response = this.acsClientStub.request(client, request);
        return response;
    }

    private DescribeBackupsResponse retrieveBackups(IAcsClient client, String regionId, String dbInstanceId, String backupId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, dbInstanceId, backupId)) {
            throw new PluginException("Either regionId, dbInstanceID or backupId cannot be null or empty");
        }

        logger.info("Retrieving backup info...");

        DescribeBackupsRequest request = new DescribeBackupsRequest();
        request.setRegionId(regionId);
        request.setDBInstanceId(dbInstanceId);
        request.setBackupId(backupId);

        DescribeBackupsResponse response;
        response = this.acsClientStub.request(client, request);
        return response;
    }
}
