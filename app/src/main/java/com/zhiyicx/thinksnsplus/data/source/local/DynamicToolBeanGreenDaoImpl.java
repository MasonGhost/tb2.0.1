package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/24
 * @contact email:450127106@qq.com
 */

public class DynamicToolBeanGreenDaoImpl extends CommonCacheImpl<DynamicToolBean> {
    @Inject
    public DynamicToolBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(DynamicToolBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<DynamicToolBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DynamicToolBean getSingleDataFromCache(Long primaryKey) {
        DynamicToolBeanDao dynamicToolBeanDao = getWDaoSession().getDynamicToolBeanDao();
        return dynamicToolBeanDao.load(primaryKey);
    }

    @Override
    public List<DynamicToolBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        DynamicToolBeanDao dynamicToolBeanDao = getWDaoSession().getDynamicToolBeanDao();
        dynamicToolBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        DynamicToolBeanDao dynamicToolBeanDao = getWDaoSession().getDynamicToolBeanDao();
        dynamicToolBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(DynamicToolBean dta) {
        DynamicToolBeanDao dynamicToolBeanDao = getWDaoSession().getDynamicToolBeanDao();
        dynamicToolBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(DynamicToolBean newData) {

    }

    @Override
    public long insertOrReplace(DynamicToolBean newData) {
        DynamicToolBeanDao dynamicToolBeanDao = getWDaoSession().getDynamicToolBeanDao();
        return dynamicToolBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<DynamicToolBean> newData) {
        DynamicToolBeanDao dynamicToolBeanDao = getWDaoSession().getDynamicToolBeanDao();
        dynamicToolBeanDao.insertOrReplaceInTx(newData);
    }
}
