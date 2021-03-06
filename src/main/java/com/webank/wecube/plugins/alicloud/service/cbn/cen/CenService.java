package com.webank.wecube.plugins.alicloud.service.cbn.cen;

import com.webank.wecube.plugins.alicloud.dto.cbn.cen.*;

import java.util.List;

/**
 * @author howechen
 */
public interface CenService {

    List<CoreCreateCenResponseDto> createCen(List<CoreCreateCenRequestDto> requestDtoList);

    List<CoreDeleteCenResponseDto> deleteCen(List<CoreDeleteCenRequestDto> requestDtoList);

    List<CoreAttachCenChildResponseDto> attachCenChild(List<CoreAttachCenChildRequestDto> requestDtoList);

    List<CoreDetachCenChildResponseDto> detachCenChild(List<CoreDetachCenChildRequestDto> requestDtoList);
}
