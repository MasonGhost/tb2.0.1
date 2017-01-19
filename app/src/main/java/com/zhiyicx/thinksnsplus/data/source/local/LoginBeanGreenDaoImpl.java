package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBeanDao;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/6
 * @contact email:450127106@qq.com
 */

public class AuthBeanGreenDaoImpl extends CommonCacheImpl<AuthBean> {
    public AuthBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public void saveSingleData(AuthBean singleData) {
        AuthBeanDao AuthBeanDao = getWDaoSession().getAuthBeanDao();
        AuthBeanDao.insert(singleData);
       AuthBeanDao.save(singleData);
    }

    @Override
    public void saveMultiData(List<AuthBean> multiData) {
        AuthBeanDao AuthBeanDao = getWDaoSession().getAuthBeanDao();
        AuthBeanDao.saveInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public AuthBean getSingleDataFromCache(String key) {
        AuthBeanDao AuthBeanDao = getRDaoSession().getAuthBeanDao();
        return AuthBeanDao.load(Long.parseLong(key));
    }

    @Override
    public List<AuthBean> getMultiDataFromCache() {
        AuthBeanDao AuthBeanDao = getRDaoSession().getAuthBeanDao();
        return AuthBeanDao.loadAll();
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(String key) {
        AuthBeanDao AuthBeanDao = getWDaoSession().getAuthBeanDao();
    }

    @Override
    public void updateSingleData(AuthBean newData) {
        AuthBeanDao AuthBeanDao = getWDaoSession().getAuthBeanDao();
    }
}
