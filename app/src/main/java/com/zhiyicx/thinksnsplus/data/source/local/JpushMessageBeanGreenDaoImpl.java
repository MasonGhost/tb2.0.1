package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 赞列表数据库
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */

public class JpushMessageBeanGreenDaoImpl extends CommonCacheImpl<JpushMessageBean> {

    @Inject
    public JpushMessageBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(JpushMessageBean singleData) {
        JpushMessageBeanDao jpushMessageDao = getWDaoSession().getJpushMessageBeanDao();
        return jpushMessageDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<JpushMessageBean> multiData) {
        JpushMessageBeanDao jpushMessageDao = getWDaoSession().getJpushMessageBeanDao();
        jpushMessageDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public JpushMessageBean getSingleDataFromCache(Long primaryKey) {
        JpushMessageBeanDao jpushMessageDao = getRDaoSession().getJpushMessageBeanDao();
        return jpushMessageDao.load(primaryKey);
    }

    @Override
    public List<JpushMessageBean> getMultiDataFromCache() {
        JpushMessageBeanDao jpushMessageDao = getRDaoSession().getJpushMessageBeanDao();
        return jpushMessageDao.queryBuilder()
                .orderDesc(JpushMessageBeanDao.Properties.Creat_time)
                .limit(TSListFragment.DEFAULT_PAGE_SIZE)
                .list();
    }

    /**
     * 获取最新的 3 条 评论的数据
     *
     * @return
     */
    public List<JpushMessageBean> getCommentJpushMessage() {
        JpushMessageBeanDao jpushMessageDao = getRDaoSession().getJpushMessageBeanDao();
        return jpushMessageDao.queryBuilder()
                .where(JpushMessageBeanDao.Properties.Action.eq(JpushMessageTypeConfig.JPUSH_MESSAGE_ACTION_COMMENT))
                .orderDesc(JpushMessageBeanDao.Properties.Creat_time)
                .limit(3)
                .list();
    }

    /**
     * 获取最新的 3 条 点赞的的数据
     *
     * @return
     */
    public List<JpushMessageBean> getDigJpushMessage() {
        JpushMessageBeanDao jpushMessageDao = getRDaoSession().getJpushMessageBeanDao();
        return jpushMessageDao.queryBuilder()
                .where(JpushMessageBeanDao.Properties.Action.eq(JpushMessageTypeConfig.JPUSH_MESSAGE_ACTION_DIGG))
                .orderDesc(JpushMessageBeanDao.Properties.Creat_time)
                .limit(3)
                .list();
    }

    @Override
    public void clearTable() {
        JpushMessageBeanDao jpushMessageDao = getWDaoSession().getJpushMessageBeanDao();
        jpushMessageDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        JpushMessageBeanDao jpushMessageDao = getWDaoSession().getJpushMessageBeanDao();
        jpushMessageDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(JpushMessageBean dta) {
        JpushMessageBeanDao jpushMessageDao = getWDaoSession().getJpushMessageBeanDao();
        jpushMessageDao.delete(dta);
    }

    @Override
    public void updateSingleData(JpushMessageBean newData) {
        JpushMessageBeanDao jpushMessageDao = getWDaoSession().getJpushMessageBeanDao();
        jpushMessageDao.update(newData);
    }

    @Override
    public long insertOrReplace(JpushMessageBean newData) {
        JpushMessageBeanDao jpushMessageDao = getWDaoSession().getJpushMessageBeanDao();
        return jpushMessageDao.insertOrReplace(newData);
    }

}
