package com.webank.wecube.plugins.alicloud.service.ecs.disk;

import com.google.common.io.ByteStreams;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author howechen
 */
@Component
public class DiskScriptHelper {
    protected static final String DISK_SCRIPT_PATH = "diskScript/";
    protected static final String MOUNT_SCRIPT_PATH = DISK_SCRIPT_PATH + "formatAndMountDisk.py";
    protected static final String MOUNT_SCRIPT_NAME = "formatAndMountDisk.py";
    protected static final String UNMOUNT_SCRIPT_PATH = DISK_SCRIPT_PATH + "unmountDisk.py";
    protected static final String UNMOUNT_SCRIPT_NAME = "unmountDisk.py";
    protected static final String UNFORMATTED_DISK_INFO_SCRIPT_PATH = DISK_SCRIPT_PATH + "getUnformattedDisk.py";
    protected static final String UNFORMATTED_DISK_INFO_SCRIPT_NAME = "getUnformattedDisk.py";
    protected static final String DEFAULT_REMOTE_DIRECTORY_PATH = "/tmp/";


    private final byte[] mountDiskByteArray;
    private final byte[] unmountDiskByteArray;
    private final byte[] unFormattedDiskByteArray;

    @SuppressWarnings("UnstableApiUsage")
    public DiskScriptHelper() throws IOException {
        this.mountDiskByteArray = ByteStreams.toByteArray(new ClassPathResource(MOUNT_SCRIPT_PATH).getInputStream());
        this.unmountDiskByteArray = ByteStreams.toByteArray(new ClassPathResource(UNMOUNT_SCRIPT_PATH).getInputStream());
        this.unFormattedDiskByteArray = ByteStreams.toByteArray(new ClassPathResource(UNFORMATTED_DISK_INFO_SCRIPT_PATH).getInputStream());
    }

    protected Pair<String, byte[]> getMountDiskScriptPair() {
        return new ImmutablePair<>(MOUNT_SCRIPT_NAME, mountDiskByteArray);
    }

    protected Pair<String, byte[]> getUnmountDiskScriptPair() {
        return new ImmutablePair<>(UNMOUNT_SCRIPT_NAME, unmountDiskByteArray);
    }

    protected Pair<String, byte[]> getUnFormattedDiskScriptPair() {
        return new ImmutablePair<>(UNFORMATTED_DISK_INFO_SCRIPT_NAME, unFormattedDiskByteArray);
    }
}
