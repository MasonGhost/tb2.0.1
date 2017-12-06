package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/22/10:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AnswerDraftBeanGreenDaoImpl extends CommonCacheImpl<AnswerDraftBean> {

    private AnswerDraftBeanDao mAnswerDraftBeanDao;

    @Inject
    public AnswerDraftBeanGreenDaoImpl(Application context) {
        super(context);
        mAnswerDraftBeanDao = getWDaoSession().getAnswerDraftBeanDao();
    }

    @Override
    public long saveSingleData(AnswerDraftBean singleData) {
        return mAnswerDraftBeanDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<AnswerDraftBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public AnswerDraftBean getSingleDataFromCache(Long primaryKey) {
        return getRDaoSession().getAnswerDraftBeanDao().load(primaryKey);
    }

    @Override
    public List<AnswerDraftBean> getMultiDataFromCache() {
        return getRDaoSession().getAnswerDraftBeanDao().loadAll();
    }

    public List<BaseDraftBean> getMultiBasetDraftDataFromCache() {
        List<AnswerDraftBean> realData = getMultiDataFromCache();
        List<BaseDraftBean> needData = new ArrayList<>();
        if (!realData.isEmpty()) {
            needData.addAll(realData);
        }
        Collections.reverse(needData);
        return needData;
    }

    @Override
    public void clearTable() {
        mAnswerDraftBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        mAnswerDraftBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(AnswerDraftBean dta) {
        if (dta == null) {
            return;
        }
        mAnswerDraftBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(AnswerDraftBean newData) {
        saveSingleData(newData);
    }

    @Override
    public long insertOrReplace(AnswerDraftBean newData) {
        return saveSingleData(newData);
    }
}
