package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/24
 * @contact email:450127106@qq.com
 */

public class DynamicCommentBeanGreenDaoImpl extends CommonCacheImpl<DynamicCommentBean> {
    @Inject
    public DynamicCommentBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(DynamicCommentBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<DynamicCommentBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DynamicCommentBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<DynamicCommentBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(DynamicCommentBean dynamicCommentBean) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        dynamicCommentBeanDao.delete(dynamicCommentBean);
    }

    @Override
    public void updateSingleData(DynamicCommentBean newData) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        dynamicCommentBeanDao.deleteByKey(newData.get_id());
    }

    @Override
    public long insertOrReplace(DynamicCommentBean newData) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        return dynamicCommentBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<DynamicCommentBean> newData) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        dynamicCommentBeanDao.insertOrReplaceInTx(newData);
    }

    /**
     * 获取最新的动态列表
     */
    public List<DynamicCommentBean> getLocalComments(Long feed_mark) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        List<DynamicCommentBean> dynamicCommentBeen = new ArrayList<>();
        dynamicCommentBeen.addAll(dynamicCommentBeanDao.queryBuilder()
                .where(DynamicCommentBeanDao.Properties.Comment_id.isNull())
                .orderDesc(DynamicCommentBeanDao.Properties._id)
                .list());
        dynamicCommentBeen.addAll(dynamicCommentBeanDao.queryBuilder()
                .where(DynamicCommentBeanDao.Properties.Feed_mark.eq(feed_mark), DynamicCommentBeanDao.Properties.Comment_id.isNotNull())
                .orderDesc(DynamicCommentBeanDao.Properties.Comment_id)
                .list());
        return dynamicCommentBeen;
    }
}
