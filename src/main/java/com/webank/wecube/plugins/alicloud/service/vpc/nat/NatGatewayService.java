package com.webank.wecube.plugins.alicloud.service.vpc.nat;

import com.webank.wecube.plugins.alicloud.dto.vpc.nat.*;

import java.util.List;

/**
 * @author howechen
 */
public interface NatGatewayService {

    List<CoreCreateNatGatewayResponseDto> createNatGateway(List<CoreCreateNatGatewayRequestDto> requestDtoList);

    List<CoreDeleteNatGatewayResponseDto> deleteNatGateway(List<CoreDeleteNatGatewayRequestDto> requestDtoList);

    List<CoreCreateSnatEntryResponseDto> createSnatEntry(List<CoreCreateSnatEntryRequestDto> requestDtoList);

    List<CoreDeleteSnatEntryResponseDto> deleteSnatEntry(List<CoreDeleteSnatEntryRequestDto> requestDtoList);

}
