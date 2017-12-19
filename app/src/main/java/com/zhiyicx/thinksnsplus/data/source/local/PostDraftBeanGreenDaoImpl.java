package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/12/19/14:57
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PostDraftBeanGreenDaoImpl extends CommonCacheImpl<PostDraftBean> {

    @Inject
    public PostDraftBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(PostDraftBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<PostDraftBean> multiData) {
        getWDaoSession().getPostDraftBeanDao().insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public PostDraftBean getSingleDataFromCache(Long primaryKey) {
        return getRDaoSession().getPostDraftBeanDao().load(primaryKey);
    }

    @Override
    public List<PostDraftBean> getMultiDataFromCache() {
        return getRDaoSession().getPostDraftBeanDao().loadAll();
    }

    @Override
    public void clearTable() {
        getWDaoSession().getPostDraftBeanDao().deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        getWDaoSession().getPostDraftBeanDao().deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(PostDraftBean dta) {
        getWDaoSession().getPostDraftBeanDao().delete(dta);
    }

    @Override
    public void updateSingleData(PostDraftBean newData) {

    }

    @Override
    public long insertOrReplace(PostDraftBean newData) {
        return getWDaoSession().getPostDraftBeanDao().insertOrReplace(newData);
    }
}
