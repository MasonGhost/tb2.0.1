package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoListBeanGreenDaoImpl extends CommonCacheImpl<InfoListBean> {

    private InfoListBeanDao mInfoListBeanDao;

    @Inject
    public InfoListBeanGreenDaoImpl(Context context) {
        super(context);
        mInfoListBeanDao = getWDaoSession().getInfoListBeanDao();
    }

    @Override
    public long saveSingleData(InfoListBean singleData) {
        return insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<InfoListBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public InfoListBean getSingleDataFromCache(Long primaryKey) {
        return mInfoListBeanDao.load(primaryKey);
    }

    @Override
    public List<InfoListBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(InfoListBean dta) {

    }

    @Override
    public void updateSingleData(InfoListBean newData) {

    }

    @Override
    public long insertOrReplace(InfoListBean newData) {
        if (newData == null) {
            return -1;
        }
        return mInfoListBeanDao.insertOrReplace(newData);
    }
}
