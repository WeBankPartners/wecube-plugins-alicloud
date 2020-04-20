<?xml version="1.0" encoding="UTF-8"?>
<package name="alicloud" version="{{PLUGIN_VERSION}}">
    <!-- 1.依赖分析 - 描述运行本插件包需要的其他插件包 -->
    <packageDependencies>
        <packageDependency name="wecmdb" version="v1.4.0"/>
    </packageDependencies>

    <!-- 2.菜单注入 - 描述运行本插件包需要注入的菜单 -->
    <menus>
    </menus>

    <!-- 3.数据模型 - 描述本插件包的数据模型,并且描述和Framework数据模型的关系 -->
    <dataModel>
    </dataModel>

    <!-- 4.系统参数 - 描述运行本插件包需要的系统参数 -->
    <systemParameters>
    </systemParameters>

    <!-- 5.权限设定 -->
    <authorities>
    </authorities>

    <!-- 6.运行资源 - 描述部署运行本插件包需要的基础资源(如主机、虚拟机、容器、数据库等) -->
    <resourceDependencies>
        <docker imageName="{{IMAGENAME}}" containerName="{{CONTAINERNAME}}" portBindings="{{PORTBINDINGS}}" volumeBindings="/etc/localtime:/etc/localtime,{{BASE_MOUNT_PATH}}/alicloud/logs:/logs" envVariables=""/>
    </resourceDependencies>

    <!-- 7.插件列表 - 描述插件包中单个插件的输入和输出 -->
    <plugins>
        <plugin name="VPC">
            <interface action="create" path="/alicloud/v1/vpc/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cidrBlock</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vpcId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">vpcName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">description</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">resourceGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">routeTableId</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vpc/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">vpcId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="VSwitch">
            <interface action="create" path="/alicloud/v1/vswitch/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cidrBlock</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vSwitchId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">vpcId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">zoneId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">description</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vSwitchName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">routeTableId</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vswitch/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">vSwitchId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="RouteTable">
            <interface action="create" path="/alicloud/v1/route_table/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">vpcId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">routeTableName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">routeTableId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">routeTableId</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/route_table/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">routeTableId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="associate_vswitch" path="/alicloud/v1/route_table/vswitch/associate">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">routeTableId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">vSwitchId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="VM">
            <interface action="create" path="/alicloud/v1/vm/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">seed</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">instanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceSpec</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">imageId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">securityGroupId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">instanceName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">autoRenew</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">autoRenewPeriod</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">hostName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">password</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">zoneId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">systemDiskSize</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">systemDiskCategory</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vSwitchId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">privateIpAddress</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">instanceChargeType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">period</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">periodUnit</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">password</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">instanceSpec</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vm/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="start" path="/alicloud/v1/vm/start">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="stop" path="/alicloud/v1/vm/stop">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">forceStop</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="bind security group" path="/alicloud/v1/vm/security-group/bind">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="RDS">
            <interface action="create_db" path="/alicloud/v1/rds/db/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <!-- account info-->
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">seed</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">accountName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">accountPassword</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">accountDescription</parameter>
                    <!-- db info-->
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">dBInstanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">dBInstanceSpec</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">dBInstanceClass</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">dBInstanceStorage</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">engine</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">engineVersion</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">payType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">securityIPList</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">dBInstanceDescription</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">zoneId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vpcId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vSwitchId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">privateIpAddress</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">usedTime</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">period</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">dBInstanceStorageType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">autoRenew</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">category</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">dBParamGroupId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">dBTimeZone</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">dBIsIgnoreCase</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">accountName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">accountPassword</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">connectionString</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">port</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete_db" path="/alicloud/v1/rds/db/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">dBInstanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="modify_security_ip" path="/alicloud/v1/rds/security_ip/modify">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">dBInstanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">securityIps</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">dBInstanceIPArrayName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">whitelistNetworkType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">modifyMode</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="create_backup" path="/alicloud/v1/rds/backup/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">dBInstanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">securityIps</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">dBInstanceIPArrayName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">whitelistNetworkType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">modifyMode</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">backupId</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete_backup" path="/alicloud/v1/rds/backup/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">backupId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">dBInstanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
  </plugins>
</package>


