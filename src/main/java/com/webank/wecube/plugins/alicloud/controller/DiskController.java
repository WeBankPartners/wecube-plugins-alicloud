package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.ecs.disk.*;
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
    public CoreResponseDtoBkp<?> createDisk(@RequestBody CoreRequestDtoBkp<CoreCreateDiskRequestDto> request) {
        List<CoreCreateDiskResponseDto> result;
        try {
            result = this.diskService.createDisk(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreCreateDiskResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDtoBkp<?> deleteDisk(@RequestBody CoreRequestDtoBkp<CoreDeleteDiskRequestDto> request) {
        List<CoreDeleteDiskResponseDto> result;
        try {
            result = this.diskService.deleteDisk(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreDeleteDiskResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/attach")
    @ResponseBody
    public CoreResponseDtoBkp<?> attachDisk(@RequestBody CoreRequestDtoBkp<CoreAttachDiskRequestDto> request) {
        List<CoreAttachDiskResponseDto> result;
        try {
            result = this.diskService.attachDisk(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreAttachDiskResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/detach")
    @ResponseBody
    public CoreResponseDtoBkp<?> detachDisk(@RequestBody CoreRequestDtoBkp<CoreDetachDiskRequestDto> request) {
        List<CoreDetachDiskResponseDto> result;
        try {
            result = this.diskService.detachDisk(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreDetachDiskResponseDto>().okayWithData(result);
    }
}
