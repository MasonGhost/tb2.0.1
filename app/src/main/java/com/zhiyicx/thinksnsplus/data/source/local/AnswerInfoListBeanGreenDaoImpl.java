package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/16/17:04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AnswerInfoListBeanGreenDaoImpl extends CommonCacheImpl<AnswerInfoBean> {

    @Inject
    public AnswerInfoListBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(AnswerInfoBean singleData) {
        return getWDaoSession().getAnswerInfoBeanDao().insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<AnswerInfoBean> multiData) {
        getWDaoSession().getAnswerInfoBeanDao().insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public AnswerInfoBean getSingleDataFromCache(Long primaryKey) {
        return getRDaoSession().getAnswerInfoBeanDao().load(primaryKey);
    }

    @Override
    public List<AnswerInfoBean> getMultiDataFromCache() {
        return getRDaoSession().getAnswerInfoBeanDao().loadAll();
    }

    @Override
    public void clearTable() {
        getWDaoSession().getAnswerInfoBeanDao().deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        getWDaoSession().getAnswerInfoBeanDao().deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(AnswerInfoBean dta) {
        getRDaoSession().getAnswerInfoBeanDao().delete(dta);
    }

    @Override
    public void updateSingleData(AnswerInfoBean newData) {
        saveSingleData(newData);
    }

    @Override
    public long insertOrReplace(AnswerInfoBean newData) {
        return saveSingleData(newData);
    }
}
