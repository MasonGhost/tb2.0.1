package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;


import com.zhiyicx.thinksnsplus.data.beans.SearchHistoryBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;
import com.zhiyicx.thinksnsplus.modules.tb.search.SearchHistoryBean;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 搜索历史数据
 * @Author Jungle68
 * @Date 2018/3/1
 * @Contact master.jungle68@gmail.com
 */
public class SearchBeanGreenDaoImpl extends CommonCacheImpl<SearchHistoryBean> {

    @Inject
    public SearchBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(SearchHistoryBean singleData) {
        SearchHistoryBeanDao searchHistoryBeanDao = getWDaoSession().getSearchHistoryBeanDao();
        return searchHistoryBeanDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<SearchHistoryBean> multiData) {
        SearchHistoryBeanDao searchHistoryBeanDao = getWDaoSession().getSearchHistoryBeanDao();
        searchHistoryBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public SearchHistoryBean getSingleDataFromCache(Long primaryKey) {
        SearchHistoryBeanDao searchHistoryBeanDao = getRDaoSession().getSearchHistoryBeanDao();
        return searchHistoryBeanDao.load(primaryKey);
    }


    @Override
    public List<SearchHistoryBean> getMultiDataFromCache() {
        SearchHistoryBeanDao searchHistoryBeanDao = getRDaoSession().getSearchHistoryBeanDao();
        return searchHistoryBeanDao.queryBuilder()
                .orderDesc(SearchHistoryBeanDao.Properties.Create_time)
                .list();
    }

    /**
     * @param size first show size
     * @return 第一次显示的
     */
    public List<SearchHistoryBean> getFristShowData(int size, String type) {
        SearchHistoryBeanDao searchHistoryBeanDao = getRDaoSession().getSearchHistoryBeanDao();
        return searchHistoryBeanDao.queryBuilder()
                .where(SearchHistoryBeanDao.Properties.Type.eq(type))
                .orderDesc(SearchHistoryBeanDao.Properties.Create_time)
                .limit(size)
                .list();
    }

    /**
     * @param type
     * @return 返回当前类型的历史记录
     */
    public List<SearchHistoryBean> getAllDataByType(String type) {
        SearchHistoryBeanDao searchHistoryBeanDao = getRDaoSession().getSearchHistoryBeanDao();
        return searchHistoryBeanDao.queryBuilder()
                .where(SearchHistoryBeanDao.Properties.Type.eq(type))
                .orderDesc(SearchHistoryBeanDao.Properties.Create_time)
                .list();
    }

    /**
     * 清楚所有当前类型的历史记录
     */
    public void clearAllSearchHistoryByType(String type) {
        SearchHistoryBeanDao searchHistoryBeanDao = getWDaoSession().getSearchHistoryBeanDao();
        searchHistoryBeanDao.deleteInTx(getAllDataByType(type));
    }

    /**
     * @param qaSearchHistoryBean
     */
    public void saveHistoryDataByType(SearchHistoryBean qaSearchHistoryBean) {
        SearchHistoryBeanDao searchHistoryBeanDao = getRDaoSession().getSearchHistoryBeanDao();
        SearchHistoryBean tmpe = searchHistoryBeanDao.queryBuilder()
                .where(SearchHistoryBeanDao.Properties.Content.eq(qaSearchHistoryBean.getContent()), SearchHistoryBeanDao.Properties
                        .Type.eq(qaSearchHistoryBean.getType()))
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
        SearchHistoryBeanDao searchHistoryBeanDao = getWDaoSession().getSearchHistoryBeanDao();
        searchHistoryBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        SearchHistoryBeanDao searchHistoryBeanDao = getWDaoSession().getSearchHistoryBeanDao();
        searchHistoryBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(SearchHistoryBean dta) {
        SearchHistoryBeanDao searchHistoryBeanDao = getWDaoSession().getSearchHistoryBeanDao();
        searchHistoryBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(SearchHistoryBean newData) {
        SearchHistoryBeanDao searchHistoryBeanDao = getWDaoSession().getSearchHistoryBeanDao();
        searchHistoryBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(SearchHistoryBean newData) {
        SearchHistoryBeanDao searchHistoryBeanDao = getWDaoSession().getSearchHistoryBeanDao();
        return searchHistoryBeanDao.insertOrReplace(newData);
    }

}
