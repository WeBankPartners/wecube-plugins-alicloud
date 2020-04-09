package com.webank.wecube.plugins.alicloud.utils;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
public interface PluginResourceUtils {
    Logger logger = LoggerFactory.getLogger(PluginResourceUtils.class);

    /**
     * Return one file's absolute path
     *
     * @param resourceClassPath resource file's class path
     * @return file's absolute path
     * @throws PluginException when file cannot be found
     */
    static String getResourceAbsolutePath(String resourceClassPath) throws PluginException {
        try {
            return ResourceUtils.getFile(resourceClassPath).getAbsolutePath();
        } catch (FileNotFoundException e) {
            throw new PluginException(e.getMessage());
        }
    }

    /**
     * Return file's absolute path list
     *
     * @param resourceClassPaths resource file's class path array
     * @return file's absolute path
     * @throws PluginException when file cannot be found
     */
    static List<String> getResourceAbsolutePath(String... resourceClassPaths) throws PluginException {
        List<String> resultList = new ArrayList<>();
        for (String resourceClassPath : resourceClassPaths) {
            try {
                String result = ResourceUtils.getFile(resourceClassPath).getAbsolutePath();
                resultList.add(result);
            } catch (FileNotFoundException e) {
                throw new PluginException(e.getMessage());
            }
        }
        return resultList;
    }

    /**
     * Return files' absolute path which files are under the same directory
     *
     * @param directoryClassPath directory's class path
     * @return files' absolute path
     * @throws PluginException when file cannot be found
     */
    static List<String> getAllResourceAbsolutePath(String directoryClassPath) throws PluginException {
        try {
            return Arrays.stream(Objects.requireNonNull(ResourceUtils.getFile(directoryClassPath).listFiles())).map(File::getAbsolutePath).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new PluginException(e.getMessage());
        }
    }
}
