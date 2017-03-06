package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.log.LogUtils;
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
                , ApiConfig.MAX_NUMBER_PER_PAGE + "");
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
                , ApiConfig.MAX_NUMBER_PER_PAGE + ""
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
                , ApiConfig.MAX_NUMBER_PER_PAGE + "");
        return followFansBeanList;

    }

}
