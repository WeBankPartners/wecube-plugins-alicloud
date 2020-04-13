package com.webank.wecube.plugins.alicloud;

import com.webank.wecube.plugins.alicloud.support.password.PasswordManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PasswordManagerTest extends BaseSpringBootTest {

    @Autowired
    private PasswordManager passwordManager;

    private final String guid = "0015_0000000020";
    private final String seed = "seed-wecube2.1-2020";
    private final ***REMOVED***
    private final String encryptedPassword = "{cipher_a}5f5fb6fd46cd7cf9183893425d1d6506";


    @Test
    public void givenGuidSeedAndPassword_encryptPassword_shouldSucceed() {
        final String encryptedPassword = passwordManager.encryptPassword(guid, seed, this.password);
        assert this.encryptedPassword.equals(encryptedPassword);
    }

    @Test
    public void givenGuidSeedAndPassword_decryptPassword_shouldSucceed() {
        final String decryptedPassword = passwordManager.decryptPassword(guid, seed, this.encryptedPassword);
        assert this.password.equals(decryptedPassword);
    }
}

