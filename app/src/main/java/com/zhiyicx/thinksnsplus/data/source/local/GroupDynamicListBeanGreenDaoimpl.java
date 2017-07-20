package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/18/17:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class GroupDynamicListBeanGreenDaoimpl extends CommonCacheImpl<GroupDynamicListBean> {

    private GroupDynamicListBeanDao mGroupDynamicListBeanDao;

    @Inject
    public GroupDynamicListBeanGreenDaoimpl(Application context) {
        super(context);
        mGroupDynamicListBeanDao = getWDaoSession().getGroupDynamicListBeanDao();
    }

    @Override
    public long saveSingleData(GroupDynamicListBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<GroupDynamicListBean> multiData) {
        mGroupDynamicListBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public GroupDynamicListBean getSingleDataFromCache(Long primaryKey) {
        return mGroupDynamicListBeanDao.load(primaryKey);
    }

    @Override
    public List<GroupDynamicListBean> getMultiDataFromCache() {
        return mGroupDynamicListBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        mGroupDynamicListBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mGroupDynamicListBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(GroupDynamicListBean dta) {
        mGroupDynamicListBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(GroupDynamicListBean newData) {

    }

    @Override
    public long insertOrReplace(GroupDynamicListBean newData) {
        return mGroupDynamicListBeanDao.insertOrReplace(newData);
    }

    public List<GroupDynamicListBean> getMySendingUnSuccessDynamic(long user_id) {
        return mGroupDynamicListBeanDao.queryDeep(" where " + " T." + GroupDynamicListBeanDao.Properties.User_id.columnName + " = ? and " + " T." +
                        GroupDynamicListBeanDao.Properties.State.columnName + " != " + GroupDynamicListBean.SEND_SUCCESS + "  ORDER BY "
                        + " T." + GroupDynamicListBeanDao.Properties.Id.columnName + " DESC "
                , String.valueOf(user_id));
    }

    public UserInfoBean getComment(){

        return null;
    }
}
