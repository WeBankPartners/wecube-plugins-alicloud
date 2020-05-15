package com.webank.wecube.plugins.alicloud.service.rds;

import com.aliyuncs.IAcsClient;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreCreateBackupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreCreateBackupResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreDeleteBackupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.backup.CoreDeleteBackupResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreCreateDBInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreCreateDBInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreDeleteDBInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreDeleteDBInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityGroup.CoreModifyDBSecurityGroupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityGroup.CoreModifyDBSecurityGroupResponseDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityIP.CoreModifySecurityIPsRequestDto;
import com.webank.wecube.plugins.alicloud.dto.rds.securityIP.CoreModifySecurityIPsResponseDto;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;

import java.util.List;

/**
 * @author howechen
 */
public interface RDSService {
    List<CoreCreateDBInstanceResponseDto> createDB(List<CoreCreateDBInstanceRequestDto> requestDtoList);

    List<CoreDeleteDBInstanceResponseDto> deleteDB(List<CoreDeleteDBInstanceRequestDto> requestDtoList);

    List<CoreModifySecurityIPsResponseDto> modifySecurityIPs(List<CoreModifySecurityIPsRequestDto> requestDtoList);

    List<CoreModifySecurityIPsResponseDto> appendSecurityIps(List<CoreModifySecurityIPsRequestDto> requestDtoList);

    List<CoreModifySecurityIPsResponseDto> deleteSecurityIps(List<CoreModifySecurityIPsRequestDto> requestDtoList);

    List<CoreModifyDBSecurityGroupResponseDto> appendSecurityGroup(List<CoreModifyDBSecurityGroupRequestDto> requestDtoList);

    List<CoreModifyDBSecurityGroupResponseDto> removeSecurityGroup(List<CoreModifyDBSecurityGroupRequestDto> requestDtoList);

    List<CoreCreateBackupResponseDto> createBackup(List<CoreCreateBackupRequestDto> requestDtoList);

    List<CoreDeleteBackupResponseDto> deleteBackup(List<CoreDeleteBackupRequestDto> requestDtoList);

    Boolean ifDBInstanceInStatus(IAcsClient client, String regionId, String dbInstanceId, RDSStatus status) throws PluginException, AliCloudException;

    Boolean ifRDSAccountCreated(IAcsClient client, String regionId, String accountName, String dBInstanceId) throws PluginException, AliCloudException;
}
