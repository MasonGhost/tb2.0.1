package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.QASearchHistoryBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe (意见反馈 + 系统公告) 表数据库
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */
public class QASearchBeanGreenDaoImpl extends CommonCacheImpl<QASearchHistoryBean> {

    @Inject
    public QASearchBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(QASearchHistoryBean singleData) {
        QASearchHistoryBeanDao walletBeanDao = getWDaoSession().getQASearchHistoryBeanDao();
        return walletBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<QASearchHistoryBean> multiData) {
        QASearchHistoryBeanDao walletBeanDao = getWDaoSession().getQASearchHistoryBeanDao();
        walletBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public QASearchHistoryBean getSingleDataFromCache(Long primaryKey) {
        QASearchHistoryBeanDao walletBeanDao = getRDaoSession().getQASearchHistoryBeanDao();
        return walletBeanDao.load(primaryKey);
    }


    @Override
    public List<QASearchHistoryBean> getMultiDataFromCache() {
        QASearchHistoryBeanDao walletBeanDao = getRDaoSession().getQASearchHistoryBeanDao();
        return walletBeanDao.queryBuilder()
                .orderDesc(QASearchHistoryBeanDao.Properties.Create_time)
                .list();
    }

    public List<QASearchHistoryBean> getFristShowData(int size) {
        QASearchHistoryBeanDao walletBeanDao = getRDaoSession().getQASearchHistoryBeanDao();
        return walletBeanDao.queryBuilder()
                .orderDesc(QASearchHistoryBeanDao.Properties.Create_time)
                .limit(size)
                .list();
    }

    @Override
    public void clearTable() {
        QASearchHistoryBeanDao walletBeanDao = getWDaoSession().getQASearchHistoryBeanDao();
        walletBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        QASearchHistoryBeanDao walletBeanDao = getWDaoSession().getQASearchHistoryBeanDao();
        walletBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(QASearchHistoryBean dta) {
        QASearchHistoryBeanDao walletBeanDao = getWDaoSession().getQASearchHistoryBeanDao();
        walletBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(QASearchHistoryBean newData) {
        QASearchHistoryBeanDao walletBeanDao = getWDaoSession().getQASearchHistoryBeanDao();
        walletBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(QASearchHistoryBean newData) {
        QASearchHistoryBeanDao walletBeanDao = getWDaoSession().getQASearchHistoryBeanDao();
        return walletBeanDao.insertOrReplace(newData);
    }

}
