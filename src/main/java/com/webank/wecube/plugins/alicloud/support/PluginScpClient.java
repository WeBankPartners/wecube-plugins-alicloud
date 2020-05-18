package com.webank.wecube.plugins.alicloud.support;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author howechen
 */
@Component
public class PluginScpClient {
    public static String DEFAULT_USER = "root";
    public static final int PORT = PluginSshdClient.PORT;

    private static final Logger logger = LoggerFactory.getLogger(PluginScpClient.class);

    public boolean isAuthedWithPassword(String ip, Integer port, String user, String password) {
        Connection connection = new Connection(ip, port);
        try {
            return connection.authenticateWithPassword(user, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean isAuthedWithPublicKey(String ip, Integer port, String user, File privateKey, String password) {
        Connection connection = new Connection(ip, port);
        try {
            return connection.authenticateWithPublicKey(user, privateKey, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAuth(String ip, Integer port, String user, String password, String privateKey, boolean usePassword) {
        if (usePassword) {
            return isAuthedWithPassword(ip, port, user, password);
        } else {
            return isAuthedWithPublicKey(ip, port, user, new File(privateKey), password);
        }
    }

    public void getFile(String ip, Integer port, String user, String password, String privateKey, boolean usePassword, String remoteFile, String path) {
        Connection connection = new Connection(ip, port);
        try {
            connection.connect();
            boolean isAuthed = isAuth(ip, port, user, password, privateKey, usePassword);
            if (isAuthed) {
                System.out.println("Authentication is successful!");
                SCPClient scpClient = connection.createSCPClient();
                scpClient.get(remoteFile, path);
            } else {
                System.out.println("Authentication failed!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public void putFile(String ip, Integer port, String user, String password, String privateKey, boolean usePassword, String localFile, String remoteTargetDirectory) {
        logger.info("Start to connect {}:{}", ip, port);
        Connection connection = new Connection(ip, port);
        try {
            connection.connect();
            boolean isAuthed = isAuth(ip, port, user, password, privateKey, usePassword);
            if (isAuthed) {
                SCPClient scpClient = connection.createSCPClient();
                scpClient.put(localFile, remoteTargetDirectory);
            } else {
                System.out.println("Authentication failed!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public void put(String ip, Integer port, String user, String password, String localFile, String remoteTargetDirectory) throws PluginException {
        Connection connection = new Connection(ip, port);
        try {
            connection.connect();
            boolean isConnected = connection.authenticateWithPassword(user, password);
            if (isConnected) {
                logger.info("Connection is OK");
                SCPClient scpClient = connection.createSCPClient();
                logger.info("scp local file [{}] to remote target directory [{}]", localFile, remoteTargetDirectory);
                scpClient.put(localFile, remoteTargetDirectory, "7777");
            } else {
                logger.info("User or password incorrect");
                throw new PluginException("User or password incorrect");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PluginException("Run 'scp' command meet error: " + e.getMessage());
        } finally {
            connection.close();
        }
    }

    public void put(String ip, Integer port, String user, String password, byte[] data, String remoteFileName, String remoteTargetDirectory) throws PluginException {
        Connection connection = new Connection(ip, port);
        try {
            connection.connect();
            boolean isConnected = connection.authenticateWithPassword(user, password);
            if (isConnected) {
                logger.info("Connection is OK");
                SCPClient scpClient = connection.createSCPClient();
                scpClient.put(data, remoteFileName, remoteTargetDirectory, "7777");
            } else {
                logger.info("User or password incorrect");
                throw new PluginException("User or password incorrect");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PluginException("Run 'scp' command meet error: " + e.getMessage());
        } finally {
            connection.close();
        }
    }
}

