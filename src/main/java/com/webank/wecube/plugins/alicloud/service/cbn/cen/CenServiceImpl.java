package com.webank.wecube.plugins.alicloud.service.cbn.cen;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.cbn.model.v20170912.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.cbn.cen.*;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
import com.webank.wecube.plugins.alicloud.support.PluginSdkBridge;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimer;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimerTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
@Service
public class CenServiceImpl implements CenService {

    private static final Logger logger = LoggerFactory.getLogger(CenService.class);

    private final AcsClientStub acsClientStub;
    private final DtoValidator validator;

    @Autowired
    public CenServiceImpl(AcsClientStub acsClientStub, DtoValidator validator) {
        this.acsClientStub = acsClientStub;
        this.validator = validator;
    }

    @Override
    public List<CoreCreateCenResponseDto> createCen(List<CoreCreateCenRequestDto> requestDtoList) {
        List<CoreCreateCenResponseDto> resultList = new ArrayList<>();
        for (CoreCreateCenRequestDto requestDto : requestDtoList) {
            CoreCreateCenResponseDto result = new CoreCreateCenResponseDto();
            try {

                validator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                final String cenId = requestDto.getCenId();
                if (StringUtils.isNotEmpty(cenId)) {
                    final DescribeCensResponse describeCensResponse = this.retrieveCen(client);
                    final List<DescribeCensResponse.Cen> foundCenList = describeCensResponse.getCens().stream().filter(cen -> cenId.equals(cen.getCenId())).collect(Collectors.toList());

                    if (foundCenList.size() == 1) {
                        final DescribeCensResponse.Cen cen = foundCenList.get(0);
                        result = result.fromSdkCrossLineage(cen);
                        result.setRequestId(describeCensResponse.getRequestId());
                        continue;
                    }

                }

                logger.info("Creating Cen: {}", requestDto.toString());

                CreateCenRequest request = requestDto.toSdk();
                CreateCenResponse response = this.acsClientStub.request(client, request);
                result = result.fromSdk(response);

                // wait till CEN is in active status
                Function<?, Boolean> func = o -> ifCenInStatus(client, response.getCenId(), CenStatus.Active);
                PluginTimer.runTask(new PluginTimerTask(func));


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
    public List<CoreDeleteCenResponseDto> deleteCen(List<CoreDeleteCenRequestDto> requestDtoList) {
        List<CoreDeleteCenResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteCenRequestDto requestDto : requestDtoList) {
            CoreDeleteCenResponseDto result = new CoreDeleteCenResponseDto();

            try {

                validator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                final String cenId = requestDto.getCenId();
                if (StringUtils.isNotEmpty(cenId)) {
                    final DescribeCensResponse describeCensResponse = this.retrieveCen(client);
                    final List<DescribeCensResponse.Cen> foundCenList = describeCensResponse.getCens().stream().filter(cen -> cenId.equals(cen.getCenId())).collect(Collectors.toList());

                    if (foundCenList.isEmpty()) {
                        logger.info("The cen given by ID: [{}] has already been deleted.", cenId);
                        result.setRequestId(describeCensResponse.getRequestId());
                        continue;
                    }
                }

                // check if cen has child instances

                final DescribeCenAttachedChildInstancesResponse describeCenAttachedChildInstancesResponse = this.retrieveCenAttachedChildInstance(client, cenId);
                final List<DescribeCenAttachedChildInstancesResponse.ChildInstance> childInstances = describeCenAttachedChildInstancesResponse.getChildInstances();
                if (!childInstances.isEmpty()) {
                    detachAllChildInstances(client, cenId, childInstances);
                }

                logger.info("Deleting Cen: {}", requestDto.toString());

                final DeleteCenRequest deleteCenRequest = requestDto.toSdk();
                final DeleteCenResponse response = this.acsClientStub.request(client, deleteCenRequest);
                result = result.fromSdk(response);

                // check if cen has already been deleted
                Function<?, Boolean> func = o -> this.checkIfCenHasBeenDeleted(client, cenId);
                PluginTimer.runTask(new PluginTimerTask(func));

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

    private void detachAllChildInstances(IAcsClient client, String cenId, List<DescribeCenAttachedChildInstancesResponse.ChildInstance> childInstances) throws PluginException, AliCloudException {
        logger.info("Detaching all child instance first.");

        for (DescribeCenAttachedChildInstancesResponse.ChildInstance childInstance : childInstances) {
            final DetachCenChildInstanceRequest detachCenChildInstanceRequest = PluginSdkBridge.toSdk(childInstance, DetachCenChildInstanceRequest.class);
            detachCenChildInstanceRequest.setCenId(cenId);
            this.acsClientStub.request(client, detachCenChildInstanceRequest);
        }

        Function<?, Boolean> func = o -> this.checkIfCenHasNoChildInstance(client, cenId);
        PluginTimer.runTask(new PluginTimerTask(func));
    }

    @Override
    public List<CoreAttachCenChildResponseDto> attachCenChild(List<CoreAttachCenChildRequestDto> requestDtoList) {
        List<CoreAttachCenChildResponseDto> resultList = new ArrayList<>();
        for (CoreAttachCenChildRequestDto requestDto : requestDtoList) {
            CoreAttachCenChildResponseDto result = new CoreAttachCenChildResponseDto();

            try {

                validator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                logger.info("Attaching Cen child instance: {}", requestDto.toString());

                AttachCenChildInstanceRequest request = requestDto.toSdk();
                AttachCenChildInstanceResponse response = this.acsClientStub.request(client, request);
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
    public List<CoreDetachCenChildResponseDto> detachCenChild(List<CoreDetachCenChildRequestDto> requestDtoList) {
        List<CoreDetachCenChildResponseDto> resultList = new ArrayList<>();
        for (CoreDetachCenChildRequestDto requestDto : requestDtoList) {
            CoreDetachCenChildResponseDto result = new CoreDetachCenChildResponseDto();

            try {

                validator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                logger.info("Detaching Cen child instance: {}", requestDto.toString());

                DetachCenChildInstanceRequest request = requestDto.toSdk();
                DetachCenChildInstanceResponse response = this.acsClientStub.request(client, request);
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


    private DescribeCensResponse retrieveCen(IAcsClient client) throws AliCloudException, PluginException {

        DescribeCensRequest request = new DescribeCensRequest();
        DescribeCensResponse response;
        response = this.acsClientStub.request(client, request);
        return response;
    }

    private DescribeCenAttachedChildInstancesResponse retrieveCenAttachedChildInstance(IAcsClient client, String cenId) throws PluginException, AliCloudException {

        if (StringUtils.isEmpty(cenId)) {
            throw new PluginException("The CenId cannot be empty or null.");
        }

        DescribeCenAttachedChildInstancesRequest retrieveAttachedChildInstanceRequest = new DescribeCenAttachedChildInstancesRequest();
        retrieveAttachedChildInstanceRequest.setCenId(cenId);

        return this.acsClientStub.request(client, retrieveAttachedChildInstanceRequest);
    }

    private Boolean checkIfCenHasNoChildInstance(IAcsClient client, String cenId) {
        final DescribeCenAttachedChildInstancesResponse describeCenAttachedChildInstancesResponse = this.retrieveCenAttachedChildInstance(client, cenId);
        return describeCenAttachedChildInstancesResponse.getChildInstances().isEmpty();
    }

    private Boolean checkIfCenHasBeenDeleted(IAcsClient client, String cenId) {
        boolean ifCenDeleted = false;
        final DescribeCensResponse describeCensResponse = this.retrieveCen(client);
        final List<DescribeCensResponse.Cen> foundCenList = describeCensResponse.getCens().stream().filter(cen -> cenId.equals(cen.getCenId())).collect(Collectors.toList());

        if (foundCenList.isEmpty()) {
            ifCenDeleted = true;
        }
        return ifCenDeleted;
    }

    private boolean ifCenInStatus(IAcsClient client, String cenId, CenStatus... statusArray) {

        DescribeCensRequest request = new DescribeCensRequest();

        final DescribeCensResponse response = acsClientStub.request(client, request);
        if (response.getCens().isEmpty()) {
            throw new PluginException("Current account doesn't have any CENs");
        }

        final List<DescribeCensResponse.Cen> foundCenList = response.getCens()
                .stream()
                .filter(cen -> StringUtils.equals(cen.getCenId(), cenId))
                .collect(Collectors.toList());

        if (foundCenList.isEmpty()) {
            throw new PluginException(String.format("Current account doesn't have CEN resource according to given CenID: [%s]", cenId));
        }

        final List<String> statusList = Arrays.stream(statusArray).map(Enum::toString).collect(Collectors.toList());

        return statusList.contains(foundCenList.get(0).getStatus());
    }
}
