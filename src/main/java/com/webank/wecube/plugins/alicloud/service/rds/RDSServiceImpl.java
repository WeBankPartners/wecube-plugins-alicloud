package com.webank.wecube.plugins.alicloud.service.rds;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.rds.model.v20140815.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.cloudParam.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.cloudParam.DBCloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreCreateBackupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreCreateBackupResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreDeleteBackupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreDeleteBackupResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreCreateDBInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreCreateDBInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreDeleteDBInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreDeleteDBInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityGroup.CoreModifyDBSecurityGroupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityGroup.CoreModifyDBSecurityGroupResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityIP.CoreModifySecurityIPsRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityIP.CoreModifySecurityIPsResponseDto;
import com.webank.wecube.plugins.alicloud.support.*;
import com.webank.wecube.plugins.alicloud.support.password.PasswordManager;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.RDSResourceSeeker;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs.SpecInfo;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimer;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimerTask;
import com.webank.wecube.plugins.alicloud.utils.PluginObjectUtils;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
@Service
public class RDSServiceImpl implements RDSService {
    private static final String TOLERABLE_DB_NOT_FOUND_CODE = "InvalidDBInstanceId.NotFound";

    private static final Logger logger = LoggerFactory.getLogger(RDSService.class);

    private final AcsClientStub acsClientStub;
    private final DtoValidator dtoValidator;
    private final PasswordManager passwordManager;
    private final RDSResourceSeeker rdsResourceSeeker;

    @Autowired
    public RDSServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator, PasswordManager passwordManager, RDSResourceSeeker rdsResourceSeeker) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
        this.passwordManager = passwordManager;
        this.rdsResourceSeeker = rdsResourceSeeker;
    }

    @Override
    public List<CoreCreateDBInstanceResponseDto> createDB(List<CoreCreateDBInstanceRequestDto> requestDtoList) {
        List<CoreCreateDBInstanceResponseDto> resultList = new ArrayList<>();
        for (CoreCreateDBInstanceRequestDto requestDto : requestDtoList) {

            CoreCreateDBInstanceResponseDto result = new CoreCreateDBInstanceResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final DBCloudParamDto dbCloudParamDto = DBCloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = dbCloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, dbCloudParamDto);

                // zoneId adaption
                zoneIdAdaption(client, dbCloudParamDto, requestDto);

                if (StringUtils.isNotEmpty(requestDto.getDBInstanceId())) {
                    final String instanceId = requestDto.getDBInstanceId();
                    DescribeDBInstancesResponse retrieveDBInstance;
                    try {
                        retrieveDBInstance = this.retrieveDBInstance(client, regionId, instanceId);
                        if (!retrieveDBInstance.getItems().isEmpty()) {
                            final DescribeDBInstancesResponse.DBInstance dbInstance = retrieveDBInstance.getItems().get(0);
                            result = result.fromSdkCrossLineage(dbInstance);
                            result.setRequestId(retrieveDBInstance.getRequestId());
                            continue;
                        }
                    } catch (AliCloudException ex) {
                        if (!TOLERABLE_DB_NOT_FOUND_CODE.equalsIgnoreCase(ex.getErrCode())) {
                            throw ex;
                        }
                    }

                }

                // get Parameter group from current account
                final String parameterGroupId = fetchParameterGroupId(client, regionId, requestDto.getEngine(), requestDto.getEngineVersion());
                requestDto.setdBParamGroupId(parameterGroupId);

                logger.info("Creating DB instance: {}", requestDto.toString());

                // find available resource according to AliCloud's stock and return the result that match the wecube's dBInstanceSpec
                SpecInfo foundSpecInfo = new SpecInfo();
                if (!StringUtils.isEmpty(requestDto.getdBInstanceSpec()) && StringUtils.isEmpty(requestDto.getDBInstanceClass())) {
                    foundSpecInfo = rdsResourceSeeker.findAvailableResource(client,
                            requestDto.getEngine(),
                            requestDto.getdBInstanceSpec(),
                            regionId,
                            requestDto.getZoneId(),
                            requestDto.getEngineVersion(),
                            requestDto.getPayType(),
                            requestDto.getDBInstanceStorageType(),
                            requestDto.getCategory());
                    requestDto.setDBInstanceClass(foundSpecInfo.getResourceClass());
                }

                final CreateDBInstanceRequest createDBInstanceRequest = requestDto.toSdk();
                CreateDBInstanceResponse response;
                response = this.acsClientStub.request(client, createDBInstanceRequest, regionId);

                // set up Plugin Timer to check if the resource is not creating any more
                final String createdDBInstanceId = response.getDBInstanceId();
                Function<?, Boolean> func = o -> !ifDBInstanceInStatus(client, regionId, createdDBInstanceId, RDSStatus.CREATING);
                PluginTimer.runTask(new PluginTimerTask(func));


                // generate and encrypt password
                if (StringUtils.isEmpty(requestDto.getAccountPassword())) {
                    requestDto.setAccountPassword(passwordManager.generateRDSPassword());
                }
                final String encryptedPassword = passwordManager.encryptPassword(requestDto.getGuid(), requestDto.getSeed(), requestDto.getAccountPassword());

                // create an RDS account with just created DB instance bound onto
                createRDSAccount(requestDto, regionId, client, createdDBInstanceId);

                // bind security group to the created RDS instance
                if (StringUtils.isNotEmpty(requestDto.getSecurityGroupId())) {
                    bindSecurityGroupToInstance(client, regionId, requestDto.getSecurityGroupId(), response.getDBInstanceId());
                }

                // get instance's private ip address
                final String dbInstancePrivateIpAddr = getDBInstancePrivateIpAddr(client, regionId, response.getDBInstanceId());

                // return result
                result = result.fromSdk(response, requestDto.getAccountName(), encryptedPassword, foundSpecInfo);
                result.setPrivateIpAddress(dbInstancePrivateIpAddr);


            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }
        }
        return resultList;
    }

    private void createRDSAccount(CoreCreateDBInstanceRequestDto requestDto, String regionId, IAcsClient client, String createdDBInstanceId) throws PluginException, AliCloudException {

        Function<?, Boolean> func;
        final CreateAccountRequest createAccountRequest = PluginSdkBridge.toSdk(requestDto, CreateAccountRequest.class, true);

        createAccountRequest.setDBInstanceId(createdDBInstanceId);

        logger.info("Creating RDS account...");

        this.acsClientStub.request(client, createAccountRequest, regionId);
        func = o -> ifAccountIsAvailable(client, regionId, createdDBInstanceId, createAccountRequest.getAccountName());
        PluginTimer.runTask(new PluginTimerTask(func));
    }


    @Override
    public List<CoreDeleteDBInstanceResponseDto> deleteDB(List<CoreDeleteDBInstanceRequestDto> requestDtoList) {
        List<CoreDeleteDBInstanceResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteDBInstanceRequestDto requestDto : requestDtoList) {

            CoreDeleteDBInstanceResponseDto result = new CoreDeleteDBInstanceResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                final String dbInstanceId = requestDto.getDBInstanceId();

                DescribeDBInstancesResponse describeDBInstancesResponse = this.retrieveDBInstance(client, regionId, dbInstanceId);
                if (0 == describeDBInstancesResponse.getTotalRecordCount()) {
                    logger.info("The given db instance has already been released...");
                    result.setRequestId(describeDBInstancesResponse.getRequestId());
                    continue;
                }

                logger.info("Deleting DB instance: {}", requestDto.toString());

                final DeleteDBInstanceRequest deleteDBInstanceRequest = requestDto.toSdk();
                DeleteDBInstanceResponse response;
                response = this.acsClientStub.request(client, deleteDBInstanceRequest, regionId);


                // set up Plugin Timer to check if the resource has been deleted
                Function<?, Boolean> func = o -> ifDBInstanceIsDeleted(client, regionId, requestDto.getDBInstanceId());
                PluginTimer.runTask(new PluginTimerTask(func));

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreModifySecurityIPsResponseDto> modifySecurityIPs(List<CoreModifySecurityIPsRequestDto> requestDtoList) {
        List<CoreModifySecurityIPsResponseDto> resultList = new ArrayList<>();
        for (CoreModifySecurityIPsRequestDto requestDto : requestDtoList) {

            CoreModifySecurityIPsResponseDto result = new CoreModifySecurityIPsResponseDto();
            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                logger.info("Modifying rds instance's security ip: {}", requestDto.toString());

                ModifySecurityIpsResponse response = modifySecurityIps(requestDto, client, regionId);

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreModifySecurityIPsResponseDto> appendSecurityIps(List<CoreModifySecurityIPsRequestDto> requestDtoList) {

        List<CoreModifySecurityIPsResponseDto> resultList = new ArrayList<>();
        for (CoreModifySecurityIPsRequestDto requestDto : requestDtoList) {

            CoreModifySecurityIPsResponseDto result = new CoreModifySecurityIPsResponseDto();
            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                final DescribeDBInstanceIPArrayListResponse.DBInstanceIPArray foundDBInstance = queryDBInstance(requestDto.getdBInstanceId(), client, regionId);

                // remove the exist element in new ip list
                final List<String> currentIpList = Arrays.asList(foundDBInstance.getSecurityIPList().split(","));
                final List<String> newIpList = PluginStringUtils.splitStringList(requestDto.getSecurityIps()).stream().distinct().collect(Collectors.toList());
                newIpList.removeAll(currentIpList);
                requestDto.setSecurityIps(PluginStringUtils.stringifyObjectList(newIpList));
                requestDto.setModifyMode("Append");

                final ModifySecurityIpsResponse response = modifySecurityIps(requestDto, client, regionId);

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreModifySecurityIPsResponseDto> deleteSecurityIps(List<CoreModifySecurityIPsRequestDto> requestDtoList) {
        List<CoreModifySecurityIPsResponseDto> resultList = new ArrayList<>();
        for (CoreModifySecurityIPsRequestDto requestDto : requestDtoList) {

            CoreModifySecurityIPsResponseDto result = new CoreModifySecurityIPsResponseDto();
            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                final DescribeDBInstanceIPArrayListResponse.DBInstanceIPArray foundDBInstance = queryDBInstance(requestDto.getdBInstanceId(), client, regionId);

                // retain the common eleement in two lists
                final List<String> currentIpList = Arrays.asList(foundDBInstance.getSecurityIPList().split(","));
                List<String> newIpList = PluginStringUtils.splitStringList(requestDto.getSecurityIps()).stream().distinct().collect(Collectors.toList());
                // newIpList now is the intersection of both current ip list and request ip list
                newIpList = currentIpList.stream().filter(newIpList::contains).collect(Collectors.toList());

                if (newIpList.size() == currentIpList.size()) {
                    // AliCloud doesn't allow delete all securityIps
                    // set a default security ip as AliCloud officially do
                    requestDto.setSecurityIps("127.0.0.1");
                    requestDto.setModifyMode("Cover");
                } else {
                    requestDto.setSecurityIps(PluginStringUtils.stringifyObjectList(newIpList));
                    requestDto.setModifyMode("Delete");
                }


                final ModifySecurityIpsResponse response = modifySecurityIps(requestDto, client, regionId);

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreModifyDBSecurityGroupResponseDto> appendSecurityGroup(List<CoreModifyDBSecurityGroupRequestDto> requestDtoList) {
        List<CoreModifyDBSecurityGroupResponseDto> resultList = new ArrayList<>();
        for (CoreModifyDBSecurityGroupRequestDto requestDto : requestDtoList) {

            CoreModifyDBSecurityGroupResponseDto result = new CoreModifyDBSecurityGroupResponseDto();
            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                // append target security group list to current's
                List<String> currentSGList = queryDBSecurityGroup(requestDto.getdBInstanceId(), client, regionId);
                List<String> targetSGList = PluginStringUtils.splitStringList(requestDto.getSecurityGroupId());
                currentSGList.addAll(targetSGList);
                targetSGList = currentSGList.stream().distinct().collect(Collectors.toList());
                requestDto.setSecurityGroupId(PluginStringUtils.stringifyListWithoutBracket(targetSGList));

                final ModifySecurityGroupConfigurationRequest request = requestDto.toSdk();
                final ModifySecurityGroupConfigurationResponse response = acsClientStub.request(client, request, regionId);

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreModifyDBSecurityGroupResponseDto> removeSecurityGroup(List<CoreModifyDBSecurityGroupRequestDto> requestDtoList) {
        List<CoreModifyDBSecurityGroupResponseDto> resultList = new ArrayList<>();
        for (CoreModifyDBSecurityGroupRequestDto requestDto : requestDtoList) {

            CoreModifyDBSecurityGroupResponseDto result = new CoreModifyDBSecurityGroupResponseDto();
            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                // remove target security group list from current's (with non-exist id removal)
                List<String> currentSGList = queryDBSecurityGroup(requestDto.getdBInstanceId(), client, regionId);
                List<String> targetSGList = PluginStringUtils.splitStringList(requestDto.getSecurityGroupId());
                currentSGList.removeAll(targetSGList);
                requestDto.setSecurityGroupId(PluginStringUtils.stringifyListWithoutBracket(currentSGList));

                final ModifySecurityGroupConfigurationRequest request = requestDto.toSdk();
                final ModifySecurityGroupConfigurationResponse response = acsClientStub.request(client, request, regionId);

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreCreateBackupResponseDto> createBackup(List<CoreCreateBackupRequestDto> requestDtoList) {
        List<CoreCreateBackupResponseDto> resultList = new ArrayList<>();
        for (CoreCreateBackupRequestDto requestDto : requestDtoList) {

            CoreCreateBackupResponseDto result = new CoreCreateBackupResponseDto();

            try {

                dtoValidator.validate(requestDto);

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

                logger.info("Creating backup: {}", requestDto.toString());

                final CreateBackupRequest createBackupRequest = requestDto.toSdk();
                CreateBackupResponse response;
                response = this.acsClientStub.request(client, createBackupRequest, regionId);

                final String backupJobId = response.getBackupJobId();

                // wait for the backup job to be finished
                Function<?, Boolean> func = o -> ifBackupTaskInStatus(client, regionId, dbInstanceId, backupJobId, BackupStatus.FINISHED, BackupStatus.FAILED);
                PluginTimer.runTask(new PluginTimerTask(func));

                // retrieve backup info according to the backup job id from the AliCloud
                // only will return backup id when backup task status has been set to finished
                final DescribeBackupTasksResponse.BackupJob backupJob = this.retrieveBackupFromJobId(client, dbInstanceId, backupJobId);

                result = result.fromSdkCrossLineage(backupJob);
                result.setRequestId(response.getRequestId());
                result.setBackupJobId(backupJobId);

                if (StringUtils.equals(BackupStatus.FAILED.getStatus(), result.getBackupStatus())) {
                    throw new PluginException("Backup job failed");
                }

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
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

                dtoValidator.validate(requestDto);

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

                logger.info("Deleting backup: {}", requestDto.toString());

                final DeleteBackupRequest deleteBackupRequest = requestDto.toSdk();
                deleteBackupRequest.setBackupId(backupId);
                DeleteBackupResponse response = this.acsClientStub.request(client, deleteBackupRequest, regionId);

                // setup a timer task to retrieve if the backup has been deleted
                final String finalBackupId = backupId;
                Function<?, Boolean> func = o -> this.ifBackupHasBeenDeleted(client, regionId, dbInstanceId, finalBackupId);
                PluginTimer.runTask(new PluginTimerTask(func));

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }

        }
        return resultList;
    }

    @Override
    public Boolean ifDBInstanceInStatus(IAcsClient client, String regionId, String dbInstanceId, RDSStatus status) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, dbInstanceId)) {
            throw new PluginException("Either regionId or instanceId cannot be null or empty.");
        }

        DescribeDBInstancesRequest request = new DescribeDBInstancesRequest();
        request.setDBInstanceId(dbInstanceId);

        final DescribeDBInstancesResponse response = this.acsClientStub.request(client, request, regionId);

        final Optional<DescribeDBInstancesResponse.DBInstance> foundDBInstanceOpt = response.getItems().stream().filter(dbInstance -> StringUtils.equals(dbInstanceId, dbInstance.getDBInstanceId())).findFirst();

        foundDBInstanceOpt.orElseThrow(() -> new PluginException(String.format("Cannot found DB instance by instanceId: [%s]", dbInstanceId)));

        final DescribeDBInstancesResponse.DBInstance dbInstance = foundDBInstanceOpt.get();

        return StringUtils.equals(status.getStatus(), dbInstance.getDBInstanceStatus());
    }

    public Boolean ifBackupTaskInStatus(IAcsClient client, String regionId, String dbInstanceId, String backupJobId, BackupStatus... statusArray) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, backupJobId)) {
            throw new PluginException("Either regionId or instanceId cannot be null or empty.");
        }

        DescribeBackupTasksRequest request = new DescribeBackupTasksRequest();
        request.setDBInstanceId(dbInstanceId);
        request.setBackupJobId(Integer.valueOf(backupJobId));


        final DescribeBackupTasksResponse response = this.acsClientStub.request(client, request, regionId);

        final Optional<DescribeBackupTasksResponse.BackupJob> foundBackupJobOpt = response.getItems().stream().filter(backupTask -> StringUtils.equals(backupJobId, backupTask.getBackupJobId())).findFirst();

        foundBackupJobOpt.orElseThrow(() -> new PluginException(String.format("Cannot found backup job by dbInstanceId: [%s] and backupJobId: [%s]", dbInstanceId, backupJobId)));

        final DescribeBackupTasksResponse.BackupJob backupJob = foundBackupJobOpt.get();

        final List<String> statusList = Arrays.stream(statusArray).map(BackupStatus::getStatus).collect(Collectors.toList());

        return statusList.contains(backupJob.getBackupStatus());
    }

    private Boolean ifDBInstanceIsDeleted(IAcsClient client, String regionId, String dBInstanceId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, dBInstanceId)) {
            throw new PluginException("Either regionId or dBInstanceId cannot be null or empty.");
        }

        DescribeDBInstancesResponse response;
        try {
            response = this.retrieveDBInstance(client, regionId, dBInstanceId);
        } catch (AliCloudException ex) {
            // AliCloud's RDS will throw error when the resource has been deleted.
            if (ex.getMessage().contains("The specified instance is not found.")) {
                return true;
            } else {
                throw ex;
            }
        }
        return response.getTotalRecordCount() == 0;
    }

    private Boolean ifBackupHasBeenDeleted(IAcsClient client, String regionId, String dBInstanceId, String backupId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, dBInstanceId, backupId)) {
            throw new PluginException("Either regionId or dBInstanceId cannot be null or empty.");
        }

        DescribeBackupsResponse response;
        try {
            response = this.retrieveBackups(client, regionId, dBInstanceId, backupId);
        } catch (AliCloudException ex) {
            // AliCloud's RDS will throw error when the resource has been deleted.
            if (ex.getMessage().contains("The specified instance is not found.")) {
                return true;
            } else {
                throw ex;
            }
        }
        return 0 == Integer.parseInt(response.getTotalRecordCount());
    }


    private DescribeBackupTasksResponse.BackupJob retrieveBackupFromJobId(IAcsClient client, String dbInstanceId, String backupJobId) throws PluginException, AliCloudException {
        DescribeBackupTasksRequest retrieveTasksRequest = new DescribeBackupTasksRequest();
        retrieveTasksRequest.setDBInstanceId(dbInstanceId);
        retrieveTasksRequest.setBackupJobId(Integer.valueOf(backupJobId));
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
        request.setDBInstanceId(dbInstanceId);

        DescribeDBInstancesResponse response;
        response = this.acsClientStub.request(client, request, regionId);
        return response;
    }

    private DescribeBackupsResponse retrieveBackups(IAcsClient client, String regionId, String dbInstanceId, String backupId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, dbInstanceId, backupId)) {
            throw new PluginException("Either regionId, dbInstanceID or backupId cannot be null or empty");
        }

        logger.info("Retrieving backup info...");

        DescribeBackupsRequest request = new DescribeBackupsRequest();
        request.setDBInstanceId(dbInstanceId);
        request.setBackupId(backupId);

        DescribeBackupsResponse response;
        response = this.acsClientStub.request(client, request, regionId);
        return response;
    }

    /**
     * Fetch parameter group id, if no parameter group found, then create one and return
     *
     * @param client   acsclient
     * @param regionId regionId
     * @return found parameter gropu id
     * @throws PluginException   plugin exception
     * @throws AliCloudException alicloud exception
     */
    private String fetchParameterGroupId(IAcsClient client, String regionId, String engine, String engineVersion) throws PluginException, AliCloudException {
        final String presetGroupName = "rds" + "_" + engine + "_" + engineVersion;

        DescribeParameterGroupsRequest request = new DescribeParameterGroupsRequest();

        final DescribeParameterGroupsResponse response = acsClientStub.request(client, request, regionId);
        final List<DescribeParameterGroupsResponse.ParameterGroup> groupList = response.getParameterGroups();
        final List<String> foundId = groupList.stream().filter(parameterGroup -> StringUtils.equalsIgnoreCase(presetGroupName, parameterGroup.getParameterGroupName())).map(DescribeParameterGroupsResponse.ParameterGroup::getParameterGroupId).collect(Collectors.toList());

        String result;
        if (foundId.isEmpty()) {
            // create new one
            createParameterGroup(client, regionId, engine, engineVersion, presetGroupName);
            return fetchParameterGroupId(client, regionId, engine, engineVersion);
        } else {
            result = foundId.get(0);
        }
        return result;
    }

    /**
     * Create new parameter group
     *
     * @param client          acsclient
     * @param regionId        regionId
     * @param engine          engine name
     * @param engineVersion   engine version
     * @param presetGroupName preset group name
     * @throws PluginException   plugin exception
     * @throws AliCloudException alicloud exception
     */
    private void createParameterGroup(IAcsClient client, String regionId, String engine, String engineVersion, String presetGroupName) throws PluginException, AliCloudException {

        CreateParameterGroupRequest request = new CreateParameterGroupRequest();
        request.setEngine(engine.toLowerCase());
        request.setEngineVersion(engineVersion);
        request.setParameterGroupName(presetGroupName);
        request.setParameters(PluginObjectUtils.mapObjectToJsonStr(new RdsParameterGroupTemplate()));

        acsClientStub.request(client, request, regionId);
    }

    private void bindSecurityGroupToInstance(IAcsClient client, String regionId, String securityGroupId, String dbInstanceId) {
        ModifySecurityGroupConfigurationRequest request = new ModifySecurityGroupConfigurationRequest();
        request.setSecurityGroupId(securityGroupId);
        request.setDBInstanceId(dbInstanceId);

        acsClientStub.request(client, request, regionId);
    }


    private static class RdsParameterGroupTemplate {
        @JsonProperty(value = "character_set_server")
        private String characterSetServer = "utf8";

        public RdsParameterGroupTemplate() {
        }

        public String getCharacterSetServer() {
            return characterSetServer;
        }

        public void setCharacterSetServer(String characterSetServer) {
            this.characterSetServer = characterSetServer;
        }
    }


    private ModifySecurityIpsResponse modifySecurityIps(CoreModifySecurityIPsRequestDto requestDto, IAcsClient client, String regionId) throws AliCloudException {
        final ModifySecurityIpsRequest request = requestDto.toSdk();
        ModifySecurityIpsResponse response;
        response = this.acsClientStub.request(client, request, regionId);
        return response;
    }

    private List<String> queryDBSecurityGroup(String dBInstanceId, IAcsClient client, String regionId) throws AliCloudException {
        DescribeSecurityGroupConfigurationRequest request = new DescribeSecurityGroupConfigurationRequest();
        request.setDBInstanceId(dBInstanceId);

        final DescribeSecurityGroupConfigurationResponse response = acsClientStub.request(client, request, regionId);
        return response.getItems().stream().map(DescribeSecurityGroupConfigurationResponse.EcsSecurityGroupRelation::getSecurityGroupId).collect(Collectors.toList());

    }

    private DescribeDBInstanceIPArrayListResponse.DBInstanceIPArray queryDBInstance(String dBInstanceId, IAcsClient client, String regionId) throws PluginException, AliCloudException {
        DescribeDBInstanceIPArrayListRequest queryRequest = new DescribeDBInstanceIPArrayListRequest();
        queryRequest.setDBInstanceId(dBInstanceId);
        final DescribeDBInstanceIPArrayListResponse queryResponse = acsClientStub.request(client, queryRequest, regionId);


        if (queryResponse.getItems().isEmpty()) {
            throw new PluginException(String.format("Cannot find dBInstance by given instanceId: [%s]", dBInstanceId));
        }

        return queryResponse.getItems().get(0);
    }

    private boolean ifAccountIsAvailable(IAcsClient client, String regionId, String dBInstanceId, String accountName) throws PluginException, AliCloudException {

        logger.info("Querying account info to check the status...");

        DescribeAccountsRequest request = new DescribeAccountsRequest();
        request.setDBInstanceId(dBInstanceId);
        request.setAccountName(accountName);

        final DescribeAccountsResponse response = acsClientStub.request(client, request, regionId);

        if (response.getAccounts().isEmpty()) {
            throw new PluginException("Cannot find account info.");
        }

        final String currentStatus = response.getAccounts().get(0).getAccountStatus();

        return StringUtils.equalsIgnoreCase(AccountStatus.Available.toString(), currentStatus);
    }

    private String getDBInstancePrivateIpAddr(IAcsClient client, String regionId, String dBInstanceId) throws PluginException, AliCloudException {

        logger.info("Retrieving instance: [{}]'s private ip address.", dBInstanceId);

        DescribeDBInstanceNetInfoRequest request = new DescribeDBInstanceNetInfoRequest();
        request.setDBInstanceId(dBInstanceId);

        final DescribeDBInstanceNetInfoResponse response = acsClientStub.request(client, request, regionId);

        final List<String> privateIpAddrList = response.getDBInstanceNetInfos().stream()
                .filter(dbInstanceNetInfo -> StringUtils.equals(DBInstanceIPType.Private.toString(), dbInstanceNetInfo.getIPType()))
                .map(DescribeDBInstanceNetInfoResponse.DBInstanceNetInfo::getIPAddress)
                .collect(Collectors.toList());

        if (privateIpAddrList.isEmpty()) {
            throw new PluginException("The resource doesn't have private ip");
        }

        return privateIpAddrList.get(0);

    }

    private void zoneIdAdaption(IAcsClient client, DBCloudParamDto dbCloudParamDto, CoreCreateDBInstanceRequestDto requestDto) throws PluginException, AliCloudException {

        final String availableZoneId;
        if (StringUtils.equalsIgnoreCase(RDSCategory.HighAvailability.toString(), requestDto.getCategory())) {
            // high availability category
            availableZoneId = queryAvailableZoneId(client, dbCloudParamDto);

            ZoneIdHelper.ifZoneInAvailableZoneId(requestDto.getZoneId(), availableZoneId);

        } else {
            // basic category
            final List<String> zoneIdList = PluginStringUtils.splitStringList(requestDto.getZoneId());
            if (zoneIdList.isEmpty()) {
                throw new PluginException("Invalid zoneId.");
            }

            availableZoneId = zoneIdList.get(0).trim();

            if (ZoneIdHelper.ifZoneIdContainsMAZ(availableZoneId)) {
                throw new PluginException("The given zoneId contains MAZ which is invalid.");
            }

        }

        requestDto.setZoneId(availableZoneId);

    }

    private String queryAvailableZoneId(IAcsClient client, DBCloudParamDto dbCloudParamDto) {
        DescribeRegionsRequest request = new DescribeRegionsRequest();
        final DescribeRegionsResponse response = acsClientStub.request(client, request, dbCloudParamDto.getRegionId());

        final Optional<String> zoneOpt = response.getRegions()
                .stream()
                .filter(rdsRegion -> StringUtils.containsIgnoreCase(rdsRegion.getZoneId(), dbCloudParamDto.getRegionGroup()))
                .map(DescribeRegionsResponse.RDSRegion::getZoneId)
                .filter(s -> StringUtils.containsIgnoreCase(s, dbCloudParamDto.getRegionGroup()))
                .findFirst();

        zoneOpt.orElseThrow(() -> new PluginException(String.format("Cannot find available zone by given region group: [%s]", dbCloudParamDto.getRegionGroup())));

        return zoneOpt.get();
    }

    private enum DBInstanceIPType {
        // inner
        Inner,
        // public
        Public,
        // private
        Private
    }
}
