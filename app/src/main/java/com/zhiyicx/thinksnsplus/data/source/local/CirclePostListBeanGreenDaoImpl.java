package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/12/05/9:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostListBeanGreenDaoImpl extends CommonCacheImpl<CirclePostListBean> {

    @Inject
    CirclePostCommentBeanGreenDaoImpl mCirclePostCommentBeanGreenDao;

    @Inject
    public CirclePostListBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(CirclePostListBean singleData) {
        return insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<CirclePostListBean> multiData) {
        getWDaoSession().getCirclePostListBeanDao().insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public CirclePostListBean getSingleDataFromCache(Long primaryKey) {
        return getRDaoSession().getCirclePostListBeanDao().load(primaryKey);
    }

    @Override
    public List<CirclePostListBean> getMultiDataFromCache() {
        return getRDaoSession().getCirclePostListBeanDao().loadAll();
    }

    @Override
    public void clearTable() {
        getWDaoSession().getCirclePostListBeanDao().deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        getWDaoSession().getCirclePostListBeanDao().deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(CirclePostListBean dta) {
        getWDaoSession().getCirclePostListBeanDao().delete(dta);
    }

    @Override
    public void updateSingleData(CirclePostListBean newData) {

    }

    @Override
    public long insertOrReplace(CirclePostListBean newData) {
        return getWDaoSession().getCirclePostListBeanDao().insertOrReplace(newData);
    }

    public List<CirclePostListBean> getDataWithComments() {
        List<CirclePostListBean> data = getRDaoSession().getCirclePostListBeanDao().loadAll();
        for (CirclePostListBean postListBean : data) {
            postListBean.setComments(mCirclePostCommentBeanGreenDao.getCommentByPostId(postListBean.getId()));
        }
        return data;
    }
}
