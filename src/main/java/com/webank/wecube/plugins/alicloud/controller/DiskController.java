package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.ecs.disk.*;
import com.webank.wecube.plugins.alicloud.service.ecs.disk.DiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/disk")
public class DiskController {

    private DiskService diskService;

    @Autowired
    public DiskController(DiskService diskService) {
        this.diskService = diskService;
    }

    @PostMapping(path = "/create_attach")
    @ResponseBody
    public CoreResponseDto<CoreCreateAttachDiskResponseDto> createDisk(@RequestBody CoreRequestDto<CoreCreateAttachDiskRequestDto> request) {
        List<CoreCreateAttachDiskResponseDto> result = this.diskService.createAttachDisk(request.getInputs());
        return new CoreResponseDto<CoreCreateAttachDiskResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/detach_delete")
    @ResponseBody
    public CoreResponseDto<CoreDetachDeleteDiskResponseDto> deleteDisk(@RequestBody CoreRequestDto<CoreDetachDeleteDiskRequestDto> request) {
        List<CoreDetachDeleteDiskResponseDto> result = this.diskService.detachDeleteDisk(request.getInputs());
        return new CoreResponseDto<CoreDetachDeleteDiskResponseDto>().withErrorCheck(result);
    }
}
