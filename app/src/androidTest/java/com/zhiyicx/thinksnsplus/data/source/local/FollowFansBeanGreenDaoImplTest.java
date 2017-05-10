package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;
import android.text.TextUtils;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/16
 * @Contact master.jungle68@gmail.com
 */
public class FollowFansBeanGreenDaoImplTest {
    private static final String TAG = "FollowFansBeanGreenDaoI";
    private FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    private FollowFansBean mFollowFansBean;

    @Before
    public void setUp() {
        mFollowFansBeanGreenDao = new FollowFansBeanGreenDaoImpl((Application) AppApplication.getContext());
        mFollowFansBean = new FollowFansBean();
        mFollowFansBean.setId(System.currentTimeMillis());
        UserInfoBean targetUserinfo = new UserInfoBean();
        targetUserinfo.setUser_id((long) 16);
        mFollowFansBean.setTargetUserInfo(targetUserinfo);
        mFollowFansBean.setTargetUserId(targetUserinfo.getUser_id());
        UserInfoBean originUserInfo = new UserInfoBean();
        originUserInfo.setUser_id((long) 10);
        mFollowFansBean.setOriginUserInfo(originUserInfo);
        mFollowFansBean.setOriginUserId(originUserInfo.getUser_id());
        mFollowFansBean.setOrigintargetUser(String.valueOf(originUserInfo.getUser_id()));

    }

    /**
     * 通过 id 获取工具栏
     *
     * @throws Exception
     */
    @Test
    public void getSingleDataFromCache() throws Exception {
        mFollowFansBeanGreenDao.insertOrReplace(mFollowFansBean);
        FollowFansBean followFansBean = mFollowFansBeanGreenDao.getSingleDataFromCache(mFollowFansBean.getId());
        LogUtils.d(TAG,"followFansBean = " + followFansBean.toString());
        Assert.assertTrue(followFansBean.getOriginUserId() == 10);
    }

    /**
     * 通过 id 删除数据
     *
     * @throws Exception
     */
    @Test
    public void deleteSingleCacheData() throws Exception {
        mFollowFansBeanGreenDao.insertOrReplace(mFollowFansBean);
        mFollowFansBeanGreenDao.deleteSingleCache(mFollowFansBean.getId());
        FollowFansBean dynamicToolBean = mFollowFansBeanGreenDao.getSingleDataFromCache(mFollowFansBean.getId());
        Assert.assertTrue(dynamicToolBean == null || dynamicToolBean.getId() == 0);
    }

    /**
     * 通过模型删除数据
     *
     * @throws Exception
     */
    @Test
    public void deleteSingleCacheByKey() throws Exception {
        mFollowFansBeanGreenDao.insertOrReplace(mFollowFansBean);
        mFollowFansBeanGreenDao.deleteSingleCache(mFollowFansBean);
        FollowFansBean dynamicToolBean = mFollowFansBeanGreenDao.getSingleDataFromCache(mFollowFansBean.getId());
        Assert.assertTrue(dynamicToolBean == null || dynamicToolBean.getId() == 0);
    }

    /**
     * 新增或者更新数据
     *
     * @throws Exception
     */
    @Test
    public void insertOrReplace() throws Exception {
        mFollowFansBeanGreenDao.insertOrReplace(mFollowFansBean);
        FollowFansBean dynamicToolBean = mFollowFansBeanGreenDao.getSingleDataFromCache(mFollowFansBean.getId());
        LogUtils.d(TAG,"dynamicToolBean = " + dynamicToolBean.toString());
        Assert.assertTrue(dynamicToolBean.getOriginUserId() == 10);
        Assert.assertTrue(!TextUtils.isEmpty(dynamicToolBean.getOrigintargetUser()));
    }

}