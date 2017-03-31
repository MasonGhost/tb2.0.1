package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe 动态数据库测试
 * @Author Jungle68
 * @Date 2017/3/2
 * @Contact master.jungle68@gmail.com
 */
public class DynamicBeanGreenDaoImplTest {

    @Rule
    public ActivityTestRule<SettingsActivity> mActivityRule = new ActivityTestRule(SettingsActivity.class);
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;

    @Before
    public void init() {
        mDynamicBeanGreenDao = new DynamicBeanGreenDaoImpl((Application) mActivityRule.getActivity().getApplicationContext());
    }

    /**
     * 测试插入动态 和更新
     *
     * @throws Exception
     */
    @Test
    public void insertOrReplace() throws Exception {
        List<DynamicBean> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DynamicBean dynamicBean = new DynamicBean();
            dynamicBean.setUser_id(1);
            dynamicBean.setState(1);
            Thread.sleep(1);
            dynamicBean.setFeed_mark(Long.valueOf(("1" + System.currentTimeMillis())));
            datas.add(dynamicBean);
        }
        mDynamicBeanGreenDao.insertOrReplace(datas);
        List<DynamicBean> result = mDynamicBeanGreenDao.getMySendingUnSuccessDynamic(1L);
        System.out.println("result = " + result.toString());
        Assert.assertTrue(result.size() > 0);
        result.get(0).setUser_id(2);
        mDynamicBeanGreenDao.insertOrReplace(result.get(0));
        Assert.assertTrue(mDynamicBeanGreenDao.getMySendingUnSuccessDynamic(2L).size() > 0);

    }

    /**
     * 获取热门的动态列表
     *
     * @throws Exception
     */
    @Test
    public void getHotDynamicList() throws Exception {
        long testid = 20000000;
        List<DynamicBean> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DynamicBean dynamicBean = new DynamicBean();
            dynamicBean.setFeed_id(testid + i);
            dynamicBean.setUser_id(1);
            dynamicBean.setIsFollowed(true);
            Thread.sleep(1);
            dynamicBean.setHot_creat_time(System.currentTimeMillis());
            dynamicBean.setFeed_mark(Long.valueOf(("1" + System.currentTimeMillis())));
            datas.add(dynamicBean);
        }
        mDynamicBeanGreenDao.insertOrReplace(datas);
        Thread.sleep(100);
        Assert.assertTrue(mDynamicBeanGreenDao.getHotDynamicList(System.currentTimeMillis()).size() > 0);
        Assert.assertTrue(mDynamicBeanGreenDao.getHotDynamicList(System.currentTimeMillis()).get(1).getHot_creat_time() > mDynamicBeanGreenDao.getHotDynamicList(System.currentTimeMillis()).get(2).getHot_creat_time());
    }

    /**
     * 获取关注列表动态
     *
     * @throws Exception
     */
    @Test
    public void getFollowedDynamicList() throws Exception {
        long testid = 20000000;
        List<DynamicBean> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DynamicBean dynamicBean = new DynamicBean();
            dynamicBean.setFeed_id(testid + i);
            dynamicBean.setUser_id(1);
            dynamicBean.setIsFollowed(true);
            Thread.sleep(1);
            dynamicBean.setFeed_mark(Long.valueOf(("1" + System.currentTimeMillis())));
            datas.add(dynamicBean);
        }
        mDynamicBeanGreenDao.insertOrReplace(datas);
        Assert.assertTrue(mDynamicBeanGreenDao.getFollowedDynamicList(testid + 10).get(0).getFeed_id() == 20000009);

    }

    /**
     * 获取最新动态
     *
     * @throws Exception
     */
    @Test
    public void getNewestDynamicList() throws Exception {
        long testid = 10000000;
        List<DynamicBean> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DynamicBean dynamicBean = new DynamicBean();
            dynamicBean.setFeed_id(testid + i);
            dynamicBean.setUser_id(1);
            dynamicBean.setState(1);
            Thread.sleep(1);
            dynamicBean.setFeed_mark(Long.valueOf(("1" + System.currentTimeMillis())));
            datas.add(dynamicBean);
        }
        mDynamicBeanGreenDao.insertOrReplace(datas);
        Assert.assertTrue(mDynamicBeanGreenDao.getNewestDynamicList(testid + 10).get(0).getFeed_id() == 10000009);
    }

    /**
     * 测试我没有发送成功的动态
     *
     * @throws Exception
     */
    @Test
    public void getMySendingUnsuccessDynamic() throws Exception {
        List<DynamicBean> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DynamicBean dynamicBean = new DynamicBean();
            dynamicBean.setUser_id(1);
            dynamicBean.setState(1);
            Thread.sleep(1);
            dynamicBean.setFeed_mark(Long.valueOf(("1" + System.currentTimeMillis())));
            datas.add(dynamicBean);
        }
        mDynamicBeanGreenDao.insertOrReplace(datas);
        List<DynamicBean> result = mDynamicBeanGreenDao.getMySendingUnSuccessDynamic(1L);
        Assert.assertTrue(result.size() > 0);
    }

    /**
     * 通过 Feed mark 获取动态
     *
     * @throws Exception
     */
    @Test
    public void getDynamicByFeedMark() throws Exception {
        DynamicBean dynamicBean = new DynamicBean();
        dynamicBean.setUser_id(1);
        dynamicBean.setState(1);
        Thread.sleep(1);
        Long feedmark = Long.valueOf(("1" + System.currentTimeMillis()));
        dynamicBean.setFeed_mark(feedmark);
        mDynamicBeanGreenDao.insertOrReplace(dynamicBean);
        DynamicBean result = mDynamicBeanGreenDao.getDynamicByFeedMark(feedmark);
        Assert.assertTrue(result.getUser_id() == 1);
    }

}