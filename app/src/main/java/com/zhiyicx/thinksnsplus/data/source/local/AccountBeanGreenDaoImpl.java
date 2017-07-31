package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.AccountBean;
import com.zhiyicx.thinksnsplus.data.beans.AccountBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/26
 * @contact email:648129313@qq.com
 */

public class AccountBeanGreenDaoImpl extends CommonCacheImpl<AccountBean>{

    private AccountBeanDao mAccountBeanDao;

    @Inject
    public AccountBeanGreenDaoImpl(Application context) {
        super(context);
        mAccountBeanDao = getWDaoSession().getAccountBeanDao();
    }

    @Override
    public long saveSingleData(AccountBean singleData) {
        return mAccountBeanDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<AccountBean> multiData) {
        mAccountBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public AccountBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<AccountBean> getMultiDataFromCache() {
        return mAccountBeanDao.loadAll();
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mAccountBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(AccountBean dta) {
        mAccountBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(AccountBean newData) {
        mAccountBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(AccountBean newData) {
        return mAccountBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplaceByName(AccountBean newData) {
        List<AccountBean> list = mAccountBeanDao.queryBuilder()
                .where(AccountBeanDao.Properties.AccountName.eq(newData.getAccountName())).list();
        if (list == null || list.size() == 0){
            mAccountBeanDao.insertOrReplace(newData);
        }
    }
}
