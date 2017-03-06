package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.DynamicBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;

import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */

public class DynamicDigListBeanGreenDaoImpl extends CommonCacheImpl<DynamicDigListBean> {
    @Inject
    public DynamicDigListBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DynamicDigListBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<DynamicDigListBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void updateSingleData(DynamicDigListBean newData) {

    }

    @Override
    public long insertOrReplace(DynamicDigListBean newData) {
        DynamicDigListBeanDao dynamicDigListBeanDao = getWDaoSession().getDynamicDigListBeanDao();
        return dynamicDigListBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<DynamicDigListBean> digListBeen) {
        DynamicDigListBeanDao dynamicDigListBeanDao = getWDaoSession().getDynamicDigListBeanDao();
        dynamicDigListBeanDao.insertOrReplaceInTx(digListBeen);
    }
}
