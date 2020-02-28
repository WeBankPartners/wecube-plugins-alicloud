package com.webank.wecube.plugins.alicloud.service.securityGroup;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.securityGroup.CoreCreateSecurityGroupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.securityGroup.CoreCreateSecurityGroupResponseDto;
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
public class SecurityGroupServiceImpl implements SecurityGroupService {
    private final static Logger logger = LoggerFactory.getLogger(SecurityGroupService.class);

    private AcsClientStub acsClientStub;

    @Autowired
    public SecurityGroupServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateSecurityGroupResponseDto> createSecurityGroup(List<CoreCreateSecurityGroupRequestDto> requestDtoList) throws PluginException {
        List<CoreCreateSecurityGroupResponseDto> result = new ArrayList<>();
        for (CoreCreateSecurityGroupRequestDto requestDto : requestDtoList) {
            final String securityGroupId = requestDto.getSecurityGroupId();
            if (StringUtils.isNotEmpty(securityGroupId)) {
                final DescribeSecurityGroupsResponse foundSecurityGroup = this.retrieveSecurityGroup(requestDto.getRegionId(), securityGroupId);
                result.add(new CoreCreateSecurityGroupResponseDto(foundSecurityGroup.getRequestId(), foundSecurityGroup.getSecurityGroups().get(0).getSecurityGroupId()));
                continue;
            }

            CreateSecurityGroupRequest request = new CoreCreateSecurityGroupRequestDto();
            request = CoreCreateSecurityGroupRequestDto.toSdk(requestDto);
            final IAcsClient client = this.acsClientStub.generateAcsClient(requestDto.getRegionId());
            CreateSecurityGroupResponse response;
            try {
                response = this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }
            result.add(CoreCreateSecurityGroupResponseDto.fromSdk(response));

        }
        return result;
    }

    @Override
    public void deleteSecurityGroup(List<DeleteSecurityGroupRequest> deleteSecurityGroupRequestList) throws PluginException {
        for (DeleteSecurityGroupRequest deleteSecurityGroupRequest : deleteSecurityGroupRequestList) {
            final String regionId = deleteSecurityGroupRequest.getRegionId();
            if (StringUtils.isEmpty(regionId)) {
                String msg = "The message cannot be null or empty.";
                logger.error(msg);
                throw new PluginException(regionId);
            }
            final IAcsClient client = this.acsClientStub.generateAcsClient(regionId);
            try {
                this.acsClientStub.request(client, deleteSecurityGroupRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }
        }
    }

    private DescribeSecurityGroupsResponse retrieveSecurityGroup(String regionId, String securityGroupId) throws PluginException {

        if (StringUtils.isAnyEmpty(regionId, securityGroupId)) {
            String msg = "Either regionId or securityGroupId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info("Retrieving security group info... The region ID is: [{}] and security group ID is: [{}]", regionId, securityGroupId);

        final IAcsClient client = this.acsClientStub.generateAcsClient(regionId);
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
