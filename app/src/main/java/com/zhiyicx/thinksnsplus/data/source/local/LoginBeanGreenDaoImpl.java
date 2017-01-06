package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.LoginBean;
import com.zhiyicx.thinksnsplus.data.beans.LoginBeanDao;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/6
 * @contact email:450127106@qq.com
 */

public class LoginBeanGreenDaoImpl extends CommonCacheImpl<LoginBean> {
    public LoginBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public void saveSingleData(LoginBean singleData) {
        LoginBeanDao loginBeanDao = getWDaoSession().getLoginBeanDao();
        loginBeanDao.insert(singleData);
       loginBeanDao.save(singleData);
    }

    @Override
    public void saveMultiData(List<LoginBean> multiData) {
        LoginBeanDao loginBeanDao = getWDaoSession().getLoginBeanDao();
        loginBeanDao.saveInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public LoginBean getSingleDataFromCache(String key) {
        LoginBeanDao loginBeanDao = getRDaoSession().getLoginBeanDao();
        return loginBeanDao.load(Long.parseLong(key));
    }

    @Override
    public List<LoginBean> getMultiDataFromCache() {
        LoginBeanDao loginBeanDao = getRDaoSession().getLoginBeanDao();
        return loginBeanDao.loadAll();
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(String key) {
        LoginBeanDao loginBeanDao = getWDaoSession().getLoginBeanDao();
    }

    @Override
    public void updateSingleData(LoginBean newData) {
        LoginBeanDao loginBeanDao = getWDaoSession().getLoginBeanDao();
    }
}
