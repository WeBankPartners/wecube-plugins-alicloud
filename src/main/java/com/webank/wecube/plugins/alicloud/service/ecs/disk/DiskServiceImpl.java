package com.webank.wecube.plugins.alicloud.service.ecs.disk;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.ecs.disk.*;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
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
public class DiskServiceImpl implements DiskService {

    private static Logger logger = LoggerFactory.getLogger(DiskService.class);

    private AcsClientStub acsClientStub;
    private DtoValidator dtoValidator;

    @Autowired
    public DiskServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
    }

    @Override
    public List<CoreCreateDiskResponseDto> createDisk(List<CoreCreateDiskRequestDto> coreCreateLoadBalancerRequestDtoList) {
        List<CoreCreateDiskResponseDto> resultList = new ArrayList<>();
        for (CoreCreateDiskRequestDto requestDto : coreCreateLoadBalancerRequestDtoList) {

            CoreCreateDiskResponseDto result = new CoreCreateDiskResponseDto();
            try {

                dtoValidator.validate(requestDto);

                // check region id
                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                if (StringUtils.isEmpty(regionId)) {
                    throw new PluginException("The region id cannot be NULL or empty.");
                }

                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String diskId = requestDto.getDiskId();

                if (StringUtils.isNotEmpty(diskId)) {
                    // if disk id is not empty, retrieve disk info
                    final DescribeDisksResponse retrieveDiskResponse = this.retrieveDisk(client, regionId, diskId);
                    if (retrieveDiskResponse.getDisks().size() == 1) {
                        final DescribeDisksResponse.Disk foundDisk = retrieveDiskResponse.getDisks().get(0);
                        result.fromSdkCrossLineage(foundDisk);
                        result.setRequestId(retrieveDiskResponse.getRequestId());
                        continue;
                    }
                }


                // if disk id is empty, create disk
                final CreateDiskRequest createDiskRequest = requestDto.toSdk();
                createDiskRequest.setRegionId(regionId);
                CreateDiskResponse response;
                response = this.acsClientStub.request(client, createDiskRequest);
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
    public List<CoreDeleteDiskResponseDto> deleteDisk(List<CoreDeleteDiskRequestDto> coreDeleteLoadBalancerRequestDtoList) {
        List<CoreDeleteDiskResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteDiskRequestDto requestDto : coreDeleteLoadBalancerRequestDtoList) {

            CoreDeleteDiskResponseDto result = new CoreDeleteDiskResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                if (StringUtils.isEmpty(requestDto.getDiskId())) {
                    throw new PluginException("The disk id cannot be empty or null.");
                }

                final String diskId = requestDto.getDiskId();
                final DescribeDisksResponse foundedVpcInfo = this.retrieveDisk(client, regionId, diskId);

                // check if disk already deleted
                if (0 == foundedVpcInfo.getTotalCount()) {
                    continue;
                }


                // delete disk
                logger.info("Deleting disk, disk ID: [{}], disk region:[{}]", diskId, regionId);
                DeleteDiskRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                final DeleteDiskResponse response = this.acsClientStub.request(client, request);

                // re-check if disk has already been deleted
                if (0 != this.retrieveDisk(client, regionId, diskId).getTotalCount()) {
                    String msg = String.format("The disk: [%s] from region: [%s] hasn't been deleted", diskId, regionId);
                    logger.error(msg);
                    throw new PluginException(msg);
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
    public List<CoreAttachDiskResponseDto> attachDisk(List<CoreAttachDiskRequestDto> coreAttachDiskRequestDtoList) {
        List<CoreAttachDiskResponseDto> resultList = new ArrayList<>();
        for (CoreAttachDiskRequestDto requestDto : coreAttachDiskRequestDtoList) {
            CoreAttachDiskResponseDto result = new CoreAttachDiskResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                final AttachDiskRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                if (StringUtils.isAnyEmpty(request.getDiskId(), request.getInstanceId())) {
                    throw new PluginException("Either disk ID or instance ID cannot be empty or null.");
                }

                AttachDiskResponse response;
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
    public List<CoreDetachDiskResponseDto> detachDisk(List<CoreDetachDiskRequestDto> coreDetachDiskRequestDtoList) {
        List<CoreDetachDiskResponseDto> resultList = new ArrayList<>();
        for (CoreDetachDiskRequestDto requestDto : coreDetachDiskRequestDtoList) {

            CoreDetachDiskResponseDto result = new CoreDetachDiskResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                final DetachDiskRequest request = requestDto.toSdk();
                request.setRegionId(regionId);

                if (StringUtils.isAnyEmpty(request.getDiskId(), request.getInstanceId())) {
                    throw new PluginException("Either disk ID or instance ID cannot be empty or null.");
                }

                DetachDiskResponse response = this.acsClientStub.request(client, request);
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
    public DescribeDisksResponse retrieveDisk(IAcsClient client, String regionId, String diskId) throws PluginException, AliCloudException {

        if (StringUtils.isAnyEmpty(regionId, diskId)) {
            throw new PluginException("Either regionId or diskId cannot be null or empty.");
        }

        logger.info("Retrieving disk info, the disk ID: [{}].", diskId);

        // create new request
        DescribeDisksRequest retrieveDiskRequest = new DescribeDisksRequest();
        retrieveDiskRequest.setRegionId(regionId);
        retrieveDiskRequest.setDiskIds(PluginStringUtils.stringifyList(diskId));
        // send the request and handle the error, then return the response
        return this.acsClientStub.request(client, retrieveDiskRequest);
    }
}
