package com.webank.wecube.plugins.alicloud;

import com.webank.wecube.plugins.alicloud.support.password.PasswordManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PasswordManagerTest extends BaseSpringBootTest {

    @Autowired
    private PasswordManager passwordManager;
    private String guid = "1234";
    private String seed = "2234";
    private String password = "pluginTest";
    private String encryptedPassword = "{cipher_a}6dff2cce25457bdd2ad2b516b3b48259";


    @Test
    public void encryptTest() {
        final String encryptedPassword = passwordManager.encryptPassword(guid, seed, this.password);
        assert this.encryptedPassword.equals(encryptedPassword);
    }

    @Test
    public void decryptTest() {
        final String decryptedPassword = passwordManager.decryptPassword(guid, seed, this.encryptedPassword);
        assert this.password.equals(decryptedPassword);
    }
}

