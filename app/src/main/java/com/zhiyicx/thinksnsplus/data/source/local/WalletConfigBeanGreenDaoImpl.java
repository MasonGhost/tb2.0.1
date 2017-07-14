package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/14/17:57
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WalletConfigBeanGreenDaoImpl extends CommonCacheImpl<WalletConfigBean> {

    private WalletConfigBeanDao mWalletConfigBeanDao;

    @Inject
    public WalletConfigBeanGreenDaoImpl(Application context) {
        super(context);
        mWalletConfigBeanDao = getWDaoSession().getWalletConfigBeanDao();
    }

    @Override
    public long saveSingleData(WalletConfigBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<WalletConfigBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public WalletConfigBean getSingleDataFromCache(Long primaryKey) {
        return mWalletConfigBeanDao.load(primaryKey);
    }

    @Override
    public List<WalletConfigBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        mWalletConfigBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(WalletConfigBean dta) {

    }

    @Override
    public void updateSingleData(WalletConfigBean newData) {

    }

    @Override
    public long insertOrReplace(WalletConfigBean newData) {
        return mWalletConfigBeanDao.insertOrReplace(newData);
    }
}
