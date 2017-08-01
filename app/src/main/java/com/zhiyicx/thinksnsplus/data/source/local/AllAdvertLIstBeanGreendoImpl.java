package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/01/10:37
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AllAdvertLIstBeanGreendoImpl extends CommonCacheImpl<AllAdverListBean> {

    private AllAdverListBeanDao mAllAdverListBeanDao;

    @Inject
    public AllAdvertLIstBeanGreendoImpl(Application context) {
        super(context);
        mAllAdverListBeanDao = getWDaoSession().getAllAdverListBeanDao();
    }

    @Override
    public long saveSingleData(AllAdverListBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<AllAdverListBean> multiData) {
        mAllAdverListBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public AllAdverListBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<AllAdverListBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        mAllAdverListBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(AllAdverListBean dta) {

    }

    @Override
    public void updateSingleData(AllAdverListBean newData) {

    }

    @Override
    public long insertOrReplace(AllAdverListBean newData) {
        return 0;
    }

    public AllAdverListBean getBootAdvert() {

        List<AllAdverListBean> data = mAllAdverListBeanDao.queryBuilder().where(AllAdverListBeanDao.Properties.Channel.eq("boot")).build().list();
        if (data != null && !data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }
}
