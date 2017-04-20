package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 用户信息存储实现
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class BackgroundRequestTaskBeanGreenDaoImpl extends CommonCacheImpl<BackgroundRequestTaskBean> {
    @Inject
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

    /**
     * 获取某个人的后台任务列表
     *
     * @param user_id
     * @return
     */
    public List<BackgroundRequestTaskBean> getMultiDataFromCacheByUserId(Long user_id) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getRDaoSession().getBackgroundRequestTaskBeanDao();
        List<BackgroundRequestTaskBean> datas = backgroundRequestTaskBeanDao.queryBuilder()
                .where(BackgroundRequestTaskBeanDao.Properties.User_id.eq(user_id))
                .list();
        return datas;
    }

    @Override
    public void clearTable() {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getWDaoSession().getBackgroundRequestTaskBeanDao();
        backgroundRequestTaskBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
    }

    @Override
    public void deleteSingleCache(BackgroundRequestTaskBean dta) {
        BackgroundRequestTaskBeanDao backgroundRequestTaskBeanDao = getWDaoSession().getBackgroundRequestTaskBeanDao();
        backgroundRequestTaskBeanDao.delete(dta);
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
