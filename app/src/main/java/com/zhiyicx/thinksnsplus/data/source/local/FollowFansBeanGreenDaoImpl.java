package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/16
 * @contact email:450127106@qq.com
 */

public class FollowFansBeanGreenDaoImpl extends CommonCacheImpl<FollowFansBean> {
    public FollowFansBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(FollowFansBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<FollowFansBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public FollowFansBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<FollowFansBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void updateSingleData(FollowFansBean newData) {

    }

    @Override
    public long insertOrReplace(FollowFansBean newData) {
        FollowFansBeanDao followFansBeanDao = getWDaoSession().getFollowFansBeanDao();
        return followFansBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<FollowFansBean> newData) {
        FollowFansBeanDao followFansBeanDao = getWDaoSession().getFollowFansBeanDao();
        followFansBeanDao.insertOrReplaceInTx(newData);
    }

    /**
     * 获取某个人的粉丝列表的用户信息:谁关注了我
     */
    public List<FollowFansBean> getSomeOneFans(int userId) {
        FollowFansBeanDao followFansBeanDao = getRDaoSession().getFollowFansBeanDao();
        return followFansBeanDao.queryDeep("where " + FollowFansBeanDao
                .Properties.FollowedUserId.columnName + " = ? and " + FollowFansBeanDao.Properties.FollowState.columnName + " != ? ", userId + "", FollowFansBean.UNFOLLOWED_STATE + "");

        /*QueryBuilder<FollowFansBean> qb = followFansBeanDao.queryBuilder();
        // 粉丝关注表中，FollowedUserId为当前id，且不包含状态值为未关注的数据
        qb.where(FollowFansBeanDao
                .Properties.FollowedUserId.eq(userId), FollowFansBeanDao.Properties.FollowState.notEq(FollowFansBean.UNFOLLOWED_STATE));
        return qb.list();*/
    }

    /**
     * 获取某个人的关注列表的用户信息
     */
    public List<FollowFansBean> getSomeOneFollower(int userId) {
        FollowFansBeanDao followFansBeanDao = getRDaoSession().getFollowFansBeanDao();
        ;
        return followFansBeanDao.queryDeep("where " + FollowFansBeanDao
                .Properties.UserId.columnName + " = ? and " + FollowFansBeanDao.Properties.FollowState.columnName + " != ? ", userId + "", FollowFansBean.UNFOLLOWED_STATE + "");
       /* QueryBuilder<FollowFansBean> qb = followFansBeanDao.queryBuilder();
        // 粉丝关注表中，userID为当前id，且不包含状态值为未关注的数据
        qb.where(FollowFansBeanDao
                .Properties.UserId.eq(userId), FollowFansBeanDao.Properties.FollowState.notEq(FollowFansBean.UNFOLLOWED_STATE));
        return qb.list();*/
    }

    /**
     * 获取某个人的相互关注列表的用户信息
     */
    public List<FollowFansBean> getSomeOneFollowEachOther(int userId) {
        FollowFansBeanDao followFansBeanDao = getRDaoSession().getFollowFansBeanDao();
        return followFansBeanDao.queryDeep("where " + FollowFansBeanDao
                .Properties.UserId.columnName + " = ? and " + FollowFansBeanDao.Properties.FollowState.columnName + " = ? ", userId + "", FollowFansBean.FOLLOWED_EACHOTHER_STATE + "");
       /* QueryBuilder<FollowFansBean> qb = followFansBeanDao.queryBuilder();
        // 粉丝关注表中，userID为当前id，且不包含状态值为未关注的数据
        qb.where(FollowFansBeanDao
                .Properties.UserId.eq(userId), FollowFansBeanDao.Properties.FollowState.eq(FollowFansBean.FOLLOWED_EACHOTHER_STATE));
        return qb.list();*/
    }

    /**
     * 将某条数据设为关注状态
     *
     * @return 返回更新后的状态
     */
    public int setStateToFollowed(FollowFansBean newData) {
        if (newData == null) {
            return newData.getFollowState();
        }
        long userId = newData.getUserId();
        long followedId = newData.getFollowedUserId();
        FollowFansBeanDao followFansBeanDao = getWDaoSession().getFollowFansBeanDao();
        QueryBuilder<FollowFansBean> qb = followFansBeanDao.queryBuilder();
        // 看看数据库这个人是否关注了我
        qb.where(FollowFansBeanDao
                .Properties.UserFollowedId.eq(followedId + "$" + userId), FollowFansBeanDao
                .Properties.FollowState.notEq(FollowFansBean.UNFOLLOWED_STATE));
        if (qb.list().isEmpty()) {// 看来没有关注我
            newData.setFollowState(FollowFansBean.IFOLLOWED_STATE);
        } else {// 关注我了，那就互相关注吧
            newData.setFollowState(FollowFansBean.FOLLOWED_EACHOTHER_STATE);
            // 将另外一条数据设为互相关注
            FollowFansBean followedPerson = new FollowFansBean();
            followedPerson.setFollowState(FollowFansBean.FOLLOWED_EACHOTHER_STATE);
            followedPerson.setUserId(followedId);
            followedPerson.setFollowedUserId(userId);
            followedPerson.setUserFollowedId(null);
            insertOrReplace(followedPerson);
        }
        insertOrReplace(newData);
        return newData.getFollowState();
    }

    /**
     * 取消某条数据的关注状态
     */
    public int setStateToUnFollowed(FollowFansBean newData) {
        if (newData == null) {
            return newData.getFollowState();
        }
        long userId = newData.getUserId();
        long followedId = newData.getFollowedUserId();
        FollowFansBeanDao followFansBeanDao = getWDaoSession().getFollowFansBeanDao();
        QueryBuilder<FollowFansBean> qb = followFansBeanDao.queryBuilder();
        // 看看数据库这个人和我是否互相关注
        qb.where(FollowFansBeanDao
                .Properties.UserFollowedId.eq(followedId + "$" + userId), FollowFansBeanDao
                .Properties.FollowState.notEq(FollowFansBean.FOLLOWED_EACHOTHER_STATE));
        if (qb.list().isEmpty()) {// 看来没有互相关注
            newData.setFollowState(FollowFansBean.UNFOLLOWED_STATE);
        } else {// 关注我了，那就互相关注吧
            newData.setFollowState(FollowFansBean.UNFOLLOWED_STATE);
            // 将另外一条数据设为关注了我
            FollowFansBean followedPerson = new FollowFansBean();
            followedPerson.setFollowState(FollowFansBean.IFOLLOWED_STATE);
            followedPerson.setUserId(followedId);
            followedPerson.setFollowedUserId(userId);
            followedPerson.setUserFollowedId(null);
            insertOrReplace(followedPerson);
        }
        insertOrReplace(newData);
        return newData.getFollowState();
    }
}
