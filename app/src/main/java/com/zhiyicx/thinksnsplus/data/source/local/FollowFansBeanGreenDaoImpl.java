package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;

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
        FollowFansBeanDao followFansBeanDao = getWDaoSession().getFollowFansBeanDao();
        return followFansBeanDao.load(primaryKey);
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
        FollowFansBeanDao followFansBeanDao = getWDaoSession().getFollowFansBeanDao();
        followFansBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(FollowFansBean dta) {
        FollowFansBeanDao followFansBeanDao = getWDaoSession().getFollowFansBeanDao();
        followFansBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(FollowFansBean newData) {

    }

    @Override
    public long insertOrReplace(FollowFansBean newData) {
        if (newData == null) {
            return -1;
        }
        FollowFansBeanDao followFansBeanDao = getWDaoSession().getFollowFansBeanDao();
        return followFansBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<FollowFansBean> newData) {
        if (newData == null) {
            return;
        }
        FollowFansBeanDao followFansBeanDao = getWDaoSession().getFollowFansBeanDao();
        followFansBeanDao.insertOrReplaceInTx(newData);
    }

    /**
     * 清除数据
     *
     * @param type
     * @param user_id
     */
    public void deleteDataByType(int type, Long user_id) {
        FollowFansBeanDao followFansBeanDao = getRDaoSession().getFollowFansBeanDao();
        List<FollowFansBean> datas;
        switch (type) {
            case FollowFansListFragment.FOLLOW_FRAGMENT_PAGE:
                datas = followFansBeanDao.queryDeep(" where " + FollowFansBeanDao
                                .Properties.OriginUserId.columnName + " = ? and "
                                + FollowFansBeanDao.Properties.Target_follow_status.columnName + " = ? "// 目标用户对我的关注状态为关注

                        , user_id + "", FollowFansBean.IFOLLOWED_STATE + "");
                followFansBeanDao.deleteInTx(datas);
                break;
            case FollowFansListFragment.FANS_FRAGMENT_PAGE:
                datas = followFansBeanDao.queryDeep(" where " + FollowFansBeanDao
                                .Properties.OriginUserId.columnName + " = ? and "
                                + FollowFansBeanDao.Properties.Target_follow_status.columnName + " = ? " // 目标用户对我的关注状态为关注
                        , user_id + ""
                        , FollowFansBean.IFOLLOWED_STATE + ""
                );
                followFansBeanDao.deleteInTx(datas);
                break;

            default:

        }

    }

    /**
     * 获取某个人的粉丝列表的用户信息:谁关注了我
     *
     * @param maxId 当前页面的最小id
     */
    public List<FollowFansBean> getSomeOneFans(int userId, int maxId) {
        // 第一次没有maxId，需要处理
        if (maxId <= 0) {
            maxId = Integer.MAX_VALUE;
        }
        FollowFansBeanDao followFansBeanDao = getRDaoSession().getFollowFansBeanDao();
        List<FollowFansBean> followFansBeanList = followFansBeanDao.queryDeep(" where " + FollowFansBeanDao
                        .Properties.OriginUserId.columnName + " = ? and "
                        + FollowFansBeanDao.Properties.Target_follow_status.columnName + " = ? and" // 目标用户对我的关注状态为关注
                        + " T.\"" + FollowFansBeanDao.Properties.Id.columnName + "\"" + " < ? "
                        + " order by " + "T.\"" + FollowFansBeanDao.Properties.Id.columnName + "\"" + " DESC"
                        + " limit ?"
                , userId + ""
                , FollowFansBean.IFOLLOWED_STATE + ""
                , maxId + ""
                , TSListFragment.DEFAULT_PAGE_SIZE + "");
        LogUtils.i("fansList_db-->" + followFansBeanList.size() + followFansBeanList.toString());
        return followFansBeanList;
    }

    /**
     * 获取某个人的关注列表的用户信息
     */
    public List<FollowFansBean> getSomeOneFollower(int userId, int maxId) {
        // 第一次没有maxId，需要处理
        if (maxId <= 0) {
            maxId = Integer.MAX_VALUE;
        }
        FollowFansBeanDao followFansBeanDao = getRDaoSession().getFollowFansBeanDao();
        List<FollowFansBean> followFansBeanList = followFansBeanDao.queryDeep(" where " + FollowFansBeanDao
                        .Properties.OriginUserId.columnName + " = ? and "
                        + FollowFansBeanDao.Properties.Origin_follow_status.columnName + " = ? and"// 我对目标用户的关注状态为关注
                        + " T.\"" + FollowFansBeanDao.Properties.Id.columnName + "\"" + " < ?"
                        + " order by " + "T.\"" + FollowFansBeanDao.Properties.Id.columnName + "\"" + " DESC"
                        + " limit ?"
                , userId + ""
                , FollowFansBean.IFOLLOWED_STATE + ""
                , maxId + ""
                , TSListFragment.DEFAULT_PAGE_SIZE + ""
        );
        LogUtils.i("followList_db-->" + followFansBeanList.size() + followFansBeanList.toString());
        return followFansBeanList;
    }

    /**
     * 获取某个人的相互关注列表的用户信息
     */
    public List<FollowFansBean> getSomeOneFollowEachOther(int userId, int maxId) {
        // 第一次没有maxId，需要处理
        if (maxId <= 0) {
            maxId = Integer.MAX_VALUE;
        }
        FollowFansBeanDao followFansBeanDao = getRDaoSession().getFollowFansBeanDao();
        List<FollowFansBean> followFansBeanList = followFansBeanDao.queryDeep(" where " + FollowFansBeanDao
                        .Properties.OriginUserId.columnName + " = ? and "
                        + FollowFansBeanDao.Properties.Origin_follow_status.columnName + " = ? and "
                        + FollowFansBeanDao.Properties.Target_follow_status.columnName + " = ? and "
                        + " T.\"" + FollowFansBeanDao.Properties.Id.columnName + "\"" + " < ?"
                        + " order by " + FollowFansBeanDao.Properties.Id.columnName + " DESC"
                        + " limit ?"
                , userId + ""
                , FollowFansBean.IFOLLOWED_STATE + ""
                , FollowFansBean.IFOLLOWED_STATE + ""
                , maxId + ""
                , TSListFragment.DEFAULT_PAGE_SIZE + "");
        return followFansBeanList;

    }

    /**
     * 获取主体用户对 目标用户的关注状态
     *
     * @param origin_user_id  主体用户 id
     * @param tartget_user_id 目标用户 id
     * @return
     */
    public FollowFansBean getFollowState(long origin_user_id, long tartget_user_id) {
        FollowFansBeanDao followFansBeanDao = getRDaoSession().getFollowFansBeanDao();
        List<FollowFansBean> result = followFansBeanDao.queryBuilder()
                .where(FollowFansBeanDao.Properties.OriginUserId.eq(origin_user_id), FollowFansBeanDao.Properties.TargetUserId.eq(tartget_user_id))
                .list();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

}
