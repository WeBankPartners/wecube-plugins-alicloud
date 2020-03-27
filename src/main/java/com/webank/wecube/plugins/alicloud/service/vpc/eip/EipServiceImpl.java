package com.webank.wecube.plugins.alicloud.service.vpc.eip;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.eip.*;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
import org.apache.commons.lang3.EnumUtils;
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
public class EipServiceImpl implements EipService {

    public enum AssociatedInstanceType {EcsInstance, SlbInstance, Nat, HaVip, NetworkInterface}

    private static final Logger logger = LoggerFactory.getLogger(EipService.class);

    private AcsClientStub acsClientStub;
    private DtoValidator dtoValidator;

    @Autowired
    public EipServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
    }

    @Override
    public List<CoreAllocateEipResponseDto> allocateEipAddress(List<CoreAllocateEipRequestDto> requestDtoList) {
        List<CoreAllocateEipResponseDto> resultList = new ArrayList<>();
        for (CoreAllocateEipRequestDto requestDto : requestDtoList) {
            CoreAllocateEipResponseDto result = new CoreAllocateEipResponseDto();
            try {
                this.dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                if (StringUtils.isNotEmpty(requestDto.getAllocationId())) {
                    DescribeEipAddressesRequest retrieveEipAddressRequest = new DescribeEipAddressesRequest();
                    retrieveEipAddressRequest.setAllocationId(requestDto.getAllocationId());
                    retrieveEipAddressRequest.setRegionId(regionId);
                    final DescribeEipAddressesResponse describeEipAddressesResponse = this.retrieveEipAddress(client, retrieveEipAddressRequest);
                    if (!describeEipAddressesResponse.getEipAddresses().isEmpty()) {
                        final DescribeEipAddressesResponse.EipAddress eipAddress = describeEipAddressesResponse.getEipAddresses().get(0);
                        result = result.fromSdkCrossLineage(eipAddress);
                        result.setRequestId(describeEipAddressesResponse.getRequestId());
                        continue;
                    }
                }

                logger.info("Allocating EIP address...");

                AllocateEipAddressRequest allocateEipAddressRequest = requestDto.toSdk();
                AllocateEipAddressResponse response = this.acsClientStub.request(client, allocateEipAddressRequest);
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
    public List<CoreReleaseEipResponseDto> releaseEipAddress(List<CoreReleaseEipRequestDto> requestDtoList) {
        List<CoreReleaseEipResponseDto> resultList = new ArrayList<>();
        for (CoreReleaseEipRequestDto requestDto : requestDtoList) {
            CoreReleaseEipResponseDto result = new CoreReleaseEipResponseDto();
            try {
                this.dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                DescribeEipAddressesRequest retrieveEipAddressRequest = new DescribeEipAddressesRequest();
                retrieveEipAddressRequest.setAllocationId(requestDto.getAllocationId());
                retrieveEipAddressRequest.setRegionId(regionId);
                final DescribeEipAddressesResponse describeEipAddressesResponse = this.retrieveEipAddress(client, retrieveEipAddressRequest);
                if (describeEipAddressesResponse.getEipAddresses().isEmpty()) {
                    result.setRequestId(describeEipAddressesResponse.getRequestId());
                    logger.info("The Eip address doesn't exist or has been released already.");
                    continue;
                }


                logger.info("Releasing EIP address...");

                ReleaseEipAddressRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                ReleaseEipAddressResponse response = this.acsClientStub.request(client, request);
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
    public void releaseEipAddress(IAcsClient client, String regionId, List<String> eipAllocationId) throws PluginException, AliCloudException {
        for (String allocationId : eipAllocationId) {

            logger.info("Releasing EIP address...");

            ReleaseEipAddressRequest request = new ReleaseEipAddressRequest();
            request.setRegionId(regionId);
            request.setAllocationId(allocationId);
            this.acsClientStub.request(client, request);
        }
    }

    @Override
    public void unAssociateEipAddress(IAcsClient client, String regionId, List<String> eipAllocationId, String instanceId, String instanceType) throws PluginException, AliCloudException {
        for (String allocationId : eipAllocationId) {

            logger.info("Un-associating EIP address with the instance...");

            UnassociateEipAddressRequest request = new UnassociateEipAddressRequest();
            request.setRegionId(regionId);
            request.setAllocationId(allocationId);
            request.setInstanceId(instanceId);
            request.setInstanceType(instanceType);
            this.acsClientStub.request(client, request);
        }
    }

    @Override
    public List<CoreAssociateEipResponseDto> associateEipAddress(List<CoreAssociateEipRequestDto> requestDtoList) {
        List<CoreAssociateEipResponseDto> resultList = new ArrayList<>();
        for (CoreAssociateEipRequestDto requestDto : requestDtoList) {
            CoreAssociateEipResponseDto result = new CoreAssociateEipResponseDto();
            try {
                this.dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                logger.info("Associating EIP address...");

                AssociateEipAddressRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                AssociateEipAddressResponse response = this.acsClientStub.request(client, request);
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
    public List<CoreUnAssociateEipResponseDto> unAssociateEipAddress(List<CoreUnAssociateEipRequestDto> requestDtoList) {
        List<CoreUnAssociateEipResponseDto> resultList = new ArrayList<>();
        for (CoreUnAssociateEipRequestDto requestDto : requestDtoList) {
            CoreUnAssociateEipResponseDto result = new CoreUnAssociateEipResponseDto();
            try {
                this.dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                logger.info("Un-associating EIP address...");

                UnassociateEipAddressRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                UnassociateEipAddressResponse response = this.acsClientStub.request(client, request);
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
    public boolean ifEipIsAvailable(IAcsClient client, String regionId, String associatedInstanceType, String associatedInstanceId) throws PluginException, AliCloudException {

        if (!EnumUtils.isValidEnumIgnoreCase(AssociatedInstanceType.class, associatedInstanceType)) {
            throw new PluginException("Invalid associatedInstanceType.");
        }

        if (StringUtils.isAnyEmpty(regionId, associatedInstanceId)) {
            throw new PluginException("Either regionId or associatedInstanceId cannot be null or empty.");
        }

        logger.info("Retrieving if the given resource have no Eip bound.");

        DescribeEipAddressesRequest request = new DescribeEipAddressesRequest();
        request.setRegionId(regionId);
        request.setAssociatedInstanceType(associatedInstanceType);
        request.setAssociatedInstanceId(associatedInstanceId);

        DescribeEipAddressesResponse response;
        response = this.acsClientStub.request(client, request);
        return response.getTotalCount().equals(0);
    }

    private DescribeEipAddressesResponse retrieveEipAddress(IAcsClient client, DescribeEipAddressesRequest request) throws AliCloudException, PluginException {
        logger.info("Retrieving EIP address...");
        DescribeEipAddressesResponse response = this.acsClientStub.request(client, request);
        return response;
    }
}
