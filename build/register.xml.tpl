  
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
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">identity_params</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cloud_params</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="Y">cidr</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="" required="N">id</parameter>
                </inputParameters>
                <outputParameters>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">guid</parameter>
                    <parameter datatype="string" mappingType="entity" mappingEntityExpression="">id</parameter>
                </outputParameters>
            </interface>
        </plugin>
  </plugins>
</package>


