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
        return 0;
    }

    @Override
    public void saveMultiData(List<AnswerInfoBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public AnswerInfoBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<AnswerInfoBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(AnswerInfoBean dta) {

    }

    @Override
    public void updateSingleData(AnswerInfoBean newData) {

    }

    @Override
    public long insertOrReplace(AnswerInfoBean newData) {
        return 0;
    }
}
