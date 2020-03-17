package com.webank.wecube.plugins.alicloud.service.cen;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.cbn.model.v20170912.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.cen.*;
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
import java.util.stream.Collectors;

/**
 * @author howechen
 */
@Service
public class CenServiceImpl implements CenService {

    private static final Logger logger = LoggerFactory.getLogger(CenService.class);

    private AcsClientStub acsClientStub;

    @Autowired
    public CenServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateCenResponseDto> createCen(List<CoreCreateCenRequestDto> requestDtoList) {
        List<CoreCreateCenResponseDto> resultList = new ArrayList<>();
        for (CoreCreateCenRequestDto requestDto : requestDtoList) {
            CoreCreateCenResponseDto result = new CoreCreateCenResponseDto();
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);

            try {

                final String cenId = requestDto.getCenId();
                if (StringUtils.isNotEmpty(cenId)) {
                    final DescribeCensResponse.Cen foundCen = this.retrieveCen(client, cenId);
                    if (null != foundCen) {
                        result = PluginSdkBridge.fromSdk(foundCen, CoreCreateCenResponseDto.class);
                        continue;
                    }
                }

                logger.info("Creating Cen...");

                CreateCenRequest request = PluginSdkBridge.toSdk(requestDto, CreateCenRequest.class);
                CreateCenResponse response = this.acsClientStub.request(client, request);
                result = PluginSdkBridge.fromSdk(response, CoreCreateCenResponseDto.class);

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
    public List<CoreDeleteCenResponseDto> deleteCen(List<CoreDeleteCenRequestDto> requestDtoList) {
        List<CoreDeleteCenResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteCenRequestDto requestDto : requestDtoList) {
            CoreDeleteCenResponseDto result = new CoreDeleteCenResponseDto();

            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);

            try {

                final String cenId = requestDto.getCenId();
                if (StringUtils.isNotEmpty(cenId)) {
                    final DescribeCensResponse.Cen foundCen = this.retrieveCen(client, cenId);
                    if (null == foundCen) {
                        continue;
                    }
                }

                logger.info("Deleting Cen...");

                CreateCenRequest request = PluginSdkBridge.toSdk(requestDto, CreateCenRequest.class);
                CreateCenResponse response = this.acsClientStub.request(client, request);
                result = PluginSdkBridge.fromSdk(response, CoreDeleteCenResponseDto.class);

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
    public List<CoreAttachCenChildResponseDto> attachCenChild(List<CoreAttachCenChildRequestDto> requestDtoList) {
        List<CoreAttachCenChildResponseDto> resultList = new ArrayList<>();
        for (CoreAttachCenChildRequestDto requestDto : requestDtoList) {
            CoreAttachCenChildResponseDto result = new CoreAttachCenChildResponseDto();
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);

            try {

                logger.info("Attaching Cen child instance...");

                AttachCenChildInstanceRequest request = PluginSdkBridge.toSdk(requestDto, AttachCenChildInstanceRequest.class);
                AttachCenChildInstanceResponse response = this.acsClientStub.request(client, request);
                result = PluginSdkBridge.fromSdk(response, CoreAttachCenChildResponseDto.class);

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
    public List<CoreDetachCenChildResponseDto> detachCenChild(List<CoreDetachCenChildRequestDto> requestDtoList) {
        List<CoreDetachCenChildResponseDto> resultList = new ArrayList<>();
        for (CoreDetachCenChildRequestDto requestDto : requestDtoList) {
            CoreDetachCenChildResponseDto result = new CoreDetachCenChildResponseDto();
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);

            try {

                logger.info("Attaching Cen child instance...");

                DetachCenChildInstanceRequest request = PluginSdkBridge.toSdk(requestDto, DetachCenChildInstanceRequest.class);
                DetachCenChildInstanceResponse response = this.acsClientStub.request(client, request);
                result = PluginSdkBridge.fromSdk(response, CoreDetachCenChildResponseDto.class);

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


    private DescribeCensResponse.Cen retrieveCen(IAcsClient client, String cenId) throws AliCloudException, PluginException {

        logger.info("Retrieving Cen. CenId: [{}]", cenId);

        DescribeCensRequest request = new DescribeCensRequest();
        DescribeCensResponse response;
        response = this.acsClientStub.request(client, request);
        final List<DescribeCensResponse.Cen> foundCenList = response.getCens().stream().filter(cen -> cenId.equals(cen.getCenId())).collect(Collectors.toList());

        if (foundCenList.size() == 1) {
            return foundCenList.get(0);
        } else {
            return null;
        }
    }

}
