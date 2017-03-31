package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/24
 * @contact email:450127106@qq.com
 */
public class DynamicDetailBeanGreenDaoImpl extends CommonCacheImpl<DynamicDetailBean> {
    @Inject
    public DynamicDetailBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(DynamicDetailBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<DynamicDetailBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DynamicDetailBean getSingleDataFromCache(Long primaryKey) {
        DynamicDetailBeanDao dynamicDetailBeanDao = getWDaoSession().getDynamicDetailBeanDao();
        return dynamicDetailBeanDao.load(primaryKey);
    }

    @Override
    public List<DynamicDetailBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        DynamicDetailBeanDao dynamicDetailBeanDao = getWDaoSession().getDynamicDetailBeanDao();
        dynamicDetailBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        DynamicDetailBeanDao dynamicDetailBeanDao = getWDaoSession().getDynamicDetailBeanDao();
        dynamicDetailBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(DynamicDetailBean dta) {
        DynamicDetailBeanDao dynamicDetailBeanDao = getWDaoSession().getDynamicDetailBeanDao();
        dynamicDetailBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(DynamicDetailBean newData) {

    }

    @Override
    public long insertOrReplace(DynamicDetailBean newData) {
        DynamicDetailBeanDao dynamicDetailBeanDao = getWDaoSession().getDynamicDetailBeanDao();
        return dynamicDetailBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<DynamicDetailBean> newData) {
        DynamicDetailBeanDao dynamicDetailBeanDao = getWDaoSession().getDynamicDetailBeanDao();
        dynamicDetailBeanDao.insertOrReplaceInTx(newData);
    }

}
