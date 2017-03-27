package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * @Author Jliuer
 * @Date 2017/03/27
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoCommentListBeanDaoImpl extends CommonCacheImpl<InfoCommentListBean> {

    private InfoCommentListBeanDao mInfoCommentListBeanDao;

    @Inject
    public InfoCommentListBeanDaoImpl(Application context) {
        super(context);
        mInfoCommentListBeanDao = getWDaoSession().getInfoCommentListBeanDao();
    }

    @Override
    public long saveSingleData(InfoCommentListBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<InfoCommentListBean> multiData) {
        mInfoCommentListBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public InfoCommentListBean getSingleDataFromCache(Long primaryKey) {
        return mInfoCommentListBeanDao.load(primaryKey);
    }

    @Override
    public List<InfoCommentListBean> getMultiDataFromCache() {
        return mInfoCommentListBeanDao.loadAll();
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(InfoCommentListBean dta) {
        mInfoCommentListBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(InfoCommentListBean newData) {
        mInfoCommentListBeanDao.updateInTx(newData);
    }

    @Override
    public long insertOrReplace(InfoCommentListBean newData) {
        return mInfoCommentListBeanDao.insertOrReplace(newData);
    }

    public List<InfoCommentListBean> getMySendingComment() {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }
        return mInfoCommentListBeanDao.queryBuilder()
                .where(InfoCommentListBeanDao.Properties.User_id.eq
                                (AppApplication.getmCurrentLoginAuth().getUser_id()),
                        InfoCommentListBeanDao.Properties.Id.isNull())
                .orderDesc(InfoCommentListBeanDao.Properties._id)
                .list();


    }
}
