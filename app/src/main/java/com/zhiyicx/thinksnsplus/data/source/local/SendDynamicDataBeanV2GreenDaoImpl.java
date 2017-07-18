package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2Dao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/10/17:27
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SendDynamicDataBeanV2GreenDaoImpl extends CommonCacheImpl<SendDynamicDataBeanV2> {

    private SendDynamicDataBeanV2Dao mSendDynamicDataBeanV2Dao;

    @Inject
    public SendDynamicDataBeanV2GreenDaoImpl(Application context) {
        super(context);
        mSendDynamicDataBeanV2Dao = getWDaoSession().getSendDynamicDataBeanV2Dao();
    }

    @Override
    public long saveSingleData(SendDynamicDataBeanV2 singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<SendDynamicDataBeanV2> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public SendDynamicDataBeanV2 getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<SendDynamicDataBeanV2> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(SendDynamicDataBeanV2 dta) {

    }

    @Override
    public void updateSingleData(SendDynamicDataBeanV2 newData) {

    }

    @Override
    public long insertOrReplace(SendDynamicDataBeanV2 newData) {
        return mSendDynamicDataBeanV2Dao.insertOrReplace(newData);
    }

    public SendDynamicDataBeanV2 getSendDynamicDataBeanV2ByFeedMark(String feed_mark) {
        return mSendDynamicDataBeanV2Dao.queryBuilder().where(SendDynamicDataBeanV2Dao.Properties.Feed_mark.eq(feed_mark)).unique();
    }

    public void delteSendDynamicDataBeanV2ByFeedMark(String feed_mark){
        mSendDynamicDataBeanV2Dao.delete(mSendDynamicDataBeanV2Dao.queryBuilder().
                where(SendDynamicDataBeanV2Dao.Properties.Feed_mark.eq(feed_mark)).unique());
    }
}
