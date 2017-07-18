package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/05
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopDynamicCommentBeanGreenDaoImpl extends CommonCacheImpl<TopDynamicCommentBean> {

    private TopDynamicCommentBeanDao mTopDynamicCommentBeanDao;

    @Inject
    public TopDynamicCommentBeanGreenDaoImpl(Application context) {
        super(context);
        mTopDynamicCommentBeanDao = getWDaoSession().getTopDynamicCommentBeanDao();
    }

    @Override
    public long saveSingleData(TopDynamicCommentBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<TopDynamicCommentBean> multiData) {
        mTopDynamicCommentBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public TopDynamicCommentBean getSingleDataFromCache(Long primaryKey) {
        return mTopDynamicCommentBeanDao.load(primaryKey);
    }

    @Override
    public List<TopDynamicCommentBean> getMultiDataFromCache() {
        return mTopDynamicCommentBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        mTopDynamicCommentBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mTopDynamicCommentBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(TopDynamicCommentBean dta) {
        mTopDynamicCommentBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(TopDynamicCommentBean newData) {

    }

    @Override
    public long insertOrReplace(TopDynamicCommentBean newData) {
        return mTopDynamicCommentBeanDao.insertOrReplace(newData);
    }
}
