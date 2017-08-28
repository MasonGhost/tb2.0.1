package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/16/10:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AnswerCommentListBeanGreenDaoImpl extends CommonCacheImpl<AnswerCommentListBean> {

    AnswerCommentListBeanDao mAnswerCommentListBeanDao;

    @Inject
    public AnswerCommentListBeanGreenDaoImpl(Application context) {
        super(context);
        mAnswerCommentListBeanDao = getWDaoSession().getAnswerCommentListBeanDao();
    }

    @Override
    public long saveSingleData(AnswerCommentListBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<AnswerCommentListBean> multiData) {
        mAnswerCommentListBeanDao.insertOrReplaceInTx();
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public AnswerCommentListBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<AnswerCommentListBean> getMultiDataFromCache() {
        return mAnswerCommentListBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        mAnswerCommentListBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mAnswerCommentListBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(AnswerCommentListBean dta) {
        mAnswerCommentListBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(AnswerCommentListBean newData) {

    }

    @Override
    public long insertOrReplace(AnswerCommentListBean newData) {
        return mAnswerCommentListBeanDao.insertOrReplace(newData);
    }

    public AnswerCommentListBean getCommentByCommentMark(long comment_mark) {
        List<AnswerCommentListBean> result = mAnswerCommentListBeanDao.queryBuilder()
                .where(AnswerCommentListBeanDao.Properties.Comment_mark.eq(comment_mark))
                .list();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }
}
