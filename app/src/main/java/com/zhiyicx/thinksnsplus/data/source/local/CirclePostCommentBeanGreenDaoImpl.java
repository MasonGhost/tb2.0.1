package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/11/30/13:42
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostCommentBeanGreenDaoImpl extends CommonCacheImpl<CirclePostCommentBean> {

    @Inject
    public CirclePostCommentBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(CirclePostCommentBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<CirclePostCommentBean> multiData) {
        getWDaoSession().getCirclePostCommentBeanDao().insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public CirclePostCommentBean getSingleDataFromCache(Long primaryKey) {
        return getRDaoSession().getCirclePostCommentBeanDao().load(primaryKey);
    }

    @Override
    public List<CirclePostCommentBean> getMultiDataFromCache() {
        return getRDaoSession().getCirclePostCommentBeanDao().loadAll();
    }

    @Override
    public void clearTable() {
        getWDaoSession().getCirclePostCommentBeanDao().deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        getWDaoSession().getCirclePostCommentBeanDao().deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(CirclePostCommentBean dta) {
        getWDaoSession().getCirclePostCommentBeanDao().delete(dta);
    }

    @Override
    public void updateSingleData(CirclePostCommentBean newData) {
        insertOrReplace(newData);
    }

    @Override
    public long insertOrReplace(CirclePostCommentBean newData) {
        return getWDaoSession().getCirclePostCommentBeanDao().insertOrReplace(newData);
    }

    public List<CirclePostCommentBean> getMySendingComment(int i) {
        return new ArrayList<>();
    }

    public List<CirclePostCommentBean> getCommentByPostId(long postId) {
        return getRDaoSession().getCirclePostCommentBeanDao().queryBuilder()
                .where(CirclePostCommentBeanDao.Properties.Post_id.eq(postId)).build().list();
    }

    public CirclePostCommentBean getCircleCommentsByCommentMark(Long comment_mark) {
        List<CirclePostCommentBean> result = getRDaoSession().getCirclePostCommentBeanDao().queryBuilder()
                .where(CirclePostCommentBeanDao.Properties.Comment_mark.eq(comment_mark))
                .build()
                .list();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }
}
