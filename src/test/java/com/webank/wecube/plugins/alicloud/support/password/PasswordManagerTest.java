package com.webank.wecube.plugins.alicloud.support.password;

import com.webank.wecube.plugins.alicloud.BaseSpringBootTest;
import com.webank.wecube.plugins.alicloud.support.password.PasswordManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PasswordManagerTest extends BaseSpringBootTest {

    @Autowired
    private PasswordManager passwordManager;

    @Test
    public void givenGuidSeedAndPassword_encryptPassword_shouldSucceed() {
      
    }

    @Test
    public void givenGuidSeedAndPassword_decryptPassword_shouldSucceed() {
       
    }
}

