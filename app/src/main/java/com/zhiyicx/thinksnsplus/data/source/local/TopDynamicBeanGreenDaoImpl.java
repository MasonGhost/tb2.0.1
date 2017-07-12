package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/12/17:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopDynamicBeanGreenDaoImpl extends CommonCacheImpl<TopDynamicBean> {

    private TopDynamicBeanDao mTopDynamicBeanDao;

    @Inject
    public TopDynamicBeanGreenDaoImpl(Application context) {
        super(context);
        mTopDynamicBeanDao = getWDaoSession().getTopDynamicBeanDao();
    }

    @Override
    public long saveSingleData(TopDynamicBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<TopDynamicBean> multiData) {
        mTopDynamicBeanDao.insertOrReplaceInTx(multiData);
    }

    public void saveMultiDataConvert(List<DynamicDetailBeanV2> multiData) {
        List<BaseListBean> convertData = new ArrayList<>();
        List<TopDynamicBean> realData = new ArrayList<>();
        convertData.addAll(multiData);
        for (BaseListBean data : convertData) {
            TopDynamicBean test = (TopDynamicBean) data;
            realData.add(test);
        }
        saveMultiData(realData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public TopDynamicBean getSingleDataFromCache(Long primaryKey) {
        return mTopDynamicBeanDao.load(primaryKey);
    }

    @Override
    public List<TopDynamicBean> getMultiDataFromCache() {
        return mTopDynamicBeanDao.loadAll();
    }

    public List<DynamicDetailBeanV2> getMultiConvertDataFromCache() {
        List<TopDynamicBean> localData = mTopDynamicBeanDao.loadAll();
        List<BaseListBean> multiData = new ArrayList<>();
        List<DynamicDetailBeanV2> resultData = new ArrayList<>();
        multiData.addAll(localData);
        for (BaseListBean data : multiData) {
            DynamicDetailBeanV2 test = (DynamicDetailBeanV2) data;
            resultData.add(test);
        }
        return resultData;
    }

    @Override
    public void clearTable() {
        mTopDynamicBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mTopDynamicBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(TopDynamicBean dta) {
        mTopDynamicBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(TopDynamicBean newData) {

    }

    @Override
    public long insertOrReplace(TopDynamicBean newData) {
        return mTopDynamicBeanDao.insertOrReplace(newData);
    }

//    public DynamicDetailBeanV2 topDynamic2normal(){
//
//    }
}
