package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/06/09/9:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RechargeSuccessBeanGreenDaoImpl extends CommonCacheImpl<RechargeSuccessBean> {

    RechargeSuccessBeanDao mRechargeSuccessBeanDao;

    @Inject
    public RechargeSuccessBeanGreenDaoImpl(Application context) {
        super(context);
        mRechargeSuccessBeanDao = getWDaoSession().getRechargeSuccessBeanDao();
    }

    @Override
    public long saveSingleData(RechargeSuccessBean singleData) {
        return mRechargeSuccessBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<RechargeSuccessBean> multiData) {
        mRechargeSuccessBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public RechargeSuccessBean getSingleDataFromCache(Long primaryKey) {
        return mRechargeSuccessBeanDao.load(primaryKey);
    }

    @Override
    public List<RechargeSuccessBean> getMultiDataFromCache() {
        return mRechargeSuccessBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        mRechargeSuccessBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mRechargeSuccessBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(RechargeSuccessBean dta) {
        mRechargeSuccessBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(RechargeSuccessBean newData) {
        mRechargeSuccessBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(RechargeSuccessBean newData) {
        return mRechargeSuccessBeanDao.insertOrReplace(newData);
    }

    public List<RechargeSuccessBean> selectBillByAction(int action) {
        return mRechargeSuccessBeanDao.queryBuilder()
                .where(RechargeSuccessBeanDao.Properties.Action.eq(action))
                .orderDesc(RechargeSuccessBeanDao.Properties.Created_at)
                .list();
    }
}
