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

    @PostMapping(path = "/create")
    @ResponseBody
    public CoreResponseDto<CoreCreateDiskResponseDto> createDisk(@RequestBody CoreRequestDto<CoreCreateDiskRequestDto> request) {
        List<CoreCreateDiskResponseDto> result = this.diskService.createDisk(request.getInputs());
        return new CoreResponseDto<CoreCreateDiskResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteDiskResponseDto> deleteDisk(@RequestBody CoreRequestDto<CoreDeleteDiskRequestDto> request) {
        List<CoreDeleteDiskResponseDto> result = this.diskService.deleteDisk(request.getInputs());
        return new CoreResponseDto<CoreDeleteDiskResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/attach")
    @ResponseBody
    public CoreResponseDto<CoreAttachDiskResponseDto> attachDisk(@RequestBody CoreRequestDto<CoreAttachDiskRequestDto> request) {
        List<CoreAttachDiskResponseDto> result = this.diskService.attachDisk(request.getInputs());
        return new CoreResponseDto<CoreAttachDiskResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/detach")
    @ResponseBody
    public CoreResponseDto<CoreDetachDiskResponseDto> detachDisk(@RequestBody CoreRequestDto<CoreDetachDiskRequestDto> request) {
        List<CoreDetachDiskResponseDto> result = this.diskService.detachDisk(request.getInputs());
        return new CoreResponseDto<CoreDetachDiskResponseDto>().withErrorCheck(result);
    }
}
