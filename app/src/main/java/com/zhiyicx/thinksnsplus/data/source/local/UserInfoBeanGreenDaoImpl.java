package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 用户信息存储实现
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class UserInfoBeanGreenDaoImpl extends CommonCacheImpl<UserInfoBean> {

    @Inject
    public UserInfoBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(UserInfoBean singleData) {
        UserInfoBeanDao userInfoBeanDao = getWDaoSession().getUserInfoBeanDao();
        return userInfoBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<UserInfoBean> multiData) {
        if (multiData == null) {
            return;
        }
        UserInfoBeanDao userInfoBeanDao = getWDaoSession().getUserInfoBeanDao();
        userInfoBeanDao.insertOrReplaceInTx(multiData);
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
        UserInfoBeanDao userInfoBeanDao = getWDaoSession().getUserInfoBeanDao();
        userInfoBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        UserInfoBeanDao userInfoBeanDao = getWDaoSession().getUserInfoBeanDao();
        userInfoBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(UserInfoBean dta) {

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

    public void insertOrReplace(List<UserInfoBean> newData) {
        UserInfoBeanDao userInfoBeanDao = getWDaoSession().getUserInfoBeanDao();
        userInfoBeanDao.insertOrReplaceInTx(newData);
    }

    /**
     * 获取本地关注列表
     *
     * @param maxId
     * @return
     */
    public List<UserInfoBean> getFollowingUserInfo(long maxId) {
        if (maxId == 0) {
            maxId = System.currentTimeMillis();
        }
        UserInfoBeanDao userInfoBeanDao = getRDaoSession().getUserInfoBeanDao();
        return userInfoBeanDao.queryBuilder()
                .where(UserInfoBeanDao.Properties.Follower.eq(true), UserInfoBeanDao.Properties.User_id.lt(maxId))
                .limit(TSListFragment.DEFAULT_PAGE_SIZE)
                .orderDesc(UserInfoBeanDao.Properties.User_id)
                .list();
    }

    /**
     * 获取本地粉丝列表
     *
     * @param maxId
     * @return
     */
    public List<UserInfoBean> getFollowerUserInfo(long maxId) {
        if (maxId == 0) {
            maxId = System.currentTimeMillis();
        }
        UserInfoBeanDao userInfoBeanDao = getRDaoSession().getUserInfoBeanDao();
        return userInfoBeanDao.queryBuilder()
                .where(UserInfoBeanDao.Properties.Following.eq(true), UserInfoBeanDao.Properties.User_id.lt(maxId))
                .limit(TSListFragment.DEFAULT_PAGE_SIZE)
                .orderDesc(UserInfoBeanDao.Properties.User_id)
                .list();
    }

    public List<UserInfoBean> getUserInfoByPhone(String phone) {
        UserInfoBeanDao userInfoBeanDao = getRDaoSession().getUserInfoBeanDao();
        return userInfoBeanDao.queryBuilder()
                .where(UserInfoBeanDao.Properties.Phone.eq(phone))
                .list();

    }

    /**
     * 获取本地的互相关注列表
     *
     * @param maxId
     * @return
     */
    public List<UserInfoBean> getUserFriendsList(long maxId) {
        if (maxId == 0) {
            maxId = System.currentTimeMillis();
        }
        UserInfoBeanDao userInfoBeanDao = getRDaoSession().getUserInfoBeanDao();
        return userInfoBeanDao.queryBuilder()
                .where(UserInfoBeanDao.Properties.Follower.eq(true), UserInfoBeanDao.Properties.Following.eq(true), UserInfoBeanDao.Properties.User_id.lt(maxId))
                .limit(TSListFragment.DEFAULT_PAGE_SIZE)
                .orderDesc(UserInfoBeanDao.Properties.User_id)
                .list();
    }

    /**
     * 获取本地的互相关注列表
     *
     * @param ids 用户id
     * @return
     */
    public List<UserInfoBean> getUserListByIds(Long... ids) {
        List<UserInfoBean> result = new ArrayList<>();
        if (ids.length <= 0) {
            return result;
        }
        for (Long id : ids) {
            UserInfoBean userInfoBean = getSingleDataFromCache(id);
            result.add(userInfoBean);
        }
        return result;
    }

    public String getUserName(String id) {
        Long userId = null;
        try {
            userId = Long.parseLong(id);
        } catch (Exception e) {
            return "未知用户";
        }
        UserInfoBeanDao userInfoBeanDao = getRDaoSession().getUserInfoBeanDao();
        UserInfoBean userInfoBean = userInfoBeanDao.load(userId);
        return userInfoBean.getName();
    }

}
