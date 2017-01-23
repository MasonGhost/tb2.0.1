package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBeanDao;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/6
 * @contact email:450127106@qq.com
 */

public class AuthBeanGreenDaoImpl extends CommonCacheImpl<AuthBean> {
    public AuthBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(AuthBean singleData) {
        AuthBeanDao authBeanDao = getWDaoSession().getAuthBeanDao();
     return    authBeanDao.insert(singleData);
//        authBeanDao.save(singleData);
    }

    @Override
    public void saveMultiData(List<AuthBean> multiData) {
        AuthBeanDao authBeanDao = getWDaoSession().getAuthBeanDao();
        authBeanDao.insertInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public AuthBean getSingleDataFromCache(Long primaryKey) {
        AuthBeanDao authBeanDao = getRDaoSession().getAuthBeanDao();
        return authBeanDao.load(primaryKey);
    }

    @Override
    public List<AuthBean> getMultiDataFromCache() {
        AuthBeanDao authBeanDao = getRDaoSession().getAuthBeanDao();
        return authBeanDao.loadAll();
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        AuthBeanDao authBeanDao = getWDaoSession().getAuthBeanDao();
        authBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void updateSingleData(AuthBean newData) {
        AuthBeanDao authBeanDao = getWDaoSession().getAuthBeanDao();
        authBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(AuthBean newData) {
        AuthBeanDao authBeanDao = getWDaoSession().getAuthBeanDao();
        return authBeanDao.insertOrReplace(newData);
    }
}
