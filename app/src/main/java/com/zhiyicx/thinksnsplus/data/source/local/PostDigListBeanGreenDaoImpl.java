package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/12/05/13:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PostDigListBeanGreenDaoImpl extends CommonCacheImpl<PostDigListBean> {

    @Inject
    public PostDigListBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(PostDigListBean singleData) {
        return getWDaoSession().getPostDigListBeanDao().insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<PostDigListBean> multiData) {
        getWDaoSession().getPostDigListBeanDao().insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public PostDigListBean getSingleDataFromCache(Long primaryKey) {
        return getRDaoSession().getPostDigListBeanDao().load(primaryKey);
    }

    @Override
    public List<PostDigListBean> getMultiDataFromCache() {
        return getRDaoSession().getPostDigListBeanDao().loadAll();
    }

    @Override
    public void clearTable() {
        getWDaoSession().getPostDigListBeanDao().deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        getWDaoSession().getPostDigListBeanDao().deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(PostDigListBean dta) {
        getWDaoSession().getPostDigListBeanDao().delete(dta);
    }

    @Override
    public void updateSingleData(PostDigListBean newData) {
        saveSingleData(newData);
    }

    @Override
    public long insertOrReplace(PostDigListBean newData) {
        return saveSingleData(newData);
    }

}
