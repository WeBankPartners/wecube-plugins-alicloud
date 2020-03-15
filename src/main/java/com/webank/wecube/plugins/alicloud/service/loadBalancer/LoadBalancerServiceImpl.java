package com.webank.wecube.plugins.alicloud.service.loadBalancer;

import com.aliyuncs.AcsRequest;
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
import com.webank.wecube.plugins.alicloud.support.PluginSdkBridge;
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
public class LoadBalancerServiceImpl implements LoadBalancerService {

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
    public List<CoreAddBackendServerResponseDto> addBackendServer(List<CoreAddBackendServerRequestDto> coreAddBackendServerRequestDtoList) throws PluginException {
        List<CoreAddBackendServerResponseDto> resultList = new ArrayList<>();
        for (CoreAddBackendServerRequestDto requestDto : coreAddBackendServerRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final Integer listenerPort = requestDto.getListenerPort();
            final String loadBalancerId = requestDto.getLoadBalancerId();
            final String listenerProtocol = requestDto.getListenerProtocol();

            if (!EnumUtils.isValidEnumIgnoreCase(listenerProtocolType.class, listenerProtocol.toUpperCase())) {
                throw new PluginException("The listenerProtocol is an invalid type.");
            }

            // check if there is a listener exists
            boolean ifListenerExists = checkIfListenerExists(client, regionId, listenerPort, loadBalancerId, listenerProtocol);

            CoreAddBackendServerResponseDto result = new CoreAddBackendServerResponseDto();
            if (ifListenerExists) {
                // check if listener is already bind the VServerGroup
                String listenerBondVServerGroupId = retrieveVServerGroupId(client, regionId, listenerPort, loadBalancerId, listenerProtocol);
                if (StringUtils.isNotEmpty(listenerBondVServerGroupId)) {
                    // VServerGroup already bound with that listener
                    // add backendServer on that VServerGroup
                    logger.info("Adding backend server on existed VServerGroup");
                    final AddVServerGroupBackendServersResponse modifyResponse = this.addBackendServerOnVServerGroup(client, regionId, requestDto.getBackendServers(), listenerBondVServerGroupId);

                    result.setRequestId(modifyResponse.getRequestId());
                    result.setVServerGroupId(listenerBondVServerGroupId);
                    result.setBackendServers(PluginSdkBridge.fromSdkList(modifyResponse.getBackendServers(), CoreAddBackendServerResponseDto.BackendServer.class));
                } else {
                    // create VServerGroup with backendServer info
                    logger.info("Creating new VServerGroup...");
                    final CreateVServerGroupResponse createdVServerGroup = createVServerGroup(requestDto, client);

                    // bind VServerGroup to that listener
                    logger.info("Binding created VServerGroup to the existed listener...");
                    final String createdVServerGroupId = createdVServerGroup.getVServerGroupId();
                    this.bindVServerGroupToListener(client, regionId, requestDto, createdVServerGroupId);

                    result = PluginSdkBridge.fromSdk(createdVServerGroup, CoreAddBackendServerResponseDto.class);
                }
            } else {
                // listener doesn't exist

                // create VServerGroup with backendServer info
                logger.info("Creating new VServerGroup...");
                final CreateVServerGroupResponse createdVServerGroup = createVServerGroup(requestDto, client);

                // add the new listener with created VServerGroupId
                logger.info("Creating new listener with just created new VServerGroup...");
                this.createNewListener(client, regionId, requestDto, createdVServerGroup.getVServerGroupId());

                result = PluginSdkBridge.fromSdk(createdVServerGroup, CoreAddBackendServerResponseDto.class);
            }

            // start the created listener
            this.startListener(client, listenerPort, loadBalancerId, regionId);

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
            final Integer listenerPort = requestDto.getListenerPort();
            final String listenerProtocol = requestDto.getListenerProtocol();
            final String loadBalancerId = requestDto.getLoadBalancerId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            CoreRemoveBackendServerResponseDto result = new CoreRemoveBackendServerResponseDto();

            if (null == listenerPort) {
                throw new PluginException("Listener port cannot be null.");
            }

            if (StringUtils.isAnyEmpty(listenerProtocol, loadBalancerId)) {
                throw new PluginException("Either the  listener protocol or loadBalancerId can not be empty or null");
            }

            logger.info("Retrieving the vServerGroupId bound on listener port: [{}] with protocol: [{}] from load balancer ID: [{}]", listenerPort, listenerProtocol, loadBalancerId);

            final String vServerGroupId = this.retrieveVServerGroupId(client, regionId, listenerPort, loadBalancerId, listenerProtocol);

            if (StringUtils.isEmpty(vServerGroupId)) {
                throw new PluginException("Cannot find vServerGroup ID by the given info.");
            }

            logger.info("The vServerGroupId found: [{}], removing backendServers from that vServerGroup", vServerGroupId);

            RemoveVServerGroupBackendServersRequest request = PluginSdkBridge.toSdk(requestDto, RemoveVServerGroupBackendServersRequest.class);
            request.setVServerGroupId(vServerGroupId);
            RemoveVServerGroupBackendServersResponse response;
            try {
                response = this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }

            result = PluginSdkBridge.fromSdk(response, CoreRemoveBackendServerResponseDto.class);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    private AddVServerGroupBackendServersResponse addBackendServerOnVServerGroup(IAcsClient client, String regionId, String backendServers, String vServerGroupId) {
        if (StringUtils.isAnyEmpty(regionId, backendServers, vServerGroupId)) {
            throw new PluginException("The regionId, backendServers or vServerGroupId cannot be empty or null");
        }

        AddVServerGroupBackendServersRequest request = new AddVServerGroupBackendServersRequest();
        request.setRegionId(regionId);
        request.setBackendServers(backendServers);
        request.setVServerGroupId(vServerGroupId);

        AddVServerGroupBackendServersResponse response;
        try {
            response = this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }
        return response;

    }

    private String retrieveVServerGroupId(IAcsClient client, String regionId, Integer listenerPort, String loadBalancerId, String listenerProtocol) {
        String vServerGroupId = StringUtils.EMPTY;
        switch (EnumUtils.getEnumIgnoreCase(listenerProtocolType.class, listenerProtocol)) {
            case HTTP:
                break;
            case UDP:
                break;
            case TCP:
                DescribeLoadBalancerTCPListenerAttributeRequest tcpListenerAttributeRequest = new DescribeLoadBalancerTCPListenerAttributeRequest();
                tcpListenerAttributeRequest.setLoadBalancerId(loadBalancerId);
                tcpListenerAttributeRequest.setListenerPort(listenerPort);
                tcpListenerAttributeRequest.setRegionId(regionId);
                DescribeLoadBalancerTCPListenerAttributeResponse describeLoadBalancerTCPListenerAttributeResponse;
                try {
                    describeLoadBalancerTCPListenerAttributeResponse = this.acsClientStub.request(client, tcpListenerAttributeRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                vServerGroupId = describeLoadBalancerTCPListenerAttributeResponse.getVServerGroupId();
                break;
            case HTTPS:
                break;
            default:
                break;
        }
        return vServerGroupId;
    }

    private CreateVServerGroupResponse createVServerGroup(CoreAddBackendServerRequestDto requestDto, IAcsClient client) {
        CreateVServerGroupRequest createVServerGroupRequest = PluginSdkBridge.toSdk(requestDto, CreateVServerGroupRequest.class);
        CreateVServerGroupResponse createVServerGroupResponse;
        try {
            createVServerGroupResponse = this.acsClientStub.request(client, createVServerGroupRequest);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }
        return createVServerGroupResponse;
    }

    private void createNewListener(IAcsClient client, String regionId, CoreAddBackendServerRequestDto requestDto, String vServerGroupId) throws PluginException {
        AcsRequest<?> request = null;
        switch (EnumUtils.getEnumIgnoreCase(listenerProtocolType.class, requestDto.getListenerProtocol())) {
            case HTTP:
                request = PluginSdkBridge.toSdk(requestDto, CreateLoadBalancerHTTPListenerRequest.class);
                break;
            case UDP:
                request = PluginSdkBridge.toSdk(requestDto, CreateLoadBalancerUDPListenerRequest.class);
                break;
            case TCP:
                CreateLoadBalancerTCPListenerRequest createLoadBalancerTCPListenerRequest = new CreateLoadBalancerTCPListenerRequest();
                createLoadBalancerTCPListenerRequest.setBandwidth(requestDto.getBandwidth());
                createLoadBalancerTCPListenerRequest.setListenerPort(requestDto.getListenerPort());
                createLoadBalancerTCPListenerRequest.setLoadBalancerId(requestDto.getLoadBalancerId());
                createLoadBalancerTCPListenerRequest.setVServerGroupId(vServerGroupId);
                request = createLoadBalancerTCPListenerRequest;
                break;
            case HTTPS:
                request = PluginSdkBridge.toSdk(requestDto, CreateLoadBalancerHTTPSListenerRequest.class);
                break;
            default:
                break;
        }

        if (null == request) {
            throw new PluginException("Cannot create new listener, the request is null.");
        }

        request.setRegionId(regionId);

        try {
            this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }

    }

    private void bindVServerGroupToListener(IAcsClient client, String regionId, CoreAddBackendServerRequestDto requestDto, String vServerGroupId) throws PluginException {
        AcsRequest<?> request = null;
        switch (EnumUtils.getEnumIgnoreCase(listenerProtocolType.class, requestDto.getListenerProtocol())) {
            case HTTP:
                request = PluginSdkBridge.toSdk(requestDto, CreateLoadBalancerHTTPListenerRequest.class);
                break;
            case UDP:
                request = PluginSdkBridge.toSdk(requestDto, CreateLoadBalancerUDPListenerRequest.class);
                break;
            case TCP:
                SetLoadBalancerTCPListenerAttributeRequest setLoadBalancerTCPListenerAttributeRequest = new SetLoadBalancerTCPListenerAttributeRequest();
                setLoadBalancerTCPListenerAttributeRequest.setListenerPort(requestDto.getListenerPort());
                setLoadBalancerTCPListenerAttributeRequest.setLoadBalancerId(requestDto.getLoadBalancerId());
                setLoadBalancerTCPListenerAttributeRequest.setVServerGroupId(vServerGroupId);
                request = setLoadBalancerTCPListenerAttributeRequest;
                break;
            case HTTPS:
                request = PluginSdkBridge.toSdk(requestDto, CreateLoadBalancerHTTPSListenerRequest.class);
                break;
            default:
                break;
        }

        if (null == request) {
            throw new PluginException("Cannot create new listener, the request is null.");
        }

        request.setRegionId(regionId);

        try {
            this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }
    }

    private boolean checkIfListenerExists(IAcsClient client, String regionId, Integer listenerPort, String loadBalancerId, String listenerProtocol) {
        boolean ifListenerExists = true;
        switch (EnumUtils.getEnumIgnoreCase(listenerProtocolType.class, listenerProtocol)) {
            case HTTP:
                break;
            case UDP:
                break;
            case TCP:
                DescribeLoadBalancerTCPListenerAttributeRequest tcpListenerAttributeRequest = new DescribeLoadBalancerTCPListenerAttributeRequest();
                tcpListenerAttributeRequest.setLoadBalancerId(loadBalancerId);
                tcpListenerAttributeRequest.setListenerPort(listenerPort);
                tcpListenerAttributeRequest.setRegionId(regionId);
                try {
                    this.acsClientStub.request(client, tcpListenerAttributeRequest);
                } catch (AliCloudException ex) {
                    if (ex.getMessage().contains("The specified resource does not exist.")) {
                        ifListenerExists = false;
                    } else {
                        throw new PluginException(ex.getMessage());
                    }
                }
                break;
            case HTTPS:
                break;
            default:
                break;
        }
        return ifListenerExists;
    }


    private void startListener(IAcsClient client, Integer listenerPort, String loadBalancerId, String regionId) throws PluginException {
        if (null == listenerPort) {
            throw new PluginException("The listener port cannot be empty.");
        }
        if (StringUtils.isAnyEmpty(loadBalancerId, regionId)) {
            throw new PluginException("Either loadBalancerId and regionId cannot be null or empty.");
        }

        logger.info("Starting load balancer listener, listener port: [{}], loadBalancerId: [{}], regionId: [{}]", listenerPort, loadBalancerId, regionId);

        StartLoadBalancerListenerRequest request = new StartLoadBalancerListenerRequest();
        request.setRegionId(regionId);
        request.setListenerPort(listenerPort);
        request.setLoadBalancerId(loadBalancerId);

        try {
            this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }
    }

    public enum listenerProtocolType {TCP, UDP, HTTP, HTTPS}
}
