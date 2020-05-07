package com.webank.wecube.plugins.alicloud.utils;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    @Deprecated
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
    @Deprecated
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
    @Deprecated
    static List<String> getAllResourceAbsolutePath(String directoryClassPath) throws PluginException {
        try {
            return Arrays.stream(Objects.requireNonNull(ResourceUtils.getFile(directoryClassPath).listFiles())).map(File::getAbsolutePath).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new PluginException(e.getMessage());
        }
    }

    /**
     * Get absolute path list from Resource array
     *
     * @param resources Resource array
     * @return absolute path list
     * @throws PluginException while getting the file
     */
    static List<String> getResourceAbsolutePath(org.springframework.core.io.Resource... resources) throws PluginException {
        List<String> result = new ArrayList<>();
        for (org.springframework.core.io.Resource resource : resources) {
            result.add(getResourceAbsolutePath(resource));
        }
        return result;
    }

    /**
     * Get absolute path from Resource
     *
     * @param resource Resource
     * @return absolute path
     * @throws PluginException while getting the file
     */
    static String getResourceAbsolutePath(Resource resource) throws PluginException {
        String result;
        try {
            result = resource.getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new PluginException(e.getMessage());
        }
        return result;
    }
}
