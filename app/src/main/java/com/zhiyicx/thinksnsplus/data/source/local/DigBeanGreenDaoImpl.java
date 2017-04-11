package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.DigBean;
import com.zhiyicx.thinksnsplus.data.beans.DigBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe  赞列表数据库
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */

public class DigBeanGreenDaoImpl extends CommonCacheImpl<DigBean> {

    @Inject
    public DigBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(DigBean singleData) {
        DigBeanDao digBeanDao = getWDaoSession().getDigBeanDao();
        return digBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<DigBean> multiData) {
        DigBeanDao digBeanDao = getWDaoSession().getDigBeanDao();
        digBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DigBean getSingleDataFromCache(Long primaryKey) {
        DigBeanDao digBeanDao = getRDaoSession().getDigBeanDao();
        return digBeanDao.load(primaryKey);
    }

    @Override
    public List<DigBean> getMultiDataFromCache() {
        DigBeanDao digBeanDao = getRDaoSession().getDigBeanDao();
        return digBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        DigBeanDao digBeanDao = getWDaoSession().getDigBeanDao();
        digBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        DigBeanDao digBeanDao = getWDaoSession().getDigBeanDao();
        digBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(DigBean dta) {
        DigBeanDao digBeanDao = getWDaoSession().getDigBeanDao();
        digBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(DigBean newData) {
        DigBeanDao digBeanDao = getWDaoSession().getDigBeanDao();
        digBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(DigBean newData) {
        DigBeanDao digBeanDao = getWDaoSession().getDigBeanDao();
        return digBeanDao.insertOrReplace(newData);
    }
}
