package com.webank.wecube.plugins.alicloud.service.vpc.eip;

public enum EipStatus {
    // associating
    Associating,
    // un-associating
    Unassociating,
    // in use
    InUse,
    // available
    Available
}
