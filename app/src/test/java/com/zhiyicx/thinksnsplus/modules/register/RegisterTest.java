package com.zhiyicx.thinksnsplus.modules.register;

import com.zhiyicx.thinksnsplus.data.source.remote.LoginClient;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/7
 * @Contact master.jungle68@gmail.com
 */
public class RegisterTest {

    private LoginClient mLoginClient;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void finalTest() {
        final BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMax_retry_count(1);
        Assert.assertTrue(backgroundRequestTaskBean.getMax_retry_count() == 1);
    }


}