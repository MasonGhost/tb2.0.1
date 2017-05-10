package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/04/04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicAlbumListBeanGreenDaoImpl extends CommonCacheImpl<MusicAlbumListBean> {

    private MusicAlbumListBeanDao mMusicAlbumListBeanDao;

    @Inject
    public MusicAlbumListBeanGreenDaoImpl(Application context) {
        super(context);
        mMusicAlbumListBeanDao = getWDaoSession().getMusicAlbumListBeanDao();
    }

    @Override
    public long saveSingleData(MusicAlbumListBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<MusicAlbumListBean> multiData) {
        mMusicAlbumListBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public MusicAlbumListBean getSingleDataFromCache(Long primaryKey) {
        return mMusicAlbumListBeanDao.load(primaryKey);
    }

    @Override
    public List<MusicAlbumListBean> getMultiDataFromCache() {
        return mMusicAlbumListBeanDao.loadAll();
    }

    @Override
    public void clearTable() {
        mMusicAlbumListBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(MusicAlbumListBean dta) {

    }

    @Override
    public void updateSingleData(MusicAlbumListBean newData) {
        if (newData == null)
            return;
        mMusicAlbumListBeanDao.insertOrReplace(newData);
    }

    @Override
    public long insertOrReplace(MusicAlbumListBean newData) {
        if (newData == null)
            return -1;
        return mMusicAlbumListBeanDao.insertOrReplace(newData);
    }

    public List<MusicAlbumListBean> getMyCollectAlbum() {
        MusicAlbumListBeanDao musicAlbumListBeanDao = getRDaoSession().getMusicAlbumListBeanDao();
        QueryBuilder queryBuilder = musicAlbumListBeanDao.queryBuilder();
        queryBuilder.where(MusicAlbumListBeanDao.Properties.Is_collection.eq(1))// 已收藏
                .limit(TSListFragment.DEFAULT_PAGE_SIZE)// 每次取20条
                .orderDesc(MusicAlbumListBeanDao.Properties.Id);// 专辑id倒序
        return queryBuilder.list();
    }
}
