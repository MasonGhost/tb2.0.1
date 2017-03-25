package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoTypeBeanGreenDaoImpl extends CommonCacheImpl<InfoTypeBean> {
    private InfoTypeBeanDao mInfoTypeBeanDao;

    @Inject
    public InfoTypeBeanGreenDaoImpl(Context context) {
        super(context);
        mInfoTypeBeanDao = getWDaoSession().getInfoTypeBeanDao();
    }

    @Override
    public long saveSingleData(InfoTypeBean singleData) {
        return insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<InfoTypeBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public InfoTypeBean getSingleDataFromCache(Long primaryKey) {
        return mInfoTypeBeanDao.load(primaryKey);
    }

    @Override
    public List<InfoTypeBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        mInfoTypeBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(InfoTypeBean dta) {

    }

    @Override
    public void updateSingleData(InfoTypeBean newData) {

    }

    @Override
    public long insertOrReplace(InfoTypeBean newData) {
        if (newData == null) {
            return -1;
        }
        clearTable();
        return mInfoTypeBeanDao.insertOrReplace(newData);
    }
}
