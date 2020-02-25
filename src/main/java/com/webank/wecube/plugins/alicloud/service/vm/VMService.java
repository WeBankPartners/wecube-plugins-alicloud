package com.webank.wecube.plugins.alicloud.service.vm;

import com.aliyuncs.ecs.model.v20140526.DeleteInstanceRequest;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMResponseDto;

import java.util.List;

/**
 * @author howechen
 */
public interface VMService {

    List<CoreCreateVMResponseDto> createVM(List<CoreCreateVMRequestDto> coreCreateVMRequestDtoList);

    void deleteVM(List<DeleteInstanceRequest> coreCreateVMRequestDtoList);
}
