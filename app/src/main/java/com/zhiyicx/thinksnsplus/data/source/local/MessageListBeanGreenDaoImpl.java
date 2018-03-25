package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.TbMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.TbMessageBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 机构消息
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */
public class MessageListBeanGreenDaoImpl extends CommonCacheImpl<TbMessageBean> {

    @Inject
    public MessageListBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(TbMessageBean singleData) {
        TbMessageBeanDao tbMessageBeanDao = getWDaoSession().getTbMessageBeanDao();
        return tbMessageBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<TbMessageBean> multiData) {
        TbMessageBeanDao tbMessageBeanDao = getWDaoSession().getTbMessageBeanDao();
        tbMessageBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public TbMessageBean getSingleDataFromCache(Long primaryKey) {
        TbMessageBeanDao tbMessageBeanDao = getRDaoSession().getTbMessageBeanDao();
        return tbMessageBeanDao.load(primaryKey);
    }


    /**
     * 通过 user id 获取钱包信息
     *
     * @param userId
     * @return
     */
    public TbMessageBean getSingleDataFromCacheByUserId(long userId) {
        TbMessageBeanDao tbMessageBeanDao = getRDaoSession().getTbMessageBeanDao();
        return tbMessageBeanDao.queryBuilder()
                .where(TbMessageBeanDao.Properties.User_id.eq(userId))
                .unique();
    }

    @Override
    public List<TbMessageBean> getMultiDataFromCache() {
        TbMessageBeanDao tbMessageBeanDao = getRDaoSession().getTbMessageBeanDao();
        return tbMessageBeanDao.queryBuilder()
                .where(TbMessageBeanDao.Properties.Channel.isNotNull(), TbMessageBeanDao.Properties.MLoginUserId.eq(AppApplication
                        .getmCurrentLoginAuth()))
                .orderAsc(TbMessageBeanDao.Properties.PinnedTime)
                .list();
    }


    @Override
    public void clearTable() {
        TbMessageBeanDao tbMessageBeanDao = getWDaoSession().getTbMessageBeanDao();
        tbMessageBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        TbMessageBeanDao tbMessageBeanDao = getWDaoSession().getTbMessageBeanDao();
        tbMessageBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(TbMessageBean dta) {
        TbMessageBeanDao tbMessageBeanDao = getWDaoSession().getTbMessageBeanDao();
        tbMessageBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(TbMessageBean newData) {
        TbMessageBeanDao tbMessageBeanDao = getWDaoSession().getTbMessageBeanDao();
        tbMessageBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(TbMessageBean newData) {
        TbMessageBeanDao tbMessageBeanDao = getWDaoSession().getTbMessageBeanDao();
        return tbMessageBeanDao.insertOrReplace(newData);
    }

}
