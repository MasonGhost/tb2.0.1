package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/10
 * @contact email:450127106@qq.com
 */

public class ChannelSubscripBeanGreenDaoImpl extends CommonCacheImpl<ChannelSubscripBean> {
    @Inject
    public ChannelSubscripBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(ChannelSubscripBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<ChannelSubscripBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public ChannelSubscripBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<ChannelSubscripBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(ChannelSubscripBean dta) {

    }

    @Override
    public void updateSingleData(ChannelSubscripBean newData) {

    }

    @Override
    public long insertOrReplace(ChannelSubscripBean newData) {
        return 0;
    }
}
