package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe (意见反馈 + 系统公告) 表数据库
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */
public class WalletBeanGreenDaoImpl extends CommonCacheImpl<WalletBean> {

    @Inject
    public WalletBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(WalletBean singleData) {
        WalletBeanDao walletBeanDao = getWDaoSession().getWalletBeanDao();
        return walletBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<WalletBean> multiData) {
        WalletBeanDao walletBeanDao = getWDaoSession().getWalletBeanDao();
        walletBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public WalletBean getSingleDataFromCache(Long primaryKey) {
        WalletBeanDao walletBeanDao = getRDaoSession().getWalletBeanDao();
        return walletBeanDao.load(primaryKey);
    }

    /**
     * 通过 user id 获取钱包信息
     *
     * @param userId
     * @return
     */
    public WalletBean getSingleDataFromCacheByUserId(long userId) {
        WalletBeanDao walletBeanDao = getRDaoSession().getWalletBeanDao();
        return walletBeanDao.queryBuilder()
                .where(WalletBeanDao.Properties.User_id.eq(userId))
                .unique();
    }

    @Override
    public List<WalletBean> getMultiDataFromCache() {
        WalletBeanDao walletBeanDao = getRDaoSession().getWalletBeanDao();
        return walletBeanDao.loadAll();
    }


    @Override
    public void clearTable() {
        WalletBeanDao walletBeanDao = getWDaoSession().getWalletBeanDao();
        walletBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        WalletBeanDao walletBeanDao = getWDaoSession().getWalletBeanDao();
        walletBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(WalletBean dta) {
        WalletBeanDao walletBeanDao = getWDaoSession().getWalletBeanDao();
        walletBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(WalletBean newData) {
        WalletBeanDao walletBeanDao = getWDaoSession().getWalletBeanDao();
        walletBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(WalletBean newData) {
        WalletBeanDao walletBeanDao = getWDaoSession().getWalletBeanDao();
        return walletBeanDao.insertOrReplace(newData);
    }

}
