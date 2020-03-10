package com.webank.wecube.plugins.alicloud.service.loadBalancer;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.slb.model.v20140515.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer.CoreAddBackendServerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer.CoreAddBackendServerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer.CoreRemoveBackendServerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer.CoreRemoveBackendServerResponseDto;
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
public class LoadBalancerServiceImpl implements LoadBalancerService {
    @Override
    public List<CoreAddBackendServerResponseDto> addBackendServer(List<CoreAddBackendServerRequestDto> coreAddBackendServerRequestDtoList) throws PluginException {
        List<CoreAddBackendServerResponseDto> resultList = new ArrayList<>();
        for (CoreAddBackendServerRequestDto requestDto : coreAddBackendServerRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final Integer listenerPort = requestDto.getListenerPort();
            final String loadBalancerId = requestDto.getLoadBalancerId();

            // check if there is VServerGroupId bound
            DescribeLoadBalancerTCPListenerAttributeRequest tcpListenerAttributeRequest = new DescribeLoadBalancerTCPListenerAttributeRequest();
            tcpListenerAttributeRequest.setLoadBalancerId(loadBalancerId);
            tcpListenerAttributeRequest.setListenerPort(listenerPort);
            tcpListenerAttributeRequest.setRegionId(regionId);
            DescribeLoadBalancerTCPListenerAttributeResponse tcpListenerAttributeResponse;
            try {
                tcpListenerAttributeResponse = this.acsClientStub.request(client, tcpListenerAttributeRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }

            final String foundVServerGroupId = tcpListenerAttributeResponse.getVServerGroupId();
            if (StringUtils.isNotEmpty(foundVServerGroupId)) {
                logger.info("Found VServerGroupId: [{}]", foundVServerGroupId);
                // there is VServerGroup bound in the request listener and port
                if (StringUtils.isNotEmpty(requestDto.getBackendServers())) {
                    // add the new backendServer to that particular VServerGroup
                    logger.info(String.format("Adding backend server to VServerGroup with ID: [%s]", foundVServerGroupId));
                    final AddBackendServersRequest addBackendServersRequest = CoreAddBackendServerRequestDto.toSdk(requestDto);
                    addBackendServersRequest.setRegionId(regionId);
                    AddBackendServersResponse response;
                    try {
                        response = this.acsClientStub.request(client, addBackendServersRequest);
                    } catch (AliCloudException ex) {
                        throw new PluginException(ex.getMessage());
                    }
                    CoreAddBackendServerResponseDto result = CoreAddBackendServerResponseDto.fromSdk(response);
                    result.setGuid(requestDto.getGuid());
                    result.setCallbackParameter(requestDto.getCallbackParameter());
                    resultList.add(result);

                } else {
                    // only want to retrieve the info of resource
                    // then retrieve the VServerGroup's all backendServers' into then return
                    logger.info(String.format("Retrieving VServerGroup ID: [%s]'s all backend servers info...", foundVServerGroupId));

                    DescribeVServerGroupAttributeRequest vServerGroupAttributeRequest = new DescribeVServerGroupAttributeRequest();
                    vServerGroupAttributeRequest.setRegionId(regionId);
                    vServerGroupAttributeRequest.setVServerGroupId(foundVServerGroupId);
                    DescribeVServerGroupAttributeResponse vServerGroupAttributeResponse;
                    try {
                        vServerGroupAttributeResponse = this.acsClientStub.request(client, vServerGroupAttributeRequest);
                    } catch (AliCloudException ex) {
                        throw new PluginException(ex.getMessage());
                    }
                    final List<DescribeVServerGroupAttributeResponse.BackendServer> foundBackendServerList = vServerGroupAttributeResponse.getBackendServers();

                    CoreAddBackendServerResponseDto result = new CoreAddBackendServerResponseDto();
                    result.setBackendServers(CoreAddBackendServerResponseDto.transferRetrieveBackendServerInfoFromSDK(foundBackendServerList));
                    result.setRequestId(vServerGroupAttributeResponse.getRequestId());
                    result.setGuid(requestDto.getGuid());
                    result.setCallbackParameter(requestDto.getCallbackParameter());
                    resultList.add(result);
                }

            } else {
                logger.info("No VServerGroup bound, need to add new VServerGroup then add the backend server.");
                // no VServerGroup bound, need to create new VServerGroup and bind a new backend server onto this VServerGroup
                // create new VServerGroup
                final String backendServerInfoString = requestDto.getBackendServers();
                CreateVServerGroupRequest createVServerGroupRequest = new CreateVServerGroupRequest();
                createVServerGroupRequest.setRegionId(regionId);
                createVServerGroupRequest.setLoadBalancerId(loadBalancerId);
                createVServerGroupRequest.setBackendServers(backendServerInfoString);

                CreateVServerGroupResponse createVServerGroupResponse;
                try {
                    createVServerGroupResponse = this.acsClientStub.request(client, createVServerGroupRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }

                final String createdVServerGroupId = createVServerGroupResponse.getVServerGroupId();

                logger.info("VServerGroup: [{}] created successfully.", createdVServerGroupId);

                // create new TCP listener, add that VServerGroupId on that listener
                CreateLoadBalancerTCPListenerRequest createLoadBalancerTCPListenerRequest = new CreateLoadBalancerTCPListenerRequest();
                createLoadBalancerTCPListenerRequest.setRegionId(regionId);
                createLoadBalancerTCPListenerRequest.setListenerPort(listenerPort);
                createLoadBalancerTCPListenerRequest.setLoadBalancerId(loadBalancerId);
                createLoadBalancerTCPListenerRequest.setVServerGroupId(createdVServerGroupId);

                try {
                    this.acsClientStub.request(client, createLoadBalancerTCPListenerRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }

                logger.info("The listener bound with ServerGroupId: [{}] created successfully.", createdVServerGroupId);

                CoreAddBackendServerResponseDto result = new CoreAddBackendServerResponseDto();
                result.setBackendServers(CoreAddBackendServerResponseDto.transferCreatedBackendServerInfoFromSDK(createVServerGroupResponse.getBackendServers()));
                result.setRequestId(createVServerGroupResponse.getRequestId());
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }
        }
        return resultList;
    }

    private static Logger logger = LoggerFactory.getLogger(LoadBalancerService.class);

    private AcsClientStub acsClientStub;

    @Autowired
    public LoadBalancerServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateLoadBalancerResponseDto> createLoadBalancer(List<CoreCreateLoadBalancerRequestDto> coreCreateLoadBalancerRequestDtoList) throws PluginException {
        List<CoreCreateLoadBalancerResponseDto> resultList = new ArrayList<>();
        for (CoreCreateLoadBalancerRequestDto requestDto : coreCreateLoadBalancerRequestDtoList) {
            // check region id
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();

            if (StringUtils.isEmpty(regionId)) {
                throw new PluginException("The region id cannot be NULL or empty.");
            }
            requestDto.setRegionId(regionId);

            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String loadBalancerId = requestDto.getLoadBalancerId();
            CoreCreateLoadBalancerResponseDto result = new CoreCreateLoadBalancerResponseDto();


            if (StringUtils.isNotEmpty(loadBalancerId)) {
                // if load balancer id is not empty, retrieve load balancer info
                final DescribeLoadBalancersResponse foundLoadBalancerResponse = this.retrieveLoadBalancer(client, regionId, loadBalancerId);
                if (foundLoadBalancerResponse.getLoadBalancers().size() == 1) {
                    final DescribeLoadBalancersResponse.LoadBalancer foundLoadBalancer = foundLoadBalancerResponse.getLoadBalancers().get(0);
                    result = CoreCreateLoadBalancerResponseDto.fromSdk(foundLoadBalancer);
                }
            } else {
                // if load balancer id is empty, create load balancer
                final CreateLoadBalancerRequest createLoadBalancerRequest = CoreCreateLoadBalancerRequestDto.toSdk(requestDto);
                CreateLoadBalancerResponse response;
                try {
                    response = this.acsClientStub.request(client, createLoadBalancerRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                result = CoreCreateLoadBalancerResponseDto.fromSdk(response);
            }
            resultList.add(result);

        }
        return resultList;
    }

    private DescribeLoadBalancersResponse retrieveLoadBalancer(IAcsClient client, String regionId, String loadBalancerId) throws PluginException {

        if (StringUtils.isAnyEmpty(regionId, loadBalancerId)) {
            String msg = "Either regionId or loadBalancerId cannot be empty or null";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info("Retriving load balancer info, regionId: [{}], loadBalancerId: [{}]", regionId, loadBalancerId);

        DescribeLoadBalancersRequest request = new DescribeLoadBalancersRequest();
        request.setRegionId(regionId);
        request.setLoadBalancerId(loadBalancerId);

        DescribeLoadBalancersResponse response;
        try {
            response = this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }

        return response;
    }

    @Override
    public List<CoreDeleteLoadBalancerResponseDto> deleteLoadBalancer(List<CoreDeleteLoadBalancerRequestDto> coreDeleteLoadBalancerRequestDtoList) throws PluginException {
        List<CoreDeleteLoadBalancerResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteLoadBalancerRequestDto requestDto : coreDeleteLoadBalancerRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);


            final String loadBalancerId = requestDto.getLoadBalancerId();
            if (StringUtils.isEmpty(loadBalancerId)) {
                throw new PluginException("The load balancer id cannot be empty or null.");
            }

            final DescribeLoadBalancersResponse foundLoadBalancerInfo = this.retrieveLoadBalancer(client, regionId, loadBalancerId);

            // check if load balancer already deleted
            if (0 == foundLoadBalancerInfo.getTotalCount()) {
                continue;
            }

            // delete VPC
            logger.info("Deleting load balancer, load balancer ID: [{}], regionID" +
                    ":[{}]", requestDto.getLoadBalancerId(), regionId);
            final DeleteLoadBalancerRequest deleteLoadBalancerRequest = CoreDeleteLoadBalancerRequestDto.toSdk(requestDto);
            final DeleteLoadBalancerResponse response = this.acsClientStub.request(client, deleteLoadBalancerRequest);

            // re-check if VPC has already been deleted
            if (0 != this.retrieveLoadBalancer(client, regionId, loadBalancerId).getTotalCount()) {
                String msg = String.format("The VPC: [%s] from region: [%s] hasn't been deleted", loadBalancerId, regionId);
                logger.error(msg);
                throw new PluginException(msg);
            }

            final CoreDeleteLoadBalancerResponseDto result = CoreDeleteLoadBalancerResponseDto.fromSdk(response);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<CoreRemoveBackendServerResponseDto> removeBackendServer(List<CoreRemoveBackendServerRequestDto> coreRemoveBackendServerRequestDtoList) throws PluginException {
        List<CoreRemoveBackendServerResponseDto> resultList = new ArrayList<>();
        for (CoreRemoveBackendServerRequestDto requestDto : coreRemoveBackendServerRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            RemoveBackendServersRequest request = CoreRemoveBackendServerRequestDto.toSdk(requestDto);
            RemoveBackendServersResponse response;
            try {
                response = this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }

            CoreRemoveBackendServerResponseDto result = CoreRemoveBackendServerRequestDto.fromSdk(response);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    public enum listenerProtocolType {TCP, UDP, HTTP, HTTPS}
}
