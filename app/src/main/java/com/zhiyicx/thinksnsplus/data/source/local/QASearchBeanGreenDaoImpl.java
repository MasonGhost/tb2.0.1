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
        return walletBeanDao.insertOrReplace(singleData);
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

    /**
     * @param size first show size
     * @return 第一次显示的
     */
    public List<QASearchHistoryBean> getFristShowData(int size, int type) {
        QASearchHistoryBeanDao walletBeanDao = getRDaoSession().getQASearchHistoryBeanDao();
        return walletBeanDao.queryBuilder()
                .where(QASearchHistoryBeanDao.Properties.Type.eq(type))
                .orderDesc(QASearchHistoryBeanDao.Properties.Create_time)
                .limit(size)
                .list();
    }

    /**
     * @return 全部问答搜索历史
     */
    public List<QASearchHistoryBean> getQASearchHistory() {
        QASearchHistoryBeanDao walletBeanDao = getRDaoSession().getQASearchHistoryBeanDao();
        return walletBeanDao.queryBuilder()
                .where(QASearchHistoryBeanDao.Properties.Type.eq(QASearchHistoryBean.TYPE_QA))
                .orderDesc(QASearchHistoryBeanDao.Properties.Create_time)
                .list();
    }

    /**
     * @return 全部问答话题搜索历史
     */
    public List<QASearchHistoryBean> getQATopicSearchHistory() {
        QASearchHistoryBeanDao walletBeanDao = getRDaoSession().getQASearchHistoryBeanDao();
        return walletBeanDao.queryBuilder()
                .where(QASearchHistoryBeanDao.Properties.Type.eq(QASearchHistoryBean.TYPE_QA_TOPIC))
                .orderDesc(QASearchHistoryBeanDao.Properties.Create_time)
                .list();
    }

    /**
     * 清楚所有问答搜索
     */
    public void clearAllQASearchHistory() {
        QASearchHistoryBeanDao walletBeanDao = getWDaoSession().getQASearchHistoryBeanDao();
        walletBeanDao.deleteInTx(getQASearchHistory());
    }

    /**
     * 清楚所有问答搜索
     */
    public void clearAllQATopicSearchHistory() {
        QASearchHistoryBeanDao walletBeanDao = getWDaoSession().getQASearchHistoryBeanDao();
        walletBeanDao.deleteInTx(getQATopicSearchHistory());
    }

    /**
     * @param qaSearchHistoryBean
     * @param type
     */
    public void saveHistoryDataByType(QASearchHistoryBean qaSearchHistoryBean, int type) {
        QASearchHistoryBeanDao walletBeanDao = getRDaoSession().getQASearchHistoryBeanDao();
        QASearchHistoryBean tmpe = walletBeanDao.queryBuilder()
                .where(QASearchHistoryBeanDao.Properties.Content.eq(qaSearchHistoryBean.getContent()), QASearchHistoryBeanDao.Properties.Type.eq(type))
                .unique();

        if (tmpe == null) {
            System.out.println("tmpe = ");
            insertOrReplace(qaSearchHistoryBean);
        } else {
            System.out.println("tmpe = " + tmpe.toString());
            tmpe.setCreate_time(qaSearchHistoryBean.getCreate_time());
            insertOrReplace(tmpe);
        }
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
