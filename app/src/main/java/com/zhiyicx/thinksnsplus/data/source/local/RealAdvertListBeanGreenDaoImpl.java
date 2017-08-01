package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/01/11:37
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RealAdvertListBeanGreenDaoImpl extends CommonCacheImpl<RealAdvertListBean> {

    RealAdvertListBeanDao mRealAdvertListBeanDao;

    @Inject
    public RealAdvertListBeanGreenDaoImpl(Application context) {
        super(context);
        mRealAdvertListBeanDao = getWDaoSession().getRealAdvertListBeanDao();
    }

    @Override
    public long saveSingleData(RealAdvertListBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<RealAdvertListBean> multiData) {
        mRealAdvertListBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public RealAdvertListBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<RealAdvertListBean> getMultiDataFromCache() {
        return mRealAdvertListBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        mRealAdvertListBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(RealAdvertListBean dta) {

    }

    @Override
    public void updateSingleData(RealAdvertListBean newData) {

    }

    @Override
    public long insertOrReplace(RealAdvertListBean newData) {
        return 0;
    }
}
