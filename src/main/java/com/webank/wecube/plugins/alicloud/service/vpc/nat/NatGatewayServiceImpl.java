package com.webank.wecube.plugins.alicloud.service.vpc.nat;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.google.common.collect.Lists;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.cloudParam.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.nat.*;
import com.webank.wecube.plugins.alicloud.service.vpc.eip.AssociatedInstanceType;
import com.webank.wecube.plugins.alicloud.service.vpc.eip.EipService;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimer;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimerTask;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
@Service
public class NatGatewayServiceImpl implements NatGatewayService {

    private static final Logger logger = LoggerFactory.getLogger(NatGatewayService.class);

    private final AcsClientStub acsClientStub;
    private final DtoValidator dtoValidator;
    private final EipService eipService;

    @Autowired
    public NatGatewayServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator, EipService eipService) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
        this.eipService = eipService;
    }

    @Override
    public List<CoreCreateNatGatewayResponseDto> createNatGateway(List<CoreCreateNatGatewayRequestDto> requestDtoList) {
        List<CoreCreateNatGatewayResponseDto> resultList = new ArrayList<>();
        for (CoreCreateNatGatewayRequestDto requestDto : requestDtoList) {
            CoreCreateNatGatewayResponseDto result = new CoreCreateNatGatewayResponseDto();
            try {

                dtoValidator.validate(requestDto);

                logger.info("Creating NAT gateway: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                if (StringUtils.isNotEmpty(requestDto.getNatGatewayId())) {
                    DescribeNatGatewaysRequest retrieveNatGatewayRequest = new DescribeNatGatewaysRequest();
                    retrieveNatGatewayRequest.setNatGatewayId(requestDto.getNatGatewayId());
                    final DescribeNatGatewaysResponse describeNatGatewaysResponse = this.retrieveNatGateway(client, retrieveNatGatewayRequest, regionId);

                    if (!describeNatGatewaysResponse.getNatGateways().isEmpty()) {
                        result = result.fromSdkCrossLineage(describeNatGatewaysResponse.getNatGateways().get(0));
                        result.setRequestId(describeNatGatewaysResponse.getRequestId());
                        continue;
                    }
                }

                logger.info("Creating NAT gateway...");

                CreateNatGatewayRequest createNatGatewayRequest = requestDto.toSdk();
                CreateNatGatewayResponse response = this.acsClientStub.request(client, createNatGatewayRequest, regionId);
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
    public List<CoreDeleteNatGatewayResponseDto> deleteNatGateway(List<CoreDeleteNatGatewayRequestDto> requestDtoList) {
        List<CoreDeleteNatGatewayResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteNatGatewayRequestDto requestDto : requestDtoList) {
            CoreDeleteNatGatewayResponseDto result = new CoreDeleteNatGatewayResponseDto();
            try {

                dtoValidator.validate(requestDto);

                logger.info("Deleting NAT gateway: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();
                final DescribeNatGatewaysResponse describeNatGatewaysResponse = this.retrieveNatGateway(client, requestDto.getNatGatewayId(), regionId);
                if (describeNatGatewaysResponse.getNatGateways().isEmpty()) {
                    String msg = String.format("Cannot find Nat gateway info by ID: [%s]", requestDto.getNatGatewayId());
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                // need to un-associate then release the eip first
                final DescribeNatGatewaysResponse.NatGateway foundNatGateway = describeNatGatewaysResponse.getNatGateways().get(0);
                final List<DescribeNatGatewaysResponse.NatGateway.IpList> eipList = foundNatGateway.getIpLists();
                final String natGatewayId = foundNatGateway.getNatGatewayId();
                if (!eipList.isEmpty()) {
                    final List<String> allocationIdList = eipList.stream().map(DescribeNatGatewaysResponse.NatGateway.IpList::getAllocationId).collect(Collectors.toList());

                    logger.info("Un-associating NAT gateway with bound EIp...");
                    this.eipService.unAssociateEipAddress(client, regionId, allocationIdList, natGatewayId, "Nat");

                }

                // setup a timer to poll whether the EIP has already un-associated with the NAT gateway
                Function<?, Boolean> checkIfAllEipUnAssociated = o -> this.eipService.ifEipIsAvailable(client, regionId, AssociatedInstanceType.Nat.toString(), natGatewayId);
                PluginTimer.runTask(new PluginTimerTask(checkIfAllEipUnAssociated));

                logger.info("Deleting NAT gateway...");

                DeleteNatGatewayRequest request = requestDto.toSdk();
                DeleteNatGatewayResponse response = this.acsClientStub.request(client, request, regionId);
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
    public List<CoreCreateSnatEntryResponseDto> createSnatEntry(List<CoreCreateSnatEntryRequestDto> requestDtoList) {
        List<CoreCreateSnatEntryResponseDto> resultList = new ArrayList<>();
        for (CoreCreateSnatEntryRequestDto requestDto : requestDtoList) {
            CoreCreateSnatEntryResponseDto result = new CoreCreateSnatEntryResponseDto();
            try {

                dtoValidator.validate(requestDto);

                logger.info("Creating SNAT entry: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                // bind eip to nat gateway
                bindEipToNat(client, regionId, requestDto.getNatId(), requestDto.getSnatIp());

                // create snat entry
                final String listStr = PluginStringUtils.handleCoreListStr(requestDto.getSnatIp(), true);
                requestDto.setSnatIp(listStr);


                CreateSnatEntryRequest request = requestDto.toSdk();
                CreateSnatEntryResponse response = this.acsClientStub.request(client, request, regionId);

                // wait till snat entry status is not pending
                Function<?, Boolean> func = o -> ifSnatEntryNotInStatus(client, regionId, requestDto.getSnatTableId(), response.getSnatEntryId(), SnatEntryStatus.Pending);
                PluginTimer.runTask(new PluginTimerTask(func));

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
    public List<CoreDeleteSnatEntryResponseDto> deleteSnatEntry(List<CoreDeleteSnatEntryRequestDto> requestDtoList) {
        List<CoreDeleteSnatEntryResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteSnatEntryRequestDto requestDto : requestDtoList) {
            CoreDeleteSnatEntryResponseDto result = new CoreDeleteSnatEntryResponseDto();
            try {

                dtoValidator.validate(requestDto);

                logger.info("Deleting SNAT entry: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                // find snat ips from entry id
                final String[] ips = retrieveSnatIpsFromEntryId(requestDto, client, regionId);

                DeleteSnatEntryRequest request = requestDto.toSdk();
                DeleteSnatEntryResponse response = this.acsClientStub.request(client, request, regionId);
                result = result.fromSdk(response);

                // wait till snat entry has been deleted
                Function<?, Boolean> func = o -> ifSnatEntryHasBeenDeleted(client, regionId, requestDto.getSnatTableId(), requestDto.getSnatEntryId());
                PluginTimer.runTask(new PluginTimerTask(func));

                // unbind snat ip from nat
                eipService.unbindIpFromInstance(client, regionId, requestDto.getNatId(), AssociatedInstanceType.Nat, ips);

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
    public List<CoreModifySnatEntryResponseDto> appendSnatEntry(List<CoreModifySnatEntryRequestDto> requestDtoList) {
        List<CoreModifySnatEntryResponseDto> resultList = new ArrayList<>();
        for (CoreModifySnatEntryRequestDto requestDto : requestDtoList) {
            CoreModifySnatEntryResponseDto result = new CoreModifySnatEntryResponseDto();
            try {

                dtoValidator.validate(requestDto);

                logger.info("Appending SNAT entry: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();
                final String natId = requestDto.getNatId();

                // query the current entry's ip
                final List<String> currentIpList = retrieveCurrentSnatIpList(client, regionId, requestDto.getSnatTableId(), requestDto.getSnatEntryId());

                // clear up the eip list, remove bound eip from request
                final List<String> targetIpList = PluginStringUtils.splitStringList(requestDto.getSnatIp());
                targetIpList.removeAll(currentIpList);

                // bind target eip to nat gateway
                for (String ip : targetIpList) {
                    eipService.bindIpToInstance(client, regionId, natId, AssociatedInstanceType.Nat, ip);
                }

                // append the new-bound eip to the current list
                currentIpList.addAll(targetIpList);
                requestDto.setSnatIp(PluginStringUtils.stringifyListWithoutBracket(currentIpList));

                // modify the snat entry
                ModifySnatEntryRequest request = requestDto.toSdk();
                ModifySnatEntryResponse response = this.acsClientStub.request(client, request, regionId);

                // wait till snat entry status is not pending or modifying
                Function<?, Boolean> func = o -> ifSnatEntryNotInStatus(client, regionId, requestDto.getSnatTableId(), requestDto.getSnatEntryId(), SnatEntryStatus.Pending, SnatEntryStatus.Modifying);
                PluginTimer.runTask(new PluginTimerTask(func));

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
    public List<CoreModifySnatEntryResponseDto> pruneSnatEntry(List<CoreModifySnatEntryRequestDto> requestDtoList) {
        List<CoreModifySnatEntryResponseDto> resultList = new ArrayList<>();
        for (CoreModifySnatEntryRequestDto requestDto : requestDtoList) {
            CoreModifySnatEntryResponseDto result = new CoreModifySnatEntryResponseDto();
            try {

                dtoValidator.validate(requestDto);

                logger.info("Pruning SNAT entry: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                // query the current entry's ip
                final List<String> currentIpList = retrieveCurrentSnatIpList(client, regionId, requestDto.getSnatTableId(), requestDto.getSnatEntryId());

                // calculate the intersection of current and target ip list, remove non-exist ip
                List<String> targetIpList = PluginStringUtils.splitStringList(requestDto.getSnatIp());
                targetIpList = currentIpList.stream().filter(targetIpList::contains).collect(Collectors.toList());
                currentIpList.removeAll(targetIpList);
                requestDto.setSnatIp(PluginStringUtils.stringifyListWithoutBracket(currentIpList));

                // send the request
                ModifySnatEntryRequest request = requestDto.toSdk();
                ModifySnatEntryResponse response = this.acsClientStub.request(client, request, regionId);

                // wait till snat entry status is not pending
                Function<?, Boolean> func = o -> ifSnatEntryNotInStatus(client, regionId, requestDto.getSnatTableId(), requestDto.getSnatEntryId(), SnatEntryStatus.Pending);
                PluginTimer.runTask(new PluginTimerTask(func));

                // un-bind the eip from the nat
                eipService.unbindIpFromInstance(client, regionId, requestDto.getNatId(), AssociatedInstanceType.Nat, targetIpList.toArray(new String[0]));

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

    private String[] retrieveSnatIpsFromEntryId(CoreDeleteSnatEntryRequestDto requestDto, IAcsClient client, String regionId) {
        final DescribeSnatTableEntriesResponse.SnatTableEntry foundSnatEntry = retrieveSnatEntryByEntryId(client, regionId, requestDto.getSnatTableId(), requestDto.getSnatEntryId());
        return foundSnatEntry.getSnatIp().split(",");
    }

    private DescribeNatGatewaysResponse retrieveNatGateway(IAcsClient client, String natGatewayId, String regionId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(natGatewayId, regionId)) {
            throw new PluginException("Either natGatewayId or regionId cannot be null or empty");
        }

        DescribeNatGatewaysRequest request = new DescribeNatGatewaysRequest();
        request.setNatGatewayId(natGatewayId);

        return this.retrieveNatGateway(client, request, regionId);
    }

    private DescribeNatGatewaysResponse retrieveNatGateway(IAcsClient client, DescribeNatGatewaysRequest request, String regionId) throws AliCloudException, PluginException {

        if (StringUtils.isAnyEmpty(regionId, request.getNatGatewayId())) {
            throw new PluginException("Either regionId or natGatewayId cannot be null or empty.");
        }

        logger.info("Retrieving NAT gateway info...");
        return this.acsClientStub.request(client, request, regionId);
    }

    /**
     * Check if EIP bind NAT, if not, bind them
     *
     * @param client   alicloud client
     * @param regionId regionId
     * @param natId    natId
     * @param snatIp   could be list snatIp
     */
    private void bindEipToNat(IAcsClient client, String regionId, String natId, String snatIp) {
        final String[] ipArray = PluginStringUtils.splitStringList(snatIp).toArray(new String[0]);
        eipService.bindIpToInstance(client, regionId, natId, AssociatedInstanceType.Nat, ipArray);
    }

    private DescribeSnatTableEntriesResponse.SnatTableEntry retrieveSnatEntryByEntryId(IAcsClient client, String regionId, String snatTableId, String snatEntryId) {

        DescribeSnatTableEntriesRequest request = new DescribeSnatTableEntriesRequest();
        request.setSnatTableId(snatTableId);
        request.setSnatEntryId(snatEntryId);

        final DescribeSnatTableEntriesResponse response = acsClientStub.request(client, request, regionId);

        if (response.getSnatTableEntries().isEmpty()) {
            throw new PluginException(String.format("Cannot find snat entry by given snat table Id: [%s], snat entry id: [%s]", snatTableId, snatEntryId));
        }
        return response.getSnatTableEntries().get(0);
    }

    private boolean ifSnatEntryInStatus(IAcsClient client, String regionId, String snatTableId, String snatEntryId, SnatEntryStatus... statusArray) {
        final DescribeSnatTableEntriesResponse.SnatTableEntry foundEntry = retrieveSnatEntryByEntryId(client, regionId, snatTableId, snatEntryId);

        final List<String> statusStringList = Lists.newArrayList(statusArray).stream().map(Enum::toString).collect(Collectors.toList());
        return statusStringList.contains(foundEntry.getStatus());
    }


    private boolean ifSnatEntryNotInStatus(IAcsClient client, String regionId, String snatTableId, String snatEntryId, SnatEntryStatus... statusArray) {
        final DescribeSnatTableEntriesResponse.SnatTableEntry foundEntry = retrieveSnatEntryByEntryId(client, regionId, snatTableId, snatEntryId);
        final List<String> statusStringList = Lists.newArrayList(statusArray).stream().map(Enum::toString).collect(Collectors.toList());
        return !statusStringList.contains(foundEntry.getStatus());
    }

    private boolean ifSnatEntryHasBeenDeleted(IAcsClient client, String regionId, String snatTableId, String snatEntryId) {
        DescribeSnatTableEntriesRequest request = new DescribeSnatTableEntriesRequest();
        request.setSnatTableId(snatTableId);
        request.setSnatEntryId(snatEntryId);

        final DescribeSnatTableEntriesResponse response = acsClientStub.request(client, request, regionId);

        return response.getSnatTableEntries().isEmpty();
    }

    private List<String> retrieveCurrentSnatIpList(IAcsClient client, String regionId, String snatTableId, String snatEntryId) {
        final DescribeSnatTableEntriesResponse.SnatTableEntry foundEntry = retrieveSnatEntryByEntryId(client, regionId, snatTableId, snatEntryId);
        return Lists.newArrayList(foundEntry.getSnatIp().split(","));
    }
}
