package com.webank.wecube.plugins.alicloud.service.vpc.eip;

/**
 * @author howechen
 */
public enum AssociatedInstanceType {
    // ecs
    EcsInstance,
    // slb
    SlbInstance,
    // nat
    Nat,
    // HaVip
    HaVip,
    // network interface
    NetworkInterface
}
