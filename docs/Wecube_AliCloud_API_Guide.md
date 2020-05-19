 # Wecube AliCloud API Guide
 
 提供统一接口定义，为使用者提供清晰明了的使用方法。
 
 ## API 操作资源(Resources):
 
 **私有网络**
 
 - [私有网络创建](#vpc-create)
 - [私有网络销毁](#vpc-delete)
 
 **交换机**
 
 - [交换机创建](#vswitch-create) 
 - [交换机销毁](#vswitch-delete)
 - [自定义路由表交换机创建](#vswitch-with-route-table-create)
 - [自定义路由表交换机删除](#vswitch-with-route-table-delete)
 
 **路由表**
 
 - [路由表创建](#route-table-create)
 - [路由表销毁](#route-table-delete)
 - [路由表绑定交换机](#route-table-associate-vswitch)
 
 **路由策略**
 
 - [路由策略创建](#route-entry-create)
 - [路由策略销毁](#route-entry-delete)
 
 **NAT网关**
 
 - [NAT网关创建](#nat-gateway-create)
 - [NAT网关销毁](#nat-gateway-delete)
 
 **NAT网关SNAT**
 
 - [NAT网关SNAT规则创建](#snat-entry-create)
 - [NAT网关SNAT规则销毁](#snat-entry-delete)
 - [NAT网关SNAT规则IP添加](#snat-entry-add)
 - [NAT网关SNAT规则IP移除](#snat-entry-remove)
 
 **云企业网**
 
 - [云企业网创建](#cen-create)
 - [云企业网销毁](#cen-delete)
 - [云企业网添加网络](#cen-attach)
 - [云企业网移除网络](#cen-detach)
 
 **安全组**
 
 - [安全组创建](#security-group-create)
 - [安全组销毁](#security-group-delete)
 - [安全组规则添加](#security-group-policy-authorize)
 - [安全组规则删除](#security-group-ploicy-revoke)
 
 **云服务器**
 
 - [云服务器创建](#vm-create)
 - [云服务器销毁](#vm-terminate)
 - [云服务器启动](#vm-start)
 - [云服务器停机](#vm-stop)
 - [云服务器绑定安全组](#vm-bind-security-group)
 - [云服务器解绑安全组](#vm-unbind-security-group)
 
 **云硬盘管理**
 
 - [云硬盘创建](#disk-create)
 - [云硬盘销毁](#disk-delete)
 
 **负载均衡**
 
 - [负载均衡创建](#lb-create)
 - [负载均衡销毁](#lb-delete)
 - [负载均衡后端服务器添加](#lb-backend-server-add)
 - [负载均衡后端服务器移除](#lb-backend-server-remove)
 
 **云数据库RDS**
 
 - [云数据库RDS实例创建](#rds-create)
 - [云数据库RDS实例销毁](#rds-delete)
 - [云数据库RDS备份创建](#rds-backup-create)
 - [云数据库RDS备份销毁](#rds-backup-delete)
 - [云数据库RDS添加白名单](#rds-security-ip-add)
 - [云数据库RDS移除白名单](#rds-security-ip-remove)
 
 **云数据库Redis**
 
 - [云数据库Redis创建](#redis-create)
 - [云数据库Redis销毁](#redis-delete)
 
 
 **弹性公网IP**
 
 - [弹性公网IP创建](#eip-create)
 - [弹性公网IP销毁](#eip-delete)
 - [弹性公网IP绑定实例](#eip-associate)
 - [弹性公网IP解绑实例](#eip-un-associate)
 
## API 概览及实例:
 
### 私有网络
 
#### <span id="vpc-create">私有网络创建</span>
 
[POST] /alicloud/v1/vpc/create
 
 ##### 输入参数：
 
参数名称|类型|必选|描述
|:--:|:--:|:--:|:--:|
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
vpcId|string|否|VPC实例ID，若有值，则会检查该VPC是否已存在，若已存在，则不创建
vpcName|string|是|VPC名称
cidrBlock|string|是|VPC网段
description|string|否|VPC描述
 
##### 输出参数：

参数名称|类型|描述
|:--:|:--:|:--:|   
guid|string|CI类型全局唯一ID
vpcId|string|VPC实例ID
routeTableId|string|路由表ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vpc/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"vpcId": "",
			"vpcName": "test_vpc1",
			"cidrBlock": "xx.xx.xx.xx/xx",
			"description": "test vpc"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "vpcId": "vpc-t4n0iti7ecd8erg63nngq",
                "routeTableId": "vtb-t4nbzj7n8suk306n0fqto"
            }
        ]
    }
}
```

#### <span id="vpc-delete">私有网络销毁</span>

[POST] /alicloud/v1/vpc/delete

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
vpcId|string|是|VPC实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--    
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vpc/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"vpcId": "vpc-t4n0iti7ecd8erg63nngq"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
            }
        ]
    }
}
```

### 交换机

#### <span id="vswitch-create">交换机创建</span>

[POST] /alicloud/v1/vswitch/create

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
vSwitchId|string|否|交换机实例ID，若有值，则会检查该交换机是否已存在，若已存在，则不创建
vSwitchName|string|是|交换机名称
vpcId|string|是|VPC实例ID
cidrBlock|string|是|交换机的网段
zoneId|string|是|要创建的交换机所属的可用区ID
description|string|否|交换机的描述

##### 输出参数：

参数名称|类型|描述
:--|:--|:--    
guid|string|CI类型全局唯一ID
vSwitchId|string|交换机实例ID
routeTableId|string|交换机默认路由表ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vswitch/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"vSwitchName": "test_vswitch1",
			"vpcId": "vpc-t4nu397hag3n2u0cnftv7",
			"cidrBlock": "xx.xx.xx.xx/xx",
			"zoneId": "ap-southeast-1b",
			"description": "test vswitch1"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "vSwitchId": "vsw-t4n83ebdl4jvqc24rm5pt"
            }
        ]
    }
}
```

#### <span id="vswitch-delete">交换机销毁</span>

[POST] /alicloud/v1/vswitch/delete

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
vSwitchId|string|是|交换机实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vswitch/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"vSwitchId": "vsw-t4n83ebdl4jvqc24rm5pt"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="vswitch-with-route-table-create">自定义路由表交换机创建</span>

[POST] /alicloud/v1/vswitch/create/route_table/bind

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
vSwitchId|string|否|交换机实例ID，若有值，则会检查该交换机是否已存在，若已存在，则不创建
vSwitchName|string|是|交换机名称
vpcId|string|是|VPC实例ID
cidrBlock|string|是|交换机的网段
zoneId|string|是|要创建的交换机所属的可用区ID
description|string|否|交换机的描述

##### 输出参数：

参数名称|类型|描述
:--|:--|:--    
guid|string|CI类型全局唯一ID
vSwitchId|string|交换机实例ID
routeTableId|string|交换机自定义路由表ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vswitch/create/route_table/bind \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"vSwitchName": "test_vswitch2",
			"vpcId": "vpc-t4nu397hag3n2u0cnftv7",
			"cidrBlock": "xx.xx.xx.xx/xx",
			"zoneId": "ap-southeast-1b",
			"description": "test vswitch2"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "routeTableId": "vtb-t4nsx8dunbqmhmuvvcdur",
                "vSwitchId": "vsw-t4nmkrt4iw96bzliflzy1"
            }
        ]
    }
}
```

#### <span id="vswitch-with-route-table-delete">自定义路由表交换机删除</span>

[POST] /alicloud/v1/vswitch/delete/route_table/unbind

##### 输入参数：
参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
vSwitchId|string|是|交换机实例ID

##### 输出参数：
参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vswitch/delete/route_table/unbind \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"vSwitchId": "vsw-t4nmkrt4iw96bzliflzy1"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

### 路由表

#### <span id="route-table-create">路由表创建</span>

[POST] /alicloud/v1/route_table/create

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
routeTableId|string|否|路由表实例ID，若有值，则会检查该路由表是否已存在，若已存在，则不创建
routeTableName|string|否|路由表名称
vpcId|string|是|VPC实例ID
description|string|否|路由表描述

##### 输出参数：

参数名称|类型|描述
:--|:--|:--    
guid|string|CI类型全局唯一ID
routeTableId|string|路由表实例ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/route_table/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"routeTableName": "test_route_table",
			"vpcId": "vpc-t4nu397hag3n2u0cnftv7",
			"description": "test route table"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "routeTableId": "vtb-t4nwtmqy18tpgzhkup3m6"
            }
        ]
    }
}
```

#### <span id="route-table-delete">路由表销毁</span>

[POST] /alicloud/v1/route_table/delete

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
routeTableId|string|是|路由表实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/route_table/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"routeTableId": "vtb-t4nwtmqy18tpgzhkup3m6"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="route-table-associate-vswitch">路由表绑定交换机</span>

[POST] /alicloud/v1/route_table/vswitch/associate

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
routeTableId|string|是|路由表实例ID
vSwitchId|string|是|交换机实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/route_table/vswitch/associate \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"routeTableId": "vtb-t4n70cxitxen1mfsx5t2x",
			"vSwitchId": "vsw-t4ndwyqqlxmczbyr6t3f3"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

### 路由策略

#### <span id="route-entry-create">路由策略创建</span>

[POST] /alicloud/v1/route_table/route_entry/create

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
routeTableId|string|是|路由表实例ID
destinationCidrBlock|string|是|目标网段
nextHopType|string|否|下一跳实例类型，支持以下类型："Instance", "NatGateway"等
nextHopId|string|是|下一跳实例ID
routeEntryName|string|否|路由策略名称

##### 输出参数：

参数名称|类型|描述
:--|:--|:--    
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/route_table/route_entry/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"routeTableId": "vtb-t4n70cxitxen1mfsx5t2x",
			"nextHopType": "NatGateway",
			"destinationCidrBlock": "xx.xx.xx.xx/xx",
			"nextHopId": "ngw-t4nkybnb2eb9c1qm64wuo",
			"routeEntryName": "test_route_entry"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="route-entry-delete">路由策略销毁</span>

[POST] /alicloud/v1/route_table/route_entry/delete

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
routeTableId|string|是|路由表实例ID
destinationCidrBlock|string|是|目标网段
nextHopId|string|是|下一跳实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--    
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/route_table/route_entry/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"routeTableId": "vtb-t4n70cxitxen1mfsx5t2x",
			"destinationCidrBlock": "xx.xx.xx.xx/xx",
			"nextHopId": "ngw-t4nkybnb2eb9c1qm64wuo"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

### NAT网关

#### <span id="nat-gateway-create">NAT网关创建</span>

[POST] /alicloud/v1/vpc/nat/create

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
natGatewayId|string|否|NAT网关实例ID，若有值，则会检查该NAT网关是否已存在，若已存在，则不创建
name|string|否|NAT网关名称
vpcId|string|是|VPC实例ID
vSwitchId|string|否|NAT网关所属的交换机的ID
spec|string|否|NAT网关的规格：Small(默认)，Middle，Large，XLarge.1
instanceChargeType|string|否|计费方式：PrePaid，PostPaid(默认值)
pricingCycle|string|否|包年包月的计费周：Month(默认值)，Year
duration|string|否|购买时长：1～9(Month)，1～3(Year)
description|string|否|NAT网关描述

##### 输出参数：

参数名称|类型|描述
:--|:--|:--    
guid|string|CI类型全局唯一ID
natGatewayId|string|NAT网关实例ID
snatTableId|string|SNAT表的ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vpc/nat/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"name": "test_nat",
			"vpcId": "vpc-t4nu397hag3n2u0cnftv7",
			"vSwitchId": "vsw-t4nmkrt4iw96bzliflzy1",
			"spec": "Small",
			"instanceChargeType": "PostPaid",
			"description": "test nat"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "natGatewayId": "ngw-t4nkybnb2eb9c1qm64wuo",
                "snatTableId": "stb-t4n2yfn8z2m5ktu1o60pr"
            }
        ]
    }
}
```

#### <span id="nat-gateway-delete">NAT网关销毁</span>

[POST] /alicloud/v1/vpc/nat/delete

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
natGatewayId|string|是|NAT网关实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

### NAT网关SNAT

#### <span id="snat-entry-create">NAT网关SNAT规则创建</span>

[POST] /alicloud/v1/vpc/nat/snat_entry/create

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
natId|string|是|NAT网关实例ID
snatTableId|string|是|SNAT表ID
snatIp|string|是|公网IP地址，支持多个IP[ip1,ip2]
sourceVSwitchId|string|否|需要公网访问的交换机的ID
sourceCIDR|string|否|交换机或ECS实例的网段，与SourceVSwtichId参数互斥
snatEntryName|string|否|SNAT条目的名称

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID
snatEntryId|string|SNAT条目ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vpc/nat/snat_entry/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"natId": "ngw-t4nkybnb2eb9c1qm64wuo",
			"snatTableId": "stb-t4n2yfn8z2m5ktu1o60pr",
			"snatIp": "[xx.xx.xx.xx,xx.xx.xx.xx]",
			"sourceVSwitchId": "vsw-t4nmkrt4iw96bzliflzy1",
			"snatEntryName": "test_snat_entry"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "snatEntryId": "snat-t4nimzwmoc4a42rrxo6hn"
            }
        ]
    }
}
```

#### <span id="snat-entry-delete">NAT网关SNAT规则销毁</span>

[POST] /alicloud/v1/vpc/nat/snat_entry/delete

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
natId|string|是|NAT网关实例ID
snatTableId|string|是|SNAT表ID
snatEntryId|string|是|SNAT条目的ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vpc/nat/snat_entry/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"natId": "ngw-t4nkybnb2eb9c1qm64wuo",
			"snatTableId": "stb-t4n2yfn8z2m5ktu1o60pr",
			"snatEntryId": "snat-t4nimzwmoc4a42rrxo6hn"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="snat-entry-add">NAT网关SNAT规则IP添加</span>

[POST] /alicloud/v1/vpc/nat/snat_entry/modify/append

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
snatEntryId|string|是|SNAT条目的ID
natId|string|是|NAT网关实例ID
snatTableId|string|是|SNAT表ID
snatIp|string|是|公网IP地址，支持多个IP[ip1,ip2]

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vpc/nat/snat_entry/modify/append \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"natId": "ngw-t4nkybnb2eb9c1qm64wuo",
			"snatTableId": "stb-t4n2yfn8z2m5ktu1o60pr",
			"snatIp": "[xx.xx.xx.xx,xx.xx.xx.xx]",
			"snatEntryId": "snat-t4nimzwmoc4a42rrxo6hn"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="snat-entry-remove">NAT网关SNAT规则IP移除</span>

[POST] /alicloud/v1/vpc/nat/snat_entry/modify/remove

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
snatEntryId|string|是|SNAT条目的ID
natId|string|是|NAT网关实例ID
snatTableId|string|是|SNAT表ID
snatIp|string|是|公网IP地址，支持多个IP[ip1,ip2]

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vpc/nat/snat_entry/modify/remove \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"natId": "ngw-t4nkybnb2eb9c1qm64wuo",
			"snatTableId": "stb-t4n2yfn8z2m5ktu1o60pr",
			"snatIp": "[xx.xx.xx.xx]",
			"snatEntryId": "snat-t4nimzwmoc4a42rrxo6hn"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

### 云企业网

#### <span id="cen-create">云企业网创建</span>

[POST] /alicloud/v1/cen/create

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
cenId|string|否|CEN实例ID，若有值，则会检查该NAT网关是否已存在，若已存在，则不创建
name|string|是|CEN名称
description|string|否|CEN描述

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID
cenId|string|CEN实例ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/cen/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"name": "test_cen",
			"description": "test cen"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "cenId": "cen-bfffghnat1dmt9p18o"
            }
        ]
    }
}
```

#### <span id="cen-delete">云企业网销毁</span>

[POST] /alicloud/v1/cen/delete

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
cenId|string|是|CEN实例ID

#### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/cen/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"cenId": "cen-bfffghnat1dmt9p18o"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="cen-attach">云企业网添加网络</span>

[POST] /alicloud/v1/cen/attach

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
cenId|string|是|CEN实例ID
childInstanceId|string|是|指定待加载的网络实例的ID
childInstanceRegionId|string|否|CEN描网络实例所在的地域，默认为cloudParams指定的地域
childInstanceType|string|否|网络实例的类型：VPC，VBR，CCN
childInstanceOwnerId|string|否|跨账号加载网络实例场景下，网络实例所属账号的UID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/cen/attach \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"cenId": "cen-bfffghnat1dmt9p18o",
			"childInstanceId": "vpc-t4nu397hag3n2u0cnftv7"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="cen-detach">云企业网移除网络</span>

[POST] /alicloud/v1/cen/detach

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
cenId|string|是|CEN实例ID
childInstanceId|string|是|指定待加载的网络实例的ID
childInstanceRegionId|string|否|CEN描网络实例所在的地域，默认为cloudParams指定的地域
childInstanceType|string|否|网络实例的类型：VPC，VBR，CCN
childInstanceOwnerId|string|否|跨账号加载网络实例场景下，网络实例所属账号的UID
cenOwnerId|string|否|CEN实例所属账号的UID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/cen/detach \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"cenId": "cen-bfffghnat1dmt9p18o",
			"childInstanceId": "vpc-t4nu397hag3n2u0cnftv7"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

### 安全组

#### <span id="security-group-create">安全组创建</span>

[POST] /alicloud/v1/security_group/create

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
securityGroupId|string|否|安全组实例ID，若有值，则会检查该安全组是否已存在，若已存在，则不创建
securityGroupName|string|是|安全组名称
securityGroupType|string|N|安全组类型：normal，enterprise
vpcId|string|N|VPC网络ID
description|string|是|安全组描述

##### 输出参数：

参数名称|类型|描述
:--|:--|:--    
request_id|string|请求ID
guid|string|CI类型全局唯一ID
securityGroupId|string|安全组实例ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/security_group/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"securityGroupName": "test_security_group",
			"description": "test security group"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "securityGroupId": "sg-t4n1d2wurvisird07abx"
            }
        ]
    }
}
```

#### <span id="security-group-delete">安全组销毁</span>

[POST] /alicloud/v1/security_group/delete

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
securityGroupId|string|是|安全组实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/security_group/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"securityGroupId": "sg-t4n1d2wurvisird07abx"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="security-group-policy-authorize">安全组规则添加</span>

[POST] /alicloud/v1/security_group/authorize

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
securityGroupId|string|是|安全组实例ID
policy|string|是|accept 或 drop
cidrIp|string|是|源端IPv4 CIDR地址段。支持CIDR格式和IPv4格式的IP地址范围
ipProtocol|string|是|传输层协议, 取值:tcp,udp,icmp,gre,all
portRange|string|是|目的端安全组开放的传输层协议相关的端口范围
actionType|string|是|出站规则或者入站规则，取值egress 或 ingress
description|string|是|安全组规则描述

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/security_group/authorize \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"securityGroupId": "sg-t4n1d2wurvisird07abx",
			"policy": "accept",
			"cidrIp": "[192.168.xx.xx,192.168.xx.192/26,192.168.xx.193]",
			"ipProtocol": "[tcp,tcp,udp]",
			"portRange": "[8080,22-8000,8888]",
			"actionType": "egress",
			"description": "test security group rule"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="security-group-policy-revoke">安全组规则删除</span>

[POST] /alicloud/v1/security_group/revoke

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
securityGroupId|string|是|安全组实例ID
policy|string|是|出站规则或者入站规则，取值egress 或 ingress
cidrIp|string|是|源端IPv4 CIDR地址段。支持CIDR格式和IPv4格式的IP地址范围
ipProtocol|string|是|传输层协议, 取值:tcp,udp,icmp,gre,all
portRange|string|是|目的端安全组开放的传输层协议相关的端口范围
actionType|string|是|accept 或 drop

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/security_group/revoke \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"securityGroupId": "sg-t4n1d2wurvisird07abx",
			"policy": "accept",
			"cidrIp": "[192.168.xx.xx,192.168.xx.192/26]",
			"ipProtocol": "[tcp,tcp]",
			"portRange": "[8080,22-8000]",
			"actionType": "egress"
        }
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

### 云服务器

#### <span id="vm-create">云服务器创建</span>

[POST] /alicloud/v1/vm/create

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
instanceId|string|否|云服务器实例ID，若有值，则会检查该云服务器是否已存在，若已存在，则不创建
seed|string|是|云服务器实例密钥种子
password|string|否|实例的密码
instanceName|string|否|云服务器实例名称
instanceSpec|string|是|创建实例机型，如1C1G
instanceFamily|string|是|创建实例规则族，如c5
systemDiskSize|string|否|实例系统盘大小(G)
systemDiskCategory|string|否|实例系统盘类型：cloud_efficiency,cloud_ssd,cloud,ephemeral_ssd
imageId|string|是|镜像文件ID，启动实例时选择的镜像资源
vSwitchId|string|否|交换机实例ID
privateIpAddress|string|否|实例私网IP地址
securityGroupId|string|否|指定新创建实例所属于的安全组ID，同一个安全组内的实例之间可以互相访问
zoneId|string|是|实例所属的可用区ID
instanceChargeType|string|否|实例的付费方式：PrePaid,PostPaid(默认)
period|string|否|购买资源的时长
periodUnit|string|否|购买资源的时长：Week,Month(默认)
autoRenew|string|否|是否要自动续费：true,false(默认)
autoRenewPeriod|string|否|每次自动续费的时长
resourceTag|string|否|标签值，如[key1=value1;key2=value2]

##### 输出参数：

参数名称|类型|描述
:--|:--|:--    
guid|string|CI类型全局唯一ID
instanceId|string|云服务器实例ID
cpu|string|云服务器CPU核数
memory|string|云服务器内存大小
password|string|云服务器root密码
privateIp|string|云服务器私有IP

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vm/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"seed": "seed123",
			"password": "Abcd1234",
			"instanceName": "test_vm",
			"instanceSpec": "1c1g",
			"instanceFamily": "c5",
			"systemDiskSize": "40",
			"systemDiskCategory": "cloud_ssd",
			"imageId": "ubuntu_18_04_64_20G_alibase_20190624.vhd",
			"vSwitchId": "vsw-t4n1d7ng0b37kv2a6od11",
			"privateIpAddress": "10.128.64.xx",
			"zoneId": "ap-southeast-1b",
			"instanceChargeType": "PostPaid",
			"resourceTag": "[key1=value1;key2=value2]"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "instanceId": "i-t4n7rbtfeh1si9quyw94",
                "instanceType": "ecs.c5.large",
                "cpu": "2",
                "memory": "4",
                "privateIp": "10.128.64.xx",
                "password": "{cipher_a}9b28cb58bd8aca2a1f0cef1ea58f6756"
            }
        ]
    }
}
```

#### <span id="vm-delete">云服务器销毁</span>

[POST] /alicloud/v1/vm/delete

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
instanceId|string|是|云服务器实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vm/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"instanceId": "i-t4n7rbtfeh1si9quyw94"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="vm-start">云服务器启动</span>

[POST] /alicloud/v1/vm/start

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
instanceId|string|是|云服务器实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vm/start \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"instanceId": "i-t4n7rbtfeh1si9quyw94"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="vm-stop">云服务器停机</span>

[POST] /alicloud/v1/vm/stop

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
instanceId|string|是|云服务器实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vm/stop \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"instanceId": "i-t4n7rbtfeh1si9quyw94"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="vm-bind-security-group">云服务器绑定安全组</span>

[POST] /alicloud/v1/vm/security-group/bind

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
instanceId|string|是|云服务器实例ID
securityGroupId|string|是|云服务器需要绑定的安全组ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vm/security-group/bind \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"instanceId": "i-t4n7rbtfeh1si9quyw94",
			"securityGroupId": "sg-t4n7rbtfeh1si9qpqiqj"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="vm-unbind-security-group">云服务器解绑安全组</span>

[POST] /alicloud/v1/vm/security-group/unbind

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
instanceId|string|是|云服务器实例ID
securityGroupId|string|是|云服务器需要绑定的安全组ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vm/security-group/unbind \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"instanceId": "i-t4n7rbtfeh1si9quyw94",
			"securityGroupId": "sg-t4n7rbtfeh1si9qpqiqj"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

### 云硬盘管理

#### <span id="disk-create">云硬盘创建</span>

[POST] /alicloud/v1/disk/create_attach

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
seed|string|是|云服务器实例密钥种子
instanceGuid|string|是|云服务器实例对应CI类型全局唯一ID
password|string|是|云服务器实例的密码
fileSystemType|string|是|初始化云硬盘的文件系统类型：ext3,ext4,xfs
mountDir|string|是|云硬盘挂载的目录
instanceId|string|是|云服务器实例ID
diskId|string|否|云硬盘ID，若有值，则会检查该云硬盘是否已存在，若已存在，则不创建
zoneId|string|是|云硬盘实例所属的可用区ID
diskName|string|否|云硬盘名称
size|string|是|云硬盘的大小(G)
diskCategory|string|否|云硬盘类型：cloud(默认),cloud_efficiency,cloud_ssd,cloud_essd
description|string|否|云硬盘描述

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID
diskId|string|云硬盘ID
volumeName|string|云硬盘的卷名称

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/disk/create_attach \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"seed": "seed123",
			"instanceGuid": "0033_0000000001",
			"password": "{cipher_a}9b28cb58bd8aca2a1f0cef1ea58f6756",
			"fileSystemType": "ext3",
			"mountDir": "/data/test",
			"instanceId": "i-t4n7rbtfeh1si9quyw94",
			"zoneId": "ap-southeast-1b",
			"diskName": "test_disk",
			"size": "40",
			"diskCategory": "cloud_ssd",
			"description": "test disk"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "diskId": "d-t4n96gxbb5gxotao8ibe",
                "volumeName": "/dev/vdb"
            }
        ]
    }
}
```

#### <span id="disk-delete">云硬盘销毁</span>

[POST] /alicloud/v1/disk/detach_delete

##### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
seed|string|是|云服务器实例密钥种子
instanceGuid|string|是|云服务器实例对应CI类型全局唯一ID
password|string|是|云服务器实例的密码
unmountDir|string|是|云硬盘挂载的目录
instanceId|string|是|云服务器实例ID
diskId|string|是|云硬盘ID
volumeName|string|是|云硬盘的卷名称

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/disk/detach_delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"seed": "seed123",
			"instanceGuid": "0033_0000000001",
			"password": "{cipher_a}9b28cb58bd8aca2a1f0cef1ea58f6756",
			"unmountDir": "/data/test",
			"instanceId": "i-t4n7rbtfeh1si9quyw94",
			"diskId": "d-t4n96gxbb5gxotao8ibe",
            "volumeName": "/dev/vdb"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

### 负载均衡

#### <span id="lb-create">负载均衡创建</span>

[POST] /alicloud/v1/load_balancer/create

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
loadBalancerId|string|否|负载均衡实例ID，若有值，则会检查该实例是否已存在，若已存在，则不创建
addressType|string|是|负载均衡实例的网络类型：internet,intranet
internetChargeType|string|否|公网类型实例的付费方式：paybybandwidth,paybytraffic(默认)
bandWidth|string|否|监听的带宽峰值，-1(即不限制带宽峰值)或1-5120
loadBalancerName|string|否|负载均衡实例名称
vpcId|string|否|VPC实例ID
vSwitchId|string|否|交换机ID
masterZoneId|string|否|负载均衡实例的主可用区ID
slaveZoneId|string|否|负载均衡实例的备可用区ID
loadBalancerSpec|string|否|负载均衡实例的规格
payType|string|否|实例的计费类型：PostPaid,PrePaid
pricingCycle|string|否|预付费公网实例的计费周期：month或year
duration|string|否|预付费公网实例的购买时长：1~9(month),1-3(year)
address|string|否|指定负载均衡实例的私网IP地址

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID
loadBalancerId|string|负载均衡实例ID
address|string|分配的负载均衡实例的IP地址

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/load_balancer/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"addressType": "internet",
			"internetChargeType": "paybytraffic",
			"bandWidth": "5",
			"loadBalancerName": "test_lb",
			"vpcId": "vpc-t4nq3954f7lj8wk2hoy3o",
			"vSwitchId": "vsw-t4n1d7ng0b37kv2a6od11",
            "payType": "PostPaid"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "loadBalancerId": "lb-t4nvuami0zjalr9ne4fmj",
                "address": "161.117.xx.222"
            }
        ]
    }
}
```

#### <span id="lb-delete">负载均衡销毁</span>

[POST] /alicloud/v1/load_balancer/delete

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
loadBalancerId|string|是|负载均衡实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/load_balancer/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
            "loadBalancerId": "lb-t4nvuami0zjalr9ne4fmj"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="lb-backend-server-add">负载均衡后端服务器添加</span>

[POST] /alicloud/v1/load_balancer/backend_server/add

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
loadBalancerId|string|是|负载均衡实例ID
hostIds|string|是|后端服务器实例ID
hostPorts|string|是|后端服务器端口
vServerGroupName|string|否|虚拟服务器组名称
bandwidth|string|是|监听的带宽峰值，-1(即不限制带宽峰值)或1-5120
listenerPort|string|是|监听器端口
listenerProtocol|string|是|监听器协议

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID
vServerGroupId|string|虚拟服务器组ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/load_balancer/backend_server/add \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"loadBalancerId": "lb-t4nvuami0zjalr9ne4fmj",
			"hostIds": "[i-t4n7rbtfeh1si9quyw94]",
			"hostPorts": "[8080]",
			"vServerGroupName": "test_lb",
			"bandwidth": "5",
			"listenerPort": "8080",
            "listenerProtocol": "tcp"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "vServerGroupId": "rsp-t4nfwp9ozu68v"
            }
        ]
    }
}
```

#### <span id="lb-backend-server-remove">负载均衡后端服务器移除</span>

[POST] /alicloud/v1/load_balancer/backend_server/remove

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
loadBalancerId|string|是|负载均衡实例ID
vServerGroupId|string|是|虚拟服务器组ID
hostIds|string|是|后端服务器实例ID
hostPorts|string|是|后端服务器端口
listenerPort|string|是|监听器端口
listenerProtocol|string|是|监听器协议
deleteListener|string|是|是否删除监听器：Y或者N

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/load_balancer/backend_server/remove \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
			"loadBalancerId": "lb-t4nvuami0zjalr9ne4fmj",
			"hostIds": "[i-t4n7rbtfeh1si9quyw94]",
			"hostPorts": "[8080]",
			"vServerGroupId": "rsp-t4nfwp9ozu68v",
			"deleteListener": "Y",
			"listenerPort": "8080",
            "listenerProtocol": "tcp"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

### 云数据库RDS

#### <span id="rds-create">云数据库RDS创建</sapn>

[POST] /alicloud/v1/rds/db/create

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
seed|string|是|RDS实例密钥种子
accountName|string|是|指定创建账户名称
accountPassword|string|否|账户密码
accountDescription|string|否|账户描述
securityGroupId|string|否|安全组ID
dBInstanceId|string|否|RDS实例ID，若有值，则会检查该实例是否已存在，若已存在，则不创建
dBInstanceSpec|string|是|实例类型，如1C2G
dBInstanceClass|string|否|实例规格
dBInstanceStorage|string|是|实例存储空间(G),每5GB进行递增
engine|string|是|数据库类型:MySQL or MariaDB
engineVersion|string|是|数据库版本,MySQL：5.5/5.6/5.7/8.0;MariaDB：10.3
securityIPList|string|是|该实例的IP白名单,如[xx.xx.xx.xx,xx.xx.xx.xx/24]
zoneId|string|否|可用区ID
vpcId|string|否|VPC实例ID
vSwitchId|string|否|交换机ID
privateIpAddress|string|否|设置实例的内网IP
dBInstanceStorageType|string|是|实例存储类型：local_ssd,cloud_ssd,cloud_essd
category|string|否|实例系列：Basic,HighAvailability,AlwaysOn,Finance
dBIsIgnoreCase|string|string|表名是否区分大小写：1(默认),0(区分大小写)
dBTimeZone|string|否|UTC时区
payType|string|否|实例的付费类型：Postpaid,Prepaid
period|string|否|指定预付费实例为包年或者包月类型：Year或者Month
usedTime|string|否|指定购买时长：1～3(Year),1~9(Month)
autoRenew|string|否|实例是否自动续费：true或者false
dBInstanceDescription|string|否|实例描述

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID
accountName|string|指定创建账户名称
accountPassword|string|账户密码
dBInstanceClass|string|实例规格
cpu|string|RDS实例内核核数
memory|string|RDS实例内存大小
dBInstanceId|string|DS实例ID
connectionString|string|数据库连接地址
port|string|RDS实例端口

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/rds/db/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
            "seed": "seed123",
            "accountName": "root1",
            "accountPassword": "Abcd1234",
            "accountDescription": "test account",
            "securityGroupId": "sg-t4n7rbtfeh1si9qpqiqj",
            "dBInstanceSpec": "1C1G",
            "dBInstanceStorage": "40",
            "engine": "MySQL",
            "engineVersion": "5.7",
            "securityIPList": "[192.168.xx.xx,192.168.xx.0/24]",
            "vpcId": "vpc-t4nq3954f7lj8wk2hoy3o",
            "vSwitchId": "vsw-t4n1d7ng0b37kv2a6od11",
            "dBInstanceStorageType": "local_ssd",
            "category": "HighAvailability",
            "dBIsIgnoreCase": "1",
            "payType": "PostPaid",
            "dBInstanceDescription": "test rds"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "connectionString": "rm-t4n64t9pm1yj736g6.mysql.singapore.rds.aliyuncs.com",
                "port": "3306",
                "accountName": "root1",
                "cpu": "1",
                "memory": "1",
                "dBInstanceId": "rm-t4n64t9pm1yj736g6",
                "accountPassword": "{cipher_a}9b28cb58bd8aca2a1f0cef1ea58f6756",
                "dBInstanceClass": "rds.mysql.t1.small"
            }
        ]
    }
}
```

#### <span id="rds-delete">云数据库RDS销毁</sapn>

[POST] /alicloud/v1/rds/db/delete

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
dBInstanceId|string|是|RDS实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/rds/db/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
            "dBInstanceId": "rm-t4n64t9pm1yj736g6"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="rds-backup-create">云数据库RDS备份创建</sapn>

[POST] /alicloud/v1/rds/backup/create

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
dBInstanceId|string|是|RDS实例ID
dBName|string|否|数据库列表，多个数据库之间用英文逗号（,）隔开
backupStrategy|string|否|备份策略：db或者instance
backupMethod|string|否|Logical,Physical(默认),Snapshot
backupType|string|否|Auto(默认)或者FullBackup

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID
backupId|string|备份ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/rds/backup/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
            "dBInstanceId": "rm-t4n64t9pm1yj736g6",
            "backupStrategy": "db",
            "backupMethod": "Physical"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "backupId": "507512199"
            }
        ]
    }
}
```

#### <span id="rds-backup-delete">云数据库RDS备份删除</sapn>

[POST] /alicloud/v1/rds/backup/delete

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
dBInstanceId|string|是|RDS实例ID
backupId|string|是|备份ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/rds/backup/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
            "dBInstanceId": "rm-t4n64t9pm1yj736g6",
            "backupId": "507512199"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="rds-security-ip-add">云数据库RDS白名单添加</sapn>

[POST] /alicloud/v1/rds/security_ip/append

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
dBInstanceId|string|是|RDS实例ID
securityIps|string|是|IP白名单，如[xx.xx.xx.xx,xx.xx.xx.xx/xx]
dBInstanceIPArrayName|string|否|需要修改的IP白名单分组名称，默认操作“Default”分组
whitelistNetworkType|string|否|白名单的网络类型：Classic,VPC,MIX(默认)

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/rds/security_ip/append \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
            "securityIps": "[192.168.0.xx,10.0.xx.1]",
            "dBInstanceId": "rm-t4n64t9pm1yj736g6"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <span id="rds-security-ip-remove">云数据库RDS白名单移除</sapn>

[POST] /alicloud/v1/rds/security_ip/delete

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
dBInstanceId|string|是|RDS实例ID
securityIps|string|是|IP白名单，如[xx.xx.xx.xx,xx.xx.xx.xx/xx]
dBInstanceIPArrayName|string|否|需要修改的IP白名单分组名称，默认操作“Default”分组
whitelistNetworkType|string|否|白名单的网络类型：Classic,VPC,MIX(默认)

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/rds/security_ip/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
            "securityIps": "[192.168.xx.6,10.0.xx.1]",
            "dBInstanceId": "rm-t4n64t9pm1yj736g6"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

### 云数据库Redis

#### <span id="redis-create">云数据库Redis创建</sapn>

[POST] /alicloud/v1/redis/create

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
seed|string|是|RDS实例密钥种子
instanceId|string|否|Redis实例ID，若有值，则会检查该实例是否已存在，若已存在，则不创建
instanceName|string|否|实例名称
password|string|否|实例密码
capacity|string|是|实例的存储容量(MB)
zoneId|string|是|可用区ID
engineVersion|string|是|版本类型：2.8(默认),4.0,5.0
securityIps|string|否|安全IP白名单
securityGroupId|string|否|安全组ID
networkType|string|否|网络类型：CLASSIC或者VPC
vpcId|string|否|VPC实例ID
vSwitchId|string|否|交换机ID
srcDBInstanceId|string|否|如需基于某个实例的备份集创建新实例，则在此参数中传递源实例的ID
backupId|string|否|如需基于某个实例的备份集创建新实例，则在此参数中传递源实例的备份集ID
privateIpAddress|string|否|指定新实例的内网IP地址
chargeType|string|是|付费类型：PostPaid(默认),PrePaid
period|string|否|付费周期
autoRenew|string|否|是否开启自动续费：true(开启),false(默认)

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID
instanceId|string|Redis实例ID
password|string|实例密码
port|string|实例端口
privateIpAddr|string|实例的内网IP地址
connectionDomain|string|

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/redis/create \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
            "seed": "seed123",
            "instanceName": "test_redis",
            "password": "Abcd1234",
            "capacity": "1024",
            "zoneId": "ap-southeast-1b",
            "engineVersion": "4.0",
            "securityIps": "0.0.0.0/32",
            "securityGroupId": "sg-t4n7rbtfeh1si9qpqiqj",
            "networkType": "VPC",
            "vpcId": "vpc-t4nq3954f7lj8wk2hoy3o",
            "vSwitchId": "vsw-t4n1d7ng0b37kv2a6od11",
            "chargeType": "PostPaid"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "instanceId": "r-t4nd94f0f7cf4bb4",
                "port": "6379",
                "privateIpAddr": "1024",
                "password": "{cipher_a}9b28cb58bd8aca2a1f0cef1ea58f6756"
            }
        ]
    }
}
```

#### <span id="redis-delete">云数据库Redis销毁</sapn>

[POST] /alicloud/v1/redis/delete

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
instanceId|string|是|Redis实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/redis/delete \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=******",
			"cloudParams": "regionId=ap-southeast-1",
            "instanceId": "r-t4nd94f0f7cf4bb4"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

### 弹性公网IP

#### <sapn id="eip-create">弹性公网IP创建</sapn>

[POST] /alicloud/v1/vpc/eip/allocate

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
allocationId|string|否|实例ID，若有值，则会检查该实例是否已存在，若已存在，则不创建
cbpName|string|否|共享带宽名称
bandwidth|string|否|EIP的带宽峰值，单位为Mbps，默认值为5
instanceChargeType|string|否|EIP的计费方式：PrePaid,PostPaid(默认)
internetChargeType|string|否|EIP的计量方式：PayByBandwidth(默认值),PayByTraffic
period|string|否|购买时长：1～3(Year),1~9(Month)
pricingCycle|string|否|包年包月的计费周期：Month,Year

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID
allocationId|string|弹性公网实例ID
eipAddress|string|弹性公网IP
cbpId|string|共享带宽ID
cbpName|string|共享带宽名称

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vpc/eip/allocate \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"cbpName": "test_cbp",
			"bandwidth": "5",
			"instanceChargeType": "PostPaid",
			"internetChargeType": "PayByBandwidth"
		},
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"cbpName": "test_cbp",
			"bandwidth": "5",
			"instanceChargeType": "PostPaid",
			"internetChargeType": "PayByBandwidth"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
                "allocationId": "eip-t4nxfcb1xc08dbkknjg6x",
                "cbpId": "cbwp-t4n0wy2ovuasq46e1tc23",
                "cbpName": "test_cbp",
                "eipAddress": "xx.xx.xx.xx"
            },
            {
                "guid": "0033_0000000001",
                "allocationId": "eip-t4nwk9pcc41nuxwh1cgui",
                "cbpId": "cbwp-t4n0wy2ovuasq46e1tc23",
                "cbpName": "test_cbp",
                "eipAddress": "xx.xx.xx.xx"
            }
        ]
    }
}
```

#### <sapn id="eip-delete">弹性公网IP销毁</sapn>

[POST] /alicloud/v1/vpc/eip/release

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
allocationId|string|否|实例ID
cbpName|string|否|共享带宽名称

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vpc/eip/release \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"cbpName": "test_cbp",
			"allocationId": "eip-t4nxfcb1xc08dbkknjg6x"
		},
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"cbpName": "test_cbp",
			"allocationId": "eip-t4nwk9pcc41nuxwh1cgui"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001",
            },
            {
                "guid": "0033_0000000001",
            }
        ]
    }
}
```

#### <sapn id="eip-associate">弹性公网IP绑定实例</sapn>

[POST] /alicloud/v1/vpc/eip/associate

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
|stringallocationId|是|EIP实例ID，
instanceId|string|是|绑定实例ID
instanceType|string|是|绑定实例类型：Nat,SlbInstance,EcsInstance等
instanceRegionId|string|否|绑定实例所在区域

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vpc/eip/associate \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"allocationId": "eip-t4nxfcb1xc08dbkknjg6x",
			"instanceId": "ngw-t4nkybnb2eb9c1qm64wuo",
			"instanceType": "Nat"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```

#### <sapn id="eip-un-associate">弹性公网IP解绑实例</sapn>

[POST] /alicloud/v1/vpc/eip/un-associate

#### 输入参数：

参数名称|类型|必选|描述
:--|:--|:--|:-- 
guid|string|是|CI类型全局唯一ID
identityParams|string|是|公有云远程连接参数，包括accessKeyId和secret
cloudParams|string|是|公有云公共参数，包括regionId(地域ID)等
allocationId|string|是|EIP实例ID，
instanceId|string|是|绑定实例ID

##### 输出参数：

参数名称|类型|描述
:--|:--|:--
guid|string|CI类型全局唯一ID

##### 示例：

输入：

```
curl -X POST http://127.0.0.1:8080/alicloud/v1/vpc/eip/un-associate \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"inputs": [
		{
			"guid":"0033_0000000001",
			"identityParams": "accessKeyId=*******;secret=*******",
			"cloudParams": "regionId=ap-southeast-1",
			"allocationId": "eip-t4nxfcb1xc08dbkknjg6x",
			"instanceId": "ngw-t4nkybnb2eb9c1qm64wuo"
		}
	]
}'
```

输出：

```
{
    "resultCode": "0",
    "resultMessage": "Success",
    "results": {
        "outputs": [
            {
                "guid": "0033_0000000001"
            }
        ]
    }
}
```