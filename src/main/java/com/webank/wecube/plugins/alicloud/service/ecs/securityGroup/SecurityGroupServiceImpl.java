package com.webank.wecube.plugins.alicloud.service.ecs.securityGroup;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup.*;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
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

    private final AcsClientStub acsClientStub;
    private final DtoValidator dtoValidator;

    @Autowired
    public SecurityGroupServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
    }

    @Override
    public List<CoreCreateSecurityGroupResponseDto> createSecurityGroup(List<CoreCreateSecurityGroupRequestDto> coreCreateSecurityGroupRequestDtoList) throws PluginException {
        List<CoreCreateSecurityGroupResponseDto> resultList = new ArrayList<>();
        for (CoreCreateSecurityGroupRequestDto requestDto : coreCreateSecurityGroupRequestDtoList) {
            CoreCreateSecurityGroupResponseDto result = new CoreCreateSecurityGroupResponseDto();

            try {

                dtoValidator.validate(requestDto);

                logger.info("Creating security group: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                final String securityGroupId = requestDto.getSecurityGroupId();
                if (StringUtils.isNotEmpty(securityGroupId)) {
                    final DescribeSecurityGroupsResponse foundSecurityGroup = this.retrieveSecurityGroup(client, regionId, securityGroupId);
                    final DescribeSecurityGroupsResponse.SecurityGroup securityGroup = foundSecurityGroup.getSecurityGroups().get(0);
                    if (!foundSecurityGroup.getSecurityGroups().isEmpty()) {
                        result = result.fromSdkCrossLineage(securityGroup);
                        result.setRequestId(foundSecurityGroup.getRequestId());
                        continue;
                    }
                }

                CreateSecurityGroupRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                CreateSecurityGroupResponse response;
                response = this.acsClientStub.request(client, request);
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
    public List<CoreDeleteSecurityGroupResponseDto> deleteSecurityGroup(List<CoreDeleteSecurityGroupRequestDto> coreDeleteSecurityGroupRequestDtoList) throws PluginException {
        List<CoreDeleteSecurityGroupResponseDto> resultList = new ArrayList<>();

        for (CoreDeleteSecurityGroupRequestDto requestDto : coreDeleteSecurityGroupRequestDtoList) {
            CoreDeleteSecurityGroupResponseDto result = new CoreDeleteSecurityGroupResponseDto();

            try {

                dtoValidator.validate(requestDto);

                logger.info("Deleting security group: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                if (StringUtils.isEmpty(regionId)) {
                    String msg = "The message cannot be null or empty.";
                    logger.error(msg);
                    throw new PluginException(regionId);
                }

                DeleteSecurityGroupRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                final DeleteSecurityGroupResponse response = this.acsClientStub.request(client, request);
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
    public List<CoreAuthorizeSecurityGroupResponseDto> authorizeSecurityGroup(List<CoreAuthorizeSecurityGroupRequestDto> coreAuthorizeSecurityGroupRequestDtoList) throws PluginException {
        List<CoreAuthorizeSecurityGroupResponseDto> resultList = new ArrayList<>();
        for (CoreAuthorizeSecurityGroupRequestDto requestDto : coreAuthorizeSecurityGroupRequestDtoList) {
            CoreAuthorizeSecurityGroupResponseDto result = new CoreAuthorizeSecurityGroupResponseDto();
            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                if (Boolean.parseBoolean(requestDto.getIsEgress())) {
                    // egress authorization
                    AuthorizeSecurityGroupEgressRequest egressRequest = requestDto.toSdkCrossLineage(AuthorizeSecurityGroupEgressRequest.class);
                    egressRequest.setRegionId(regionId);
                    AuthorizeSecurityGroupEgressResponse egressResponse;
                    egressResponse = this.acsClientStub.request(client, egressRequest);
                    result = result.fromSdkCrossLineage(egressResponse);
                } else {
                    // not egress authorization
                    AuthorizeSecurityGroupRequest request = requestDto.toSdk();
                    request.setRegionId(regionId);
                    AuthorizeSecurityGroupResponse response;
                    response = this.acsClientStub.request(client, request);
                    result = result.fromSdk(response);
                }

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
    public List<CoreRevokeSecurityGroupResponseDto> revokeSecurityGroup(List<CoreRevokeSecurityGroupRequestDto> coreRevokeSecurityGroupRequestDtoList) throws PluginException {
        List<CoreRevokeSecurityGroupResponseDto> resultList = new ArrayList<>();
        for (CoreRevokeSecurityGroupRequestDto requestDto : coreRevokeSecurityGroupRequestDtoList) {

            CoreRevokeSecurityGroupResponseDto result = new CoreRevokeSecurityGroupResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                if (Boolean.parseBoolean(requestDto.getIsEgress())) {
                    // egress authorization
                    RevokeSecurityGroupEgressRequest egressRequest = PluginSdkBridge.toSdk(requestDto, RevokeSecurityGroupEgressRequest.class, true);
                    egressRequest.setRegionId(regionId);
                    RevokeSecurityGroupEgressResponse egressResponse;
                    egressResponse = this.acsClientStub.request(client, egressRequest);
                    result = result.fromSdkCrossLineage(egressResponse);
                } else {
                    // not egress authorization
                    RevokeSecurityGroupRequest request = PluginSdkBridge.toSdk(requestDto, RevokeSecurityGroupRequest.class);
                    request.setRegionId(regionId);
                    RevokeSecurityGroupResponse response;
                    response = this.acsClientStub.request(client, request);
                    result = result.fromSdk(response);
                }

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

    private DescribeSecurityGroupsResponse retrieveSecurityGroup(IAcsClient client, String regionId, String securityGroupId) throws PluginException, AliCloudException {

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
