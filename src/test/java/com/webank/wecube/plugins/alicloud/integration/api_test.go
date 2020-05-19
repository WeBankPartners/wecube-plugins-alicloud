package main

import (
	"bytes"
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"reflect"
	"testing"
	"time"
)

const (
	ONLY_PRINT_IMAGES        = false
	CREATE_EXPENSIVE_RESOUCE = false //是否创建包年包月的资源
	VM_IMAGE_ID              = "ubuntu_18_04_64_20G_alibase_20190624.vhd"
	AVAILABLE_ZONE           = "ap-southeast-1b"
	REQUEST_TIMEOUT          = 900 //900s
)

type EnvironmentVars struct {
	PluginServerAddr string
	AccessKeyId      string
	Secret           string
	Region           string
}

var envVars EnvironmentVars

type CloudProviderParam struct {
	IdentityParams string `json:"identityParams,omitempty"`
	CloudParams    string `json:"cloudParams,omitempty"`
}

func getCloudProviderParam() CloudProviderParam {
	identityParams := fmt.Sprintf("accessKeyId=%v;secret=%v", envVars.AccessKeyId, envVars.Secret)
	cloudParams := fmt.Sprintf("regionId=%v", envVars.Region)

	param := CloudProviderParam{
		IdentityParams: identityParams,
		CloudParams:    cloudParams,
	}

	return param
}

func loadEnvironmentVars(t *testing.T) error {
	envVars.PluginServerAddr = os.Getenv("ALICLOUD_PLUGIN_ADDRESS")
	if envVars.PluginServerAddr == "" {
		envVars.PluginServerAddr = "127.0.0.1:8080"
	}
	envVars.AccessKeyId = os.Getenv("ACCESS_KEY_ID")
	if envVars.AccessKeyId == "" {
		return fmt.Errorf("get access_key_id from env failed")
	}

	envVars.Secret = os.Getenv("SECRET")
	if envVars.Secret == "" {
		return fmt.Errorf("get secret from env failed")
	}

	envVars.Region = os.Getenv("REGION")
	if envVars.Region == "" {
		return fmt.Errorf("get region from env failed")
	}

	t.Logf("envVars=%++v", envVars)

	return nil
}

type CreatedResources struct {
	VpcId                string `json:"vpc_id,omitempty"`
	VpcIdForCEN          string `json:"vpc_id_for_cen,omitempty"`
	VSwitchId            string `json:"v_switch_id,omitempty"`
	VSwitchIdWithRoute   string `json:"v_switch_id_with_route,omitempty"`
	SecurityGroupId      string `json:"security_group_id,omitempty"`
	VmIpPostPaid         string `json:"vm_ip_post_paid,omitempty"`
	VmIdPostPaid         string `json:"vm_id_post_paid,omitempty"`
	VmIdPrePaid          string `json:"vm_id_pre_paid,omitempty"`
	VmIpPrePaid          string `json:"vm_ip_pre_paid,omitempty"`
	EipId                string `json:"eip_id,omitempty"`
	EipAddress           string `json:"eip_address,omitempty"`
	EipIdNat             string `json:"eip_id_nat,omitempty"`
	EipAddressNat        string `json:"eip_address_nat,omitempty"`
	CENId                string `json:"cen_id,omitempty"`
	NatGatewayId         string `json:"nat_gateway_id,omitempty"`
	SnatTableId          string `json:"snat_table_id,omitempty"`
	SnatEntryId          string `json:"snat_entry_id,omitempty"`
	RouteTableId         string `json:"route_table_id,omitempty"`
	InternalLBId         string `json:"internal_lb_id,omitempty"`
	InternalLBAddress    string `json:"internal_lb_address,omitempty"`
	ExternalLBId         string `json:"external_lb_id,omitempty"`
	ExternalLBAddress    string `json:"external_lb_address,omitempty"`
	ExternalPreLBId      string `json:"external_pre_lb_id,omitempty"`
	ExternalPreLBAddress string `json:"external_pre_lb_address,omitempty"`
	VServerGroupId       string `json:"v_server_group_id,omitempty"`
	VolumeName           string `json:"volume_name,omitempty"`
	DiskId               string `json:"disk_id,omitempty"`
	DBInstanceIdPrepaid  string `json:"db_instance_id_prepaid,omitempty"`
	DBInstanceIdPostpaid string `json:"db_instance_id_postpaid,omitempty"`
	DBInstanceBackupId   string `json:"db_instance_backup_id,omitempty"`
	RedisIdPost          string `json:"redis_id_post,omitempty"`
	RedisIdPrePaid       string `json:"redis_id_prepaid,omitempty"`
}

type ResourceFunc func(string, *CreatedResources) error

type ResourceFuncEntry struct {
	TestApiName         string
	ResourcePath        string
	Func                ResourceFunc
	IsExpensiveResource bool //是否包年包月
}

var resourceCreateFuncTable = []ResourceFuncEntry{
	//create funcs
	{"createVpc", "/alicloud/v1/vpc/create", createVpc, false},
	{"createVswitch", "/alicloud/v1/vswitch/create", createVswitch, false},
	{"createVswitchWithRoute", "/alicloud/v1/vswitch/create/route_table/bind", createVswitchWithRoute, false},
	{"createSecurityGroup", "/alicloud/v1/security_group/create", createSecurityGroup, false},
	{"addSecurityPolicy", "/alicloud/v1/security_group/authorize", addSecurityPolicy, false},
	{"createPostPaidVm", "/alicloud/v1/vm/create", createPostPaidVm, false}, //按量计费
	{"createPrePaidVm", "/alicloud/v1/vm/create", createPrePaidVm, true},    //包年包月
	{"createPublicIp", "/alicloud/v1/vpc/eip/allocate", createPublicIp, false},
	{"associateIpToInstance", "/alicloud/v1/vpc/eip/associate", associateIpToInstance, false},
	{"stopVm", "/alicloud/v1/vm/stop", stopVm, false},
	{"startVm", "/alicloud/v1/vm/start", startVm, false},
	{"bindSecurityGroup", "/alicloud/v1/vm/security-group/bind", bindSecurityGroup, false},
	{"createCEN", "/alicloud/v1/cen/create", createCEN, false},
	{"attachCEN", "/alicloud/v1/cen/attach", attachCEN, false},
	{"createNatGateway", "/alicloud/v1/vpc/nat/create", createNatGateway, false},
	{"createSnatRule", "/alicloud/v1/vpc/nat/snat_entry/create", createSnatRule, false},
	{"createRoute", "/alicloud/v1/route_table/create", createRoute, false},
	{"createRouteRule", "/alicloud/v1/route_table/route_entry/create", createRouteRule, false},
	{"createInternalLb", "/alicloud/v1/load_balancer/create", createInternalLb, false},
	{"createExternalPreLb", "/alicloud/v1/load_balancer/create", createExternalPreLb, true},
	{"createExternalLb", "/alicloud/v1/load_balancer/create", createExternalLb, false},
	{"addTargetToLb", "/alicloud/v1/load_balancer/backend_server/add", addTargetToLb, false},
	{"buyAndAttachDisk", "/alicloud/v1/disk/create_attach", buyAndAttachDisk, false},
	{"ceatePrePaidRds", "/alicloud/v1/rds/db/create", ceatePrePaidRds, true},
	{"createPostPaidRds", "/alicloud/v1/rds/db/create", createPostPaidRds, false},
	{"modifyRdsSecurityGroup", "/alicloud/v1/rds/security_ip/modify", modifyRdsSecurityGroup, false},
	{"createRdsBackup", "/alicloud/v1/rds/backup/create", createRdsBackup, false},
	{"createPrePaidRedis", "/alicloud/v1/redis/create", createPrePaidRedis, true},
	{"createPostPaidRedis", "/alicloud/v1/redis/create", createPostPaidRedis, false},
}

var resourceDeleteFuncTable = []ResourceFuncEntry{
	//delete funcs
	{"deletePrePaidRedis", "/alicloud/v1/redis/delete", deletePrePaidRedis, true},
	{"deletePostPaidRedis", "/alicloud/v1/redis/delete", deletePostPaidRedis, false},
	{"deleteRdsBackup", "/alicloud/v1/rds/backup/delete", deleteRdsBackup, false},
	{"deletePrePaidRds", "/alicloud/v1/rds/db/delete", deletePrePaidRds, true},
	{"deletePostPaidRds", "/alicloud/v1/rds/db/delete", deletePostPaidRds, false},
	{"unAttachAndDestoryDisk", "/alicloud/v1/disk/detach_delete", unAttachAndDestoryDisk, false},
	{"removeTargetfromLb", "/alicloud/v1/load_balancer/backend_server/remove", removeTargetfromLb, false},
	{"deleteExternalPreLb", "/alicloud/v1/load_balancer/delete", deleteExternalPreLb, true},
	{"deleteInternalLb", "/alicloud/v1/load_balancer/delete", deleteInternalLb, false},
	{"deleteExternalLb", "/alicloud/v1/load_balancer/delete", deleteExternalLb, false},
	{"deleteRouteRule", "/alicloud/v1/route_table/route_entry/delete", deleteRouteRule, false},
	{"deleteRoute", "/alicloud/v1/route_table/delete", deleteRoute, false},
	{"deleteSnatRule", "/alicloud/v1/vpc/nat/snat_entry/delete", deleteSnatRule, false},
	{"deleteNatGateway", "/alicloud/v1/vpc/nat/delete", deleteNatGateway, false},
	{"detachCEN", "/alicloud/v1/cen/detach", detachCEN, false},
	{"deleteCEN", "/alicloud/v1/cen/delete", deleteCEN, false},
	{"unbindSecurityGroup", "/alicloud/v1/vm/security-group/unbind", unbindSecurityGroup, false},
	{"unassociateIpToInstance", "/alicloud/v1/vpc/eip/un-associate", unassociateIpToInstance, false},
	{"deletePublicIp", "/alicloud/v1/vpc/eip/release", deletePublicIp, false},
	{"deletePrePaidVm", "/alicloud/v1/vm/delete", deletePrePaidVm, true},
	{"deletePostPaidVm", "/alicloud/v1/vm/delete", deletePostPaidVm, false},
	{"removeSecurityPolicy", "/alicloud/v1/security_group/revoke", removeSecurityPolicy, false},
	{"deleteSecurityGroup", "/alicloud/v1/security_group/delete", deleteSecurityGroup, false},
	{"deleteVswitchWithRoute", "/alicloud/v1/vswitch/delete/route_table/unbind", deleteVswitchWithRoute, false},
	{"deleteVswitch", "/alicloud/v1/vswitch/delete", deleteVswitch, false},
	{"deleteVpc", "/alicloud/v1/vpc/delete", deleteVpc, false},
}

type ResourceOutputs struct {
	Outputs []ResourceOutput `json:"outputs,omitempty"`
}

type ResourceOutput struct {
	Guid            string `json:"guid,omitempty"`
	VpcId           string `json:"vpcId,omitempty"`
	VSwitchId       string `json:"vSwitchId,omitempty"`
	PrivateIp       string `json:"privateIp,omitempty"`
	InstanceId      string `json:"instanceId,omitempty"`
	SecurityGroupId string `json:"securityGroupId,omitempty"`
	EipId           string `json:"allocationId,omitempty"`
	EipAddress      string `json:"eipAddress,omitempty"`
	CENId           string `json:"cenId,omitempty"`
	NatGatewayId    string `json:"natGatewayId,omitempty"`
	SnatTableId     string `json:"snatTableId,omitempty"`
	SnatEntryId     string `json:"snatEntryId,omitempty"`
	RouteTableId    string `json:"routeTableId,omitempty"`
	LoadBalancerId  string `json:"loadBalancerId,omitempty"`
	LBAddress       string `json:"address,omitempty"`
	VServerGroupId  string `json:"vServerGroupId,omitempty"`
	DiskId          string `json:"diskId,omitempty"`
	VolumeName      string `json:"volumeName,omitempty"`
	DBInstanceId    string `json:"dBInstanceId,omitempty"`
	BackupId        string `json:"backupId,omitempty"`
}

type ResourceDeleteInputs struct {
	Inputs []ResourceDeleteInput `json:"inputs,omitempty"`
}

type ResourceDeleteInput struct {
	CloudProviderParam
	ResourceOutput
}

// OK
type VpcCreateInputs struct {
	Inputs []VpcCreateInput `json:"inputs,omitempty"`
}
type VpcCreateInput struct {
	CloudProviderParam
	Guid      string `json:"guid,omitempty"`
	CidrBlock string `json:"cidrBlock,omitempty"`
	VpcName   string `json:"vpcName,omitempty"`
}

func createVpc(path string, createdResources *CreatedResources) error {
	inputs := VpcCreateInputs{
		Inputs: []VpcCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				CidrBlock:          "192.168.1.0/24",
				VpcName:            "testApiCreated1",
			},
			VpcCreateInput{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "234",
				CidrBlock:          "192.168.2.0/24",
				VpcName:            "testApiCreated2",
			},
		},
	}
	outputs := ResourceOutputs{}
	if err := doHttpRequest(path, inputs, &outputs); err != nil {
		return err
	}
	if outputs.Outputs[0].VpcId == "" || outputs.Outputs[1].VpcId == "" {
		return fmt.Errorf("vpcId is invalid")
	}

	createdResources.VpcId = outputs.Outputs[0].VpcId
	createdResources.VpcIdForCEN = outputs.Outputs[1].VpcId
	return nil
}

func deleteVpc(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:  "123",
					VpcId: createdResources.VpcId,
				},
			},
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:  "234",
					VpcId: createdResources.VpcIdForCEN,
				},
			},
		},
	}

	outputs := ResourceOutputs{}
	if err := doHttpRequest(path, inputs, &outputs); err != nil {
		return err
	}

	return nil
}

// OK
type VswitchCreateInputs struct {
	Inputs []VswitchCreateInput `json:"inputs,omitempty"`
}

type VswitchCreateInput struct {
	CloudProviderParam
	Guid        string `json:"guid,omitempty"`
	CidrBlock   string `json:"cidrBlock,omitempty"`
	VpcId       string `json:"vpcId,omitempty"`
	ZoneId      string `json:"zoneId,omitempty"`
	VSwitchName string `json:"vSwitchName,omitempty"`
}

func createVswitch(path string, createdResources *CreatedResources) error {
	inputs := VswitchCreateInputs{
		Inputs: []VswitchCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				VpcId:              createdResources.VpcId,
				ZoneId:             AVAILABLE_ZONE,
				VSwitchName:        "testApiCreateed",
				CidrBlock:          "192.168.1.0/25",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].VSwitchId == "" {
		return fmt.Errorf("vSwitchId is invaild")
	}
	createdResources.VSwitchId = outputs.Outputs[0].VSwitchId

	return nil
}

func deleteVswitch(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:      "123",
					VSwitchId: createdResources.VSwitchId,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func createVswitchWithRoute(path string, createdResources *CreatedResources) error {
	inputs := VswitchCreateInputs{
		Inputs: []VswitchCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "234",
				VpcId:              createdResources.VpcId,
				ZoneId:             AVAILABLE_ZONE,
				VSwitchName:        "testApiCreateed_withroute",
				CidrBlock:          "192.168.1.128/25",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].VSwitchId == "" {
		return fmt.Errorf("vSwitchId is invaild")
	}
	createdResources.VSwitchIdWithRoute = outputs.Outputs[0].VSwitchId
	return nil
}

func deleteVswitchWithRoute(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:      "234",
					VSwitchId: createdResources.VSwitchIdWithRoute,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// OK
type SecurityGroupCreateInputs struct {
	Inputs []SecurityGroupCreateInput `json:"inputs,omitempty"`
}

type SecurityGroupCreateInput struct {
	CloudProviderParam
	Guid              string `json:"guid,omitempty"`
	SecurityGroupName string `json:"securityGroupName,omitempty"`
	VpcId             string `json:"vpcId,omitempty"`
}

func createSecurityGroup(path string, createResources *CreatedResources) error {
	inputs := SecurityGroupCreateInputs{
		Inputs: []SecurityGroupCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				SecurityGroupName:  "testApiCreated",
				VpcId:              createResources.VpcId,
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].SecurityGroupId == "" {
		return fmt.Errorf("securityGroupId is invaild")
	}
	createResources.SecurityGroupId = outputs.Outputs[0].SecurityGroupId
	return nil
}

func deleteSecurityGroup(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:            "123",
					SecurityGroupId: createdResources.SecurityGroupId,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// OK
type SecurityGroupPolicyInputs struct {
	Inputs []SecurityGroupPolicyInput `json:"inputs,omitempty"`
}

type SecurityGroupPolicyInput struct {
	CloudProviderParam
	Guid            string `json:"guid,omitempty"`
	ActionType      string `json:"actionType,omitempty"` //egress,ingress
	CidrIp          string `json:"cidrIp,omitempty"`
	IpProtocol      string `json:"ipProtocol,omitempty"`
	PortRange       string `json:"portRange,omitempty"`
	SecurityGroupId string `json:"securityGroupId,omitempty"`
	Policy          string `json:"policy,omitempty"`
}

func addSecurityPolicy(path string, createdResources *CreatedResources) error {
	inputs := SecurityGroupPolicyInputs{
		Inputs: []SecurityGroupPolicyInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				ActionType:         "egress",
				IpProtocol:         "[tcp]",
				PortRange:          "[1/200]",
				SecurityGroupId:    createdResources.SecurityGroupId,
				Policy:             "accept",
				CidrIp:             "[192.168.1.0/25]",
			},
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				ActionType:         "Ingress",
				IpProtocol:         "[udp]",
				PortRange:          "[1/200]",
				SecurityGroupId:    createdResources.SecurityGroupId,
				Policy:             "drop",
				CidrIp:             "[192.168.1.0/25]",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func deleteSecurityPolicy(path string, createdResources *CreatedResources) error {
	inputs := SecurityGroupPolicyInputs{
		Inputs: []SecurityGroupPolicyInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				ActionType:         "egress",
				IpProtocol:         "[tcp]",
				PortRange:          "[1/200]",
				SecurityGroupId:    createdResources.SecurityGroupId,
				Policy:             "accept",
				CidrIp:             "[192.168.1.0/25]",
			},
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				ActionType:         "Ingress",
				IpProtocol:         "[udp]",
				PortRange:          "[1/200]",
				SecurityGroupId:    createdResources.SecurityGroupId,
				Policy:             "drop",
				CidrIp:             "[192.168.1.0/25]",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func removeSecurityPolicy(path string, createdResources *CreatedResources) error {
	inputs := SecurityGroupPolicyInputs{
		Inputs: []SecurityGroupPolicyInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				ActionType:         "egress",
				IpProtocol:         "[tcp]",
				PortRange:          "[1/200]",
				SecurityGroupId:    createdResources.SecurityGroupId,
				Policy:             "accept",
				CidrIp:             "[192.168.1.0/25]",
			},
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				ActionType:         "Ingress",
				IpProtocol:         "[udp]",
				PortRange:          "[1/200]",
				SecurityGroupId:    createdResources.SecurityGroupId,
				Policy:             "drop",
				CidrIp:             "[192.168.1.0/25]",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// OK
type VmCreateInputs struct {
	Inputs []VmCreateInput `json:"inputs,omitempty"`
}

type VmCreateInput struct {
	CloudProviderParam
	Guid               string `json:"guid,omitempty"`
	Seed               string `json:"seed,omitempty"`
	InstanceSpec       string `json:"instanceSpec,omitempty"`
	ImageId            string `json:"imageId,omitempty"`
	SecurityGroupId    string `json:"securityGroupId,omitempty"`
	InstanceName       string `json:"instanceName,omitempty"`
	AutoRenew          string `json:"autoRenew,omitempty"`
	Password           string `json:"password,omitempty"`
	ZoneId             string `json:"zoneId,omitempty"`
	SystemDiskSize     string `json:"systemDiskSize,omitempty"`
	SystemDiskCategory string `json:"systemDiskCategory,omitempty"`
	VSwitchId          string `json:"vSwitchId,omitempty"`
	PrivateIpAddress   string `json:"privateIpAddress,omitempty"`
	InstanceChargeType string `json:"instanceChargeType,omitempty"`
	Period             string `json:"period,omitempty"`
	PeriodUnit         string `json:"periodUnit,omitempty"`
}

func createPrePaidVm(path string, createdResources *CreatedResources) error {
	inputs := VmCreateInputs{
		Inputs: []VmCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				Seed:               "seed",
				InstanceSpec:       "1C1G",
				ImageId:            VM_IMAGE_ID,
				SecurityGroupId:    createdResources.SecurityGroupId,
				InstanceName:       "testApiCreated_pre",
				AutoRenew:          "true",
				Password:           "Abcd1234",
				ZoneId:             AVAILABLE_ZONE,
				SystemDiskSize:     "40",
				VSwitchId:          createdResources.VSwitchId,
				InstanceChargeType: "PrePaid",
				Period:             "1",
				PeriodUnit:         "week",
			},
		},
	}

	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].InstanceId == "" {
		return fmt.Errorf("InstanceId is invaild")
	}
	createdResources.VmIdPrePaid = outputs.Outputs[0].InstanceId
	createdResources.VmIpPrePaid = outputs.Outputs[0].PrivateIp
	return nil
}

func deletePostPaidVm(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:       "123",
					InstanceId: createdResources.VmIdPostPaid,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func createPostPaidVm(path string, createdResources *CreatedResources) error {
	inputs := VmCreateInputs{
		Inputs: []VmCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				Seed:               "seed",
				InstanceSpec:       "1C1G",
				ImageId:            VM_IMAGE_ID,
				SecurityGroupId:    createdResources.SecurityGroupId,
				InstanceName:       "testApiCreated_post",
				Password:           "Abcd1234",
				ZoneId:             AVAILABLE_ZONE,
				SystemDiskSize:     "40",
				VSwitchId:          createdResources.VSwitchId,
				InstanceChargeType: "PostPaid",
			},
		},
	}

	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].InstanceId == "" {
		return fmt.Errorf("InstanceId is invaild")
	}
	createdResources.VmIdPostPaid = outputs.Outputs[0].InstanceId
	createdResources.VmIpPostPaid = outputs.Outputs[0].PrivateIp
	return nil
}

func deletePrePaidVm(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:       "234",
					InstanceId: createdResources.VmIdPrePaid,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// OK
type IpCreateInputs struct {
	Inputs []IpCreateInput `json:"inputs,omitempty"`
}
type IpCreateInput struct {
	CloudProviderParam
	Guid      string `json:"guid,omitempty"`
	Bandwidth string `json:"bandwidth,omitempty"`
	CbpName   string `json:"cbpName,omitempty"` // 共享带宽名字
	//ResourceGroupId string // NOT NEED
}

func createPublicIp(path string, createdResources *CreatedResources) error {
	inputs := IpCreateInputs{
		Inputs: []IpCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				Bandwidth:          "2",
				CbpName:            "apiTestCreated1",
			},
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				Bandwidth:          "2",
				CbpName:            "apiTestCreated2",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].EipId == "" && len(outputs.Outputs) == 2 {
		return fmt.Errorf("eipId is invaild")
	}
	createdResources.EipId = outputs.Outputs[0].EipId
	createdResources.EipAddress = outputs.Outputs[0].EipAddress
	createdResources.EipIdNat = outputs.Outputs[1].EipId
	createdResources.EipAddressNat = outputs.Outputs[1].EipAddress
	return nil
}

func deletePublicIp(path string, createdResource *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:  "123",
					EipId: createdResource.EipId,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

type IpAssociateInputs struct {
	Inputs []IpAssociateInput `json:"inputs,omitempty"`
}

type IpAssociateInput struct {
	CloudProviderParam
	Guid         string `json:"guid,omitempty"`
	InstanceId   string `json:"instanceId,omitempty"`
	AllocationId string `json:"allocationId,omitempty"`
	InstanceType string `json:"instanceType,omitempty"` // EcsInstance,Nat,SlbInstance
	//InstanceRegionId string
}

func associateIpToInstance(path string, createdResources *CreatedResources) error {
	inputs := IpAssociateInputs{
		Inputs: []IpAssociateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				InstanceId:         createdResources.VmIdPostPaid,
				AllocationId:       createdResources.EipId,
				InstanceType:       "EcsInstance",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func unassociateIpToInstance(path string, createdResources *CreatedResources) error {
	inputs := IpAssociateInputs{
		Inputs: []IpAssociateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				InstanceId:         createdResources.VmIdPostPaid,
				AllocationId:       createdResources.EipId,
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func stopVm(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:       "123",
					InstanceId: createdResources.VmIdPostPaid,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func startVm(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:       "123",
					InstanceId: createdResources.VmIdPostPaid,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

type SecurityGroupBindInputs struct {
	Inputs []SecurityGroupBindInput `json:"inputs,omitempty"`
}

type SecurityGroupBindInput struct {
	CloudProviderParam
	Guid            string `json:"guid,omitemepty"`
	SecurityGroupId string `json:"securityGroupId,omitempty"`
	InstanceId      string `json:"instanceId,omitempty"`
}

func bindSecurityGroup(path string, createdResources *CreatedResources) error {
	inputs := SecurityGroupBindInputs{
		Inputs: []SecurityGroupBindInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				SecurityGroupId:    createdResources.SecurityGroupId,
				InstanceId:         createdResources.VmIdPostPaid,
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func unbindSecurityGroup(path string, createdResources *CreatedResources) error {
	inputs := SecurityGroupBindInputs{
		Inputs: []SecurityGroupBindInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				SecurityGroupId:    createdResources.SecurityGroupId,
				InstanceId:         createdResources.VmIdPostPaid,
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// OK
type CENCreateInputs struct {
	Inputs []CENCreateInput `json:"inputs,omitempty"`
}

type CENCreateInput struct {
	CloudProviderParam
	Guid string `json:"guid,omitempty"`
	Name string `json:"name,omitempty"`
}

func createCEN(path string, createdResources *CreatedResources) error {
	inputs := CENCreateInputs{
		Inputs: []CENCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				Name:               "apiTestCreated",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].CENId == "" {
		return fmt.Errorf("CENId is invaild")
	}
	createdResources.CENId = outputs.Outputs[0].CENId
	return nil
}

func deleteCEN(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:  "123",
					CENId: createdResources.CENId,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// OK
type CENAttachInputs struct {
	Inputs []CENAttachInput `json:"inputs,omitempty"`
}

type CENAttachInput struct {
	CloudProviderParam
	Guid                  string `json:"guid,omitempty"`
	CENId                 string `json:"cenId,omitempty"`
	ChildInstanceId       string `json:"childInstanceId,omitempty"`   // VpcId
	ChildInstanceType     string `json:"childInstanceType,omitempty"` // VPC,VBR,CCN
	ChildInstanceRegionId string `json:"childInstanceRegionId,omitempty"`
}

func attachCEN(path string, createdResources *CreatedResources) error {
	inputs := CENAttachInputs{
		Inputs: []CENAttachInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				CENId:              createdResources.CENId,
				ChildInstanceId:    createdResources.VpcId,
				// ChildInstanceType:  "VPC",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func detachCEN(path string, createdResources *CreatedResources) error {
	inputs := CENAttachInputs{
		Inputs: []CENAttachInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				CENId:              createdResources.CENId,
				ChildInstanceId:    createdResources.VpcId,
				ChildInstanceType:  "VPC",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// OK
type NatCreateInputs struct {
	Inputs []NatCreateInput `json:"inputs,omitempty"`
}

type NatCreateInput struct {
	CloudProviderParam
	Guid      string `json:"guid,omitempty"`
	VpcId     string `json:"vpcId,omitempty"`
	Name      string `json:"name,omitempty"`
	VSwitchId string `json:"vSwitchId,omitempty"`
}

func createNatGateway(path string, createdResources *CreatedResources) error {
	inputs := NatCreateInputs{
		Inputs: []NatCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				VpcId:              createdResources.VpcId,
				VSwitchId:          createdResources.VSwitchId,
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].NatGatewayId == "" {
		return fmt.Errorf("natGatewayId is invaild")
	}
	createdResources.NatGatewayId = outputs.Outputs[0].NatGatewayId
	createdResources.SnatTableId = outputs.Outputs[0].SnatTableId
	return nil
}

func deleteNatGateway(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:         "123",
					NatGatewayId: createdResources.NatGatewayId,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// OK
type SnatCreateInputs struct {
	Inputs []SnatCreateInput `json:"inputs,omitempty"`
}

type SnatCreateInput struct {
	CloudProviderParam
	Guid            string `json:"guid,omitempty"`
	SnatIp          string `json:"snatIp,omitempty"`
	SnatTableId     string `json:"snatTableId,omitempty"`
	SourceVSwitchId string `json:"sourceVSwitchId,omitempty"`
	SnatEntryName   string `json:"snatEntryName,omitempty"`
	NatId           string `json:"natId,omitempty"`
	SnatEntryId     string `json:"snatEntryId,omitempty"`
}

func createSnatRule(path string, createdResources *CreatedResources) error {
	inputs := SnatCreateInputs{
		Inputs: []SnatCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				SnatIp:             createdResources.EipAddressNat,
				SnatTableId:        createdResources.SnatTableId,
				SourceVSwitchId:    createdResources.VSwitchId,
				SnatEntryName:      "apiTestCreated",
				NatId:              createdResources.NatGatewayId,
			},
		},
	}

	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].SnatEntryId == "" {
		return fmt.Errorf("snatEntryId is invaild")
	}
	createdResources.SnatEntryId = outputs.Outputs[0].SnatEntryId
	return nil
}

func deleteSnatRule(path string, createdResources *CreatedResources) error {
	inputs := SnatCreateInputs{
		Inputs: []SnatCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				SnatEntryId:        createdResources.SnatEntryId,
				SnatTableId:        createdResources.SnatTableId,
				NatId:              createdResources.NatGatewayId,
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

type RouteCreateInputs struct {
	Inputs []RouteCreateInput `json:"inputs,omitempty"`
}

type RouteCreateInput struct {
	CloudProviderParam
	Guid           string `json:"guid,omitempty"`
	VpcId          string `json:"vpcId,omitempty"`
	RouteTableName string `json:"routeTableName,omitempty"`
}

func createRoute(path string, createdResources *CreatedResources) error {
	inputs := RouteCreateInputs{
		Inputs: []RouteCreateInput{

			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				RouteTableName:     "apiTestCreated",
				VpcId:              createdResources.VpcId,
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].RouteTableId == "" {
		return fmt.Errorf("routeTableId is invaild")
	}
	createdResources.RouteTableId = outputs.Outputs[0].RouteTableId
	return nil
}

func deleteRoute(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:         "123",
					RouteTableId: createdResources.RouteTableId,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}

	return nil
}

type RouteRuleCreateInputs struct {
	Inputs []RouteRuleCreateInput `json:"inputs,omitempty"`
}

type RouteRuleCreateInput struct {
	CloudProviderParam
	Guid                 string `json:"guid,omitempty"`
	DestinationCidrBlock string `json:"destinationCidrBlock,omitempty"`
	RouteTableId         string `json:"routeTableId,omitempty"`
	NextHopId            string `json:"nextHopId,omitempty"`
	NextHopType          string `json:"nextHopType,omitempty"` // Instance,Nat
	RouteEntryName       string `json:"routeEntryName,omitempty"`
}

func createRouteRule(path string, createdResources *CreatedResources) error {
	inputs := RouteRuleCreateInputs{
		Inputs: []RouteRuleCreateInput{
			{
				CloudProviderParam:   getCloudProviderParam(),
				Guid:                 "123",
				DestinationCidrBlock: "10.1.1.0/24",
				RouteTableId:         createdResources.RouteTableId,
				NextHopId:            createdResources.VmIdPostPaid,
				// NextHopType:          "Instance",
				RouteEntryName: "apiTestCreated",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func deleteRouteRule(path string, createdResources *CreatedResources) error {
	inputs := RouteRuleCreateInputs{
		Inputs: []RouteRuleCreateInput{
			{
				CloudProviderParam:   getCloudProviderParam(),
				Guid:                 "123",
				DestinationCidrBlock: "10.1.1.0/24",
				RouteTableId:         createdResources.RouteTableId,
				NextHopId:            createdResources.VmIdPostPaid,
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// OK
type LbCreateInputs struct {
	Inputs []LbCreateInput `json:"inputs,omitempty"`
}

type LbCreateInput struct {
	CloudProviderParam
	Guid             string `json:"guid,omitempty"`
	AddressType      string `json:"addressType,omitempty"` // internet,intranet
	BandWidth        string `json:"bandWidth,omitempty"`   //-1 | 1-5120
	LoadBalancerName string `json:"loadBalancerName,omitempty"`
	VpcId            string `json:"vpcId,omitempty"`
	VSwitchId        string `json:"vSwitchId,omitempty"`
	MasterZoneId     string `json:"masterZoneId,omitempty"`
	PayType          string `json:"payType,omitempty"`
	PricingCycle     string `json:"pricingCycle,omitempty"` //month|year
	Duration         string `json:"duration,omitempty"`
}

func createExternalPreLb(path string, createdResources *CreatedResources) error {
	inputs := LbCreateInputs{
		Inputs: []LbCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				AddressType:        "intranet",
				BandWidth:          "1",
				LoadBalancerName:   "apiTestCreated_external_pre",
				VpcId:              createdResources.VpcId,
				VSwitchId:          createdResources.VSwitchId,
				MasterZoneId:       AVAILABLE_ZONE,
				PayType:            "PrePay",
				PricingCycle:       "month",
				Duration:           "1",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].LoadBalancerId == "" {
		return fmt.Errorf("loadBalancerId is invaild")
	}
	createdResources.ExternalPreLBId = outputs.Outputs[0].LoadBalancerId
	createdResources.ExternalPreLBAddress = outputs.Outputs[0].LBAddress
	return nil
}

func deleteExternalPreLb(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:           "123",
					LoadBalancerId: createdResources.ExternalPreLBId,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func createInternalLb(path string, createdResources *CreatedResources) error {
	inputs := LbCreateInputs{
		Inputs: []LbCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				AddressType:        "internet",
				BandWidth:          "1",
				LoadBalancerName:   "apiTestCreated",
				VpcId:              createdResources.VpcId,
				VSwitchId:          createdResources.VSwitchId,
				MasterZoneId:       AVAILABLE_ZONE,
				PayType:            "PayOnDemand",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].LoadBalancerId == "" {
		return fmt.Errorf("loadBalancerId is invaild")
	}
	createdResources.InternalLBId = outputs.Outputs[0].LoadBalancerId
	createdResources.InternalLBAddress = outputs.Outputs[0].LBAddress
	return nil
}

func deleteInternalLb(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:           "123",
					LoadBalancerId: createdResources.InternalLBId,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func createExternalLb(path string, createdResources *CreatedResources) error {
	inputs := LbCreateInputs{
		Inputs: []LbCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				AddressType:        "intranet",
				BandWidth:          "1",
				LoadBalancerName:   "apiTestCreated_external",
				VpcId:              createdResources.VpcId,
				VSwitchId:          createdResources.VSwitchId,
				MasterZoneId:       AVAILABLE_ZONE,
				PayType:            "PayOnDemand",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].LoadBalancerId == "" {
		return fmt.Errorf("loadBalancerId is invaild")
	}
	createdResources.ExternalLBId = outputs.Outputs[0].LoadBalancerId
	createdResources.ExternalLBAddress = outputs.Outputs[0].LBAddress
	return nil
}

func deleteExternalLb(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:           "123",
					LoadBalancerId: createdResources.ExternalLBId,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// OK
type TargetToLbInputs struct {
	Inputs []TargetToLbInput `json:"inputs,omitempty"`
}

type TargetToLbInput struct {
	CloudProviderParam
	Guid             string `json:"guid,omitempty"`
	LoadBalancerId   string `json:"loadBalancerId,omitempty"`
	VServerGroupName string `json:"vServerGroupName,omitempty"`
	VServerGroupId   string `json:"vServerGroupId,omitempty"`
	HostIds          string `json:"hostIds,omitempty"`
	HostPorts        string `json:"hostPorts,omitenpty"`
	Bandwidth        string `json:"bandwidth,omitempty"`
	ListenerPort     string `json:"listenerPort,omitempty"`
	ListenerProtocol string `json:"listenerProtocol,omitempty"`
	// TODO: need to add one bool variable to decide wwether delete listner
	DeleteListener string `json:"deleteListener,omitempty"`
}

func addTargetToLb(path string, createdResources *CreatedResources) error {
	inputs := TargetToLbInputs{
		Inputs: []TargetToLbInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				LoadBalancerId:     createdResources.ExternalLBId,
				HostIds:            "[" + createdResources.VmIdPostPaid + "]",
				HostPorts:          "[8080]",
				Bandwidth:          "5",
				ListenerPort:       "8080",
				ListenerProtocol:   "TCP",
				VServerGroupName:   "apiTestCreated",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].VServerGroupId == "" {
		return fmt.Errorf("vserverGroupId is invaild")
	}
	createdResources.VServerGroupId = outputs.Outputs[0].VServerGroupId
	return nil
}

func removeTargetfromLb(path string, createdResources *CreatedResources) error {
	inputs := TargetToLbInputs{
		Inputs: []TargetToLbInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				HostIds:            "[" + createdResources.VmIdPostPaid + "]",
				HostPorts:          "[8080]",
				ListenerPort:       "8080",
				VServerGroupId:     createdResources.VServerGroupId,
				DeleteListener:     "Y",
				ListenerProtocol:   "TCP",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].VServerGroupId == "" {
		return fmt.Errorf("vserverGroupId is invaild")
	}
	createdResources.VServerGroupId = outputs.Outputs[0].VServerGroupId
	return nil
}

// OK
type DiskBuyAndAttachInputs struct {
	Inputs []DiskBuyAndAttachInput `json:"inputs,omitempty"`
}

type DiskBuyAndAttachInput struct {
	CloudProviderParam
	Guid           string `json:"guid,omitempty"`
	Seed           string `json:"seed,omitempty"`
	Password       string `json:"password,omitempty"`
	FileSystemType string `json:"fileSystemType,omitempty"`
	MountDir       string `json:"mountDir,omitempty"`
	InstanceId     string `json:"instanceId,omitempty"`
	InstanceGuid   string `json:"instanceGuid,omitempty"`
	ZoneId         string `json:"zoneId,omitempty"`
	DiskName       string `json:"diskName,omitempty"`
	DiskSize       string `json:"size,omitempty"`
	DiskCategory   string `json:"diskCategory,omitempty"`
	VolumeName     string `json:"volumeName,omitempty"`
	UnmountDir     string `json:"unmountDir,omitempty"`
	DiskId         string `json:"diskId,omitempty"`
}

func buyAndAttachDisk(path string, createdResources *CreatedResources) error {
	inputs := DiskBuyAndAttachInputs{
		Inputs: []DiskBuyAndAttachInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				Seed:               "seed123",
				Password:           "Abcd1234",
				FileSystemType:     "ext3",
				MountDir:           "/data/test",
				InstanceId:         createdResources.VmIdPostPaid,
				InstanceGuid:       "123",
				ZoneId:             AVAILABLE_ZONE,
				DiskName:           "apiTeatCreated",
				DiskSize:           "40",
				DiskCategory:       "SATA",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].DiskId == "" {
		return fmt.Errorf("diskId is invaild")
	}
	createdResources.DiskId = outputs.Outputs[0].DiskId
	createdResources.VolumeName = outputs.Outputs[0].VolumeName
	return nil
}

func unAttachAndDestoryDisk(path string, createdResources *CreatedResources) error {
	inputs := DiskBuyAndAttachInputs{
		Inputs: []DiskBuyAndAttachInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				Seed:               "seed123",
				Password:           "Abcd1234",
				UnmountDir:         "/data/test",
				InstanceId:         createdResources.VmIdPostPaid,
				InstanceGuid:       "123",
				DiskId:             createdResources.DiskId,
				VolumeName:         createdResources.VolumeName,
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

type RdsCreateInputs struct {
	Inputs []RdsCreateInput `json:"inputs,omitempty"`
}

type RdsCreateInput struct {
	CloudProviderParam
	Guid                  string `json:"guid,omitempty"`
	Seed                  string `json:"seed,omitempty"`
	AccountName           string `json:"accountName,omitempty"`
	AccountPassword       string `json:"accountPassword,omitempty"`
	DBInstanceSpec        string `json:"dBInstanceSpec,omitempty"`
	Engine                string `json:"engine,omitempty"`
	EngineVersion         string `json:"engineVersion,omitempty"`
	PayType               string `json:"payType,omitempty"`        // Prepaid Postpaid
	UsedTime              string `json:"usedTime,omitempty"`       // 购买时长
	Period                string `json:"period,omitempty"`         // Month Year
	SecurityIPList        string `json:"securityIPList,omitempty"` // 白名单
	DBInstanceStorage     string `json:"dBInstanceStorage,omitempty"`
	DBInstanceStorageType string `json:"dBInstanceStorageType,omitempty"`
	ZoneId                string `json:"zoneId,omitempty"`
	VpcId                 string `json:"vpcId,omitempty"`
	VSwitchId             string `json:"vSwitchId,omitempty"`
	PrivateIpAddress      string `json:"privateIpAddress,omitempty"`
	AutoRenew             string `json:"autoRenew,omitenmpty"`
	DBIsIgnoreCase        string `json:"dBIsIgnoreCase,omitempty"` //大小写
	Category              string `json:"category,omitempty"`
	// TODO: character set
	// CharacterSet string `json:"characterSet,omitempty"`
}

func ceatePrePaidRds(path string, createdResources *CreatedResources) error {
	inputs := RdsCreateInputs{
		Inputs: []RdsCreateInput{
			{
				CloudProviderParam:    getCloudProviderParam(),
				Guid:                  "123",
				Seed:                  "seed123",
				AccountName:           "root1",
				AccountPassword:       "Abcd1234",
				DBInstanceSpec:        "1C1G",
				Engine:                "MySQL",
				EngineVersion:         "5.7",
				PayType:               "Prepaid",
				UsedTime:              "1",
				Period:                "Month",
				DBInstanceStorage:     "40",
				DBInstanceStorageType: "local_ssd",
				SecurityIPList:        "192.168.1.0/24",
				// ZoneId:                AVAILABLE_ZONE,
				VpcId:          createdResources.VpcId,
				VSwitchId:      createdResources.VSwitchId,
				AutoRenew:      "true",
				DBIsIgnoreCase: "0",
				Category:       "HighAvailability",
				// CharacterSet:       "utf8",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].DBInstanceId == "" {
		return fmt.Errorf("DBInstanceId is invaild")
	}
	createdResources.DBInstanceIdPrepaid = outputs.Outputs[0].DBInstanceId
	return nil
}

func deletePrePaidRds(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:         "123",
					DBInstanceId: createdResources.DBInstanceIdPrepaid,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func createPostPaidRds(path string, createdResources *CreatedResources) error {
	inputs := RdsCreateInputs{
		Inputs: []RdsCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				Seed:               "seed123",
				AccountName:        "root1",
				AccountPassword:    "Abcd1234",
				DBInstanceSpec:     "1C1G",
				Engine:             "MySQL",
				EngineVersion:      "5.7",
				PayType:            "Postpaid",
				// UsedTime:           "1",
				// Period:             "Month",
				DBInstanceStorage:     "40",
				DBInstanceStorageType: "local_ssd",
				SecurityIPList:        "192.168.1.0/24",
				// ZoneId:                AVAILABLE_ZONE,
				VpcId:     createdResources.VpcId,
				VSwitchId: createdResources.VSwitchId,
				// AutoRenew:          "true",
				DBIsIgnoreCase: "0",
				Category:       "HighAvailability",
				// CharacterSet:   "utf8",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].DBInstanceId == "" {
		return fmt.Errorf("DBInstanceId is invaild")
	}
	createdResources.DBInstanceIdPostpaid = outputs.Outputs[0].DBInstanceId
	return nil
}

func deletePostPaidRds(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:         "123",
					DBInstanceId: createdResources.DBInstanceIdPostpaid,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// NEED TO MODIFY
type RdsSecurityGroupModifyInputs struct {
	Inputs []RdsSecurityGroupModifyInput `json:"inputs,omitempty"`
}

type RdsSecurityGroupModifyInput struct {
	CloudProviderParam
	Guid         string `json:"guid,omitempty"`
	DBInstanceId string `json:"dBInstanceId,omitempty"`
	SecurityIps  string `json:"securityIps,omitempty"`
	ModifyMode   string `json:"modifyMode,omitempty"`
}

func modifyRdsSecurityGroup(path string, createdResources *CreatedResources) error {
	inputs := RdsSecurityGroupModifyInputs{
		Inputs: []RdsSecurityGroupModifyInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				DBInstanceId:       createdResources.DBInstanceIdPostpaid,

				// TODO:
				SecurityIps: "192.168.1.0/23",
				ModifyMode:  "update",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}

	return nil
}

type RdsBackupInputs struct {
	Inputs []RdsBackupInput `json:"inputs,omitempty"`
}

type RdsBackupInput struct {
	CloudProviderParam
	Guid           string `json:"guid,omitempty"`
	DBInstanceId   string `json:"dBInstanceId,omitempty"`
	DBName         string `json:"dBName,omitempty"`
	BackupStrategy string `josn:"backupStrategy,omitempty"`
	BackupMethod   string `json:"backupMethod,omitempty"`
}

func createRdsBackup(path string, createdResources *CreatedResources) error {
	inputs := RdsBackupInputs{
		Inputs: []RdsBackupInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				BackupStrategy:     "instance",
				BackupMethod:       "Physical",
				DBInstanceId:       createdResources.DBInstanceIdPostpaid,
			},
		},
	}
	output := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &output)
	if err != nil {
		return err
	}
	if output.Outputs[0].BackupId == "" {
		return fmt.Errorf("backupId is invaild")
	}
	createdResources.DBInstanceBackupId = output.Outputs[0].BackupId
	return nil
}

func deleteRdsBackup(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:         "123",
					DBInstanceId: createdResources.DBInstanceIdPostpaid,
					BackupId:     createdResources.DBInstanceBackupId,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

// OK
type RedisCreateInputs struct {
	Inputs []RedisCreateInput `json:"inputs,omitempty"`
}

type RedisCreateInput struct {
	CloudProviderParam
	Guid          string `json:"guid,omitempty"`
	Seed          string `json:"seed,omitempty"`
	InstanceName  string `json:"instanceName,omitempty"`
	Password      string `json:"password,omitempty"`
	Capacity      string `json:"capacity,omitempty"`
	ZoneId        string `json:"zoneId,omitempty"`
	ChargeType    string `json:"chargeType,omitempty"`
	EngineVersion string `json:"engineVersion,omitempty"`
	NetworkType   string `json:"networkType,omitempty"`
	VpcId         string `json:"vpcId,omitempty"`
	VSwitchId     string `json:"vSwitchId,omitempty"`
	Period        string `json:"period,omitempty"`
	AutoRenew     string `json:"autoRenew,omitempty"`
	// InstanceClass string `json:"instanceClass,omitempty"`

}

func createPostPaidRedis(path string, createdResources *CreatedResources) error {
	inputs := RedisCreateInputs{
		Inputs: []RedisCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				Seed:               "seed123",
				InstanceName:       "apiTestCreated",
				Password:           "Abcd1234",
				Capacity:           "2048",
				ChargeType:         "PostPaid",
				EngineVersion:      "5.0",
				NetworkType:        "VPC",
				VpcId:              createdResources.VpcId,
				VSwitchId:          createdResources.VSwitchId,
				ZoneId:             AVAILABLE_ZONE,
				// Peried: "1",
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].InstanceId == "" {
		return fmt.Errorf("redis instanceId is invaild")
	}
	createdResources.RedisIdPost = outputs.Outputs[0].InstanceId

	return nil
}

func deletePostPaidRedis(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:       "123",
					InstanceId: createdResources.RedisIdPost,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func createPrePaidRedis(path string, createdResources *CreatedResources) error {
	inputs := RedisCreateInputs{
		Inputs: []RedisCreateInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				Guid:               "123",
				Seed:               "seed123",
				InstanceName:       "apiTestCreatedPrePaid",
				Password:           "Abcd1234",
				Capacity:           "1024",
				ChargeType:         "PrePaid",
				EngineVersion:      "4.0",
				NetworkType:        "VPC",
				VpcId:              createdResources.VpcId,
				VSwitchId:          createdResources.VSwitchId,
				Period:             "1",
				AutoRenew:          "false",
				ZoneId:             AVAILABLE_ZONE,
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	if outputs.Outputs[0].InstanceId == "" {
		return fmt.Errorf("redis instanceId is invaild")
	}
	createdResources.RedisIdPrePaid = outputs.Outputs[0].InstanceId

	return nil
}

func deletePrePaidRedis(path string, createdResources *CreatedResources) error {
	inputs := ResourceDeleteInputs{
		Inputs: []ResourceDeleteInput{
			{
				CloudProviderParam: getCloudProviderParam(),
				ResourceOutput: ResourceOutput{
					Guid:       "123",
					InstanceId: createdResources.RedisIdPrePaid,
				},
			},
		},
	}
	outputs := ResourceOutputs{}
	err := doHttpRequest(path, inputs, &outputs)
	if err != nil {
		return err
	}
	return nil
}

func isValidPointer(response interface{}) error {
	if nil == response {
		return errors.New("input param should not be nil")
	}

	if kind := reflect.ValueOf(response).Type().Kind(); kind != reflect.Ptr {
		return errors.New("input param should be pointer type")
	}

	return nil
}

type PluginResponse struct {
	ResultCode string      `json:"resultCode"`
	ResultMsg  string      `json:"resultMessage"`
	Results    interface{} `json:"results"`
}

type HttpRequest struct {
	Request   interface{}
	RequestId string `json:"requestId,omitempty"`
	Operator  string `json:"operator,omitempty"`
}

func doHttpRequest(urlPath string, request interface{}, response interface{}) error {
	if err := isValidPointer(response); err != nil {
		return err
	}

	requestBytes, err := json.Marshal(request)
	if err != nil {
		return err
	}

	url := "http://" + envVars.PluginServerAddr + urlPath
	httpRequest, err := http.NewRequest(http.MethodPost, url, bytes.NewReader(requestBytes))
	if err != nil {
		return err
	}
	httpRequest.Header.Set("Content-type", "application/json")

	client := &http.Client{
		Timeout: time.Second * REQUEST_TIMEOUT,
	}

	httpResponse, err := client.Do(httpRequest)
	if err != nil {
		return err
	}
	if httpResponse != nil {
		defer httpResponse.Body.Close()
	}

	if httpResponse.StatusCode != 200 {
		return fmt.Errorf("Cmdb DoPostHttpRequest httpResponse.StatusCode != 200,statusCode=%v", httpResponse.StatusCode)
	}

	body, err := ioutil.ReadAll(httpResponse.Body)
	if err != nil {
		return err
	}

	//logrus.Debugf("Http response, url =%s,response=%s", url, string(body))
	commonResp := PluginResponse{}
	err = json.Unmarshal(body, &commonResp)
	if err != nil {
		return err
	}
	if commonResp.ResultCode != "0" {
		return fmt.Errorf(commonResp.ResultMsg)
	}

	outputBytes, _ := json.Marshal(commonResp.Results)
	err = json.Unmarshal(outputBytes, response)
	if err != nil {
		return err
	}
	return nil
}

func TestApi(t *testing.T) {
	createdResources := CreatedResources{}
	createdResources, err := getCreatedResources("createdResources.json")
	if err != nil {
		t.Logf("get createdResources meet err=%v", err)
		fmt.Printf("get createdResources meet err=%v\n", err)
	}

	// get environmental variables
	if err := loadEnvironmentVars(t); err != nil {
		t.Errorf("loadEnvironmentVars meet err=%v", err)
		fmt.Printf("loadEnvironmentVars meet err=%v\n", err)
		return
	}
	t.Logf("getCloudProviderParam:%++v", getCloudProviderParam())
	fmt.Printf("getCloudProviderParam:%++v\n", getCloudProviderParam())

	totalCase, failedCase := 0, 0

	t.Logf("start to create resources:")
	fmt.Printf("start to create resources:\n")
	for _, entry := range resourceCreateFuncTable {
		if entry.IsExpensiveResource && !CREATE_EXPENSIVE_RESOUCE {
			continue
		}
		totalCase++
		err := entry.Func(entry.ResourcePath, &createdResources)
		if err == nil {
			t.Logf("Test case%3d:%v run ok", totalCase, entry.TestApiName)
			fmt.Printf("Test case%3d:%v run ok\n", totalCase, entry.TestApiName)
			time.Sleep(time.Second * 10)
			// save createdResources
			err := saveCreatedResources("createdResources.json", createdResources)
			if err != nil {
				t.Logf("save createdResources meet err=%v", err)
				fmt.Printf("save createdResources meet err=%v\n", err)
			}
		} else {
			failedCase++
			t.Logf("Test case%3d:%v run failed, err=%v", totalCase, entry.TestApiName, err)
			fmt.Printf("Test case%3d:%v run failed, err=%v\n", totalCase, entry.TestApiName, err)
		}
	}
	t.Logf("createdResources=%++v", createdResources)
	fmt.Printf("createdResources=%++v\n", createdResources)

	t.Logf("\nstart to delete resources:")
	fmt.Printf("\nstart to delete resources:\n")
	for _, entry := range resourceDeleteFuncTable {
		if entry.IsExpensiveResource && !CREATE_EXPENSIVE_RESOUCE {
			continue
		}
		totalCase++
		err := entry.Func(entry.ResourcePath, &createdResources)
		if err == nil {
			t.Logf("Test case%3d:%v run ok", totalCase, entry.TestApiName)
			fmt.Printf("Test case%3d:%v run ok\n", totalCase, entry.TestApiName)
			time.Sleep(time.Second * 10)
		} else {
			failedCase++
			t.Logf("Test case%3d:%v run failed, err=%v", totalCase, entry.TestApiName, err)
			fmt.Printf("Test case%3d:%v run failed, err=%v\n", totalCase, entry.TestApiName, err)
		}
	}

	t.Logf("run %v test case, %v failed", totalCase, failedCase)
	fmt.Printf("run %v test case, %v failed\n", totalCase, failedCase)
}

func saveCreatedResources(fileName string, data interface{}) error {
	f, err := os.Create(fileName)
	if err != nil {
		return err
	}
	defer f.Close()

	byteData, err := json.MarshalIndent(data, "", "  ")
	if err != nil {
		return err
	}

	_, err = f.Write(byteData)
	return err
}

func getCreatedResources(fileName string) (CreatedResources, error) {
	createdResources := CreatedResources{}
	file, err := os.Open(fileName)
	if err != nil {
		return createdResources, err
	}

	defer file.Close()
	content, err := ioutil.ReadAll(file)
	if err != nil {
		return createdResources, err
	}

	err = json.Unmarshal(content, &createdResources)
	return createdResources, err
}

func TestSaveCreatedResources(t *testing.T) {
	createdResources := CreatedResources{
		InternalLBAddress: "127.0.0.1",
		VmIdPostPaid:      "dhjsbjksdfjkas",
		VmIdPrePaid:       "kdsjanfjknakls",
	}
	err := saveCreatedResources("createdResources.json", createdResources)
	if err != nil {
		t.Logf("err=%v", err)
	}
}

func TestGetCreatedResources(t *testing.T) {
	data, err := getCreatedResources("createdResources.json")
	if err != nil {
		t.Logf("err=%v", err)
	}
	fmt.Println("before sleep")
	time.Sleep(time.Second * 10)
	fmt.Println("after sleep")
	t.Logf("data:%++v", data)
}
