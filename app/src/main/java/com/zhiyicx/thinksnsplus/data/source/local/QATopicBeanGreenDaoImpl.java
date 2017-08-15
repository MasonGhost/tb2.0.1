package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.QATopicBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */

public class QATopicBeanGreenDaoImpl extends CommonCacheImpl<QATopicBean> {

    private QATopicBeanDao mQaTopicBeanDao;

    @Inject
    public QATopicBeanGreenDaoImpl(Application context) {
        super(context);
        mQaTopicBeanDao = getWDaoSession().getQATopicBeanDao();
    }

    @Override
    public long saveSingleData(QATopicBean singleData) {
        return mQaTopicBeanDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<QATopicBean> multiData) {
        mQaTopicBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public QATopicBean getSingleDataFromCache(Long primaryKey) {
        return mQaTopicBeanDao.load(primaryKey);
    }

    @Override
    public List<QATopicBean> getMultiDataFromCache() {
        return mQaTopicBeanDao.loadAll();
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mQaTopicBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(QATopicBean dta) {
        mQaTopicBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(QATopicBean newData) {
        mQaTopicBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(QATopicBean newData) {
        return mQaTopicBeanDao.insertOrReplace(newData);
    }
}
