package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.baseproject.base.TSListFragment;
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
        DynamicBeanDao dynamicBeanDao = getWDaoSession().getDynamicBeanDao();
        return dynamicBeanDao.load(primaryKey);
    }

    @Override
    public List<DynamicBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        DynamicBeanDao dynamicBeanDao = getWDaoSession().getDynamicBeanDao();
        dynamicBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(DynamicBean dta) {
        if (dta == null) {
            return;
        }
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        if (dta.getId() == null) {
            dta = dynamicBeanDao.queryBuilder()
                    .where(DynamicBeanDao.Properties.Feed_mark.eq(dta.getFeed_mark()))
                    .unique();
        }
        dynamicBeanDao.delete(dta);
    }

    /**
     * 清除本地动态，通过 type
     *
     * @param type
     */
    public void deleteDynamicByType(String type) {
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        List<DynamicBean> datas = null;
        switch (type) {
            case ApiConfig.DYNAMIC_TYPE_FOLLOWS:
                datas = dynamicBeanDao.queryDeep(" where " + " T." + DynamicBeanDao.Properties.IsFollowed.columnName + " = 1 "); // 0 false 1 true
                for (DynamicBean data : datas) {
                    data.setIsFollowed(false);
                }

                break;
            case ApiConfig.DYNAMIC_TYPE_HOTS:
                datas = dynamicBeanDao.queryDeep(" where " + " T." + DynamicBeanDao.Properties.Hot_creat_time.columnName + " < " + System.currentTimeMillis()); // 0 false 1 true
                for (DynamicBean data : datas) {
                    data.setHot_creat_time(0L);
                }

                break;
            case ApiConfig.DYNAMIC_TYPE_NEW:

                break;
            default:
        }
        if (datas != null) {
            dynamicBeanDao.insertOrReplaceInTx(datas);
        }
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
        if (newData == null) {
            return;
        }
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
        return dynamicBeanDao.queryDeep(" where "
                        + " T." + DynamicBeanDao.Properties.Hot_creat_time.columnName + " < ?  ORDER BY "
                        + " T." + DynamicBeanDao.Properties.Hot_creat_time.columnName + " DESC LIMIT " + TSListFragment.DEFAULT_PAGE_SIZE// 创建时间倒序
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
                        + " T." + DynamicBeanDao.Properties.Feed_id.columnName + " DESC LIMIT " + TSListFragment.DEFAULT_PAGE_SIZE// feedId倒序
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
                        + " T." + DynamicBeanDao.Properties.Feed_id.columnName + " DESC LIMIT " + TSListFragment.DEFAULT_PAGE_SIZE// feedId倒序
                , new String[]{String.valueOf(feed_id)});
    }

    /**
     * 获取我正在或者发送失败的动态
     */
    public List<DynamicBean> getMySendingUnSuccessDynamic(Long userId) {

        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        return dynamicBeanDao.queryDeep(" where " + " T." + DynamicBeanDao.Properties.User_id.columnName + " = ? and " + " T." + DynamicBeanDao.Properties.State.columnName + " != " + DynamicBean.SEND_SUCCESS + "  ORDER BY "
                        + " T." + DynamicBeanDao.Properties.Id.columnName + " DESC "// feedId倒序
                , new String[]{String.valueOf(userId)});
    }

    /**
     * 通过 feed_mark 获取动态
     *
     * @param feed_mark
     * @return
     */
    public DynamicBean getDynamicByFeedMark(Long feed_mark) {
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        List<DynamicBean> datas = dynamicBeanDao.queryDeep(" where " + " T." + DynamicBeanDao.Properties.Feed_mark.columnName + " = ? "// feedId倒序
                , new String[]{String.valueOf(feed_mark)});
        if (!datas.isEmpty()) {
            return datas.get(0);
        }
        return null;
    }

    /**
     * 获取属于我的动态
     *
     * @return
     */
    public List<DynamicBean> getMyDynamics(Long userId) {
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        List<DynamicBean> datas = dynamicBeanDao.queryDeep(" where "
                        + " T." + DynamicBeanDao.Properties.User_id.columnName + " = ? "
                        + " ORDER BY  T." + DynamicBeanDao.Properties.Feed_mark.columnName + " DESC LIMIT " + TSListFragment.DEFAULT_PAGE_SIZE// 按照Feedmark倒序：userId+时间戳 ：越新的动态，feedmark越大
                , new String[]{String.valueOf(userId)});
        return datas;
    }

    /**
     * 更新关注状态
     *
     * @param userId     目标对象
     * @param isFollowed 关注状态
     */
    public void updateFollowStateByUserId(long userId, boolean isFollowed) {
        DynamicBeanDao dynamicBeanDao = getRDaoSession().getDynamicBeanDao();
        List<DynamicBean> datas = dynamicBeanDao.queryDeep(" where "
                        + " T." + DynamicBeanDao.Properties.User_id.columnName + " = ? "
                , new String[]{String.valueOf(userId)});
        for (DynamicBean data : datas) {
            data.setFollowed(isFollowed);
        }
        insertOrReplace(datas);
    }

}
