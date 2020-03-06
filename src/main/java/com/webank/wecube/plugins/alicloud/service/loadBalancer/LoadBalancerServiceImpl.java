package com.webank.wecube.plugins.alicloud.service.loadBalancer;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.slb.model.v20140515.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreCreateLoadBalancerListenerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreCreateLoadBalancerListenerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreDeleteLoadBalancerListenerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreDeleteLoadBalancerListenerResponseDto;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
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
            final DeleteLoadBalancerResponse response = this.acsClientStub.request(client, requestDto);

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
    public List<CoreCreateLoadBalancerListenerResponseDto> createListener(List<CoreCreateLoadBalancerListenerRequestDto> coreCreateLoadBalancerListenerRequestDtoList) throws PluginException {
        List<CoreCreateLoadBalancerListenerResponseDto> resultList = new ArrayList<>();
        for (CoreCreateLoadBalancerListenerRequestDto requestDto : coreCreateLoadBalancerListenerRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String loadBalancerId = requestDto.getLoadBalancerId();
            final Integer listenerPort = requestDto.getListenerPort();
            final String listenerProtocol = requestDto.getListenerProtocol();
            final Integer backendServerPort = requestDto.getBackendServerPort();
            try {
                CoreCreateLoadBalancerListenerResponseDto result = this.retrieveListener(client, regionId, loadBalancerId, listenerPort, listenerProtocol);
                resultList.add(result);
            } catch (AliCloudException ex) {
                AcsRequest request;
                switch (EnumUtils.getEnum(listenerProtocolType.class, listenerProtocol.toUpperCase())) {
                    case TCP:
                        request = CoreCreateLoadBalancerListenerRequestDto.toSdkTCPRequest(requestDto);
                        break;
                    case UDP:
                        request = CoreCreateLoadBalancerListenerRequestDto.toSdkUDPRequest(requestDto);
                        break;
                    case HTTP:
                        request = CoreCreateLoadBalancerListenerRequestDto.toSdkHTTPRequest(requestDto);
                        break;
                    case HTTPS:
                        request = CoreCreateLoadBalancerListenerRequestDto.toSdkHTTPSRequest(requestDto);
                        break;
                    default:
                        throw new PluginException("Please specify valid listener protocol type.");
                }
                final AcsResponse response = this.acsClientStub.request(client, request);
                final CoreCreateLoadBalancerListenerResponseDto result = CoreCreateLoadBalancerListenerResponseDto.fromSdk(response);
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }

        }
        return resultList;
    }

    private CoreCreateLoadBalancerListenerResponseDto retrieveListener(IAcsClient client, String regionId, String loadBalancerId, Integer listenerPort, String listenerProtocol) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, loadBalancerId, String.valueOf(listenerPort), listenerProtocol)) {
            throw new PluginException("Either regionId, loadBalancerId, listenerPort or listenerProtocol cannot be null or empty while retrieving listener info.");
        }
        CoreCreateLoadBalancerListenerResponseDto result;
        switch (EnumUtils.getEnum(listenerProtocolType.class, listenerProtocol.toUpperCase())) {
            case TCP:
                DescribeLoadBalancerTCPListenerAttributeRequest tcpRequest = new DescribeLoadBalancerTCPListenerAttributeRequest();
                tcpRequest.setRegionId(regionId);
                tcpRequest.setListenerPort(listenerPort);
                tcpRequest.setLoadBalancerId(loadBalancerId);
                final DescribeLoadBalancerTCPListenerAttributeResponse tcpResponse = this.acsClientStub.request(client, tcpRequest);
                result = CoreCreateLoadBalancerListenerResponseDto.fromSdk(tcpResponse);
                break;
            case UDP:
                DescribeLoadBalancerUDPListenerAttributeRequest udpRequest = new DescribeLoadBalancerUDPListenerAttributeRequest();
                udpRequest.setRegionId(regionId);
                udpRequest.setListenerPort(listenerPort);
                udpRequest.setLoadBalancerId(loadBalancerId);
                final DescribeLoadBalancerUDPListenerAttributeResponse udpResponse = this.acsClientStub.request(client, udpRequest);
                result = CoreCreateLoadBalancerListenerResponseDto.fromSdk(udpResponse);
                break;
            case HTTP:
                DescribeLoadBalancerHTTPListenerAttributeRequest httpRequest = new DescribeLoadBalancerHTTPListenerAttributeRequest();
                httpRequest.setRegionId(regionId);
                httpRequest.setListenerPort(listenerPort);
                httpRequest.setLoadBalancerId(loadBalancerId);
                final DescribeLoadBalancerHTTPListenerAttributeResponse httpResponse = this.acsClientStub.request(client, httpRequest);
                result = CoreCreateLoadBalancerListenerResponseDto.fromSdk(httpResponse);
                break;
            case HTTPS:
                DescribeLoadBalancerHTTPSListenerAttributeRequest httpsRequest = new DescribeLoadBalancerHTTPSListenerAttributeRequest();
                httpsRequest.setRegionId(regionId);
                httpsRequest.setListenerPort(listenerPort);
                httpsRequest.setLoadBalancerId(loadBalancerId);
                final DescribeLoadBalancerHTTPSListenerAttributeResponse httpsResponse = this.acsClientStub.request(client, httpsRequest);
                result = CoreCreateLoadBalancerListenerResponseDto.fromSdk(httpsResponse);
                break;
            default:
                throw new PluginException("Please specify valid listener protocol type");
        }

        return result;

    }

    @Override
    public List<CoreDeleteLoadBalancerListenerResponseDto> deleteListener(List<CoreDeleteLoadBalancerListenerRequestDto> coreDeleteLoadBalancerListenerRequestDtoList) throws PluginException {

        List<CoreDeleteLoadBalancerListenerResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteLoadBalancerListenerRequestDto requestDto : coreDeleteLoadBalancerListenerRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String loadBalancerId = requestDto.getLoadBalancerId();
            final Integer listenerPort = requestDto.getListenerPort();
            final String listenerProtocol = requestDto.getListenerProtocol();

            try {
                this.retrieveListener(client, regionId, loadBalancerId, listenerPort, listenerProtocol);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }

            DeleteLoadBalancerListenerRequest request = CoreDeleteLoadBalancerListenerRequestDto.toSdk(requestDto);
            DeleteLoadBalancerListenerResponse response;
            try {
                response = this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }
            CoreDeleteLoadBalancerListenerResponseDto result = CoreDeleteLoadBalancerListenerResponseDto.fromSdk(response);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);

        }
        return resultList;
    }

    public enum listenerProtocolType {TCP, UDP, HTTP, HTTPS}
}
