package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
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
    public CoreResponseDto<CoreCreateDBInstanceResponseDto> createDB(@RequestBody CoreRequestDto<CoreCreateDBInstanceRequestDto> request) {
        List<CoreCreateDBInstanceResponseDto> result = this.rdsServcie.createDB(request.getInputs());
        return new CoreResponseDto<CoreCreateDBInstanceResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/db/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteDBInstanceResponseDto> deleteDB(@RequestBody CoreRequestDto<CoreDeleteDBInstanceRequestDto> request) {
        List<CoreDeleteDBInstanceResponseDto> result = this.rdsServcie.deleteDB(request.getInputs());
        return new CoreResponseDto<CoreDeleteDBInstanceResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/security_ip/modify")
    @ResponseBody
    public CoreResponseDto<CoreModifySecurityIPsResponseDto> modifySecurityIp(@RequestBody CoreRequestDto<CoreModifySecurityIPsRequestDto> request) {
        List<CoreModifySecurityIPsResponseDto> result = this.rdsServcie.modifySecurityIPs(request.getInputs());
        return new CoreResponseDto<CoreModifySecurityIPsResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/security_ip/append")
    @ResponseBody
    public CoreResponseDto<CoreModifySecurityIPsResponseDto> appendSecurityIp(@RequestBody CoreRequestDto<CoreModifySecurityIPsRequestDto> request) {
        List<CoreModifySecurityIPsResponseDto> result = this.rdsServcie.appendSecurityIps(request.getInputs());
        return new CoreResponseDto<CoreModifySecurityIPsResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/security_ip/delete")
    @ResponseBody
    public CoreResponseDto<CoreModifySecurityIPsResponseDto> deleteSecurityIp(@RequestBody CoreRequestDto<CoreModifySecurityIPsRequestDto> request) {
        List<CoreModifySecurityIPsResponseDto> result = this.rdsServcie.deleteSecurityIps(request.getInputs());
        return new CoreResponseDto<CoreModifySecurityIPsResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/backup/create")
    @ResponseBody
    public CoreResponseDto<CoreCreateBackupResponseDto> createBackup(@RequestBody CoreRequestDto<CoreCreateBackupRequestDto> request) {
        List<CoreCreateBackupResponseDto> result = this.rdsServcie.createBackup(request.getInputs());
        return new CoreResponseDto<CoreCreateBackupResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/backup/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteBackupResponseDto> deleteBackup(@RequestBody CoreRequestDto<CoreDeleteBackupRequestDto> request) {
        List<CoreDeleteBackupResponseDto> result = this.rdsServcie.deleteBackup(request.getInputs());
        return new CoreResponseDto<CoreDeleteBackupResponseDto>().withErrorCheck(result);
    }
}
