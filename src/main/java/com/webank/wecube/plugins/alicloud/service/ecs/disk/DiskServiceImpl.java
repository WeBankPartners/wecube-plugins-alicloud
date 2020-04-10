package com.webank.wecube.plugins.alicloud.service.ecs.disk;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.ecs.disk.*;
import com.webank.wecube.plugins.alicloud.service.ecs.vm.VMService;
import com.webank.wecube.plugins.alicloud.support.*;
import com.webank.wecube.plugins.alicloud.support.password.PasswordManager;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimer;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimerTask;
import com.webank.wecube.plugins.alicloud.utils.PluginObjectUtils;
import com.webank.wecube.plugins.alicloud.utils.PluginResourceUtils;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author howechen
 */
@Service
public class DiskServiceImpl implements DiskService {

    private static Logger logger = LoggerFactory.getLogger(DiskService.class);

    private static final String DISK_SCRIPT_PATH = "classpath:diskScript/";
    private static final String MOUNT_SCRIPT_PATH = "classpath:diskScript/formatAndMountDisk.py";
    private static final String MOUNT_SCRIPT_NAME = "formatAndMountDisk.py";
    private static final String UNMOUNT_SCRIPT_PATH = "classpath:diskScript/unmountDisk.py";
    private static final String UNMOUNT_SCRIPT_NAME = "unmountDisk.py";
    private static final String UNFORMATTED_DISK_INFO_SCRIPT_PATH = "classpath:diskScript/getUnformatedDisk.py";
    private static final String UNFORMATTED_DISK_INFO_SCRIPT_NAME = "getUnformatedDisk.py";
    private static final String DEFAULT_REMOTE_DIRECTORY_PATH = "/tmp/";


    private AcsClientStub acsClientStub;
    private DtoValidator dtoValidator;
    private VMService vmService;
    private PluginScpClient pluginScpClient;
    private PluginSshdClient pluginSshdClient;
    private PasswordManager passwordManager;


    @Autowired
    public DiskServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator, VMService vmService, PluginScpClient pluginScpClient, PluginSshdClient pluginSshdClient, PasswordManager passwordManager) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
        this.vmService = vmService;
        this.pluginScpClient = pluginScpClient;
        this.pluginSshdClient = pluginSshdClient;
        this.passwordManager = passwordManager;
    }

    @Override
    public List<CoreCreateAttachDiskResponseDto> createAttachDisk(List<CoreCreateAttachDiskRequestDto> coreCreateAttachDiskRequestDtoList) {
        List<CoreCreateAttachDiskResponseDto> resultList = new ArrayList<>();
        for (CoreCreateAttachDiskRequestDto requestDto : coreCreateAttachDiskRequestDtoList) {

            CoreCreateAttachDiskResponseDto result = new CoreCreateAttachDiskResponseDto();
            try {

                dtoValidator.validate(requestDto);

                // check region id
                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                if (StringUtils.isEmpty(regionId)) {
                    throw new PluginException("The region id cannot be NULL or empty.");
                }

                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String diskId = requestDto.getDiskId();
                final String attachInstanceId = requestDto.getInstanceId();

                if (StringUtils.isNotEmpty(diskId)) {
                    // if disk id is not empty, retrieve disk info
                    final DescribeDisksResponse retrieveDiskResponse = this.retrieveDisk(client, regionId, diskId);
                    if (retrieveDiskResponse.getDisks().size() == 1) {
                        final DescribeDisksResponse.Disk foundDisk = retrieveDiskResponse.getDisks().get(0);
                        result.fromSdkCrossLineage(foundDisk);
                        result.setRequestId(retrieveDiskResponse.getRequestId());
                        continue;
                    }
                }

                // if disk id is empty, create disk
                // this toSdk() method would set instanceId to null due to the collision between zoneId and instanceId
                final CreateDiskRequest createDiskRequest = requestDto.toSdk();
                createDiskRequest.setRegionId(regionId);
                CreateDiskResponse response;
                response = this.acsClientStub.request(client, createDiskRequest);
                result = result.fromSdk(response);

                // setup a task to poll the the status of created disk until it is available
                Function<?, Boolean> func = o -> this.ifDiskInStatus(client, regionId, response.getDiskId(), DiskStatus.AVAILABLE);
                PluginTimer.runTask(new PluginTimerTask(func));

                // attach disk, reset instanceId back
                requestDto.setDiskId(response.getDiskId());
                requestDto.setInstanceId(attachInstanceId);
                final String volumeName = attachDisk(client, regionId, requestDto);

                result.setVolumeName(volumeName);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public List<CoreDetachDeleteDiskResponseDto> detachDeleteDisk(List<CoreDetachDeleteDiskRequestDto> coreDetachDeleteDiskRequestDtoList) {
        List<CoreDetachDeleteDiskResponseDto> resultList = new ArrayList<>();
        for (CoreDetachDeleteDiskRequestDto requestDto : coreDetachDeleteDiskRequestDtoList) {

            CoreDetachDeleteDiskResponseDto result = new CoreDetachDeleteDiskResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                if (StringUtils.isEmpty(requestDto.getDiskId())) {
                    throw new PluginException("The disk id cannot be empty or null.");
                }

                final String diskId = requestDto.getDiskId();
                final DescribeDisksResponse foundDiskInfo = this.retrieveDisk(client, regionId, diskId);

                // check if disk already deleted
                if (0 == foundDiskInfo.getTotalCount()) {
                    continue;
                }

                // detach disk from instance
                if (StringUtils.isNotEmpty(foundDiskInfo.getDisks().get(0).getInstanceId())) {
                    detachDisk(client, regionId, requestDto);
                }

                // delete disk
                logger.info("Deleting disk, disk ID: [{}], disk region:[{}]", diskId, regionId);
                DeleteDiskRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                final DeleteDiskResponse response = this.acsClientStub.request(client, request);

                // re-check if disk has already been deleted
                if (0 != this.retrieveDisk(client, regionId, diskId).getTotalCount()) {
                    String msg = String.format("The disk: [%s] from region: [%s] hasn't been deleted", diskId, regionId);
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }
        }
        return resultList;
    }

    private String attachDisk(IAcsClient client, String regionId, CoreCreateAttachDiskRequestDto requestDto) throws PluginException, AliCloudException {

        if (!EnumUtils.isValidEnumIgnoreCase(FileSystemType.class, requestDto.getFileSystemType())) {
            throw new PluginException("The given file system type is un-supported.");
        }

        final String vmInstanceIp = vmService.getVMIpAddress(client, regionId, requestDto.getInstanceId());
        final String password = passwordManager.decryptPassword(requestDto.getGuid(), requestDto.getSeed(), requestDto.getHostPassword());

        // scp diskScripts
        final List<String> resourceAbsolutePathList = PluginResourceUtils.getResourceAbsolutePath(UNFORMATTED_DISK_INFO_SCRIPT_PATH, MOUNT_SCRIPT_PATH);
        for (String resourceAbsolutePath : resourceAbsolutePathList) {
            pluginScpClient.put(vmInstanceIp, PluginScpClient.PORT, PluginScpClient.DEFAULT_USER, password, resourceAbsolutePath, DEFAULT_REMOTE_DIRECTORY_PATH);
        }

        // ssh to host and execute the getUnformattedDiskInfo script
        List<String> unformattedVolumeListBefore = getUnformattedDisk(vmInstanceIp, password).getVolumns();

        // attach disk request
        final AttachDiskRequest request = requestDto.toSdkCrossLineage(AttachDiskRequest.class);
        request.setRegionId(regionId);
        if (StringUtils.isAnyEmpty(request.getDiskId(), request.getInstanceId())) {
            throw new PluginException("Either disk ID or instance ID cannot be empty or null.");
        }

        this.acsClientStub.request(client, request);

        // check if the disk is in use
        Function<?, Boolean> func = o -> this.ifDiskInStatus(client, regionId, requestDto.getDiskId(), DiskStatus.IN_USE);
        PluginTimer.runTask(new PluginTimerTask(func));


        // ssh to host and execute the getUnformattedDiskInfo script again to check the difference
        // then can get the new attached disk to be formatted and mount that disk volume
        List<String> unformattedVolumeListAfter = getUnformattedDisk(vmInstanceIp, password).getVolumns();
        if (unformattedVolumeListAfter.size() <= unformattedVolumeListBefore.size()) {
            throw new PluginException("The new created disk hasn't added on to the VM instance");
        }

        // calculate the list difference
        unformattedVolumeListAfter.removeAll(unformattedVolumeListBefore);
        // get only one un-formatted disk
        final String needToFormatVolumeName = unformattedVolumeListAfter.get(0);

        // ssh to host and execute the format then mount script
        formatThenMountDisk(requestDto.getFileSystemType(), requestDto.getMountDir(), vmInstanceIp, password, needToFormatVolumeName);

        return needToFormatVolumeName;
    }

    private void detachDisk(IAcsClient client, String regionId, CoreDetachDeleteDiskRequestDto requestDto) throws PluginException, AliCloudException {


        final String vmInstanceIp = vmService.getVMIpAddress(client, regionId, requestDto.getInstanceId());
        final String password = passwordManager.decryptPassword(requestDto.getGuid(), requestDto.getSeed(), requestDto.getHostPassword());

        final DetachDiskRequest request = requestDto.toSdkCrossLineage(DetachDiskRequest.class);
        request.setRegionId(regionId);

        if (StringUtils.isAnyEmpty(request.getDiskId(), request.getInstanceId())) {
            throw new PluginException("Either disk ID or instance ID cannot be empty or null.");
        }

        // scp the unmount script to target server
        final String resourceAbsolutePath = PluginResourceUtils.getResourceAbsolutePath(UNMOUNT_SCRIPT_PATH);
        pluginScpClient.put(vmInstanceIp, PluginScpClient.PORT, PluginScpClient.DEFAULT_USER, password, resourceAbsolutePath, DEFAULT_REMOTE_DIRECTORY_PATH);

        // ssh to host and execute the unmount script
        unmountDisk(vmInstanceIp, password, requestDto.getVolumeName(), requestDto.getUnmountDir());

        DetachDiskResponse response = this.acsClientStub.request(client, request);

        // wait detaching process to finish
        Function<?, Boolean> func = o -> this.ifDiskInStatus(client, regionId, requestDto.getDiskId(), DiskStatus.AVAILABLE);
        PluginTimer.runTask(new PluginTimerTask(func));
    }

    @Override
    public DescribeDisksResponse retrieveDisk(IAcsClient client, String regionId, String diskId) throws PluginException, AliCloudException {

        if (StringUtils.isAnyEmpty(regionId, diskId)) {
            throw new PluginException("Either regionId or diskId cannot be null or empty.");
        }

        logger.info("Retrieving disk info, the disk ID: [{}].", diskId);

        // create new request
        DescribeDisksRequest retrieveDiskRequest = new DescribeDisksRequest();
        retrieveDiskRequest.setRegionId(regionId);
        retrieveDiskRequest.setDiskIds(PluginStringUtils.stringifyList(diskId));
        // send the request and handle the error, then return the response
        return this.acsClientStub.request(client, retrieveDiskRequest);
    }

    @Override
    public Boolean ifDiskInStatus(IAcsClient client, String regionId, String diskId, DiskStatus status) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, diskId)) {
            throw new PluginException("Either regionId or diskId cannot be null or empty.");
        }

        final DescribeDisksResponse describeDisksResponse = retrieveDisk(client, regionId, diskId);
        final Optional<DescribeDisksResponse.Disk> matchedDiskOpt = describeDisksResponse.getDisks().stream().filter(disk -> StringUtils.equals(diskId, disk.getDiskId())).findFirst();

        matchedDiskOpt.orElseThrow(() -> new PluginException(String.format("Cannot find disk by given diskId: [%s]", diskId)));

        final DescribeDisksResponse.Disk disk = matchedDiskOpt.get();

        return StringUtils.equals(status.getStatus(), disk.getStatus());
    }


    private void formatThenMountDisk(String fileSystemType, String mountDir, String vmInstanceIp, String password, String volumeName) throws PluginException {
        String command = "python "
                .concat(DEFAULT_REMOTE_DIRECTORY_PATH).concat(MOUNT_SCRIPT_NAME)
                .concat(" -d ").concat(volumeName)
                .concat(" -f ").concat(fileSystemType)
                .concat(" -m ").concat(mountDir);
        pluginSshdClient.runWithReturn(vmInstanceIp, PluginSshdClient.DEFAULT_USER, password, PluginSshdClient.PORT, command);
    }

    private UnformattedDiskDto getUnformattedDisk(String vmInstanceIp, String password) {
        final String runDiskInfoScriptResult = pluginSshdClient.runWithReturn(vmInstanceIp, PluginSshdClient.DEFAULT_USER, password, PluginSshdClient.PORT, "python ".concat(DEFAULT_REMOTE_DIRECTORY_PATH).concat(UNFORMATTED_DISK_INFO_SCRIPT_NAME));
        return PluginObjectUtils.mapJsonStringToObject(runDiskInfoScriptResult, UnformattedDiskDto.class);
    }

    private void unmountDisk(String vmInstanceIp, String password, String volumeName, String unmountDir) throws PluginException {
        String command = "python "
                .concat(DEFAULT_REMOTE_DIRECTORY_PATH).concat(UNMOUNT_SCRIPT_NAME)
                .concat(" -d ").concat(volumeName)
                .concat(" -m ").concat(unmountDir);
        pluginSshdClient.run(vmInstanceIp, PluginSshdClient.DEFAULT_USER, password, PluginSshdClient.PORT, command);
    }

    private Boolean ifDiskAttached(List<String> before, String vmInstanceIp, String password) {
        List<String> after = getUnformattedDisk(vmInstanceIp, password).getVolumns();
        return after.size() > before.size();
    }

}
