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
        <systemParameter name="ALICLOUD_AUTO_RENEW" scopeType="global" defaultValue="false"/>
        <systemParameter name="ALICLOUD_AUTO_RENEW_PERIOD" scopeType="global" defaultValue="3"/>
        <systemParameter name="ALICLOUD_DEFAULT_PERIOD_UNIT" scopeType="global" defaultValue="Month"/>
        <systemParameter name="ALICLOUD_MYSQL_LOWER_CASE_TABLE_NAMES" scopeType="global" defaultValue="1"/>
        <systemParameter name="ALICLOUD_DEFAULT_TIME_ZONE" scopeType="global" defaultValue="UTC+08:00"/>
        <systemParameter name="ALICLOUD_DEFAULT_SECURITY_POLICY_ACTION" scopeType="global" defaultValue="accept"/>
        <systemParameter name="ALICLOUD_DEFAULT_SECURITY_POLICY_TYPE" scopeType="global" defaultValue="egress"/>
        <systemParameter name="ALICLOUD_RDS_INSTANCE_BACKUP_STRATEGY" scopeType="global" defaultValue="instance"/>
        <systemParameter name="ALICLOUD_RDS_DATABASE_BACKUP_STRATEGY" scopeType="global" defaultValue="db"/>
        <systemParameter name="ALICLOUD_DEFAULT_RDS_BACKUP_METHOD" scopeType="global" defaultValue="logic"/>
        <systemParameter name="ALICLOUD_API_SECRET" scopeType="global" defaultValue="accessKeyId=;secret="/>
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
<plugin name="vpc" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/vpc/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cidrBlock</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">routeTableId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vpc/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="vswitch" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/vswitch/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cidrBlock</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">zoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">description</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="create-with-route-table" path="/alicloud/v1/vswitch/create/route_table/bind" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cidrBlock</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">zoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">description</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">routeTableId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vswitch/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete-with-route-table" path="/alicloud/v1/vswitch/delete/route_table/unbind" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="route-table" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/route_table/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">routeTableName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">routeTableId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">routeTableId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/route_table/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">routeTableId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="associate-vswitch" path="/alicloud/v1/route_table/vswitch/associate" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">routeTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
<plugin name="route-entry" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/route_table/route_entry/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">destinationCidrBlock</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">routeTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">nextHopId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">nextHopType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">routeEntryName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/route_table/route_entry/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">destinationCidrBlock</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">routeTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">nextHopId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
<plugin name="vm" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/vm/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">seed</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceSpec</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceFamily</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">imageId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">hostName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">autoRenew</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">autoRenewPeriod</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">password</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">zoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">systemDiskSize</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">systemDiskCategory</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">privateIpAddress</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceChargeType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">period</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">periodUnit</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">resourceTag</parameter>

                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">password</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceType</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cpu</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">memory</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">privateIp</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">hostName</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vm/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="start" path="/alicloud/v1/vm/start" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="stop" path="/alicloud/v1/vm/stop" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">forceStop</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="bind-security-group" path="/alicloud/v1/vm/security-group/bind" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="unbind-security-group" path="/alicloud/v1/vm/security-group/unbind" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
<plugin name="rds" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/rds/db/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <!-- account info-->
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">seed</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">accountName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">accountPassword</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">accountDescription</parameter>
                    <!-- db info-->
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceSpec</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceClass</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceStorage</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">engine</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">engineVersion</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">payType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityIPList</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceDescription</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">zoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">privateIpAddress</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">usedTime</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">period</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceStorageType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">autoRenew</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">category</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBTimeZone</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_MYSQL_LOWER_CASE_TABLE_NAMES">dBIsIgnoreCase</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">accountName</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">accountPassword</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceClass</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cpu</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">memory</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">connectionString</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">port</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">privateIpAddress</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/rds/db/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="append-security-group" path="/alicloud/v1/rds/security_group/append" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="remove-security-group" path="/alicloud/v1/rds/security_group/remove" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="modify-security-ip" path="/alicloud/v1/rds/security_ip/modify" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityIps</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceIPArrayName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">whitelistNetworkType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">modifyMode</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="append-security-ip" path="/alicloud/v1/rds/security_ip/append" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityIps</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceIPArrayName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">whitelistNetworkType</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete-security-ip" path="/alicloud/v1/rds/security_ip/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityIps</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceIPArrayName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">whitelistNetworkType</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="create-backup" path="/alicloud/v1/rds/backup/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">backupStrategy</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">backupMethod</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">backupType</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">backupId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete-backup" path="/alicloud/v1/rds/backup/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">backupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="redis" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/redis/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">seed</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">password</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">capacity</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">zoneId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">chargeType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">engineVersion</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityIps</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">networkType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">period</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">srcDBInstanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">backupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">privateIpAddress</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">autoRenew</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">password</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">port</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">privateIpAddr</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/redis/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="append-security-group" path="/alicloud/v1/redis/security_group/append" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="remove-security-group" path="/alicloud/v1/redis/security_group/remove" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="load-balancer" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/load_balancer/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">loadBalancerId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">addressType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">internetChargeType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">bandWidth</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">loadBalancerName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">masterZoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">slaveZoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">loadBalancerSpec</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">payType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">pricingCycle</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">duration</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">address</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">loadBalancerId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">address</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/load_balancer/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">loadBalancerId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
<plugin name="load-balancer-target" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="add" path="/alicloud/v1/load_balancer/backend_server/add" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">loadBalancerId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">hostIds</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">hostPorts</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vServerGroupName</parameter>
                    <!-- listener configurations -->
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">bandwidth</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">listenerPort</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">listenerProtocol</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vServerGroupId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="remove" path="/alicloud/v1/load_balancer/backend_server/remove" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">loadBalancerId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vServerGroupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">hostIds</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">hostPorts</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">listenerPort</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">listenerProtocol</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">deleteListener</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="disk" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create-attach" path="/alicloud/v1/disk/create_attach" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <!-- instance password -->
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">seed</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceGuid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">password</parameter>
                    <!-- attach disk to instance -->
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">fileSystemType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">mountDir</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <!-- create disk -->
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">diskId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">zoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">diskName</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">size</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">diskCategory</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">diskId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">volumeName</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="detach-delete" path="/alicloud/v1/disk/detach_delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">diskId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">seed</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceGuid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">unmountDir</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">password</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">volumeName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
                <plugin name="cloud-enterprise-network" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/cen/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cenId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">name</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cenId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/cen/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cenId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="attach" path="/alicloud/v1/cen/attach" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cenId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">childInstanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">childInstanceRegionId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">childInstanceType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">childInstanceOwnerId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="detach" path="/alicloud/v1/cen/detach" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cenId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">childInstanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">childInstanceRegionId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">childInstanceType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">childInstanceOwnerId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cenOwnerId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="security-group" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/security_group/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">description</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupType</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/security_group/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
<plugin name="security-policy" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="authorize" path="/alicloud/v1/security_group/authorize" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">actionType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cidrIp</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">ipProtocol</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">portRange</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">policy</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="revoke" path="/alicloud/v1/security_group/revoke" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">actionType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cidrIp</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">ipProtocol</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">portRange</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">policy</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
         <plugin name="nat-gateway" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/vpc/nat/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">natGatewayId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">name</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">description</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">spec</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceChargeType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">pricingCycle</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">duration</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">vSwitchId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">natGatewayId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatTableId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vpc/nat/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">natGatewayId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
                <plugin name="nat-snat" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/vpc/nat/snat_entry/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatIp</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">natId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">sourceVSwitchId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">sourceCIDR</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatEntryName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatEntryId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vpc/nat/snat_entry/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatEntryId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">natId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="append-eip" path="/alicloud/v1/vpc/nat/snat_entry/modify/append" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatEntryId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">natId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatIp</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="remove-eip" path="/alicloud/v1/vpc/nat/snat_entry/modify/remove" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatEntryId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">natId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatIp</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="eip" targetPackage="" targetEntity="" registerName="" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/vpc/eip/allocate" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">allocationId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cbpName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">bandwidth</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceChargeType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">internetChargeType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">period</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">pricingCycle</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">allocationId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">eipAddress</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cbpId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cbpName</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vpc/eip/release" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">allocationId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cbpName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="associate" path="/alicloud/v1/vpc/eip/associate" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">allocationId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceRegionId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="unassociate" path="/alicloud/v1/vpc/eip/un-associate" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">allocationId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        
        
        
        <!--最佳实践-->
        <plugin name="vpc" targetPackage="wecmdb" targetEntity="network_segment" registerName="network_segment" targetEntityFilterRule="{network_segment_usage eq 'VPC'}">
            <interface action="create" path="/alicloud/v1/vpc/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.code">cidrBlock</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.name">vpcName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.description">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.route_table_asset_id">routeTableId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vpc/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>

        <plugin name="vswitch" targetPackage="wecmdb" targetEntity="network_segment" registerName="network_segment" targetEntityFilterRule="{network_segment_usage eq 'SUBNET'}">
            <interface action="create" path="/alicloud/v1/vswitch/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}{private_route_table eq 'N'}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.code">cidrBlock</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.subnet_asset_id">vSwitchId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.f_network_segment>wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.available_zone">zoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.description">description</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.name">vSwitchName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.subnet_asset_id">vSwitchId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="create-with-route-table" path="/alicloud/v1/vswitch/create/route_table/bind" filterRule="{state_code eq 'created'}{fixed_date is NULL}{private_route_table eq 'Y'}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.code">cidrBlock</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.subnet_asset_id">vSwitchId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.f_network_segment>wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.available_zone">zoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.description">description</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.name">vSwitchName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.subnet_asset_id">vSwitchId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.route_table_asset_id">routeTableId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vswitch/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}{private_route_table eq 'N'}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.subnet_asset_id">vSwitchId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete-with-route-table" path="/alicloud/v1/vswitch/delete/route_table/unbind" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}{private_route_table eq 'Y'}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.subnet_asset_id">vSwitchId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="route-entry" targetPackage="wecmdb" targetEntity="route" registerName="route" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/route_table/route_entry/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.owner_network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.dest_network_segment>wecmdb:network_segment.code">destinationCidrBlock</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.owner_network_segment>wecmdb:network_segment.route_table_asset_id">routeTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.network_link>wecmdb:network_link.asset_id">nextHopId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.network_link>wecmdb:network_link.network_link_type>wecmdb:network_link_type.code">nextHopType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.description">routeEntryName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/route_table/route_entry/delete" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.owner_network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.dest_network_segment>wecmdb:network_segment.code">destinationCidrBlock</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.owner_network_segment>wecmdb:network_segment.route_table_asset_id">routeTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.network_link>wecmdb:network_link.asset_id">nextHopId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:route.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="vm" targetPackage="wecmdb" targetEntity="host_resource_instance" registerName="resource" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/vm/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ENCRYPT_SEED">seed</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.asset_id">instanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.resource_instance_spec>wecmdb:resource_instance_spec.code">instanceSpec</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.resource_instance_type>wecmdb:resource_instance_type.code">instanceFamily</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.resource_instance_system>wecmdb:resource_instance_system.code">imageId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.f_network_segment>wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.name">hostName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.key_name">instanceName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_AUTO_RENEW">autoRenew</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_AUTO_RENEW_PERIOD">autoRenewPeriod</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.user_password">password</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.available_zone">zoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.storage">systemDiskSize</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.storage_type>wecmdb:storage_type.code">systemDiskCategory</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.subnet_asset_id">vSwitchId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.NONE">privateIpAddress</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.charge_type>wecmdb:charge_type.code">instanceChargeType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.billing_cycle">period</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_DEFAULT_PERIOD_UNIT">periodUnit</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.NONE">resourceTag</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.asset_id">instanceId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.user_password">password</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">instanceType</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.cpu">cpu</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.memory">memory</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.ip_address">privateIp</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">hostName</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vm/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.asset_id">instanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="start" path="/alicloud/v1/vm/start" filterRule="{state_code eq 'startup'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.asset_id">instanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="stop" path="/alicloud/v1/vm/stop" filterRule="{state_code eq 'stoped'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:host_resource_instance.asset_id">instanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">forceStop</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="vm" targetPackage="wecmdb" targetEntity="app_instance" registerName="app_deploy" targetEntityFilterRule="">
            <interface action="bind_sg_app_created" path="/alicloud/v1/vm/security-group/bind" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:app_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:app_instance.host_resource_instance>wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:app_instance.host_resource_instance>wecmdb:host_resource_instance.asset_id">instanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:app_instance.unit>wecmdb:unit.security_group_id">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:app_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="unbind_sg_app_deleted" path="/alicloud/v1/vm/security-group/unbind" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:app_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:app_instance.host_resource_instance>wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:app_instance.host_resource_instance>wecmdb:host_resource_instance.asset_id">instanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:app_instance.unit>wecmdb:unit.security_group_id">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:app_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="rds" targetPackage="wecmdb" targetEntity="rdb_resource_instance" registerName="resource" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/rds/db/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <!-- account info-->
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ENCRYPT_SEED">seed</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.user_name">accountName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.user_password">accountPassword</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.user_name">accountDescription</parameter>
                    <!-- db info-->
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.f_network_segment>wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.asset_id">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.resource_instance_spec>wecmdb:resource_instance_spec.code">dBInstanceSpec</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.NONE">dBInstanceClass</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.storage">dBInstanceStorage</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.resource_set>wecmdb:resource_set.unit_type>wecmdb:unit_type.code">engine</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.resource_instance_system>wecmdb:resource_instance_system.code">engineVersion</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.charge_type>wecmdb:charge_type.code">payType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.code">securityIPList</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.description">dBInstanceDescription</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.resource_set>wecmdb:resource_set.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.available_zone">zoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.f_network_segment>wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.subnet_asset_id">vSwitchId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.NONE">privateIpAddress</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.billing_cycle">usedTime</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_DEFAULT_PERIOD_UNIT">periodUnit</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.storage_type>wecmdb:storage_type.code">dBInstanceStorageType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_AUTO_RENEW">autoRenew</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.resource_instance_type>wecmdb:resource_instance_type.code">category</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_DEFAULT_TIME_ZONE">dBTimeZone</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_MYSQL_LOWER_CASE_TABLE_NAMES">dBIsIgnoreCase</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.user_name">accountName</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.user_password">accountPassword</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">dBInstanceClass</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.cpu">cpu</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.memory">memory</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.asset_id">dBInstanceId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">connectionString</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.login_port">port</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.ip_address">privateIpAddress</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/rds/db/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.asset_id">dBInstanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="append-security-group" path="/alicloud/v1/rds/security_group/append" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.asset_id">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.f_network_segment>wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="remove-security-group" path="/alicloud/v1/rds/security_group/remove" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.asset_id">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.f_network_segment>wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="create-backup" path="/alicloud/v1/rds/backup/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.asset_id">dBInstanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.NONE">dBName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_RDS_INSTANCE_BACKUP_STRATEGY">backupStrategy</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="ALICLOUD_DEFAULT_RDS_BACKUP_METHOD">backupMethod</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="wecmdb:rdb_resource_instance.NONE">backupType</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.backup_asset_id">backupId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete-backup" path="/alicloud/v1/rds/backup/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.backup_asset_id">backupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.asset_id">dBInstanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="rds" targetPackage="wecmdb" targetEntity="rdb_instance" registerName="database" targetEntityFilterRule="">
            <interface action="create-deploy-backup" path="/alicloud/v1/rds/backup/create" filterRule="{state_code eq 'changed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.rdb_resource_instance>wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.rdb_resource_instance>wecmdb:rdb_resource_instance.asset_id">dBInstanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.name">dBName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_RDS_DATABASE_BACKUP_STRATEGY">backupStrategy</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="ALICLOUD_DEFAULT_RDS_BACKUP_METHOD">backupMethod</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="wecmdb:rdb_resource_instance.NONE">backupType</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.deploy_backup_asset_id">backupId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete-deploy-backup" path="/alicloud/v1/rds/backup/delete" filterRule="{state_code eq 'changed'}{fixed_date isnot NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.rdb_resource_instance>wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.deploy_backup_asset_id">backupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.rdb_resource_instance>wecmdb:rdb_resource_instance.asset_id">dBInstanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="create-regular-backup" path="/alicloud/v1/rds/backup/create" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.rdb_resource_instance>wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.rdb_resource_instance>wecmdb:rdb_resource_instance.asset_id">dBInstanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.name">dBName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_RDS_DATABASE_BACKUP_STRATEGY">backupStrategy</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="ALICLOUD_DEFAULT_RDS_BACKUP_METHOD">backupMethod</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="wecmdb:rdb_resource_instance.NONE">backupType</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.regular_backup_asset_id">backupId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete-regular-backup" path="/alicloud/v1/rds/backup/delete" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.rdb_resource_instance>wecmdb:rdb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance._backup_asset_id">backupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.rdb_resource_instance>wecmdb:rdb_resource_instance.asset_id">dBInstanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:rdb_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        
        <plugin name="redis" targetPackage="wecmdb" targetEntity="cache_resource_instance" registerName="resource" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/redis/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ENCRYPT_SEED">seed</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.asset_id">instanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.key_name">instanceName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.user_password">password</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.resource_instance_spec>wecmdb:resource_instance_spec.code">capacity</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.resource_set>wecmdb:resource_set.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.available_zone">zoneId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.charge_type>wecmdb:charge_type.code">chargeType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.resource_instance_system>wecmdb:resource_instance_system.code">engineVersion</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.network_segment>wecmdb:network_segment.code">securityIps</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.network_segment>wecmdb:network_segment.f_network_segment>wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.NONE">networkType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.network_segment>wecmdb:network_segment.f_network_segment>wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.network_segment>wecmdb:network_segment.subnet_asset_id">vSwitchId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.billing_cycle">period</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.NONE">srcDBInstanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.NONE">backupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.NONE">privateIpAddress</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_AUTO_RENEW">autoRenew</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.asset_id">instanceId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.user_password">password</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.login_port">port</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.ip_address">privateIpAddr</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/redis/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.asset_id">instanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="append-security-group" path="/alicloud/v1/redis/security_group/append" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.asset_id">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.network_segment>wecmdb:network_segment.f_network_segment>wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="remove-security-group" path="/alicloud/v1/redis/security_group/remove" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.asset_id">dBInstanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.network_segment>wecmdb:network_segment.f_network_segment>wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:cache_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="load-balancer" targetPackage="wecmdb" targetEntity="lb_resource_instance" registerName="resource" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/load_balancer/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.asset_id">loadBalancerId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.resource_instance_type>wecmdb:resource_instance_type.code">addressType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">internetChargeType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="">bandWidth</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.key_name">loadBalancerName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.network_segment>wecmdb:network_segment.f_network_segment>wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.network_segment>wecmdb:network_segment.subnet_asset_id">vSwitchId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.available_zone">masterZoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.NONE">slaveZoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.NONE">loadBalancerSpec</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.charge_type>wecmdb:charge_type.code">payType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_DEFAULT_PERIOD_UNIT">pricingCycle</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.billing_cycle">duration</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.NONE">address</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.asset_id">loadBalancerId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.ip_address">address</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/load_balancer/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.asset_id">loadBalancerId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_resource_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="load-balancer-target" targetPackage="wecmdb" targetEntity="lb_instance" registerName="app" targetEntityFilterRule="">
            <interface action="add" path="/alicloud/v1/load_balancer/backend_server/add" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.lb_resource_instance>wecmdb:lb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.lb_resource_instance>wecmdb:lb_resource_instance.asset_id">loadBalancerId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.unit>wecmdb:unit~(invoke_unit)wecmdb:invoke.invoked_unit>wecmdb:unit~(unit)wecmdb:app_instance.host_resource_instance>wecmdb:host_resource_instance.asset_id">hostIds</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.unit>wecmdb:unit~(invoke_unit)wecmdb:invoke.invoked_unit>wecmdb:unit~(unit)wecmdb:app_instance.port">hostPorts</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.name">vServerGroupName</parameter>
                    <!-- listener configurations -->
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">bandwidth</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.port">listenerPort</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.unit>wecmdb:unit.unit_design>wecmdb:unit_design.protocol">listenerProtocol</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.lb_listener_asset_id">vServerGroupId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="remove" path="/alicloud/v1/load_balancer/backend_server/remove" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.lb_resource_instance>wecmdb:lb_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.lb_resource_instance>wecmdb:lb_resource_instance.asset_id">loadBalancerId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.lb_listener_asset_id">vServerGroupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.unit>wecmdb:unit~(invoke_unit)wecmdb:invoke.invoked_unit>wecmdb:unit~(unit)wecmdb:app_instance.host_resource_instance>wecmdb:host_resource_instance.asset_id">hostIds</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.unit>wecmdb:unit~(invoke_unit)wecmdb:invoke.invoked_unit>wecmdb:unit~(unit)wecmdb:app_instance.port">hostPorts</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.port">listenerPort</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.unit>wecmdb:unit.unit_design>wecmdb:unit_design.protocol">listenerProtocol</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">deleteListener</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:lb_instance.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="disk" targetPackage="wecmdb" targetEntity="block_storage" registerName="block_storage" targetEntityFilterRule="">
            <interface action="create_attach" path="/alicloud/v1/disk/create_attach" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.host_resource_instance>wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.guid">guid</parameter>
                    <!-- instance password -->
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ENCRYPT_SEED">seed</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.host_resource_instance>wecmdb:host_resource_instance.guid">instanceGuid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.host_resource_instance>wecmdb:host_resource_instance.user_password">password</parameter>
                    <!-- attach disk to instance -->
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.file_system">fileSystemType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.mount_point">mountDir</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.host_resource_instance>wecmdb:host_resource_instance.asset_id">instanceId</parameter>
                    <!-- create disk -->
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.asset_id">diskId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.host_resource_instance>wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.available_zone">zoneId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.name">diskName</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.disk_size">size</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.storage_type>wecmdb:storage_type.code">diskCategory</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.description">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.asset_id">diskId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.code">volumeName</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="detach_delete" path="/alicloud/v1/disk/detach_delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.host_resource_instance>wecmdb:host_resource_instance.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.asset_id">diskId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.host_resource_instance>wecmdb:host_resource_instance.asset_id">instanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ENCRYPT_SEED">seed</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.host_resource_instance>wecmdb:host_resource_instance.guid">instanceGuid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.mount_point">unmountDir</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.host_resource_instance>wecmdb:host_resource_instance.user_password">password</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.code">volumeName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:block_storage.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="cloud-enterprise-network" targetPackage="wecmdb" targetEntity="data_center" registerName="enterprise" targetEntityFilterRule="{data_center_type eq 'ENTERPRISE'}">
            <interface action="create" path="/alicloud/v1/cen/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:data_center.guid">guid</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:data_center.cen_asset_id">cenId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:data_center.name">name</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:data_center.description">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:data_center.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:data_center.cen_asset_id">cenId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/cen/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:data_center.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:data_center.cen_asset_id">cenId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:data_center.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="cloud-enterprise-network" targetPackage="wecmdb" targetEntity="network_segment" registerName="vpc" targetEntityFilterRule="{network_segment_usage eq 'VPC'}">
            <interface action="attach" path="/alicloud/v1/cen/attach" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.region_data_center>wecmdb:data_center.cen_asset_id">cenId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.vpc_asset_id">childInstanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.region">childInstanceRegionId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.network_segment_type">childInstanceType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.cloud_uid">childInstanceOwnerId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="detach" path="/alicloud/v1/cen/detach" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.region_data_center>wecmdb:data_center.cen_asset_id">cenId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.vpc_asset_id">childInstanceId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.region">childInstanceRegionId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.network_segment_type">childInstanceType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.cloud_uid">childInstanceOwnerId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.NONE">cenOwnerId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>

        <plugin name="security-group" targetPackage="wecmdb" targetEntity="network_segment" registerName="vpc" targetEntityFilterRule="{network_segment_usage eq 'VPC'}">
            <interface action="create" path="/alicloud/v1/security_group/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}{private_security_group eq 'Y'}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.description">description</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.name">securityGroupName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.NONE">securityGroupType</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/security_group/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}{private_security_group eq 'Y'}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="security-group" targetPackage="wecmdb" targetEntity="network_segment" registerName="subnet" targetEntityFilterRule="{network_segment_usage eq 'SUBNET'}">
            <interface action="create" path="/alicloud/v1/security_group/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}{private_security_group eq 'Y'}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.f_network_segment>wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.description">description</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.name">securityGroupName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.NONE">securityGroupType</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/security_group/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}{private_security_group eq 'Y'}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="security-group" targetPackage="wecmdb" targetEntity="unit" registerName="unit" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/security_group/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.resource_set>wecmdb:resource_set.business_zone>wecmdb:business_zone.network_zone>wecmdb:network_zone.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.guid">guid</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.resource_set>wecmdb:resource_set.business_zone>wecmdb:business_zone.network_zone>wecmdb:network_zone.network_segment>wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.description">description</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.key_name">securityGroupName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.NONE">securityGroupType</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/security_group/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.resource_set>wecmdb:resource_set.business_zone>wecmdb:business_zone.network_zone>wecmdb:network_zone.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.security_group_asset_id">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:unit.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="security-policy" targetPackage="wecmdb" targetEntity="default_security_policy" registerName="default" targetEntityFilterRule="">
            <interface action="authorize" path="/alicloud/v1/security_group/authorize" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.owner_network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.security_policy_type">actionType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.policy_network_segment>wecmdb:network_segment.code">cidrIp</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.protocol">ipProtocol</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.port">portRange</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.owner_network_segment>wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.security_policy_action">policy</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.description">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="revoke" path="/alicloud/v1/security_group/revoke" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.owner_network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.security_policy_type">actionType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.policy_network_segment>wecmdb:network_segment.code">cidrIp</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.protocol">ipProtocol</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.port">portRange</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.owner_network_segment>wecmdb:network_segment.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.security_policy_action">policy</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.description">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:default_security_policy.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>

        <plugin name="security-policy" targetPackage="wecmdb" targetEntity="invoke" registerName="app" targetEntityFilterRule="">
            <interface action="authorize" path="/alicloud/v1/security_group/authorize" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_DEFAULT_SECURITY_POLICY_TYPE">actionType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.invoked_unit>wecmdb:unit~(unit)cmdb:app_instance.host_resource_instance>wecmdb:host_resource_instance.ip_address">cidrIp</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.invoked_unit>wecmdb:unit.protocol">ipProtocol</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.invoked_unit>wecmdb:unit~(unit)cmdb:app_instance.port">portRange</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.invoke_unit>wecmdb:unit.security_group_asset_id">securityGroupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_DEFAULT_SECURITY_POLICY_ACTION">policy</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.description">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="revoke" path="/alicloud/v1/security_group/revoke" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_DEFAULT_SECURITY_POLICY_TYPE">actionType</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.invoked_unit>wecmdb:unit~(unit)cmdb:app_instance.host_resource_instance>wecmdb:host_resource_instance.ip_address">cidrIp</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.invoked_unit>wecmdb:unit.protocol">ipProtocol</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.invoked_unit>wecmdb:unit~(unit)cmdb:app_instance.port">portRange</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.invoke_unit>wecmdb:unit.security_group_asset_i">securityGroupId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_DEFAULT_SECURITY_POLICY_ACTION">policy</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.description">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:invoke.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>

        <plugin name="nat-gateway" targetPackage="wecmdb" targetEntity="network_link" registerName="network_link" targetEntityFilterRule="{code eq 'nat'}">
            <interface action="create" path="/alicloud/v1/vpc/nat/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.network_segment_2>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.asset_id">natGatewayId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.network_segment_2>wecmdb:network_segment.vpc_asset_id">vpcId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.key_name">name</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.description">description</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.NONE">spec</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.NONE">instanceChargeType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_DEFAULT_PERIOD_UNIT">pricingCycle</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.NONE">duration</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.subnet_network_segment>wecmdb:network_segment.subnet_asset_id">vSwitchId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.asset_id">natGatewayId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.snat_table_asset_id">snatTableId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vpc/nat/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.network_segment_2>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.asset_id">natGatewayId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_link.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>




        <plugin name="nat-snat" targetPackage="wecmdb" targetEntity="network_segment" registerName="subnet" targetEntityFilterRule="{network_segment_usage eq 'SUBNET'}">
            <interface action="create" path="/alicloud/v1/vpc/nat/snat_entry/create" filterRule="{state_code eq 'created'}{fixed_date is NULL}{private_nat eq 'Y'}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.f_network_segment>wecmdb:network_segment~(network_segment_2)wecmdb:network_link{code eq 'nat'}.internet_ip>wecmdb:ip_address.code">snatIp</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.f_network_segment>wecmdb:network_segment~(network_segment_2)wecmdb:network_link{code eq 'nat'}.snat_table_asset_id">snatTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.f_network_segment>wecmdb:network_segment~(network_segment_2)wecmdb:network_link{code eq 'nat'}.asset_id">natId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.subnet_asset_id">sourceVSwitchId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.code">sourceCIDR</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.name">snatEntryName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.nat_rule_asset_id">snatEntryId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vpc/nat/snat_entry/delete" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}{private_nat eq 'Y'}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.nat_rule_asset_id">snatEntryId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.f_network_segment>wecmdb:network_segment~(network_segment_2)wecmdb:network_link{code eq 'nat'}.snat_table_asset_id">snatTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.f_network_segment>wecmdb:network_segment~(network_segment_2)wecmdb:network_link{code eq 'nat'}.asset_id">natId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="append-eip" path="/alicloud/v1/vpc/nat/snat_entry/modify/append" filterRule="{state_code eq 'changed'}{fixed_date is NULL}{private_nat eq 'Y'}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatEntryId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">natId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatIp</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="remove-eip" path="/alicloud/v1/vpc/nat/snat_entry/modify/remove" filterRule="">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatEntryId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatTableId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">natId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="">snatIp</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:network_segment.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>

        <plugin name="eip" targetPackage="wecmdb" targetEntity="ip_address" registerName="nat_ip" targetEntityFilterRule="">
            <interface action="create" path="/alicloud/v1/vpc/eip/allocate" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.asset_id">allocationId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address~(internet_ip)wecmdb:network_link.key_name">cbpName</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address~(internet_ip)wecmdb:network_link.netband_width">bandwidth</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.NONE">instanceChargeType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.NONE">internetChargeType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.NONE">period</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.NONE">pricingCycle</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.asset_id">allocationId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.code">eipAddress</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address~(internet_ip)wecmdb:network_link.cbp_asset_id">cbpId</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">cbpName</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete" path="/alicloud/v1/vpc/eip/release" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.asset_id">allocationId</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address~(internet_ip)wecmdb:network_link.key_name">cbpName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="associate" path="/alicloud/v1/vpc/eip/associate" filterRule="{state_code eq 'created'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.asset_id">allocationId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address~(internet_ip)wecmdb:network_link.asset_id">instanceId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address~(internet_ip)wecmdb:network_link.code">instanceType</parameter>
                    <parameter datatype="string" required="N" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.region">instanceRegionId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="unassociate" path="/alicloud/v1/vpc/eip/un-associate" filterRule="{state_code eq 'destroyed'}{fixed_date is NULL}">
                <inputParameters>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.guid">guid</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="Y" mappingType="system_variable" mappingSystemVariableName="ALICLOUD_API_SECRET">identityParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.network_segment>wecmdb:network_segment.data_center>wecmdb:data_center.location">cloudParams</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.asset_id">allocationId</parameter>
                    <parameter datatype="string" required="Y" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address~(internet_ip)wecmdb:network_link.asset_id">instanceId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" sensitiveData="N" mappingType="entity" mappingEntityExpression="wecmdb:ip_address.guid">guid</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" sensitiveData="N" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
    </plugins>
</package>
