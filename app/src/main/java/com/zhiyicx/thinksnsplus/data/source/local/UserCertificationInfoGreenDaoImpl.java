package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfoDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/4
 * @contact email:648129313@qq.com
 */

public class UserCertificationInfoGreenDaoImpl extends CommonCacheImpl<UserCertificationInfo>{

    private UserCertificationInfoDao mUserCertificationInfoDao;

    @Inject
    public UserCertificationInfoGreenDaoImpl(Application context) {
        super(context);
        mUserCertificationInfoDao = getWDaoSession().getUserCertificationInfoDao();
    }

    @Override
    public long saveSingleData(UserCertificationInfo singleData) {
        return mUserCertificationInfoDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<UserCertificationInfo> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public UserCertificationInfo getSingleDataFromCache(Long primaryKey) {
        return mUserCertificationInfoDao.load(primaryKey);
    }

    @Override
    public List<UserCertificationInfo> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mUserCertificationInfoDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(UserCertificationInfo dta) {

    }

    @Override
    public void updateSingleData(UserCertificationInfo newData) {
        mUserCertificationInfoDao.update(newData);
    }

    @Override
    public long insertOrReplace(UserCertificationInfo newData) {
        return mUserCertificationInfoDao.insertOrReplace(newData);
    }

    public UserCertificationInfo getInfoByUserId(){
        UserCertificationInfo info = null;
        List<UserCertificationInfo> list =
                mUserCertificationInfoDao.queryBuilder().
                        where(UserCertificationInfoDao.Properties.User_id.eq(AppApplication.getmCurrentLoginAuth().getUser_id())).list();
        if (list != null && list.size() > 0){
            info = list.get(0);
        }
        return info;
    }
}
