package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDtoBkp;
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
    public CoreResponseDtoBkp<?> createDB(@RequestBody CoreRequestDtoBkp<CoreCreateDBInstanceRequestDto> request) {
        List<CoreCreateDBInstanceResponseDto> result;
        try {
            result = this.rdsServcie.createDB(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreCreateDBInstanceResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/db/delete")
    @ResponseBody
    public CoreResponseDtoBkp<?> deleteDB(@RequestBody CoreRequestDtoBkp<CoreDeleteDBInstanceRequestDto> request) {
        List<CoreDeleteDBInstanceResponseDto> result;
        try {
            result = this.rdsServcie.deleteDB(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreDeleteDBInstanceResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/security_ip/modify")
    @ResponseBody
    public CoreResponseDtoBkp<?> modifySecurityIp(@RequestBody CoreRequestDtoBkp<CoreModifySecurityIPsRequestDto> request) {
        List<CoreModifySecurityIPsResponseDto> result;
        try {
            result = this.rdsServcie.modifySecurityIPs(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreModifySecurityIPsResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/backup/create")
    @ResponseBody
    public CoreResponseDtoBkp<?> createBackup(@RequestBody CoreRequestDtoBkp<CoreCreateBackupRequestDto> request) {
        List<CoreCreateBackupResponseDto> result;
        try {
            result = this.rdsServcie.createBackup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreCreateBackupResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/backup/delete")
    @ResponseBody
    public CoreResponseDtoBkp<CoreDeleteBackupResponseDto> deleteBackup(@RequestBody CoreRequestDtoBkp<CoreDeleteBackupRequestDto> request) {
        List<CoreDeleteBackupResponseDto> result;
        result = this.rdsServcie.deleteBackup(request.getInputs());
        return new CoreResponseDtoBkp<CoreDeleteBackupResponseDto>().withErrorCheck(result);
    }
}
