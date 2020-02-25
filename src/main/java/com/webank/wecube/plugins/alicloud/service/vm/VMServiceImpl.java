package com.webank.wecube.plugins.alicloud.service.vm;

import com.aliyuncs.ecs.model.v20140526.DeleteInstanceRequest;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author howechen
 */
@Service
public class VMServiceImpl implements VMService {

    @Override
    public List<CoreCreateVMResponseDto> createVM(List<CoreCreateVMRequestDto> coreCreateVMRequestDtoList) {
        return null;
    }

    @Override
    public void deleteVM(List<DeleteInstanceRequest> coreCreateVMRequestDtoList) {

    }

}
