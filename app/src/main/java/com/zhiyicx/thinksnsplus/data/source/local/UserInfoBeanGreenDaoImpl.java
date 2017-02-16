package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

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
        userInfoBeanDao.insertInTx(multiData);
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

    public void insertOrReplace(List<UserInfoBean> newData) {
        UserInfoBeanDao userInfoBeanDao = getWDaoSession().getUserInfoBeanDao();
        userInfoBeanDao.insertOrReplaceInTx(newData);
    }

    /**
     * 获取某个人的粉丝列表的用户信息:谁关注了我
     */
    public List<UserInfoBean> getSomeOneFans(int userId) {
        UserInfoBeanDao userInfoBeanDao = getRDaoSession().getUserInfoBeanDao();
        QueryBuilder<UserInfoBean> qb = userInfoBeanDao.queryBuilder();
        // 关联用户信息表和关注粉丝表，通过关注粉丝表的主体用户id作为桥梁，关注粉丝表的被关注id是某个人
        qb.join(FollowFansBean.class, FollowFansBeanDao.Properties.UserId).where(FollowFansBeanDao
                .Properties.FollowedUserId.eq(userId));
        return qb.list();
    }

    /**
     * 获取某个人的关注列表的用户信息
     */
    public List<UserInfoBean> getSomeOneFollower(int userId) {
        UserInfoBeanDao userInfoBeanDao = getRDaoSession().getUserInfoBeanDao();
        QueryBuilder<UserInfoBean> qb = userInfoBeanDao.queryBuilder();
        // 关联用户信息表和关注粉丝表，通过关注粉丝表的被关注用户id作为桥梁，关注粉丝表的主体用户是某个人
        qb.join(FollowFansBean.class, FollowFansBeanDao.Properties.FollowedUserId)
                .where(FollowFansBeanDao.Properties.UserId.eq(userId));
        return qb.list();
    }

    /**
     * 获取某个人的相互关注列表的用户信息
     */
    public List<UserInfoBean> getSomeOneFollowEachOther(int userId) {
        UserInfoBeanDao userInfoBeanDao = getRDaoSession().getUserInfoBeanDao();
        QueryBuilder<UserInfoBean> qb = userInfoBeanDao.queryBuilder();
        // 关联用户信息表和关注粉丝表，通过关注粉丝表的被关注用户id作为桥梁，关注粉丝表的主体用户是某个人
        // ，并且关注状态为相互关注
        qb.join(FollowFansBean.class, FollowFansBeanDao.Properties.FollowedUserId)
                .where(FollowFansBeanDao.Properties.UserId.eq(userId), FollowFansBeanDao.Properties.FollowState.eq(2));
        return qb.list();
    }

}
