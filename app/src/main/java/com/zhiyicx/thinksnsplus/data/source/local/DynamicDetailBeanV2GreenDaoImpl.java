package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2Dao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/06/22/17:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicDetailBeanV2GreenDaoImpl extends CommonCacheImpl<DynamicDetailBeanV2> {

    private DynamicDetailBeanV2Dao mDynamicDetailBeanV2Dao;

    @Inject
    public DynamicDetailBeanV2GreenDaoImpl(Application context) {
        super(context);
        mDynamicDetailBeanV2Dao = getWDaoSession().getDynamicDetailBeanV2Dao();
    }

    @Override
    public long saveSingleData(DynamicDetailBeanV2 singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<DynamicDetailBeanV2> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DynamicDetailBeanV2 getSingleDataFromCache(Long primaryKey) {
        return mDynamicDetailBeanV2Dao.load(primaryKey);
    }

    @Override
    public List<DynamicDetailBeanV2> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        mDynamicDetailBeanV2Dao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mDynamicDetailBeanV2Dao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(DynamicDetailBeanV2 dta) {
        mDynamicDetailBeanV2Dao.delete(dta);
    }

    @Override
    public void updateSingleData(DynamicDetailBeanV2 newData) {

    }

    @Override
    public long insertOrReplace(DynamicDetailBeanV2 newData) {
        return mDynamicDetailBeanV2Dao.insertOrReplace(newData);
    }
}
