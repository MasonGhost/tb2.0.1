package com.zhiyicx.thinksnsplus.data.source.local;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/23
 * @Contact master.jungle68@gmail.com
 */
public class BackgroundRequestTaskBeanGreenDaoImplTest {
    private BackgroundRequestTaskBeanGreenDaoImpl mBackgroundTaskBeanGreenDao;

    @Before
    public void setUp() throws Exception {
        mBackgroundTaskBeanGreenDao = new BackgroundRequestTaskBeanGreenDaoImpl(AppApplication.getContext());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void saveSingleData() throws Exception {
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setPath("zycx/100");
        HashMap<String, Object> params = new HashMap<>();
        params.put("user", "占三");
        backgroundRequestTaskBean.setParams(params);
        Assert.assertTrue(mBackgroundTaskBeanGreenDao.saveSingleData(backgroundRequestTaskBean) > 0);
    }

    @Test
    public void saveMultiData() throws Exception {
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setBackgroundtask_id(100L);
        backgroundRequestTaskBean.setPath("saveMultiData/1000");
        HashMap<String, Object> params = new HashMap<>();
        params.put("user", "saveMultiData占三");
        backgroundRequestTaskBean.setParams(params);
        BackgroundRequestTaskBean backgroundRequestTaskBean2 = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean2.setBackgroundtask_id(101L);
        backgroundRequestTaskBean2.setPath("saveMultiData/1002");
        HashMap<String, Object> params2 = new HashMap<>();
        params2.put("user", "saveMultiData占三2");
        backgroundRequestTaskBean2.setParams(params2);
        List<BackgroundRequestTaskBean> list = new ArrayList<>();
        list.add(backgroundRequestTaskBean);
        list.add(backgroundRequestTaskBean2);
        mBackgroundTaskBeanGreenDao.saveMultiData(list);
        Assert.assertTrue(mBackgroundTaskBeanGreenDao.getSingleDataFromCache(100L) != null);
    }

    @Test
    public void getSingleDataFromCache() throws Exception {

        Assert.assertTrue(mBackgroundTaskBeanGreenDao.getSingleDataFromCache(1L) != null);
    }

    @Test
    public void getMultiDataFromCache() throws Exception {
        List<BackgroundRequestTaskBean> datas = mBackgroundTaskBeanGreenDao.getMultiDataFromCache();
        System.out.println("datas = " + datas.toString());
        Assert.assertTrue(datas.size() > 0);
    }

    @Test
    public void clearTable() throws Exception {

    }

    @Test
    public void deleteSingleCache() throws Exception {

    }

    @Test
    public void updateSingleData() throws Exception {

    }

    @Test
    public void insertOrReplace() throws Exception {
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setBackgroundtask_id(1L);
        backgroundRequestTaskBean.setPath("zycx/replace");
        HashMap<String, Object> params = new HashMap<>();
        params.put("user", "repalce");
        backgroundRequestTaskBean.setParams(params);
        Assert.assertTrue(mBackgroundTaskBeanGreenDao.insertOrReplace(backgroundRequestTaskBean) > 0);
    }

}