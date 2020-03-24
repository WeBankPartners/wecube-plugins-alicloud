package com.webank.wecube.plugins.alicloud.service.disk;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.ecs.disk.*;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
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

    @Autowired
    public DiskServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateDiskResponseDto> createDisk(List<CoreCreateDiskRequestDto> coreCreateLoadBalancerRequestDtoList) throws PluginException {
        List<CoreCreateDiskResponseDto> resultList = new ArrayList<>();
        for (CoreCreateDiskRequestDto requestDto : coreCreateLoadBalancerRequestDtoList) {
            // check region id
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            if (StringUtils.isEmpty(regionId)) {
                throw new PluginException("The region id cannot be NULL or empty.");
            }
            requestDto.setRegionId(regionId);

            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String diskId = requestDto.getDiskId();
            CoreCreateDiskResponseDto result = new CoreCreateDiskResponseDto();

            if (StringUtils.isNotEmpty(diskId)) {
                // if disk id is not empty, retrieve disk info
                final DescribeDisksResponse retrieveDiskResponse = this.retrieveDisk(client, regionId, diskId);
                if (retrieveDiskResponse.getDisks().size() == 1) {
                    final DescribeDisksResponse.Disk foundDisk = retrieveDiskResponse.getDisks().get(0);
                    result = CoreCreateDiskResponseDto.fromSdk(foundDisk);
                }
            } else {
                // if disk id is empty, create disk
                final CreateDiskRequest createDiskRequest = CoreCreateDiskRequestDto.toSdk(requestDto);
                CreateDiskResponse response;
                try {
                    response = this.acsClientStub.request(client, createDiskRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                result = CoreCreateDiskResponseDto.fromSdk(response);
            }
            resultList.add(result);

        }
        return resultList;
    }

    @Override
    public List<CoreDeleteDiskResponseDto> deleteDisk(List<CoreDeleteDiskRequestDto> coreDeleteLoadBalancerRequestDtoList) throws PluginException {
        List<CoreDeleteDiskResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteDiskRequestDto requestDto : coreDeleteLoadBalancerRequestDtoList) {
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
            DeleteDiskRequest request = CoreDeleteDiskRequestDto.toSdk(requestDto);
            request.setRegionId(regionId);
            final DeleteDiskResponse response = this.acsClientStub.request(client, request);

            // re-check if disk has already been deleted
            if (0 != this.retrieveDisk(client, regionId, diskId).getTotalCount()) {
                String msg = String.format("The disk: [%s] from region: [%s] hasn't been deleted", diskId, regionId);
                logger.error(msg);
                throw new PluginException(msg);
            }

            CoreDeleteDiskResponseDto result = CoreDeleteDiskResponseDto.fromSdk(response);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());

            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<CoreAttachDiskResponseDto> attachDisk(List<CoreAttachDiskRequestDto> coreAttachDiskRequestDtoList) throws PluginException {
        List<CoreAttachDiskResponseDto> resultList = new ArrayList<>();
        for (CoreAttachDiskRequestDto requestDto : coreAttachDiskRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            final AttachDiskRequest request = CoreAttachDiskRequestDto.toSdk(requestDto);
            if (StringUtils.isAnyEmpty(request.getDiskId(), request.getInstanceId())) {
                throw new PluginException("Either disk ID or instance ID cannot be empty or null.");
            }

            AttachDiskResponse response;
            try {
                response = this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }
            CoreAttachDiskResponseDto result = CoreAttachDiskResponseDto.fromSdk(response);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public List<CoreDetachDiskResponseDto> detachDisk(List<CoreDetachDiskRequestDto> coreDetachDiskRequestDtoList) throws PluginException {
        List<CoreDetachDiskResponseDto> resultList = new ArrayList<>();
        for (CoreDetachDiskRequestDto requestDto : coreDetachDiskRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            final DetachDiskRequest request = CoreDetachDiskRequestDto.toSdk(requestDto);
            if (StringUtils.isAnyEmpty(request.getDiskId(), request.getInstanceId())) {
                throw new PluginException("Either disk ID or instance ID cannot be empty or null.");
            }

            DetachDiskResponse response = this.acsClientStub.request(client, request);
            CoreDetachDiskResponseDto result = CoreDetachDiskResponseDto.fromSdk(response);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public DescribeDisksResponse retrieveDisk(IAcsClient client, String regionId, String diskId) {
        logger.info("Retrieving disk info, the disk ID: [{}].", diskId);

        // create new request
        DescribeDisksRequest retrieveDiskRequest = new DescribeDisksRequest();
        retrieveDiskRequest.setRegionId(regionId);
        retrieveDiskRequest.setDiskIds(PluginStringUtils.stringifyList(diskId));
        // send the request and handle the error, then return the response
        return this.acsClientStub.request(client, retrieveDiskRequest);
    }
}
