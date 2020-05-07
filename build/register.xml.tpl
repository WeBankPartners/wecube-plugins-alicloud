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
                    <!--<parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">resourceGroupId</parameter>-->
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
            <interface action="create_with_route_table" path="/alicloud/v1/vswitch/create/route_table/bind">
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
            <interface action="delete_with_route_table" path="/alicloud/v1/vswitch/delete/route_table/unbind">
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
        <plugin name="RouteEntry">
            <interface action="create_route_entry" path="/alicloud/v1/route_table/route_entry/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">destinationCidrBlock</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">routeTableId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">nextHopId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">nextHopType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">routeEntryName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete_route_entry" path="/alicloud/v1/route_table/route_entry/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">destinationCidrBlock</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">routeTableId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">nextHopId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="VM">
            <interface action="create_vm" path="/alicloud/v1/vm/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">seed</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">instanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceSpec</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">imageId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">securityGroupId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">instanceName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">autoRenew</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">autoRenewPeriod</parameter>
                    <!--<parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">hostName</parameter>-->
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
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">cpu</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">memory</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">privateIp</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete_vm" path="/alicloud/v1/vm/delete">
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
            <interface action="start_vm" path="/alicloud/v1/vm/start">
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
            <interface action="stop_vm" path="/alicloud/v1/vm/stop">
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
            <interface action="bind_security_group" path="/alicloud/v1/vm/security-group/bind">
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
            <interface action="unbind_security_group" path="/alicloud/v1/vm/security-group/unbind">
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
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">securityGroupId</parameter>
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
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">dBName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">backupStrategy</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">backupMethod</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">backupType</parameter>
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
        <plugin name="Redis">
            <interface action="create_redis" path="/alicloud/v1/redis/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">seed</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">instanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">instanceName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">password</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">capacity</parameter>
                    <!--<parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceClass</parameter>-->
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">zoneId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">chargeType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">engineVersion</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">securityIps</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">securityGroupId</parameter>
                    <!--<parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">seriesType</parameter>-->
                    <!--<parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">architecture</parameter>-->
                    <!--<parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">shardNumber</parameter>-->
                    <!--<parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">supportedNodeType</parameter>-->
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">networkType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vpcId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vSwitchId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">period</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">srcDBInstanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">backupId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">privateIpAddress</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">autoRenew</parameter>
                    <!--<parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">resourceGroupId</parameter>-->
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">instanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">password</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">port</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">privateIpAddr</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete_redis" path="/alicloud/v1/redis/delete">
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
        </plugin>
        <plugin name="LoadBalancer">
            <interface action="create_lb" path="/alicloud/v1/load_balancer/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">loadBalancerId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">addressType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">internetChargeType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">bandWidth</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">loadBalancerName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vpcId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vSwitchId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">masterZoneId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">slaveZoneId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">loadBalancerSpec</parameter>
                    <!--<parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">resourceGroupId</parameter>-->
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">payType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">pricingCycle</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">duration</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">address</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">loadBalancerId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">address</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete_lb" path="/alicloud/v1/load_balancer/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">loadBalancerId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="LoadBalancerTarget">
            <interface action="add_backend_server" path="/alicloud/v1/load_balancer/backend_server/add">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">loadBalancerId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">hostIds</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">hostPorts</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vServerGroupName</parameter>
                    <!-- listener configurations -->
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">bandwidth</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">listenerPort</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">listenerProtocol</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">vServerGroupId</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="remove_backend_server" path="/alicloud/v1/load_balancer/backend_server/remove">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">loadBalancerId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">vServerGroupId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">hostIds</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">hostPorts</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">listenerPort</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">listenerProtocol</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">deleteListener</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="Disk">
            <interface action="create_attach_disk" path="/alicloud/v1/disk/create_attach">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <!-- instance password -->
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">seed</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">password</parameter>
                    <!-- attach disk to instance -->
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">fileSystemType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">mountDir</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceId</parameter>
                    <!-- create disk -->
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">diskId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">zoneId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">diskName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">size</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">diskCategory</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">diskId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">volumeName</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="detach_delete_disk" path="/alicloud/v1/disk/detach_delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">diskId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">seed</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">unmountDir</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">password</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">volumeName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="CloudEnterpriseNetwork">
            <interface action="create_cen" path="/alicloud/v1/cen/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">cenId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">name</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">cenId</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete_cen" path="/alicloud/v1/cen/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cenId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="attach_cen" path="/alicloud/v1/cen/attach">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cenId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">childInstanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">childInstanceRegionId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">childInstanceType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">childInstanceOwnerId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="detach_cen" path="/alicloud/v1/cen/detach">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cenId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">childInstanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">childInstanceRegionId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">childInstanceType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">childInstanceOwnerId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">cenOwnerId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="SecurityGroup">
            <interface action="create_security_group" path="/alicloud/v1/security_group/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">securityGroupId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vpcId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">description</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">securityGroupName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">securityGroupType</parameter>
                    <!--<parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">resourceGroupId</parameter>-->
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">securityGroupId</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete_security_group" path="/alicloud/v1/security_group/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">securityGroupId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="SecurityPolicy">
            <interface action="authorize_security_policy" path="/alicloud/v1/security_group/authorize">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">actionType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cidrIp</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">ipProtocol</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">portRange</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">securityGroupId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">policy</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="revoke_security_policy" path="/alicloud/v1/security_group/revoke">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">actionType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cidrIp</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">ipProtocol</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">portRange</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">securityGroupId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">policy</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">description</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="NatGateway">
            <interface action="create_nat_gateway" path="/alicloud/v1/vpc/nat/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">natGatewayId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">vpcId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">name</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">description</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">spec</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">instanceChargeType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">pricingCycle</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">duration</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">vSwitchId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">natGatewayId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">snatTableId</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete_nat_gateway" path="/alicloud/v1/vpc/nat/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">natGatewayId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="NatSnat">
            <interface action="create_snat_entry" path="/alicloud/v1/vpc/nat/snat_entry/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">snatIp</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">snatTableId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">natId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">sourceVSwitchId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">sourceCIDR</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">snatEntryName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">snatEntryId</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="delete_snat_entry" path="/alicloud/v1/vpc/nat/snat_entry/delete">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">snatEntryId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">snatTableId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">natId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
        <plugin name="EIP">
            <interface action="allocate_eip" path="/alicloud/v1/vpc/eip/allocate">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">allocationId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">cbpName</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">bandwidth</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">instanceChargeType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">internetChargeType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">period</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">pricingCycle</parameter>
                    <!--<parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">resourceGroupId</parameter>-->
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">allocationId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">eipAddress</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">cbpId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">cbpName</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="release_eip" path="/alicloud/v1/vpc/eip/release">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">allocationId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">cbpName</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="associate_instance" path="/alicloud/v1/vpc/eip/associate">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">allocationId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceType</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">instanceRegionId</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
            <interface action="unassociate_instance" path="/alicloud/v1/vpc/eip/un-associate">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identityParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloudParams</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">allocationId</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">instanceId</parameter>
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


