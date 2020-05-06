package com.webank.wecube.plugins.alicloud.dto.rds.db;

import com.aliyuncs.rds.model.v20140815.CreateDBInstanceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import com.webank.wecube.plugins.alicloud.service.rds.RDSCategory;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.RDSResourceSeeker;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author howechen
 */
public class CoreCreateDBInstanceRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateDBInstanceRequest> {

    static final String WECUBE_HIGH_AVAILABLE_ZONE_PATTERN = "^(.*)(MAZ|maz)(\\d+)-(.+)$";
    static final String ALICLOUD_HIGH_AVAILABLE_ZONE_PATTERN = "^(?!\\[)+(.*)(MAZ)(\\d+)(.+)(?!])+$";

    @JsonProperty("dBInstanceId")
    private String dBInstanceId;
    @NotEmpty(message = "dBInstanceSpec field is mandatory.")
    @JsonProperty("dBInstanceSpec")
    private String dBInstanceSpec;

    // RDS user management
    private String seed;
    private String accountType = "Super";
    private String accountDescription;
    @NotEmpty(message = "The accountName cannot be empty.")
    private String accountName;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    private String accountPassword;

    private String dBParamGroupId;
    private String resourceOwnerId;
    @NotEmpty(message = "dBInstanceStorage field is mandatory.")
    @JsonProperty("dBInstanceStorage")
    private String dBInstanceStorage;
    private String systemDBCharset;
    @NotEmpty(message = "engineVersion field is mandatory.")
    private String engineVersion;
    private String targetDedicatedHostIdForMaster;
    @JsonProperty("dBInstanceDescription")
    private String dBInstanceDescription;
    private String businessInfo;
    private String period;
    private String encryptionKey;
    @JsonProperty("dBInstanceClass")
    private String dBInstanceClass;
    @NotEmpty(message = "securityIPList field is mandatory.")
    @JsonProperty(value = "securityIPList")
    private String securityIPList;
    @JsonProperty(value = "vSwitchId")
    private String vSwitchId;
    private String privateIpAddress;
    private String targetDedicatedHostIdForLog;
    private String autoRenew;
    private String roleARN;
    private String zoneId;
    private String instanceNetworkType = "VPC";
    private String connectionMode;
    private String clientToken;
    private String targetDedicatedHostIdForSlave;
    @NotEmpty(message = "engine field is mandatory.")
    private String engine;
    @NotEmpty(message = "dBInstanceStorageType field is mandatory")
    @JsonProperty("dBInstanceStorageType")
    private String dBInstanceStorageType;
    private String dedicatedHostGroupId;
    @JsonProperty("dBInstanceNetType")
    private String dBInstanceNetType = "Intranet";
    private String usedTime;
    @JsonProperty(value = "vpcId")
    private String vPCId;
    private String category;
    @NotEmpty(message = "payType field is mandatory.")
    private String payType;
    @NotEmpty(message = "dBIsIgnoreCase field is mandatory")
    @JsonProperty(value = "dBIsIgnoreCase")
    private String dBIsIgnoreCase;

    public CoreCreateDBInstanceRequestDto() {
    }

    public String getDBInstanceId() {
        return dBInstanceId;
    }

    public void setDBInstanceId(String dBInstanceId) {
        this.dBInstanceId = dBInstanceId;
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getDBInstanceStorage() {
        return dBInstanceStorage;
    }

    public void setDBInstanceStorage(String dBInstanceStorage) {
        this.dBInstanceStorage = dBInstanceStorage;
    }

    public String getSystemDBCharset() {
        return systemDBCharset;
    }

    public void setSystemDBCharset(String systemDBCharset) {
        this.systemDBCharset = systemDBCharset;
    }

    public String getEngineVersion() {
        return engineVersion;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public String getTargetDedicatedHostIdForMaster() {
        return targetDedicatedHostIdForMaster;
    }

    public void setTargetDedicatedHostIdForMaster(String targetDedicatedHostIdForMaster) {
        this.targetDedicatedHostIdForMaster = targetDedicatedHostIdForMaster;
    }

    public String getDBInstanceDescription() {
        return dBInstanceDescription;
    }

    public void setDBInstanceDescription(String dBInstanceDescription) {
        this.dBInstanceDescription = dBInstanceDescription;
    }

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getDBInstanceClass() {
        return dBInstanceClass;
    }

    public void setDBInstanceClass(String dBInstanceClass) {
        this.dBInstanceClass = dBInstanceClass;
    }

    public String getSecurityIPList() {
        return securityIPList;
    }

    public void setSecurityIPList(String securityIPList) {
        this.securityIPList = securityIPList;
    }

    public String getvSwitchId() {
        return vSwitchId;
    }

    public void setvSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public void setPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddress = privateIpAddress;
    }

    public String getTargetDedicatedHostIdForLog() {
        return targetDedicatedHostIdForLog;
    }

    public void setTargetDedicatedHostIdForLog(String targetDedicatedHostIdForLog) {
        this.targetDedicatedHostIdForLog = targetDedicatedHostIdForLog;
    }

    public String getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(String autoRenew) {
        this.autoRenew = autoRenew;
    }

    public String getRoleARN() {
        return roleARN;
    }

    public void setRoleARN(String roleARN) {
        this.roleARN = roleARN;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getInstanceNetworkType() {
        return instanceNetworkType;
    }

    public void setInstanceNetworkType(String instanceNetworkType) {
        this.instanceNetworkType = instanceNetworkType;
    }

    public String getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(String connectionMode) {
        this.connectionMode = connectionMode;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getTargetDedicatedHostIdForSlave() {
        return targetDedicatedHostIdForSlave;
    }

    public void setTargetDedicatedHostIdForSlave(String targetDedicatedHostIdForSlave) {
        this.targetDedicatedHostIdForSlave = targetDedicatedHostIdForSlave;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getDBInstanceStorageType() {
        return dBInstanceStorageType;
    }

    public void setDBInstanceStorageType(String dBInstanceStorageType) {
        this.dBInstanceStorageType = dBInstanceStorageType;
    }

    public String getDedicatedHostGroupId() {
        return dedicatedHostGroupId;
    }

    public void setDedicatedHostGroupId(String dedicatedHostGroupId) {
        this.dedicatedHostGroupId = dedicatedHostGroupId;
    }

    public String getDBInstanceNetType() {
        return dBInstanceNetType;
    }

    public void setDBInstanceNetType(String dBInstanceNetType) {
        this.dBInstanceNetType = dBInstanceNetType;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }

    public String getvPCId() {
        return vPCId;
    }

    public void setvPCId(String vPCId) {
        this.vPCId = vPCId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getResourceOwnerAccount() {
        return resourceOwnerAccount;
    }

    public void setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getDBIsIgnoreCase() {
        return dBIsIgnoreCase;
    }

    public void setDBIsIgnoreCase(String dBIsIgnoreCase) {
        this.dBIsIgnoreCase = dBIsIgnoreCase;
    }

    public String getdBInstanceSpec() {
        return dBInstanceSpec;
    }

    public void setDBInstanceSpec(String dBInstanceSpec) {
        this.dBInstanceSpec = dBInstanceSpec;
    }

    public String getDBParamGroupId() {
        return dBParamGroupId;
    }

    public void setdBParamGroupId(String dBParamGroupId) {
        this.dBParamGroupId = dBParamGroupId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("dBInstanceId", dBInstanceId)
                .append("dBInstanceSpec", dBInstanceSpec)
                .append("seed", seed)
                .append("accountType", accountType)
                .append("accountDescription", accountDescription)
                .append("accountName", accountName)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("accountPassword", accountPassword)
                .append("dBParamGroupId", dBParamGroupId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("dBInstanceStorage", dBInstanceStorage)
                .append("systemDBCharset", systemDBCharset)
                .append("engineVersion", engineVersion)
                .append("targetDedicatedHostIdForMaster", targetDedicatedHostIdForMaster)
                .append("dBInstanceDescription", dBInstanceDescription)
                .append("businessInfo", businessInfo)
                .append("period", period)
                .append("encryptionKey", encryptionKey)
                .append("dBInstanceClass", dBInstanceClass)
                .append("securityIPList", securityIPList)
                .append("vSwitchId", vSwitchId)
                .append("privateIpAddress", privateIpAddress)
                .append("targetDedicatedHostIdForLog", targetDedicatedHostIdForLog)
                .append("autoRenew", autoRenew)
                .append("roleARN", roleARN)
                .append("zoneId", zoneId)
                .append("instanceNetworkType", instanceNetworkType)
                .append("connectionMode", connectionMode)
                .append("clientToken", clientToken)
                .append("targetDedicatedHostIdForSlave", targetDedicatedHostIdForSlave)
                .append("engine", engine)
                .append("dBInstanceStorageType", dBInstanceStorageType)
                .append("dedicatedHostGroupId", dedicatedHostGroupId)
                .append("dBInstanceNetType", dBInstanceNetType)
                .append("usedTime", usedTime)
                .append("vPCId", vPCId)
                .append("category", category)
                .append("payType", payType)
                .append("dBIsIgnoreCase", dBIsIgnoreCase)
                .toString();
    }

    @Override
    public void adaptToAliCloud() throws PluginException {
        if (!StringUtils.isEmpty(this.getPeriod())) {
            this.setPeriod(StringUtils.capitalize(this.getPeriod()));
        }

        if (!StringUtils.isEmpty(this.getZoneId())) {
            String resultZoneId;
            final List<String> strings = PluginStringUtils.splitStringList(this.getZoneId());
            if (StringUtils.equalsIgnoreCase(RDSCategory.Basic.toString(), this.getCategory())) {
                // basic RDS category
                if (isValidBasicZoneId(this.getZoneId())) {
                    resultZoneId = this.getZoneId();
                } else {
                    if (strings.size() != 1) {
                        throw new PluginException("RDS basic category support one zone only.");
                    } else {
                        final String rawStr = strings.get(0);
                        resultZoneId = removeMAZField(rawStr);
                    }
                }
            } else {
                // other RDS categories
                if (isValidMAZZoneId(this.getZoneId())) {
                    resultZoneId = this.getZoneId();
                } else {
                    resultZoneId = concatHighAvailableZoneId(strings);
                }
            }
            this.setZoneId(resultZoneId);
        }

        if (!StringUtils.isEmpty(this.getSecurityIPList()) && PluginStringUtils.isListStr(this.getSecurityIPList())) {
            this.setSecurityIPList(PluginStringUtils.removeSquareBracket(this.getSecurityIPList()));
        }

        if (!StringUtils.isEmpty(this.getEngine())) {
            final RDSResourceSeeker.RDSEngine engineType = EnumUtils.getEnumIgnoreCase(RDSResourceSeeker.RDSEngine.class, this.getEngine());
            if (null == engineType) {
                throw new PluginException("Invalid engine type.");
            }
            switch (engineType) {
                case MARIADB:
                    this.setEngine(RDSResourceSeeker.RDSEngine.MARIADB.getEngine());
                    break;
                case MYSQL:
                    this.setEngine(RDSResourceSeeker.RDSEngine.MYSQL.getEngine());
                    break;
                default:
                    break;
            }
        }

        if (!StringUtils.isEmpty(this.getPayType())) {
            this.setPayType(StringUtils.capitalize(this.getPayType().toLowerCase()));
        }

        if (!StringUtils.isEmpty(this.getPeriod())) {
            this.setPeriod(StringUtils.capitalize(this.getPeriod().toLowerCase()));
        }
    }

    /**
     * Remove MAZ field from given zoneId
     * example:
     * ap-southeast-MAZ2-b -> ap-southeast-2b
     *
     * @param s rawZoneId string
     * @return result string
     * @throws PluginException plugin exception
     */
    private String removeMAZField(String s) throws PluginException {
        final Pattern pattern = Pattern.compile(WECUBE_HIGH_AVAILABLE_ZONE_PATTERN, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(s);
        String result = StringUtils.EMPTY;

        /*
         * ap-southeast-1MAZ2-a
         * group 0: full word
         * group 1: ap-southeast-1
         * group 2: MAZ
         * group 3: 2
         * group 4: a
         */
        while (matcher.find()) {
            try {
                result = result.concat(matcher.group(1)).concat(matcher.group(4));
            } catch (IndexOutOfBoundsException ex) {
                throw new PluginException(ex.getMessage());
            }
        }
        return result;
    }

    /**
     * Concat dhigh available zone ID to alicloud's requirement
     * example:
     * [ap-southeast-1MAZ2-a, ap-southeast-1MAZ2-b] -> ap-southeast-1MAZ2(a,b)
     *
     * @param rawZoneStringList raw zone string list
     * @return concat result
     * @throws PluginException plugin exception
     */
    private String concatHighAvailableZoneId(List<String> rawZoneStringList) throws PluginException {
        final Pattern pattern = Pattern.compile(WECUBE_HIGH_AVAILABLE_ZONE_PATTERN, Pattern.MULTILINE);
        Set<String> prefixSet = new HashSet<>();
        Set<String> indexSet = new HashSet<>();
        Set<String> postFixSet = new HashSet<>();
        // find prefix, store postfix
        for (String rawStr : rawZoneStringList) {
            /*
             * ap-southeast-1MAZ2-a
             * group 0: full word
             * group 1: ap-southeast-1
             * group 2: MAZ
             * group 3: 2
             * group 4: a
             */
            final Matcher matcher = pattern.matcher(rawStr);
            while (matcher.find()) {
                try {
                    prefixSet.add(matcher.group(1).concat(matcher.group(2).toUpperCase()));
                    indexSet.add(matcher.group(3));
                    postFixSet.add(matcher.group(4));
                } catch (IndexOutOfBoundsException ex) {
                    throw new PluginException(ex.getMessage());
                }
            }
        }

        if (prefixSet.size() != 1) {
            throw new PluginException("Given multiple prefixes while handling zoneId.");
        }

        if (indexSet.size() != 1) {
            throw new PluginException("Given multiple indexes while handling zoneId");
        }
        List<String> postFixList = new ArrayList<>(postFixSet);
        Collections.sort(postFixList);

        // assembling postfix
        StringJoiner joiner = new StringJoiner(",", "(", ")");

        for (String postFix : postFixList) {
            joiner.add(postFix);
        }

        String prefix = prefixSet.iterator().next();
        String index = indexSet.iterator().next();
        return prefix.concat(index).concat(joiner.toString());
    }

    private boolean isValidBasicZoneId(String zoneId) {
        return !StringUtils.containsIgnoreCase(zoneId, "MAZ");
    }

    private boolean isValidMAZZoneId(String rawString) {
        return rawString.matches(ALICLOUD_HIGH_AVAILABLE_ZONE_PATTERN);
    }

}
