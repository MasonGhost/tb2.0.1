package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.User;
import com.zhiyicx.thinksnsplus.data.beans.UserDao;

import java.util.List;

/**
 * @author LiuChao
 * @describe 用户信息存储
 * @date 2016/12/29
 * @contact email:450127106@qq.com
 */

public class UserCacheImpl extends CommonCacheImpl<User> {

    public UserCacheImpl(Context context) {
        super(context);
    }

    @Override
    public void saveSingleData(User singleData) {
        UserDao userDao = getRDaoSession().getUserDao();
        userDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<User> multiData) {
        UserDao userDao = getRDaoSession().getUserDao();
        userDao.insertInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public User getSingleDataFromCache(String key) {
        UserDao userDao = getRDaoSession().getUserDao();
        return userDao.load(Long.parseLong(key));
    }

    @Override
    public List<User> getMultiDataFromCache() {
        UserDao userDao = getRDaoSession().getUserDao();
        return userDao.loadAll();
    }

    @Override
    public void clearTable() {
        UserDao userDao = getWDaoSession().getUserDao();
        userDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(String key) {
        UserDao userDao = getWDaoSession().getUserDao();
        userDao.deleteByKey(Long.parseLong(key));
    }

    @Override
    public void updateSingleData(User newData) {
        // 根据User的主键ID更新对应的实体
        UserDao userDao = getWDaoSession().getUserDao();
        userDao.update(newData);
    }
}
