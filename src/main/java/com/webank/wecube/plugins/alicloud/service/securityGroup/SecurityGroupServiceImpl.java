package com.webank.wecube.plugins.alicloud.service.securityGroup;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup.*;
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
public class SecurityGroupServiceImpl implements SecurityGroupService {
    private final static Logger logger = LoggerFactory.getLogger(SecurityGroupService.class);

    private AcsClientStub acsClientStub;

    @Autowired
    public SecurityGroupServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateSecurityGroupResponseDto> createSecurityGroup(List<CoreCreateSecurityGroupRequestDto> coreCreateSecurityGroupRequestDtoList) throws PluginException {
        List<CoreCreateSecurityGroupResponseDto> result = new ArrayList<>();
        for (CoreCreateSecurityGroupRequestDto requestDto : coreCreateSecurityGroupRequestDtoList) {

            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            final String securityGroupId = requestDto.getSecurityGroupId();
            if (StringUtils.isNotEmpty(securityGroupId)) {
                final DescribeSecurityGroupsResponse foundSecurityGroup = this.retrieveSecurityGroup(client, regionId, securityGroupId);
                result.add(new CoreCreateSecurityGroupResponseDto(foundSecurityGroup.getRequestId(), foundSecurityGroup.getSecurityGroups().get(0).getSecurityGroupId()));
            } else {

                CreateSecurityGroupRequest request = CoreCreateSecurityGroupRequestDto.toSdk(requestDto);
                request.setRegionId(regionId);
                CreateSecurityGroupResponse response;
                try {
                    response = this.acsClientStub.request(client, request);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                result.add(CoreCreateSecurityGroupResponseDto.fromSdk(response));
            }
        }
        return result;
    }

    @Override
    public void deleteSecurityGroup(List<CoreDeleteSecurityGroupRequestDto> coreDeleteSecurityGroupRequestDtoList) throws PluginException {
        for (CoreDeleteSecurityGroupRequestDto requestDto : coreDeleteSecurityGroupRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            if (StringUtils.isEmpty(regionId)) {
                String msg = "The message cannot be null or empty.";
                logger.error(msg);
                throw new PluginException(regionId);
            }

            DeleteSecurityGroupRequest request = CoreDeleteSecurityGroupRequestDto.toSdk(requestDto);
            request.setRegionId(regionId);
            try {
                this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }
        }
    }

    @Override
    public List<CoreAuthorizeSecurityGroupResponseDto> authorizeSecurityGroup(List<CoreAuthorizeSecurityGroupRequestDto> coreAuthorizeSecurityGroupRequestDtoList) throws PluginException {
        List<CoreAuthorizeSecurityGroupResponseDto> resultList = new ArrayList<>();
        for (CoreAuthorizeSecurityGroupRequestDto requestDto : coreAuthorizeSecurityGroupRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            CoreAuthorizeSecurityGroupResponseDto result;
            if (requestDto.isEgress()) {
                // egress authorization
                AuthorizeSecurityGroupEgressRequest egressRequest = PluginSdkBridge.toSdk(requestDto, AuthorizeSecurityGroupEgressRequest.class, true);
                AuthorizeSecurityGroupEgressResponse egressResponse;
                try {
                    egressResponse = this.acsClientStub.request(client, egressRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                result = PluginSdkBridge.fromSdk(egressResponse, CoreAuthorizeSecurityGroupResponseDto.class);
            } else {
                // not egress authorization
                AuthorizeSecurityGroupRequest request = PluginSdkBridge.toSdk(requestDto, AuthorizeSecurityGroupRequest.class);
                AuthorizeSecurityGroupResponse response;
                try {
                    response = this.acsClientStub.request(client, request);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                result = PluginSdkBridge.fromSdk(response, CoreAuthorizeSecurityGroupResponseDto.class);
            }
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);

        }
        return resultList;
    }

    @Override
    public List<CoreRevokeSecurityGroupResponseDto> revokeSecurityGroup(List<CoreRevokeSecurityGroupRequestDto> coreRevokeSecurityGroupRequestDtoList) throws PluginException {
        List<CoreRevokeSecurityGroupResponseDto> resultList = new ArrayList<>();
        for (CoreRevokeSecurityGroupRequestDto requestDto : coreRevokeSecurityGroupRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            CoreRevokeSecurityGroupResponseDto result;
            if (requestDto.isEgress()) {
                // egress authorization
                RevokeSecurityGroupEgressRequest egressRequest = PluginSdkBridge.toSdk(requestDto, RevokeSecurityGroupEgressRequest.class, true);
                RevokeSecurityGroupEgressResponse egressResponse;
                try {
                    egressResponse = this.acsClientStub.request(client, egressRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                result = PluginSdkBridge.fromSdk(egressResponse, CoreRevokeSecurityGroupResponseDto.class);
            } else {
                // not egress authorization
                RevokeSecurityGroupRequest request = PluginSdkBridge.toSdk(requestDto, RevokeSecurityGroupRequest.class);
                RevokeSecurityGroupResponse response;
                try {
                    response = this.acsClientStub.request(client, request);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                result = PluginSdkBridge.fromSdk(response, CoreRevokeSecurityGroupResponseDto.class);
            }
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    private DescribeSecurityGroupsResponse retrieveSecurityGroup(IAcsClient client, String regionId, String securityGroupId) throws PluginException {

        if (StringUtils.isAnyEmpty(regionId, securityGroupId)) {
            String msg = "Either regionId or securityGroupId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info("Retrieving security group info... The region ID is: [{}] and security group ID is: [{}]", regionId, securityGroupId);

        DescribeSecurityGroupsRequest request = new DescribeSecurityGroupsRequest();
        request.setRegionId(regionId);
        request.setSecurityGroupId(securityGroupId);
        final DescribeSecurityGroupsResponse response = this.acsClientStub.request(client, request);

        if (null == response || response.getSecurityGroups().isEmpty()) {
            String msg = "Cannot retrieve security group info from AliCloud server.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        return response;

    }
}
