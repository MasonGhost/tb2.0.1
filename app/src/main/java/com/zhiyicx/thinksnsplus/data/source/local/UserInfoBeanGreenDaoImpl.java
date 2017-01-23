package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBeanDao;

import java.util.List;

/**
 * @Describe 用户信息存储实现
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class UserInfoBeanGreenDaoImpl extends CommonCacheImpl<UserInfoBean> {
    public UserInfoBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(UserInfoBean singleData) {
        UserInfoBeanDao userInfoBeanDao = getWDaoSession().getUserInfoBeanDao();
        return userInfoBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<UserInfoBean> multiData) {
        UserInfoBeanDao userInfoBeanDao = getWDaoSession().getUserInfoBeanDao();
        userInfoBeanDao.saveInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public UserInfoBean getSingleDataFromCache(Long primaryKey) {
        UserInfoBeanDao userInfoBeanDao = getRDaoSession().getUserInfoBeanDao();
        return userInfoBeanDao.load(primaryKey);
    }

    @Override
    public List<UserInfoBean> getMultiDataFromCache() {
        UserInfoBeanDao userInfoBeanDao = getRDaoSession().getUserInfoBeanDao();
        return userInfoBeanDao.loadAll();
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        UserInfoBeanDao userInfoBeanDao = getWDaoSession().getUserInfoBeanDao();
        userInfoBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void updateSingleData(UserInfoBean newData) {
        UserInfoBeanDao userInfoBeanDao = getWDaoSession().getUserInfoBeanDao();
        userInfoBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(UserInfoBean newData) {
        UserInfoBeanDao userInfoBeanDao = getWDaoSession().getUserInfoBeanDao();
        return userInfoBeanDao.insertOrReplace(newData);
    }

}
