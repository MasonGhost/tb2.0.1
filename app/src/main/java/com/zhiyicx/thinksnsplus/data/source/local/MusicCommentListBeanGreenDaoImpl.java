package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/04/04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicCommentListBeanGreenDaoImpl extends CommonCacheImpl<MusicCommentListBean> {

    private MusicCommentListBeanDao mMusicCommentListBeanDao;

    @Inject
    public MusicCommentListBeanGreenDaoImpl(Application context) {
        super(context);
        mMusicCommentListBeanDao = getWDaoSession().getMusicCommentListBeanDao();
    }

    @Override
    public long saveSingleData(MusicCommentListBean singleData) {
        return mMusicCommentListBeanDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<MusicCommentListBean> multiData) {
        mMusicCommentListBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public MusicCommentListBean getSingleDataFromCache(Long primaryKey) {
        return mMusicCommentListBeanDao.load(primaryKey);
    }

    @Override
    public List<MusicCommentListBean> getMultiDataFromCache() {
        return mMusicCommentListBeanDao.loadAll();
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(MusicCommentListBean dta) {
        mMusicCommentListBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(MusicCommentListBean newData) {
        mMusicCommentListBeanDao.insertOrReplace(newData);
    }

    @Override
    public long insertOrReplace(MusicCommentListBean newData) {
        return mMusicCommentListBeanDao.insertOrReplace(newData);
    }
}
