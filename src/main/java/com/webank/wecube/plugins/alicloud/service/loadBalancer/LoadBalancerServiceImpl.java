package com.webank.wecube.plugins.alicloud.service.loadBalancer;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerRequest;
import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersRequest;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerRequestDto;
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
    public void deleteLoadBalancer(List<CoreDeleteLoadBalancerRequestDto> coreDeleteLoadBalancerRequestDtoList) throws PluginException {
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
            this.acsClientStub.request(client, requestDto);

            // re-check if VPC has already been deleted
            if (0 != this.retrieveLoadBalancer(client, regionId, loadBalancerId).getTotalCount()) {
                String msg = String.format("The VPC: [%s] from region: [%s] hasn't been deleted", loadBalancerId, regionId);
                logger.error(msg);
                throw new PluginException(msg);
            }
        }
    }
}
