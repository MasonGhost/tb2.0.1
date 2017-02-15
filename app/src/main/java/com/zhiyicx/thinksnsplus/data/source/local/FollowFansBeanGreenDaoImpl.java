package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.DaoMaster;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/15
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







}
