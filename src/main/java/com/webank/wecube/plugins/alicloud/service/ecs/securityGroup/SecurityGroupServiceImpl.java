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
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
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
public class SecurityGroupServiceImpl implements SecurityGroupService {
    @Override
    public List<CoreAuthorizeSecurityGroupResponseDto> authorizeSecurityGroup(List<CoreAuthorizeSecurityGroupRequestDto> coreAuthorizeSecurityGroupRequestDtoList) throws PluginException {
        List<CoreAuthorizeSecurityGroupResponseDto> resultList = new ArrayList<>();
        for (CoreAuthorizeSecurityGroupRequestDto requestDto : coreAuthorizeSecurityGroupRequestDtoList) {
            CoreAuthorizeSecurityGroupResponseDto result = new CoreAuthorizeSecurityGroupResponseDto();
            try {

                dtoValidator.validate(requestDto);

                logger.info("Authorizing security group with policy: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                // according to WeCMDB's data, one request will be sepereated to multiple sub-requests
                List<CoreAuthorizeSecurityGroupRequestDto> subRequestDtoList = mapToMultipleRequest(requestDto);

                List<CoreAuthorizeSecurityGroupRequestDto> succeededRequestList = new ArrayList<>();
                for (CoreAuthorizeSecurityGroupRequestDto subRequestDto : subRequestDtoList) {

                    logger.info("Authorizing security group sub-request: {}", subRequestDto.toString());
                    try {
                        result = authorizeSecurityGroup(regionId, client, subRequestDto);
                        succeededRequestList.add(subRequestDto);
                    } catch (AliCloudException ex) {
                        // once there is AliCloudException, roll back all succeeded request
                        for (CoreAuthorizeSecurityGroupRequestDto rollBackDto : succeededRequestList) {
                            revokeSecurityGroup(regionId, client, rollBackDto);
                        }
                        throw new PluginException(String.format("Error when authorizing security group, error msg: [%s]", ex.getMessage()));

                    }
                }
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
    public List<CoreRevokeSecurityGroupResponseDto> revokeSecurityGroup(List<CoreRevokeSecurityGroupRequestDto> coreRevokeSecurityGroupRequestDtoList) throws PluginException {
        List<CoreRevokeSecurityGroupResponseDto> resultList = new ArrayList<>();
        for (CoreRevokeSecurityGroupRequestDto requestDto : coreRevokeSecurityGroupRequestDtoList) {

            CoreRevokeSecurityGroupResponseDto result = new CoreRevokeSecurityGroupResponseDto();

            try {

                dtoValidator.validate(requestDto);

                logger.info("Revoking security group with policy: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                final ActionType actionType = EnumUtils.getEnumIgnoreCase(ActionType.class, requestDto.getActionType());

                if (null == actionType) {
                    throw new PluginException(String.format("Invalid action type: [%s]", actionType));
                }

                switch (EnumUtils.getEnumIgnoreCase(ActionType.class, requestDto.getActionType())) {
                    case EGRESS:
                        // egress authorization
                        RevokeSecurityGroupEgressRequest egressRequest = PluginSdkBridge.toSdk(requestDto, RevokeSecurityGroupEgressRequest.class, true);
                        egressRequest.setRegionId(regionId);
                        RevokeSecurityGroupEgressResponse egressResponse;
                        egressResponse = this.acsClientStub.request(client, egressRequest);
                        result = result.fromSdkCrossLineage(egressResponse);
                        break;
                    case INGRESS:
                        // ingress authorization
                        RevokeSecurityGroupRequest request = PluginSdkBridge.toSdk(requestDto, RevokeSecurityGroupRequest.class);
                        request.setRegionId(regionId);
                        RevokeSecurityGroupResponse response;
                        response = this.acsClientStub.request(client, request);
                        result = result.fromSdk(response);
                        break;
                    default:
                        break;
                }

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

    private CoreAuthorizeSecurityGroupResponseDto authorizeSecurityGroup(String regionId, IAcsClient client, CoreAuthorizeSecurityGroupRequestDto singleRequestDto) throws PluginException, AliCloudException {
        final ActionType actionType = EnumUtils.getEnumIgnoreCase(ActionType.class, singleRequestDto.getActionType());

        if (null == actionType) {
            throw new PluginException(String.format("Invalid action type: [%s]", actionType));
        }

        CoreAuthorizeSecurityGroupResponseDto result = new CoreAuthorizeSecurityGroupResponseDto();
        switch (actionType) {
            case EGRESS:
                // egress authorization
                AuthorizeSecurityGroupEgressRequest egressRequest = singleRequestDto.toSdkCrossLineage(AuthorizeSecurityGroupEgressRequest.class);
                egressRequest.setRegionId(regionId);
                AuthorizeSecurityGroupEgressResponse egressResponse;
                egressResponse = this.acsClientStub.request(client, egressRequest);
                result = result.fromSdkCrossLineage(egressResponse);
                break;
            case INGRESS:
                // ingress authorization
                AuthorizeSecurityGroupRequest request = singleRequestDto.toSdk();
                request.setRegionId(regionId);
                AuthorizeSecurityGroupResponse response;
                response = this.acsClientStub.request(client, request);
                result = result.fromSdk(response);
                break;
            default:
                break;
        }
        return result;
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

    private void revokeSecurityGroup(String regionId, IAcsClient client, CoreAuthorizeSecurityGroupRequestDto rollBackDto) throws AliCloudException {
        final ActionType actionType = EnumUtils.getEnumIgnoreCase(ActionType.class, rollBackDto.getActionType());

        if (null == actionType) {
            throw new PluginException(String.format("Invalid action type: [%s]", actionType));
        }

        switch (actionType) {
            case EGRESS:
                // egress authorization
                RevokeSecurityGroupEgressRequest egressRequest = rollBackDto.toSdkCrossLineage(RevokeSecurityGroupEgressRequest.class);
                egressRequest.setRegionId(regionId);
                this.acsClientStub.request(client, egressRequest);
                break;
            case INGRESS:
                // ingress authorization
                RevokeSecurityGroupRequest request = rollBackDto.toSdkCrossLineage(RevokeSecurityGroupRequest.class);
                request.setRegionId(regionId);
                this.acsClientStub.request(client, request);
                break;
            default:
                break;
        }
    }

    private List<CoreAuthorizeSecurityGroupRequestDto> mapToMultipleRequest(CoreAuthorizeSecurityGroupRequestDto requestDto) throws PluginException {
        List<CoreAuthorizeSecurityGroupRequestDto> result = new ArrayList<>();
        final String cidrIp = requestDto.getCidrIp();
        final String portRange = requestDto.getPortRange();
        final String ipProtocol = requestDto.getIpProtocol();
        // TODO: need to optimize
        if (PluginStringUtils.isListStr(cidrIp)) {
            // list cidr ip
            final List<String> splittedIpList = PluginStringUtils.splitStringList(cidrIp);
            if (PluginStringUtils.isListStr(portRange)) {
                // list port range
                final List<String> portList = PluginStringUtils.splitStringList(portRange);
                if (splittedIpList.size() != portList.size()) {
                    throw new PluginException("The cidrIp and portRange size doesn't match");
                }

                if (PluginStringUtils.isListStr(ipProtocol)) {
                    // list ipProtocol
                    final List<String> ipProtocolList = PluginStringUtils.splitStringList(ipProtocol);
                    if (splittedIpList.size() != ipProtocolList.size()) {
                        throw new PluginException("When cidrIp, portRange and ipProtocol fields are list string, the size of all three fields should be the same.");
                    }
                    for (int i = 0; i < splittedIpList.size(); i++) {
                        result.add(requestDto.forkSubRequest(splittedIpList.get(i), portList.get(i), ipProtocolList.get(i)));
                    }
                } else {
                    // single ipProtocol
                    for (int i = 0; i < splittedIpList.size(); i++) {
                        result.add(requestDto.forkSubRequest(splittedIpList.get(i), portList.get(i), ipProtocol));
                    }
                }
            } else {
                // single port range
                if (PluginStringUtils.isListStr(ipProtocol)) {
                    // list protocol
                    throw new PluginException("When cidrIp is list and portRange is single, protocol cannot be a list field");
                } else {
                    // single protocol
                    for (String ip : splittedIpList) {
                        result.add(requestDto.forkSubRequest(ip, portRange, ipProtocol));
                    }
                }
            }
        } else {
            // single cidr ip
            if (PluginStringUtils.isListStr(portRange)) {
                // list portRange
                throw new PluginException("When cidrIp is single field, portRange cannot be list.");
            } else {
                // single portRange
                if (PluginStringUtils.isListStr(ipProtocol)) {
                    // list protocol
                    throw new PluginException("When cidrIp and portRange field is single, ipProtocol field can only be single field");
                } else {
                    // single protocol
                    result.add(requestDto.forkSubRequest(cidrIp, portRange, ipProtocol));
                }
            }
        }
        return result;
    }


    public enum ActionType {
        // ingress
        INGRESS,
        // egress
        EGRESS
    }
}
