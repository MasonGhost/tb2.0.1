package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/18
 * @contact email:648129313@qq.com
 */

public class QuestionCommentBeanGreenDaoImpl extends CommonCacheImpl<QuestionCommentBean>{

    private QuestionCommentBeanDao mQuestionCommentBeanDao;

    @Inject
    public QuestionCommentBeanGreenDaoImpl(Application context) {
        super(context);
        mQuestionCommentBeanDao = getRDaoSession().getQuestionCommentBeanDao();
    }

    @Override
    public long saveSingleData(QuestionCommentBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<QuestionCommentBean> multiData) {
        mQuestionCommentBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public QuestionCommentBean getSingleDataFromCache(Long primaryKey) {
        return mQuestionCommentBeanDao.load(primaryKey);
    }

    @Override
    public List<QuestionCommentBean> getMultiDataFromCache() {
        return mQuestionCommentBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        mQuestionCommentBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mQuestionCommentBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(QuestionCommentBean dta) {
        mQuestionCommentBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(QuestionCommentBean newData) {
        mQuestionCommentBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(QuestionCommentBean newData) {
        return mQuestionCommentBeanDao.insertOrReplace(newData);
    }

    public QuestionCommentBean getCommentByCommentMark(long comment_mark) {
        List<QuestionCommentBean> result = mQuestionCommentBeanDao.queryBuilder()
                .where(QuestionCommentBeanDao.Properties.Comment_mark.eq(comment_mark))
                .list();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }
}
