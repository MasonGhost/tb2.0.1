package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;
import android.util.SparseArray;

import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/16
 * @contact email:450127106@qq.com
 */

public class FollowFansBeanGreenDao extends CommonCacheImpl<FollowFansBean> {
    public FollowFansBeanGreenDao(Context context) {
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
        QueryBuilder<FollowFansBean> qb = followFansBeanDao.queryBuilder();
        // 粉丝关注表中，FollowedUserId为当前id，且不包含状态值为未关注的数据
        qb.where(FollowFansBeanDao
                .Properties.FollowedUserId.eq(userId), FollowFansBeanDao.Properties.FollowState.notEq(FollowFansBean.UNFOLLOWED_STATE));
        return qb.list();
    }

    /**
     * 获取某个人的关注列表的用户信息
     */
    public List<FollowFansBean> getSomeOneFollower(int userId) {
        FollowFansBeanDao followFansBeanDao = getRDaoSession().getFollowFansBeanDao();
        ;
        return followFansBeanDao.queryDeep("");
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
        QueryBuilder<FollowFansBean> qb = followFansBeanDao.queryBuilder();
        // 粉丝关注表中，userID为当前id，且不包含状态值为未关注的数据
        qb.where(FollowFansBeanDao
                .Properties.UserId.eq(userId), FollowFansBeanDao.Properties.FollowState.eq(FollowFansBean.FOLLOWED_EACHOTHER_STATE));
        return qb.list();
    }
}
