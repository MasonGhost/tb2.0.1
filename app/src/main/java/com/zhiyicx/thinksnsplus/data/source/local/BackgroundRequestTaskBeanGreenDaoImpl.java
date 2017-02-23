<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBeanDao;

import java.util.List;

/**
 * @Describe 用户信息存储实现
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class BackgroundRequestTaskBeanGreenDaoImpl extends CommonCacheImpl<BackgroundRequestTaskBean> {
    public BackgroundRequestTaskBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(BackgroundRequestTaskBean singleData) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getWDaoSession().getBackgroundRequestTaskBeanDao();
        return backgroundRequestTaskBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<BackgroundRequestTaskBean> multiData) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getWDaoSession().getBackgroundRequestTaskBeanDao();
        backgroundRequestTaskBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public BackgroundRequestTaskBean getSingleDataFromCache(Long primaryKey) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getRDaoSession().getBackgroundRequestTaskBeanDao();
        return backgroundRequestTaskBeanDao.load(primaryKey);
    }

    @Override
    public List<BackgroundRequestTaskBean> getMultiDataFromCache() {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getRDaoSession().getBackgroundRequestTaskBeanDao();
        return backgroundRequestTaskBeanDao.loadAll();
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
    }

    @Override
    public void updateSingleData(BackgroundRequestTaskBean newData) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getWDaoSession().getBackgroundRequestTaskBeanDao();
        backgroundRequestTaskBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(BackgroundRequestTaskBean newData) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getWDaoSession().getBackgroundRequestTaskBeanDao();
        return backgroundRequestTaskBeanDao.insertOrReplace(newData);
    }

}
=======
package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBeanDao;

import java.util.List;

/**
 * @Describe 用户信息存储实现
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class BackgroundRequestTaskBeanGreenDaoImpl extends CommonCacheImpl<BackgroundRequestTaskBean> {
    public BackgroundRequestTaskBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(BackgroundRequestTaskBean singleData) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getWDaoSession().getBackgroundRequestTaskBeanDao();
        return backgroundRequestTaskBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<BackgroundRequestTaskBean> multiData) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getWDaoSession().getBackgroundRequestTaskBeanDao();
        backgroundRequestTaskBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public BackgroundRequestTaskBean getSingleDataFromCache(Long primaryKey) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getRDaoSession().getBackgroundRequestTaskBeanDao();
        return backgroundRequestTaskBeanDao.load(primaryKey);
    }

    @Override
    public List<BackgroundRequestTaskBean> getMultiDataFromCache() {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getRDaoSession().getBackgroundRequestTaskBeanDao();
        return backgroundRequestTaskBeanDao.loadAll();
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
    }

    @Override
    public void updateSingleData(BackgroundRequestTaskBean newData) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getWDaoSession().getBackgroundRequestTaskBeanDao();
        backgroundRequestTaskBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(BackgroundRequestTaskBean newData) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getWDaoSession().getBackgroundRequestTaskBeanDao();
        return backgroundRequestTaskBeanDao.insertOrReplace(newData);
    }

}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
