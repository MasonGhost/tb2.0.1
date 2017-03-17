package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;
import android.text.TextUtils;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/16
 * @Contact master.jungle68@gmail.com
 */
public class DynamicDetailBeanGreenDaoImplTest {

    private DynamicDetailBeanGreenDaoImpl mDynamiDetialGreenDaoImpl;
    private DynamicDetailBean mDynamicDetailBean;

    @Before
    public void setUp() {
        mDynamiDetialGreenDaoImpl = new DynamicDetailBeanGreenDaoImpl((Application) AppApplication.getContext());
        mDynamicDetailBean = new DynamicDetailBean();
        mDynamicDetailBean.setFeed_mark(Long.valueOf("51" + System.currentTimeMillis()));
        mDynamicDetailBean.setContent("hello jungle68");
        mDynamicDetailBean.setFeed_content("hello feed_content");
        mDynamicDetailBean.setCreated_at(TimeUtils.millis2String(System.currentTimeMillis()));
        mDynamicDetailBean.setLocalPhotos(new ArrayList<String>());
        mDynamicDetailBean.setStorages(new ArrayList<ImageBean>());
        mDynamicDetailBean.setFeed_id(System.currentTimeMillis());
        mDynamicDetailBean.setFeed_title("jundle68 dynamic title");
        mDynamicDetailBean.setFeed_from(10);
    }

    /**
     * 通过 feed_mark 获取工具栏
     *
     * @throws Exception
     */
    @Test
    public void getSingleDataFromCache() throws Exception {
        mDynamiDetialGreenDaoImpl.insertOrReplace(mDynamicDetailBean);
        DynamicDetailBean dynamicToolBean = mDynamiDetialGreenDaoImpl.getSingleDataFromCache(mDynamicDetailBean.getFeed_mark());
        Assert.assertTrue(dynamicToolBean.getFeed_from() == 10);
    }

    /**
     * 通过 feed_mark 删除数据
     *
     * @throws Exception
     */
    @Test
    public void deleteSingleCacheData() throws Exception {
        mDynamiDetialGreenDaoImpl.insertOrReplace(mDynamicDetailBean);
        mDynamiDetialGreenDaoImpl.deleteSingleCache(mDynamicDetailBean.getFeed_mark());
        DynamicDetailBean dynamicToolBean = mDynamiDetialGreenDaoImpl.getSingleDataFromCache(mDynamicDetailBean.getFeed_mark());
        Assert.assertTrue(dynamicToolBean == null || dynamicToolBean.getFeed_from() == 0);
    }

    /**
     * 通过模型删除数据
     *
     * @throws Exception
     */
    @Test
    public void deleteSingleCacheByKey() throws Exception {
        mDynamiDetialGreenDaoImpl.insertOrReplace(mDynamicDetailBean);
        mDynamiDetialGreenDaoImpl.deleteSingleCache(mDynamicDetailBean);
        DynamicDetailBean dynamicToolBean = mDynamiDetialGreenDaoImpl.getSingleDataFromCache(mDynamicDetailBean.getFeed_mark());
        Assert.assertTrue(dynamicToolBean == null || dynamicToolBean.getFeed_from() == 0);
    }

    /**
     * 新增或者更新数据
     *
     * @throws Exception
     */
    @Test
    public void insertOrReplace() throws Exception {
        mDynamiDetialGreenDaoImpl.insertOrReplace(mDynamicDetailBean);
        DynamicDetailBean dynamicToolBean = mDynamiDetialGreenDaoImpl.getSingleDataFromCache(mDynamicDetailBean.getFeed_mark());
        System.out.println("dynamicToolBean = " + dynamicToolBean.toString());
        Assert.assertTrue(dynamicToolBean.getFeed_from() == 10);
        Assert.assertTrue(!TextUtils.isEmpty(dynamicToolBean.getContent()));
        Assert.assertTrue(!TextUtils.isEmpty(dynamicToolBean.getTitle()));
        Assert.assertTrue(!TextUtils.isEmpty(dynamicToolBean.getCreated_at()));
        Assert.assertTrue(!TextUtils.isEmpty(dynamicToolBean.getFeed_content()));
        Assert.assertTrue(!TextUtils.isEmpty(dynamicToolBean.getTitle()));
        Assert.assertTrue(null != dynamicToolBean.getStorages());
        Assert.assertTrue(null != dynamicToolBean.getLocalPhotos());
    }

}