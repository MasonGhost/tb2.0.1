package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe 动态表，包含所有的关联表
 * @date 2017/2/24
 * @contact email:450127106@qq.com
 */

public class DynamicBeanGreenDaoImpl extends CommonCacheImpl<DynamicBean> {
    @Inject
    public DynamicBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(DynamicBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<DynamicBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DynamicBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<DynamicBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void updateSingleData(DynamicBean newData) {

    }

    @Override
    public long insertOrReplace(DynamicBean newData) {
        DynamicBeanDao dynamicBeanDao = getWDaoSession().getDynamicBeanDao();
        return dynamicBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<DynamicBean> newData) {
        DynamicBeanDao dynamicBeanDao = getWDaoSession().getDynamicBeanDao();
        dynamicBeanDao.insertOrReplaceInTx(newData);
    }

    /**
     * 获取热门的动态列表
     *
     * @return
     */
    public List<DynamicBean> getHotDynamicList() {
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        return dynamicBeanDao.queryDeep(" where " + DynamicBeanDao.Properties.Hot_creat_time.columnName + " != NULL and"
                + DynamicBeanDao.TABLENAME + "." + DynamicBeanDao.Properties.Hot_creat_time.columnName + " DESC"// 创建时间倒序
        );
    }

    /**
     * 获取关注的动态列表
     */
    public List<DynamicBean> getFollowedDynamicList() {
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        return dynamicBeanDao.queryDeep(" where " + DynamicBeanDao.Properties.IsFollowed.columnName + " = 1 and" // 0 false 1 true
                + DynamicBeanDao.TABLENAME + "." + DynamicBeanDao.Properties.Feed_id.columnName + " DESC"// feedId倒序
        );
    }

    /**
     * 获取最新的动态列表
     */
    public List<DynamicBean> getNewestDynamicList() {
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        return dynamicBeanDao.queryDeep(" where "
                + DynamicBeanDao.TABLENAME + "." + DynamicBeanDao.Properties.Feed_id.columnName + " DESC"// feedId倒序
        );
    }

    public DynamicBean getDynamicByFeedMark(int feed_mark) {
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        QueryBuilder<DynamicBean> queryBuilder = dynamicBeanDao.queryBuilder();
        queryBuilder.where(DynamicBeanDao.Properties.Feed_mark.eq(feed_mark));
        List<DynamicBean> dynamicBeanList = queryBuilder.list();
        if (dynamicBeanList == null || dynamicBeanList.isEmpty()) {
            return null;
        } else {
            return dynamicBeanList.get(0);
        }

    }
}
