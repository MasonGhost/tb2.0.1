package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.GroupManagerBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe 圈子
 * @date 2017/7/17
 * @contact email:648129313@qq.com
 */

public class GroupInfoBeanGreenDaoImpl extends CommonCacheImpl<GroupInfoBean> {

    private GroupInfoBeanDao mGroupInfoBeanDao;

    @Inject
    public GroupInfoBeanGreenDaoImpl(Application context) {
        super(context);
        mGroupInfoBeanDao = getWDaoSession().getGroupInfoBeanDao();
    }

    @Override
    public long saveSingleData(GroupInfoBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<GroupInfoBean> multiData) {
        mGroupInfoBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public GroupInfoBean getSingleDataFromCache(Long primaryKey) {
        return mGroupInfoBeanDao.load(primaryKey);
    }

    @Override
    public List<GroupInfoBean> getMultiDataFromCache() {
        return mGroupInfoBeanDao.loadAll();
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mGroupInfoBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(GroupInfoBean dta) {
        if (dta != null) {
            mGroupInfoBeanDao.delete(dta);
        }
    }

    @Override
    public void updateSingleData(GroupInfoBean newData) {
        if (newData != null) {
            mGroupInfoBeanDao.update(newData);
        }
    }

    @Override
    public long insertOrReplace(GroupInfoBean newData) {
        if (newData != null) {
            return mGroupInfoBeanDao.insertOrReplace(newData);
        }
        return 0;
    }

    /**
     * 获取用户加入的圈子
     */
    public List<GroupInfoBean> getUserJoinedGroup() {
        List<GroupInfoBean> list = null;
        list = mGroupInfoBeanDao.queryBuilder().where(GroupInfoBeanDao.Properties.Is_member.eq(1)).list();
        return list;
    }
}
