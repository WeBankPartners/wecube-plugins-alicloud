package com.webank.wecube.plugins.alicloud.service.loadBalancer;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.slb.model.v20140515.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.cloudParam.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer.*;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
import com.webank.wecube.plugins.alicloud.support.PluginSdkBridge;
import com.webank.wecube.plugins.alicloud.utils.PluginMapUtils;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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

    private static final Logger logger = LoggerFactory.getLogger(LoadBalancerService.class);

    private final AcsClientStub acsClientStub;
    private final DtoValidator dtoValidator;


    @Autowired
    public LoadBalancerServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
    }

    @Override
    public List<CoreCreateLoadBalancerResponseDto> createLoadBalancer(List<CoreCreateLoadBalancerRequestDto> coreCreateLoadBalancerRequestDtoList) {
        List<CoreCreateLoadBalancerResponseDto> resultList = new ArrayList<>();
        for (CoreCreateLoadBalancerRequestDto requestDto : coreCreateLoadBalancerRequestDtoList) {

            CoreCreateLoadBalancerResponseDto result = new CoreCreateLoadBalancerResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();

                // check region id
                if (StringUtils.isEmpty(regionId)) {
                    throw new PluginException("The region id cannot be NULL or empty.");
                }

                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String loadBalancerId = requestDto.getLoadBalancerId();

                logger.info("Creating load balancer: {}", requestDto.toString());


                if (StringUtils.isNotEmpty(loadBalancerId)) {
                    // if load balancer id is not empty, retrieve load balancer info
                    final DescribeLoadBalancersResponse foundLoadBalancerResponse = this.retrieveLoadBalancer(client, regionId, loadBalancerId);
                    if (foundLoadBalancerResponse.getLoadBalancers().size() == 1) {
                        final DescribeLoadBalancersResponse.LoadBalancer foundLoadBalancer = foundLoadBalancerResponse.getLoadBalancers().get(0);
                        result = result.fromSdkCrossLineage(foundLoadBalancer);
                        result.setRequestId(foundLoadBalancerResponse.getRequestId());
                        continue;
                    }
                }

                // if load balancer id is empty, create load balancer
                final CreateLoadBalancerRequest createLoadBalancerRequest = requestDto.toSdk();
                CreateLoadBalancerResponse response;
                response = this.acsClientStub.request(client, createLoadBalancerRequest, regionId);
                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }

        }
        return resultList;
    }

    private DescribeLoadBalancersResponse retrieveLoadBalancer(IAcsClient client, String regionId, String loadBalancerId) throws PluginException, AliCloudException {

        if (StringUtils.isAnyEmpty(regionId, loadBalancerId)) {
            String msg = "Either regionId or loadBalancerId cannot be empty or null";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info("Retriving load balancer info, regionId: [{}], loadBalancerId: [{}]", regionId, loadBalancerId);

        DescribeLoadBalancersRequest request = new DescribeLoadBalancersRequest();
        request.setLoadBalancerId(loadBalancerId);

        DescribeLoadBalancersResponse response;
        response = this.acsClientStub.request(client, request, regionId);
        return response;
    }

    @Override
    public List<CoreDeleteLoadBalancerResponseDto> deleteLoadBalancer(List<CoreDeleteLoadBalancerRequestDto> coreDeleteLoadBalancerRequestDtoList) {
        List<CoreDeleteLoadBalancerResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteLoadBalancerRequestDto requestDto : coreDeleteLoadBalancerRequestDtoList) {

            CoreDeleteLoadBalancerResponseDto result = new CoreDeleteLoadBalancerResponseDto();
            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String loadBalancerId = requestDto.getLoadBalancerId();

                logger.info("Creating load balancer: {}", requestDto.toString());

                final DescribeLoadBalancersResponse foundLoadBalancerInfo = this.retrieveLoadBalancer(client, regionId, loadBalancerId);

                // check if load balancer already deleted
                if (0 == foundLoadBalancerInfo.getTotalCount()) {
                    result.setRequestId(foundLoadBalancerInfo.getRequestId());
                    continue;
                }

                // delete VPC
                logger.info("Deleting load balancer, load balancer ID: [{}], regionID" +
                        ":[{}]", requestDto.getLoadBalancerId(), regionId);
                final DeleteLoadBalancerRequest deleteLoadBalancerRequest = requestDto.toSdk();
                final DeleteLoadBalancerResponse response = this.acsClientStub.request(client, deleteLoadBalancerRequest, regionId);

                // re-check if VPC has already been deleted
                if (0 != this.retrieveLoadBalancer(client, regionId, loadBalancerId).getTotalCount()) {
                    String msg = String.format("The VPC: [%s] from region: [%s] hasn't been deleted", loadBalancerId, regionId);
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
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
    public List<CoreAddBackendServerResponseDto> addBackendServer(List<CoreAddBackendServerRequestDto> coreAddBackendServerRequestDtoList) {
        List<CoreAddBackendServerResponseDto> resultList = new ArrayList<>();
        for (CoreAddBackendServerRequestDto requestDto : coreAddBackendServerRequestDtoList) {

            CoreAddBackendServerResponseDto result = new CoreAddBackendServerResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String loadBalancerId = requestDto.getLoadBalancerId();
                final String listenerProtocol = requestDto.getListenerProtocol();
                final Integer listenerPort;
                try {
                    listenerPort = Integer.parseInt(requestDto.getListenerPort());
                } catch (NumberFormatException ex) {
                    throw new PluginException(ex.getMessage());
                }

                logger.info("Adding backend server to load balancer: {}", requestDto.toString());

                final String backendServersString = getBackendServersString(requestDto.getHostIds(), requestDto.getHostPorts());
                requestDto.setBackendServers(backendServersString);

                if (!EnumUtils.isValidEnumIgnoreCase(ListenerProtocolType.class, listenerProtocol.toUpperCase())) {
                    throw new PluginException("The listenerProtocol is an invalid type.");
                }

                // check if there is a listener exists
                boolean ifListenerExists = checkIfListenerExists(client, regionId, listenerPort, loadBalancerId, listenerProtocol);

                if (ifListenerExists) {
                    // check if listener is already bind the VServerGroup
                    String listenerBondVServerGroupId = retrieveVServerGroupId(client, regionId, listenerPort, loadBalancerId, listenerProtocol);
                    if (StringUtils.isNotEmpty(listenerBondVServerGroupId)) {
                        // VServerGroup already bound with that listener

                        // add backendServer on that VServerGroup
                        logger.info("Adding backend server on existed VServerGroup");
                        final AddVServerGroupBackendServersResponse modifyResponse = this.addBackendServerOnVServerGroup(client, regionId, requestDto.getBackendServers(), listenerBondVServerGroupId);

                        result.setRequestId(modifyResponse.getRequestId());
                        result.setvServerGroupId(listenerBondVServerGroupId);
                        result.setBackendServers(PluginSdkBridge.fromSdkList(modifyResponse.getBackendServers(), CreateVServerGroupResponse.BackendServer.class));
                    } else {
                        // create VServerGroup with backendServer info
                        logger.info("Creating new VServerGroup...");
                        final CreateVServerGroupResponse createdVServerGroup = createVServerGroup(requestDto, client, regionId);

                        // bind VServerGroup to that listener
                        logger.info("Binding created VServerGroup to the existed listener...");
                        final String createdVServerGroupId = createdVServerGroup.getVServerGroupId();
                        this.bindVServerGroupToListener(client, regionId, requestDto, createdVServerGroupId);

                        result = result.fromSdk(createdVServerGroup);
                    }
                } else {
                    // listener doesn't exist

                    // create VServerGroup with backendServer info
                    logger.info("Creating new VServerGroup...");
                    final CreateVServerGroupResponse createdVServerGroup = createVServerGroup(requestDto, client, regionId);

                    // add the new listener with created VServerGroupId
                    logger.info("Creating new listener with just created new VServerGroup...");
                    this.createNewListener(client, regionId, requestDto, createdVServerGroup.getVServerGroupId());

                    result = result.fromSdk(createdVServerGroup);
                }

                // start the created listener
                this.startListener(client, listenerPort, loadBalancerId, regionId);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
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
    public List<CoreRemoveBackendServerResponseDto> removeBackendServer(List<CoreRemoveBackendServerRequestDto> coreRemoveBackendServerRequestDtoList) {
        List<CoreRemoveBackendServerResponseDto> resultList = new ArrayList<>();
        for (CoreRemoveBackendServerRequestDto requestDto : coreRemoveBackendServerRequestDtoList) {

            CoreRemoveBackendServerResponseDto result = new CoreRemoveBackendServerResponseDto();

            try {

                dtoValidator.validate(requestDto);
                logger.info("Removing backend server from load balancer: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String backendServersString = getBackendServersString(requestDto.getHostIds(), requestDto.getHostPorts());
                requestDto.setBackendServers(backendServersString);
                String vServerGroupId = requestDto.getvServerGroupId();
                Integer listenerPort;
                try {
                    listenerPort = Integer.parseInt(requestDto.getListenerPort());
                } catch (NumberFormatException ex) {
                    throw new PluginException(String.format("Cannot format [%s] to integer.", requestDto.getListenerPort()));
                }

                final ListenerProtocolType listenerProtocolType = EnumUtils.getEnumIgnoreCase(ListenerProtocolType.class, requestDto.getListenerProtocol());
                if (null == listenerProtocolType) {
                    throw new PluginException("Invalid listener protocol");
                }

                if (StringUtils.isEmpty(requestDto.getvServerGroupId())) {


                    final String listenerProtocol = requestDto.getListenerProtocol();
                    final String loadBalancerId = requestDto.getLoadBalancerId();

                    if (StringUtils.isAnyEmpty(listenerProtocol, loadBalancerId)) {
                        throw new PluginException("Either the listener protocol or loadBalancerId cannot be empty or null");
                    }

                    logger.info("Retrieving the vServerGroupId bound on listener port: [{}] with protocol: [{}] from load balancer ID: [{}]", listenerPort, listenerProtocol, loadBalancerId);

                    String foundVServerGroupId = this.retrieveVServerGroupId(client, regionId, listenerPort, loadBalancerId, listenerProtocol);

                    if (StringUtils.isEmpty(foundVServerGroupId)) {
                        throw new PluginException("Cannot find vServerGroup ID by the given info.");
                    }

                    vServerGroupId = foundVServerGroupId;
                }


                logger.info("The vServerGroupId found: [{}], removing backendServers from that vServerGroup", vServerGroupId);

                RemoveVServerGroupBackendServersRequest request = requestDto.toSdk();
                request.setVServerGroupId(vServerGroupId);
                request.setBackendServers(requestDto.getBackendServers());
                RemoveVServerGroupBackendServersResponse response;
                response = this.acsClientStub.request(client, request, regionId);

                result = result.fromSdk(response);

                // delete listener according to the request
                // if delete listener, delete the vServerGroup as well
                if (requestDto.ifDeleteListener()) {
                    deleteListener(requestDto, regionId, client, listenerPort);
                    deleteVSwitchGroup(client, regionId, requestDto.getvServerGroupId());
                }

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }
        }
        return resultList;
    }

    private void deleteVSwitchGroup(IAcsClient client, String regionId, String vServerGroupId) throws AliCloudException {
        DeleteVServerGroupRequest request = new DeleteVServerGroupRequest();
        request.setVServerGroupId(vServerGroupId);

        acsClientStub.request(client, request, regionId);
    }

    private void deleteListener(CoreRemoveBackendServerRequestDto requestDto, String regionId, IAcsClient client, Integer listenerPort) throws AliCloudException {
        DeleteLoadBalancerListenerRequest deleteLoadBalancerListenerRequest = new DeleteLoadBalancerListenerRequest();
        deleteLoadBalancerListenerRequest.setLoadBalancerId(requestDto.getLoadBalancerId());
        deleteLoadBalancerListenerRequest.setListenerPort(listenerPort);
        deleteLoadBalancerListenerRequest.setListenerProtocol(requestDto.getListenerProtocol());

        acsClientStub.request(client, deleteLoadBalancerListenerRequest, regionId);
    }

    private String getBackendServersString(String hostIds, String hostPorts) throws PluginException, AliCloudException {
        final String formattedHostIds = PluginStringUtils.handleCoreListStr(hostIds);
        final String formattedHostPorts = PluginStringUtils.handleCoreListStr(hostPorts);

        final List<BackendServerDto> backendServerDtos = fromRawStringList(formattedHostIds, formattedHostPorts);
        List<String> backendServerStringList = new ArrayList<>();
        for (BackendServerDto backendServerDto : backendServerDtos) {
            final String singleServerString = backendServerDto.toString();
            backendServerStringList.add(singleServerString);
        }
        return PluginStringUtils.stringifyObjectList(backendServerStringList);
    }

    private List<BackendServerDto> fromRawStringList(String hostIds, String hostPorts) throws PluginException, AliCloudException {
        final List<String> hostIdList = PluginStringUtils.splitStringList(hostIds);
        final List<String> hostPortList = PluginStringUtils.splitStringList(hostPorts);

        final List<Pair<String, String>> hostIdToPortPairList = PluginMapUtils.zipToPairList(hostIdList, hostPortList);
        List<BackendServerDto> resultList = new ArrayList<>();
        for (Pair<String, String> hostIdToPortPair : hostIdToPortPairList) {
            final String id = hostIdToPortPair.getKey();
            final String port = hostIdToPortPair.getValue();
            BackendServerDto backendServerDto = new BackendServerDto(id, port);
            resultList.add(backendServerDto);
        }
        return resultList;
    }


    private AddVServerGroupBackendServersResponse addBackendServerOnVServerGroup(IAcsClient client, String regionId, String backendServers, String vServerGroupId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, backendServers, vServerGroupId)) {
            throw new PluginException("The regionId, backendServers or vServerGroupId cannot be empty or null");
        }

        AddVServerGroupBackendServersRequest request = new AddVServerGroupBackendServersRequest();
        request.setBackendServers(backendServers);
        request.setVServerGroupId(vServerGroupId);

        AddVServerGroupBackendServersResponse response;
        response = this.acsClientStub.request(client, request, regionId);
        return response;

    }

    private String retrieveVServerGroupId(IAcsClient client, String regionId, Integer listenerPort, String loadBalancerId, String listenerProtocol) throws AliCloudException {
        String vServerGroupId = StringUtils.EMPTY;
        final ListenerProtocolType listenerProtocolType = EnumUtils.getEnumIgnoreCase(ListenerProtocolType.class, listenerProtocol);
        if (null == listenerProtocolType) {
            throw new PluginException("Invalid listener protocol");
        }
        switch (listenerProtocolType) {
            case HTTP:
                break;
            case UDP:
                break;
            case TCP:
                DescribeLoadBalancerTCPListenerAttributeRequest tcpListenerAttributeRequest = new DescribeLoadBalancerTCPListenerAttributeRequest();
                tcpListenerAttributeRequest.setLoadBalancerId(loadBalancerId);
                tcpListenerAttributeRequest.setListenerPort(listenerPort);
                DescribeLoadBalancerTCPListenerAttributeResponse describeLoadBalancerTCPListenerAttributeResponse;
                describeLoadBalancerTCPListenerAttributeResponse = this.acsClientStub.request(client, tcpListenerAttributeRequest, regionId);
                vServerGroupId = describeLoadBalancerTCPListenerAttributeResponse.getVServerGroupId();
                break;
            case HTTPS:
                break;
            default:
                break;
        }
        return vServerGroupId;
    }

    private CreateVServerGroupResponse createVServerGroup(CoreAddBackendServerRequestDto requestDto, IAcsClient client, String regionId) throws AliCloudException {
        CreateVServerGroupRequest createVServerGroupRequest = requestDto.toSdk();
        createVServerGroupRequest.setBackendServers(requestDto.getBackendServers());

        CreateVServerGroupResponse createVServerGroupResponse;
        createVServerGroupResponse = this.acsClientStub.request(client, createVServerGroupRequest, regionId);

        return createVServerGroupResponse;
    }

    private void createNewListener(IAcsClient client, String regionId, CoreAddBackendServerRequestDto requestDto, String vServerGroupId) throws PluginException, AliCloudException {
        AcsRequest<?> request = null;
        final ListenerProtocolType listenerProtocolType = EnumUtils.getEnumIgnoreCase(ListenerProtocolType.class, requestDto.getListenerProtocol());
        if (null == listenerProtocolType) {
            throw new PluginException("Invalid listener protocol");
        }
        try {
            switch (listenerProtocolType) {
                case HTTP:
                    request = PluginSdkBridge.toSdk(requestDto, CreateLoadBalancerHTTPListenerRequest.class);
                    break;
                case UDP:
                    request = PluginSdkBridge.toSdk(requestDto, CreateLoadBalancerUDPListenerRequest.class);
                    break;
                case TCP:
                    CreateLoadBalancerTCPListenerRequest createLoadBalancerTCPListenerRequest = new CreateLoadBalancerTCPListenerRequest();
                    createLoadBalancerTCPListenerRequest.setBandwidth(Integer.parseInt(requestDto.getBandwidth()));
                    createLoadBalancerTCPListenerRequest.setListenerPort(Integer.parseInt(requestDto.getListenerPort()));
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
        } catch (NumberFormatException ex) {
            throw new PluginException("Either bandwidth or listenerPort should be valid integer value.");
        }


        if (null == request) {
            throw new PluginException("Cannot create new listener, the request is null.");
        }


        this.acsClientStub.request(client, request, regionId);
    }

    private void bindVServerGroupToListener(IAcsClient client, String regionId, CoreAddBackendServerRequestDto requestDto, String vServerGroupId) throws PluginException, AliCloudException {
        AcsRequest<?> request = null;
        final ListenerProtocolType listenerProtocolType = EnumUtils.getEnumIgnoreCase(ListenerProtocolType.class, requestDto.getListenerProtocol());
        if (null == listenerProtocolType) {
            throw new PluginException("Invalid listener protocol");
        }
        try {
            switch (listenerProtocolType) {
                case HTTP:
                    SetLoadBalancerHTTPListenerAttributeRequest setLoadBalancerHTTPListenerAttributeRequest = new SetLoadBalancerHTTPListenerAttributeRequest();
                    setLoadBalancerHTTPListenerAttributeRequest.setListenerPort(Integer.parseInt(requestDto.getListenerPort()));
                    setLoadBalancerHTTPListenerAttributeRequest.setLoadBalancerId(requestDto.getLoadBalancerId());
                    setLoadBalancerHTTPListenerAttributeRequest.setVServerGroupId(vServerGroupId);
                    request = setLoadBalancerHTTPListenerAttributeRequest;
                    break;
                case UDP:
                    SetLoadBalancerUDPListenerAttributeRequest setLoadBalancerUDPListenerAttributeRequest = new SetLoadBalancerUDPListenerAttributeRequest();
                    setLoadBalancerUDPListenerAttributeRequest.setListenerPort(Integer.parseInt(requestDto.getListenerPort()));
                    setLoadBalancerUDPListenerAttributeRequest.setLoadBalancerId(requestDto.getLoadBalancerId());
                    setLoadBalancerUDPListenerAttributeRequest.setVServerGroupId(vServerGroupId);
                    request = setLoadBalancerUDPListenerAttributeRequest;
                    break;
                case TCP:
                    SetLoadBalancerTCPListenerAttributeRequest setLoadBalancerTCPListenerAttributeRequest = new SetLoadBalancerTCPListenerAttributeRequest();
                    setLoadBalancerTCPListenerAttributeRequest.setListenerPort(Integer.parseInt(requestDto.getListenerPort()));
                    setLoadBalancerTCPListenerAttributeRequest.setLoadBalancerId(requestDto.getLoadBalancerId());
                    setLoadBalancerTCPListenerAttributeRequest.setVServerGroupId(vServerGroupId);
                    request = setLoadBalancerTCPListenerAttributeRequest;
                    break;
                case HTTPS:
                    SetLoadBalancerHTTPSListenerAttributeRequest setLoadBalancerHTTPSListenerAttributeRequest = new SetLoadBalancerHTTPSListenerAttributeRequest();
                    setLoadBalancerHTTPSListenerAttributeRequest.setListenerPort(Integer.parseInt(requestDto.getListenerPort()));
                    setLoadBalancerHTTPSListenerAttributeRequest.setLoadBalancerId(requestDto.getLoadBalancerId());
                    setLoadBalancerHTTPSListenerAttributeRequest.setVServerGroupId(vServerGroupId);
                    request = setLoadBalancerHTTPSListenerAttributeRequest;
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException ex) {
            throw new PluginException("The listenerPort should be valid integer value.");
        }


        this.acsClientStub.request(client, request, regionId);
    }

    private boolean checkIfListenerExists(IAcsClient client, String regionId, Integer listenerPort, String loadBalancerId, String listenerProtocol) throws AliCloudException {
        boolean ifListenerExists = true;
        final ListenerProtocolType listenerProtocolType = EnumUtils.getEnumIgnoreCase(ListenerProtocolType.class, listenerProtocol);
        if (null == listenerProtocolType) {
            throw new PluginException("Invalid listener protocol");
        }
        try {
            switch (listenerProtocolType) {
                case HTTP:
                    DescribeLoadBalancerHTTPListenerAttributeRequest httpListenerAttributeRequest = new DescribeLoadBalancerHTTPListenerAttributeRequest();
                    httpListenerAttributeRequest.setLoadBalancerId(loadBalancerId);
                    httpListenerAttributeRequest.setListenerPort(listenerPort);
                    this.acsClientStub.request(client, httpListenerAttributeRequest, regionId);
                    break;
                case UDP:
                    DescribeLoadBalancerUDPListenerAttributeRequest udpListenerAttributeRequest = new DescribeLoadBalancerUDPListenerAttributeRequest();
                    udpListenerAttributeRequest.setLoadBalancerId(loadBalancerId);
                    udpListenerAttributeRequest.setListenerPort(listenerPort);
                    this.acsClientStub.request(client, udpListenerAttributeRequest, regionId);
                    break;
                case TCP:
                    DescribeLoadBalancerTCPListenerAttributeRequest tcpListenerAttributeRequest = new DescribeLoadBalancerTCPListenerAttributeRequest();
                    tcpListenerAttributeRequest.setLoadBalancerId(loadBalancerId);
                    tcpListenerAttributeRequest.setListenerPort(listenerPort);
                    this.acsClientStub.request(client, tcpListenerAttributeRequest, regionId);
                    break;
                case HTTPS:
                    DescribeLoadBalancerHTTPSListenerAttributeRequest httpsListenerAttributeRequest = new DescribeLoadBalancerHTTPSListenerAttributeRequest();
                    httpsListenerAttributeRequest.setLoadBalancerId(loadBalancerId);
                    httpsListenerAttributeRequest.setListenerPort(listenerPort);
                    this.acsClientStub.request(client, httpsListenerAttributeRequest, regionId);
                    break;
                default:
                    break;
            }
        } catch (AliCloudException ex) {
            if (ex.getMessage().contains("The specified resource does not exist.")) {
                ifListenerExists = false;
            } else {
                throw ex;
            }
        }

        return ifListenerExists;
    }


    private void startListener(IAcsClient client, Integer listenerPort, String loadBalancerId, String regionId) throws PluginException, AliCloudException {
        if (null == listenerPort) {
            throw new PluginException("The listener port cannot be empty.");
        }
        if (StringUtils.isAnyEmpty(loadBalancerId, regionId)) {
            throw new PluginException("Either loadBalancerId and regionId cannot be null or empty.");
        }

        logger.info("Starting load balancer listener, listener port: [{}], loadBalancerId: [{}], regionId: [{}]", listenerPort, loadBalancerId, regionId);

        StartLoadBalancerListenerRequest request = new StartLoadBalancerListenerRequest();
        request.setListenerPort(listenerPort);
        request.setLoadBalancerId(loadBalancerId);

        this.acsClientStub.request(client, request, regionId);
    }

    public enum ListenerProtocolType {
        // tcp
        TCP,
        // udp
        UDP,
        // http
        HTTP,
        // https
        HTTPS
    }
}
