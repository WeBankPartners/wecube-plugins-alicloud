package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.disk.*;
import com.webank.wecube.plugins.alicloud.service.disk.DiskService;
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
    public CoreResponseDto<?> createDisk(@RequestBody CoreRequestDto<CoreCreateDiskRequestDto> request) {
        List<CoreCreateDiskResponseDto> result;
        try {
            result = this.diskService.createDisk(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreCreateDiskResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<?> deleteDisk(@RequestBody CoreRequestDto<CoreDeleteDiskRequestDto> request) {
        List<CoreDeleteDiskResponseDto> result;
        try {
            result = this.diskService.deleteDisk(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreDeleteDiskResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/attach")
    @ResponseBody
    public CoreResponseDto<?> attachDisk(@RequestBody CoreRequestDto<CoreAttachDiskRequestDto> request) {
        List<CoreAttachDiskResponseDto> result;
        try {
            result = this.diskService.attachDisk(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreAttachDiskResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/attach")
    @ResponseBody
    public CoreResponseDto<?> detachDisk(@RequestBody CoreRequestDto<CoreDetachDiskRequestDto> request) {
        List<CoreDetachDiskResponseDto> result;
        try {
            result = this.diskService.detachDisk(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreDetachDiskResponseDto>().okayWithData(result);
    }
}
