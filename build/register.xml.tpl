  
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
        <plugin name="vpc" targetPackage="wecmdb" targetEntity="network_zone">
            <interface action="create" path="/qcloud/v1/vpc/create">
                <inputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="wecmdb:network_zone.id" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="wecmdb:network_zone.data_center>wecmdb:data_center.NONE" required="N">provider_params</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="wecmdb:network_zone.key_name" required="Y">name</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="wecmdb:network_zone.network_segment>wecmdb:network_segment.code" required="Y">cidr_block</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="wecmdb:network_zone.vpc_asset_code" required="Y">id</parameter>
                    <parameter datatype="string" mappingType="system_variable" mappingSystemVariableName="QCLOUD_API_SECRET" required="Y">api_secret</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="wecmdb:network_zone.data_center>wecmdb:data_center.location" required="Y">location</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="wecmdb:network_zone.id">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="wecmdb:network_zone.vpc_asset_code">id</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="wecmdb:network_zone.route_table_asset_code">route_table_id</parameter>
                    <parameter datatype="string" mappingType="context">errorCode</parameter>
                    <parameter datatype="string" mappingType="context">errorMessage</parameter>
                </outputParameters>
            </interface>
        </plugin>
  </plugins>
</package>


