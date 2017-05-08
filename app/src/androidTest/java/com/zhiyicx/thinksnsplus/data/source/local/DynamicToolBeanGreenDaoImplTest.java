package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/16
 * @Contact master.jungle68@gmail.com
 */
public class DynamicToolBeanGreenDaoImplTest {
    private static final String TAG = "DynamicToolBeanGreenDao";
    private DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;
    private DynamicToolBean mDynamicToolBean;

    @Before
    public void setUp() {
        mDynamicToolBeanGreenDao = new DynamicToolBeanGreenDaoImpl((Application) AppApplication.getContext());
        mDynamicToolBean = new DynamicToolBean();
        mDynamicToolBean.setFeed_mark(Long.valueOf("50" + System.currentTimeMillis()));
        mDynamicToolBean.setFeed_comment_count(10);
        mDynamicToolBean.setFeed_digg_count(10);
        mDynamicToolBean.setIs_digg_feed(1);
        mDynamicToolBean.setFeed_view_count(10);
        mDynamicToolBean.setIs_collection_feed(10);
    }

    /**
     * 通过 feed_mark 获取工具栏
     * @throws Exception
     */
    @Test
    public void getSingleDataFromCache() throws Exception {
        mDynamicToolBeanGreenDao.insertOrReplace(mDynamicToolBean);
        DynamicToolBean dynamicToolBean = mDynamicToolBeanGreenDao.getSingleDataFromCache(mDynamicToolBean.getFeed_mark());
        Assert.assertTrue(dynamicToolBean.getFeed_comment_count() == 10);
    }

    /**
     * 通过 feed_mark 删除数据
     * @throws Exception
     */
    @Test
    public void deleteSingleCacheData() throws Exception {
        mDynamicToolBeanGreenDao.insertOrReplace(mDynamicToolBean);
        mDynamicToolBeanGreenDao.deleteSingleCache(mDynamicToolBean.getFeed_mark());
        DynamicToolBean dynamicToolBean = mDynamicToolBeanGreenDao.getSingleDataFromCache(mDynamicToolBean.getFeed_mark());
        Assert.assertTrue(dynamicToolBean==null||dynamicToolBean.getFeed_comment_count()==0);
    }

    /**
     * 通过模型删除数据
     * @throws Exception
     */
    @Test
    public void deleteSingleCacheByKey() throws Exception {
        mDynamicToolBeanGreenDao.insertOrReplace(mDynamicToolBean);
        mDynamicToolBeanGreenDao.deleteSingleCache(mDynamicToolBean);
        DynamicToolBean dynamicToolBean = mDynamicToolBeanGreenDao.getSingleDataFromCache(mDynamicToolBean.getFeed_mark());
        Assert.assertTrue(dynamicToolBean==null||dynamicToolBean.getFeed_comment_count()==0);
    }

    /**
     * 新增或者更新数据
     * @throws Exception
     */
    @Test
    public void insertOrReplace() throws Exception {
        mDynamicToolBeanGreenDao.insertOrReplace(mDynamicToolBean);
        DynamicToolBean dynamicToolBean = mDynamicToolBeanGreenDao.getSingleDataFromCache(mDynamicToolBean.getFeed_mark());
        LogUtils.d(TAG,"dynamicToolBean = " + dynamicToolBean.toString());
        Assert.assertTrue(dynamicToolBean.getFeed_comment_count() == 10);
        Assert.assertTrue(dynamicToolBean.getFeed_digg_count() == 10);
        Assert.assertTrue(dynamicToolBean.getIs_digg_feed() == 1);
        Assert.assertTrue(dynamicToolBean.getFeed_view_count() == 10);
        Assert.assertTrue(dynamicToolBean.getIs_collection_feed() == 10);
    }

}