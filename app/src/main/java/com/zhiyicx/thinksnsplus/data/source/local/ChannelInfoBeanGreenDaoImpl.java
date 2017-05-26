package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/10
 * @contact email:450127106@qq.com
 */

public class ChannelInfoBeanGreenDaoImpl extends CommonCacheImpl<ChannelInfoBean> {
    @Inject
    public ChannelInfoBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(ChannelInfoBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<ChannelInfoBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public ChannelInfoBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<ChannelInfoBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        ChannelSubscripBeanDao channelSubscripBeanDao = getWDaoSession().getChannelSubscripBeanDao();
        channelSubscripBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(ChannelInfoBean dta) {

    }

    @Override
    public void updateSingleData(ChannelInfoBean newData) {

    }

    @Override
    public long insertOrReplace(ChannelInfoBean newData) {
        ChannelInfoBeanDao channelInfoBeanDao = getWDaoSession().getChannelInfoBeanDao();
        return channelInfoBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<ChannelInfoBean> newData) {
        ChannelInfoBeanDao channelInfoBeanDao = getWDaoSession().getChannelInfoBeanDao();
        channelInfoBeanDao.insertOrReplaceInTx(newData);
    }

}
