package com.webank.wecube.plugins.alicloud.support.password;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author howechen
 */
@Component
public class PasswordManager {

    public PasswordManager() {
    }

    public String generatePassword() {
        return PasswordGenerator.generatePassword();
    }

    public String encryptPassword(String guid, String seed, String password, String specifiedCipher, String algo) throws PluginException {
        String result;
        try {
            result = PasswordEnigma.encryptPassword(guid, seed, password, specifiedCipher, algo);
        } catch (CryptoException e) {
            throw new PluginException(String.format("Encounter cryptoException while encrypting password, the message is: [%s].", e.getMessage()));
        }
        return result;
    }

    public String encryptPassword(String guid, String seed, String password) throws PluginException {
        return encryptPassword(guid, seed, password, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    public String encryptPassword(String guid, String seed, String password, String specifiedCipher) throws PluginException {
        return encryptPassword(guid, seed, password, specifiedCipher, StringUtils.EMPTY);
    }

    public String encryptGeneratedPassword(String guid, String seed, String specifiedCipher, String algo) throws PluginException {
        String result;
        try {
            String password = PasswordGenerator.generatePassword();
            result = PasswordEnigma.encryptPassword(guid, seed, password, specifiedCipher, algo);
        } catch (CryptoException e) {
            throw new PluginException(String.format("Encounter cryptoException while encrypting password, the message is: [%s].", e.getMessage()));
        }
        return result;
    }

    public String encryptGeneratedPassword(String guid, String seed, String specifiedCipher) throws PluginException {
        return encryptGeneratedPassword(guid, seed, specifiedCipher, StringUtils.EMPTY);
    }

    public String encryptGeneratedPassword(String guid, String seed) throws PluginException {
        return encryptGeneratedPassword(guid, seed, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    public String decryptPassword(String guid, String seed, String encryptedPassword, String specifiedCipher, String algo) throws PluginException {
        String result;

        try {
            result = PasswordEnigma.decryptPassword(guid, seed, encryptedPassword, specifiedCipher, algo);
        } catch (CryptoException e) {
            throw new PluginException(String.format("Encounter cryptoException while decrypting password, the message is: [%s].", e.getMessage()));
        }
        return result;
    }

    public String decryptPassword(String guid, String seed, String encryptedPassword, String specifiedCipher) throws PluginException {
        return decryptPassword(guid, seed, encryptedPassword, specifiedCipher, StringUtils.EMPTY);
    }

    public String decryptPassword(String guid, String seed, String encryptedPassword) throws PluginException {
        return decryptPassword(guid, seed, encryptedPassword, StringUtils.EMPTY, StringUtils.EMPTY);
    }

}
