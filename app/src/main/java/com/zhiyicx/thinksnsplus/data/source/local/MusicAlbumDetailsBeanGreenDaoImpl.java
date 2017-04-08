package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/4/7.
 */

public class MusicAlbumDetailsBeanGreenDaoImpl extends CommonCacheImpl<MusicAlbumDetailsBean> {

    MusicAlbumDetailsBeanDao mMusicAlbumDetailsBeanDao;

    @Inject
    public MusicAlbumDetailsBeanGreenDaoImpl(Application context) {
        super(context);
        mMusicAlbumDetailsBeanDao = getWDaoSession().getMusicAlbumDetailsBeanDao();
    }

    @Override
    public long saveSingleData(MusicAlbumDetailsBean singleData) {
        return mMusicAlbumDetailsBeanDao.insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<MusicAlbumDetailsBean> multiData) {
        mMusicAlbumDetailsBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public MusicAlbumDetailsBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<MusicAlbumDetailsBean> getMultiDataFromCache() {
        return mMusicAlbumDetailsBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        mMusicAlbumDetailsBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(MusicAlbumDetailsBean dta) {

    }

    @Override
    public void updateSingleData(MusicAlbumDetailsBean newData) {
        mMusicAlbumDetailsBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(MusicAlbumDetailsBean newData) {
        return mMusicAlbumDetailsBeanDao.insertOrReplace(newData);
    }

    public MusicAlbumDetailsBean getAblumByID(int id) {
        List<MusicAlbumDetailsBean> data = mMusicAlbumDetailsBeanDao.queryBuilder().where(MusicAlbumDetailsBeanDao.Properties.Id.eq(id)).build().list();
        if (!data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }
}
