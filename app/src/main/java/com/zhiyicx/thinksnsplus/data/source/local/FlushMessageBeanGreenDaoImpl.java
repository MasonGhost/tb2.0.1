package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.FlushMessages;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessagesDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 获取用户收到的最新消息列表数据库
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */

public class FlushMessageBeanGreenDaoImpl extends CommonCacheImpl<FlushMessages> {

    @Inject
    public FlushMessageBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(FlushMessages singleData) {
        FlushMessagesDao flushMessagesDao = getWDaoSession().getFlushMessagesDao();
        return flushMessagesDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<FlushMessages> multiData) {
        FlushMessagesDao flushMessagesDao = getWDaoSession().getFlushMessagesDao();
        flushMessagesDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public FlushMessages getSingleDataFromCache(Long primaryKey) {
        FlushMessagesDao flushMessagesDao = getRDaoSession().getFlushMessagesDao();
        return flushMessagesDao.load(primaryKey);
    }

    @Override
    public List<FlushMessages> getMultiDataFromCache() {
        FlushMessagesDao flushMessagesDao = getRDaoSession().getFlushMessagesDao();
        List<FlushMessages> datas = flushMessagesDao.loadAll();
        return datas;
    }

    @Override
    public void clearTable() {
        FlushMessagesDao flushMessagesDao = getWDaoSession().getFlushMessagesDao();
        flushMessagesDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        FlushMessagesDao flushMessagesDao = getWDaoSession().getFlushMessagesDao();
        flushMessagesDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(FlushMessages dta) {
        FlushMessagesDao flushMessagesDao = getWDaoSession().getFlushMessagesDao();
        flushMessagesDao.delete(dta);
    }

    @Override
    public void updateSingleData(FlushMessages newData) {
        FlushMessagesDao flushMessagesDao = getWDaoSession().getFlushMessagesDao();
        flushMessagesDao.update(newData);
    }

    @Override
    public long insertOrReplace(FlushMessages newData) {
        FlushMessagesDao flushMessagesDao = getWDaoSession().getFlushMessagesDao();
        return flushMessagesDao.insertOrReplace(newData);
    }

}
