package com.zhiyicx.thinksnsplus.data.source.local;


import android.app.Application;
import android.text.TextUtils;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */
public class UserInfoBeanGreenDaoImplTest {
    private UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Before
    public void setUp() {
        mUserInfoBeanGreenDao = new UserInfoBeanGreenDaoImpl((Application) AppApplication.getContext());
    }

    @Test
    public void saveSingleData() throws Exception {
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUser_id((long) ((long) (100 + Math.random() * 10) + 100 + Math.random() * 100));
        userInfoBean.setName("张三");
        Assert.assertTrue(mUserInfoBeanGreenDao.saveSingleData(userInfoBean) > 0);
    }

    @Test
    public void saveMultiData() throws Exception {

    }


    @Test
    public void getSingleDataFromCache() throws Exception {
        insertOrReplace();
        UserInfoBean userINfo = mUserInfoBeanGreenDao.getSingleDataFromCache(100L);
        Assert.assertFalse(TextUtils.isEmpty(userINfo.getName()));
    }

    @Test
    public void getMultiDataFromCache() throws Exception {
        List<UserInfoBean> userinfoBeans = mUserInfoBeanGreenDao.getMultiDataFromCache();
        LogUtils.d("userinfoBeans = " + userinfoBeans);
        Assert.assertTrue(userinfoBeans.size() > 0);
    }

    @Test
    public void clearTable() throws Exception {

    }

    @Test
    public void deleteSingleCache() throws Exception {

    }

    @Test
    public void insertOrReplace() throws Exception {
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUser_id(100L);
        userInfoBean.setName("张三 : " + Math.random() * 10 + " 号");
        Assert.assertTrue(mUserInfoBeanGreenDao.insertOrReplace(userInfoBean) > 0);
    }

}