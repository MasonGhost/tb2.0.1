package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/06/02/12:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WithdrawalsListBeanGreenDaoImpl extends CommonCacheImpl<WithdrawalsListBean> {

    WithdrawalsListBeanDao mWithdrawalsListBeanDao;

    @Inject
    public WithdrawalsListBeanGreenDaoImpl(Application context) {
        super(context);
        mWithdrawalsListBeanDao = getWDaoSession().getWithdrawalsListBeanDao();
    }

    @Override
    public long saveSingleData(WithdrawalsListBean singleData) {
        return mWithdrawalsListBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<WithdrawalsListBean> multiData) {
        mWithdrawalsListBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public WithdrawalsListBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<WithdrawalsListBean> getMultiDataFromCache() {
        return mWithdrawalsListBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        mWithdrawalsListBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mWithdrawalsListBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(WithdrawalsListBean dta) {
        mWithdrawalsListBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(WithdrawalsListBean newData) {
        mWithdrawalsListBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(WithdrawalsListBean newData) {
        return mWithdrawalsListBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplaceMultiData(List<WithdrawalsListBean> multiData) {
        mWithdrawalsListBeanDao.insertOrReplaceInTx(multiData);
    }
}
