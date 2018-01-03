package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.CircleSearchHistoryBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.circle.CircleSearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 圈子搜索
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */
public class CircleSearchBeanGreenDaoImpl extends CommonCacheImpl<CircleSearchHistoryBean> {

    @Inject
    public CircleSearchBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(CircleSearchHistoryBean singleData) {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getWDaoSession().getCircleSearchHistoryBeanDao();
        return circleSearchHistoryBeanDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<CircleSearchHistoryBean> multiData) {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getWDaoSession().getCircleSearchHistoryBeanDao();
        circleSearchHistoryBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public CircleSearchHistoryBean getSingleDataFromCache(Long primaryKey) {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getRDaoSession().getCircleSearchHistoryBeanDao();
        return circleSearchHistoryBeanDao.load(primaryKey);
    }


    @Override
    public List<CircleSearchHistoryBean> getMultiDataFromCache() {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getRDaoSession().getCircleSearchHistoryBeanDao();
        return circleSearchHistoryBeanDao.queryBuilder()
                .orderDesc(CircleSearchHistoryBeanDao.Properties.Create_time)
                .list();
    }

    public List<CircleSearchHistoryBean> getMultiDataFromCacheByFrom(boolean isOutsideCircle) {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getRDaoSession().getCircleSearchHistoryBeanDao();
        return circleSearchHistoryBeanDao.queryBuilder()
                .where(CircleSearchHistoryBeanDao.Properties.IsOutSideCircle.eq(isOutsideCircle))
                .orderDesc(CircleSearchHistoryBeanDao.Properties.Create_time)
                .list();
    }

    /**
     * @param size first show size
     * @return 第一次显示的
     */
    public List<CircleSearchHistoryBean> getFristShowData(int size, int type,boolean isOutside) {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getRDaoSession().getCircleSearchHistoryBeanDao();
        return circleSearchHistoryBeanDao.queryBuilder()
                .where(CircleSearchHistoryBeanDao.Properties.Type.eq(type),
                        CircleSearchHistoryBeanDao.Properties.IsOutSideCircle.eq(isOutside))
                .orderDesc(CircleSearchHistoryBeanDao.Properties.Create_time)
                .limit(size)
                .list();
    }

    /**
     * @return 全部圈子搜索历史
     */
    public List<CircleSearchHistoryBean> getCircleSearchHistory() {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getRDaoSession().getCircleSearchHistoryBeanDao();
        return circleSearchHistoryBeanDao.queryBuilder()
                .where(CircleSearchHistoryBeanDao.Properties.Type.eq(CircleSearchHistoryBean.TYPE_CIRCLE))
                .orderDesc(CircleSearchHistoryBeanDao.Properties.Create_time)
                .list();
    }

    /**
     * @return 全部圈子帖子搜索历史
     */
    public List<CircleSearchHistoryBean> getCirclePostSearchHistory() {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getRDaoSession().getCircleSearchHistoryBeanDao();
        return circleSearchHistoryBeanDao.queryBuilder()
                .where(CircleSearchHistoryBeanDao.Properties.Type.eq(CircleSearchHistoryBean.TYPE_CIRCLE_POST))
                .orderDesc(CircleSearchHistoryBeanDao.Properties.Create_time)
                .list();
    }

    /**
     * 清楚所有问答搜索
     */
    public void clearAllQASearchHistory() {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getWDaoSession().getCircleSearchHistoryBeanDao();
        circleSearchHistoryBeanDao.deleteInTx(getCircleSearchHistory());
    }

    /**
     * 清楚所有问答搜索
     */
    public void clearAllQATopicSearchHistory() {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getWDaoSession().getCircleSearchHistoryBeanDao();
        circleSearchHistoryBeanDao.deleteInTx(getCirclePostSearchHistory());
    }

    /**
     * @param qaSearchHistoryBean
     * @param type
     */
    public void saveHistoryDataByType(CircleSearchHistoryBean qaSearchHistoryBean, int type) {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getRDaoSession().getCircleSearchHistoryBeanDao();
        CircleSearchHistoryBean tmpe = circleSearchHistoryBeanDao.queryBuilder()
                .where(CircleSearchHistoryBeanDao.Properties.Content.eq(qaSearchHistoryBean.getContent()), CircleSearchHistoryBeanDao.Properties
                        .Type.eq(type))
                .unique();

        if (tmpe == null) {
            insertOrReplace(qaSearchHistoryBean);
        } else {
            tmpe.setCreate_time(qaSearchHistoryBean.getCreate_time());
            insertOrReplace(tmpe);
        }
    }

    @Override
    public void clearTable() {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getWDaoSession().getCircleSearchHistoryBeanDao();
        circleSearchHistoryBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getWDaoSession().getCircleSearchHistoryBeanDao();
        circleSearchHistoryBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(CircleSearchHistoryBean dta) {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getWDaoSession().getCircleSearchHistoryBeanDao();
        circleSearchHistoryBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(CircleSearchHistoryBean newData) {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getWDaoSession().getCircleSearchHistoryBeanDao();
        circleSearchHistoryBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(CircleSearchHistoryBean newData) {
        CircleSearchHistoryBeanDao circleSearchHistoryBeanDao = getWDaoSession().getCircleSearchHistoryBeanDao();
        return circleSearchHistoryBeanDao.insertOrReplace(newData);
    }

}
