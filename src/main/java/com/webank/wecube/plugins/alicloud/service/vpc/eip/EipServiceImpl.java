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
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimer;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimerTask;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author howechen
 */
@Service
public class EipServiceImpl implements EipService {

    private static final Logger logger = LoggerFactory.getLogger(EipService.class);

    private final AcsClientStub acsClientStub;
    private final DtoValidator dtoValidator;

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

                logger.info("Allocating EIP address: {}", requestDto.toString());

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


                AllocateEipAddressRequest allocateEipAddressRequest = requestDto.toSdk();
                AllocateEipAddressResponse createEipResponse = this.acsClientStub.request(client, allocateEipAddressRequest);

                // if cbpName is empty, create CBP
                String cbpId = StringUtils.EMPTY;
                if (!StringUtils.isEmpty(requestDto.getName())) {
                    DescribeCommonBandwidthPackagesRequest queryCBPRequest = new DescribeCommonBandwidthPackagesRequest();
                    queryCBPRequest.setRegionId(regionId);
                    queryCBPRequest.setName(requestDto.getName());
                    cbpId = queryCBP(client, queryCBPRequest);
                }

                if (StringUtils.isEmpty(cbpId)) {
                    final CreateCommonBandwidthPackageRequest createCommonBandwidthPackageRequest = requestDto.toSdkCrossLineage(CreateCommonBandwidthPackageRequest.class);
                    createCommonBandwidthPackageRequest.setRegionId(regionId);
                    createCommonBandwidthPackageRequest.setName(requestDto.getName());
                    cbpId = createCBP(client, createCommonBandwidthPackageRequest);
                }

                final String allocationId = createEipResponse.getAllocationId();
                addEipToCBP(client, cbpId, allocationId);

                result = result.fromSdk(createEipResponse);
                result.setCbpId(cbpId);
                result.setCbpName(requestDto.getName());

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
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
    public List<CoreReleaseEipResponseDto> releaseEipAddress(List<CoreReleaseEipRequestDto> requestDtoList) {
        List<CoreReleaseEipResponseDto> resultList = new ArrayList<>();
        for (CoreReleaseEipRequestDto requestDto : requestDtoList) {
            CoreReleaseEipResponseDto result = new CoreReleaseEipResponseDto();
            try {
                this.dtoValidator.validate(requestDto);

                logger.info("Releasing EIP address: {}", requestDto.toString());

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

                // remove eip from cbp
                String foundCBPId = StringUtils.EMPTY;
                if (!StringUtils.isEmpty(requestDto.getName())) {
                    DescribeCommonBandwidthPackagesRequest queryCBP = new DescribeCommonBandwidthPackagesRequest();
                    queryCBP.setRegionId(regionId);
                    queryCBP.setName(requestDto.getName());
                    foundCBPId = queryCBP(client, queryCBP);
                } else {
                    foundCBPId = queryCBPByEip(client, regionId, requestDto.getAllocationId());
                }

                if (!StringUtils.isEmpty(foundCBPId)) {
                    removeFromCBP(client, requestDto.getAllocationId(), regionId, foundCBPId);
                }

                // release eip
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
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }

        }
        return resultList;
    }

    private String queryCBPByEip(IAcsClient client, String regionId, String allocationId) throws PluginException, AliCloudException {
        DescribeEipAddressesRequest request = new DescribeEipAddressesRequest();
        request.setAllocationId(allocationId);
        request.setRegionId(regionId);

        final DescribeEipAddressesResponse response = acsClientStub.request(client, request);
        if (response.getEipAddresses().isEmpty()) {
            return StringUtils.EMPTY;
        } else {
            return response.getEipAddresses().get(0).getBandwidthPackageId();
        }

    }

    private void removeFromCBP(IAcsClient client, String ipInstanceId, String regionId, String foundCBPId) throws AliCloudException {
        RemoveCommonBandwidthPackageIpRequest removeCommonBandwidthPackageIpRequest = new RemoveCommonBandwidthPackageIpRequest();
        removeCommonBandwidthPackageIpRequest.setRegionId(regionId);
        removeCommonBandwidthPackageIpRequest.setBandwidthPackageId(foundCBPId);
        removeCommonBandwidthPackageIpRequest.setIpInstanceId(ipInstanceId);

        acsClientStub.request(client, removeCommonBandwidthPackageIpRequest);
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

                logger.info("Associating EIP address: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                AssociateEipAddressRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                AssociateEipAddressResponse response = this.acsClientStub.request(client, request);

                // wait till the eip is not in associating status
                Function<?, Boolean> func = o -> ifEipNotInStatus(client, regionId, requestDto.getAllocationId(), EipStatus.Associating);
                PluginTimer.runTask(new PluginTimerTask(func));

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
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
    public List<CoreUnAssociateEipResponseDto> unAssociateEipAddress(List<CoreUnAssociateEipRequestDto> requestDtoList) {
        List<CoreUnAssociateEipResponseDto> resultList = new ArrayList<>();
        for (CoreUnAssociateEipRequestDto requestDto : requestDtoList) {
            CoreUnAssociateEipResponseDto result = new CoreUnAssociateEipResponseDto();
            try {
                this.dtoValidator.validate(requestDto);

                logger.info("Un-associating EIP address: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                UnassociateEipAddressRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                UnassociateEipAddressResponse response = this.acsClientStub.request(client, request);

                // wait till the eip is not in un-associating status
                Function<?, Boolean> func = o -> ifEipNotInStatus(client, regionId, requestDto.getAllocationId(), EipStatus.Unassociating);
                PluginTimer.runTask(new PluginTimerTask(func));

                result = result.fromSdk(response);
            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
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

    @Override
    public void bindIpToInstance(IAcsClient client, String regionId, String instanceId, AssociatedInstanceType instanceType, String... ipAddress) throws PluginException, AliCloudException {
        for (String ip : ipAddress) {
            if (ifIpAddressBindInstance(client, regionId, ip, instanceId, instanceType)) {
                logger.info("The ip address: {} has already bound to that instance: {}", ipAddress, instanceId);
                continue;
            }

            logger.info("Ip address: {} hasn't bound to the instance: {}, will create an association.", ip, instanceId);

            final DescribeEipAddressesResponse.EipAddress eipAddress = queryEipByAddress(client, regionId, ip);
            final String allocationId = eipAddress.getAllocationId();
            AssociateEipAddressRequest request = new AssociateEipAddressRequest();
            request.setRegionId(regionId);
            request.setAllocationId(allocationId);
            request.setInstanceId(instanceId);
            request.setInstanceType(instanceType.toString());

            logger.info("Associating EIP: {} to the instance: {}", allocationId, instanceId);

            acsClientStub.request(client, request);

            // wait till the eip is not in associating status
            Function<?, Boolean> func = o -> ifEipNotInStatus(client, regionId, allocationId, EipStatus.Associating);
            PluginTimer.runTask(new PluginTimerTask(func));
        }

    }

    @Override
    public void unbindIpFromInstance(IAcsClient client, String regionId, String instanceId, AssociatedInstanceType instanceType, String... ipAddress) throws PluginException, AliCloudException {
        for (String ip : ipAddress) {
            if (!ifIpAddressBindInstance(client, regionId, ip, instanceId, instanceType)) {
                continue;
            }
            final DescribeEipAddressesResponse.EipAddress eipAddress = queryEipByAddress(client, regionId, ip);
            final String allocationId = eipAddress.getAllocationId();
            UnassociateEipAddressRequest request = new UnassociateEipAddressRequest();
            request.setRegionId(regionId);
            request.setAllocationId(allocationId);
            request.setInstanceId(instanceId);
            request.setInstanceType(instanceType.toString());

            logger.info("Unbind EIP: {} from isntance: {}", allocationId, instanceId);

            acsClientStub.request(client, request);

            // wait till the eip not in un-associating status
            Function<?, Boolean> func = o -> ifEipNotInStatus(client, regionId, allocationId, EipStatus.Unassociating);
            PluginTimer.runTask(new PluginTimerTask(func));
        }
    }

    private DescribeEipAddressesResponse retrieveEipAddress(IAcsClient client, DescribeEipAddressesRequest request) throws AliCloudException, PluginException {
        logger.info("Retrieving EIP address...");
        return this.acsClientStub.request(client, request);
    }

    private void addEipToCBP(IAcsClient client, String cbpId, String allocationId) throws AliCloudException {
        AddCommonBandwidthPackageIpRequest request = new AddCommonBandwidthPackageIpRequest();
        request.setBandwidthPackageId(cbpId);
        request.setIpInstanceId(allocationId);

        acsClientStub.request(client, request);
    }

    private String queryCBP(IAcsClient client, DescribeCommonBandwidthPackagesRequest queryCBPRequest) throws PluginException, AliCloudException {
        final DescribeCommonBandwidthPackagesResponse response = acsClientStub.request(client, queryCBPRequest);
        if (response.getCommonBandwidthPackages().isEmpty()) {
            return StringUtils.EMPTY;
        }

        return response.getCommonBandwidthPackages().get(0).getBandwidthPackageId();

    }

    private String createCBP(IAcsClient client, CreateCommonBandwidthPackageRequest createCommonBandwidthPackageRequest) throws AliCloudException {

        final CreateCommonBandwidthPackageResponse response = acsClientStub.request(client, createCommonBandwidthPackageRequest);
        return response.getBandwidthPackageId();

    }

    private DescribeEipAddressesResponse.EipAddress queryEipByAddress(IAcsClient client, String regionId, String ipAddress) throws PluginException, AliCloudException {
        if (StringUtils.isEmpty(ipAddress)) {
            throw new PluginException("ipAddress cannot be empty or null while query EIP");
        }

        DescribeEipAddressesRequest queryEipRequest = new DescribeEipAddressesRequest();
        queryEipRequest.setRegionId(regionId);
        queryEipRequest.setEipAddress(ipAddress);

        final DescribeEipAddressesResponse response = acsClientStub.request(client, queryEipRequest);
        if (response.getEipAddresses().isEmpty()) {
            throw new PluginException(String.format("Cannot find EIP by given ip address: [%s]", ipAddress));
        }

        return response.getEipAddresses().get(0);
    }

    private boolean ifIpAddressBindInstance(IAcsClient client, String regionId, String ipAddress, String instanceId, AssociatedInstanceType instanceType) throws PluginException, AliCloudException {
        logger.info("Retrieving if the ip address: {} bind the instance: {}", ipAddress, instanceId);
        final DescribeEipAddressesResponse.EipAddress eipAddress = queryEipByAddress(client, regionId, ipAddress);

        return StringUtils.equals(instanceId, eipAddress.getInstanceId()) && StringUtils.equals(instanceType.toString(), eipAddress.getInstanceType());
    }

    private boolean ifEipInStatus(IAcsClient client, String regionId, String allocationId, EipStatus status) {
        DescribeEipAddressesRequest request = new DescribeEipAddressesRequest();
        request.setAllocationId(allocationId);

        final DescribeEipAddressesResponse response = acsClientStub.request(client, request, regionId);

        if (response.getEipAddresses().isEmpty()) {
            throw new PluginException(String.format("Cannot find EIP by given allocation Id: [%s]", allocationId));
        }

        return StringUtils.equals(status.toString(), response.getEipAddresses().get(0).getStatus());
    }

    private boolean ifEipNotInStatus(IAcsClient client, String regionId, String allocationId, EipStatus status) {
        DescribeEipAddressesRequest request = new DescribeEipAddressesRequest();
        request.setAllocationId(allocationId);

        final DescribeEipAddressesResponse response = acsClientStub.request(client, request, regionId);

        if (response.getEipAddresses().isEmpty()) {
            throw new PluginException(String.format("Cannot find EIP by given allocation Id: [%s]", allocationId));
        }

        return !StringUtils.equals(status.toString(), response.getEipAddresses().get(0).getStatus());
    }


}
