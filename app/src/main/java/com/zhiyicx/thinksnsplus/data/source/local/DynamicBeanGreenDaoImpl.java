package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

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
    public List<DynamicBean> getHotDynamicList(Long hotCreatTime) {
        if (hotCreatTime == 0) {
            hotCreatTime = System.currentTimeMillis();
        }
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        return dynamicBeanDao.queryDeep(" where " + " T." + DynamicBeanDao.Properties.Hot_creat_time.columnName + " != NULL and "
                        + " T." + DynamicBeanDao.Properties.Hot_creat_time.columnName + " < ?  ORDER BY "
                        + " T." + DynamicBeanDao.Properties.Hot_creat_time.columnName + " DESC LIMIT " + ApiConfig.DYNAMIC_PAGE_LIMIT// 创建时间倒序
                , new String[]{String.valueOf(hotCreatTime)});
    }

    /**
     * 获取关注的动态列表
     */
    public List<DynamicBean> getFollowedDynamicList(Long feed_id) {
        if (feed_id == null || feed_id == 0) {
            feed_id = System.currentTimeMillis();
        }
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        return dynamicBeanDao.queryDeep(" where " + " T." + DynamicBeanDao.Properties.IsFollowed.columnName + " = 1 and " // 0 false 1 true
                        + " T." + DynamicBeanDao.Properties.Feed_id.columnName + " < ?   ORDER BY "
                        + " T." + DynamicBeanDao.Properties.Feed_id.columnName + " DESC LIMIT " + ApiConfig.DYNAMIC_PAGE_LIMIT// feedId倒序
                , new String[]{String.valueOf(feed_id)});
    }

    /**
     * 获取最新的动态列表
     */
    public List<DynamicBean> getNewestDynamicList(Long feed_id) {
        if (feed_id == null || feed_id == 0) {
            feed_id = System.currentTimeMillis();
        }
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        return dynamicBeanDao.queryDeep(" where " + " T." + DynamicBeanDao.Properties.Feed_id.columnName + " < ?  ORDER BY "
                        + " T." + DynamicBeanDao.Properties.Feed_id.columnName + " DESC LIMIT " + ApiConfig.DYNAMIC_PAGE_LIMIT// feedId倒序
                , new String[]{String.valueOf(feed_id)});
    }

    /**
     * 获取我正在或者发送失败的动态
     */
    public List<DynamicBean> getMySendingDynamic(Long userId) {

        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        return dynamicBeanDao.queryDeep(" where " + " T." + DynamicBeanDao.Properties.User_id.columnName + " = ? and " + " T." + DynamicBeanDao.Properties.State.columnName + " != " + DynamicBean.SEND_SUCCESS + "  ORDER BY "
                        + " T." + DynamicBeanDao.Properties.Id.columnName + " DESC "// feedId倒序
                , new String[]{String.valueOf(userId)});
    }


    public DynamicBean getDynamicByFeedMark(Long feed_mark) {
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        List<DynamicBean> datas = dynamicBeanDao.queryDeep(" where " + " T." + DynamicBeanDao.Properties.Feed_mark.columnName + " = ? "// feedId倒序
                , new String[]{String.valueOf(feed_mark)});
        if (!datas.isEmpty()) {
            return datas.get(0);
        }
        return null;
    }
}
