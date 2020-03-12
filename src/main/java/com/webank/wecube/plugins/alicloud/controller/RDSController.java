package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreCreateBackupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreCreateBackupResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreDeleteBackupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreDeleteBackupResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreCreateDBInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreCreateDBInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreDeleteDBInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreDeleteDBInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityIP.CoreModifySecurityIPsRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityIP.CoreModifySecurityIPsResponseDto;
import com.webank.wecube.plugins.alicloud.service.rds.RDSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/rds")
public class RDSController {

    private RDSService rdsServcie;

    @Autowired
    public RDSController(RDSService rdsServcie) {
        this.rdsServcie = rdsServcie;
    }

    @PostMapping(path = "/db/create")
    @ResponseBody
    public CoreResponseDto<?> createDB(@RequestBody CoreRequestDto<CoreCreateDBInstanceRequestDto> request) {
        List<CoreCreateDBInstanceResponseDto> result;
        try {
            result = this.rdsServcie.createDB(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreCreateDBInstanceResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/db/delete")
    @ResponseBody
    public CoreResponseDto<?> deleteDB(@RequestBody CoreRequestDto<CoreDeleteDBInstanceRequestDto> request) {
        List<CoreDeleteDBInstanceResponseDto> result;
        try {
            result = this.rdsServcie.deleteDB(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreDeleteDBInstanceResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/security_ip/modify")
    @ResponseBody
    public CoreResponseDto<?> modifySecurityIp(@RequestBody CoreRequestDto<CoreModifySecurityIPsRequestDto> request) {
        List<CoreModifySecurityIPsResponseDto> result;
        try {
            result = this.rdsServcie.modifySecurityIPs(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreModifySecurityIPsResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/backup/create")
    @ResponseBody
    public CoreResponseDto<?> createBackup(@RequestBody CoreRequestDto<CoreCreateBackupRequestDto> request) {
        List<CoreCreateBackupResponseDto> result;
        try {
            result = this.rdsServcie.createBackup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreCreateBackupResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/backup/delete")
    @ResponseBody
    public CoreResponseDto<?> deleteBackup(@RequestBody CoreRequestDto<CoreDeleteBackupRequestDto> request) {
        List<CoreDeleteBackupResponseDto> result;
        try {
            result = this.rdsServcie.deleteBackup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreDeleteBackupResponseDto>().okayWithData(result);
    }
}
