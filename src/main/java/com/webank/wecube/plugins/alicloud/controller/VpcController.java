package com.webank.wecube.plugins.alicloud.controller;

import com.aliyuncs.vpc.model.v20160428.DeleteVpcRequest;
import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcResponseDto;
import com.webank.wecube.plugins.alicloud.service.VpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX)
public class VpcController {

    @Autowired
    private VpcService vpcService;

    @PostMapping(path = "vpc")
    @ResponseBody
    public CoreResponseDto<?> createVpc(@RequestBody CoreRequestDto<CoreCreateVpcRequestDto> coreRequestDto) {
        List<CoreCreateVpcResponseDto> result;
        try {
            result = this.vpcService.createVpc(coreRequestDto.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreCreateVpcResponseDto>().okayWithData(result);
    }

    @DeleteMapping(path = "vpc")
    @ResponseBody
    public CoreResponseDto<?> deleteVpc(@RequestBody CoreRequestDto<DeleteVpcRequest> coreRequestDto) {
        try {
            this.vpcService.deleteVpc(coreRequestDto.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return CoreResponseDto.okay();
    }


}
