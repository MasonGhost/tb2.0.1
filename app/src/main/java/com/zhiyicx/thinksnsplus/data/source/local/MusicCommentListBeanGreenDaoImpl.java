package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.ArrayList;
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

    public List<MusicCommentListBean> getMyMusicComment(int music_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }

        return mMusicCommentListBeanDao.queryBuilder().where(MusicCommentListBeanDao.Properties.Music_id.eq(music_id)
                , MusicCommentListBeanDao.Properties.User_id.eq
                        (AppApplication.getmCurrentLoginAuth().getUser_id())).build().list();
    }

    public List<MusicCommentListBean> getLocalMusicComment(int music_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }
        return mMusicCommentListBeanDao.queryBuilder().where(MusicCommentListBeanDao.Properties.Music_id.eq(music_id)).build().list();
    }

    public List<MusicCommentListBean> getLocalAblumComment(int special_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }
        return mMusicCommentListBeanDao.queryBuilder().where(MusicCommentListBeanDao.Properties.Special_id.eq(special_id)).build().list();
    }

    public List<MusicCommentListBean> getMyAblumComment(int special_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }

        return mMusicCommentListBeanDao.queryBuilder().where(MusicCommentListBeanDao.Properties.Special_id.eq(special_id)
                , MusicCommentListBeanDao.Properties.User_id.eq
                        (AppApplication.getmCurrentLoginAuth().getUser_id())).build().list();
    }

    public MusicCommentListBean getMusicCommentByCommentMark(long mark) {
        return mMusicCommentListBeanDao.queryBuilder()
                .where(MusicCommentListBeanDao.Properties.Comment_mark.eq(mark)).build().list().get(0);
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

    public List<MusicCommentListBean> getAblumCommentsCacheDataByType(String channel, long id) {
        return mMusicCommentListBeanDao.queryDeep(" where "
                        + " T." + MusicCommentListBeanDao.Properties.Channel.columnName + "= ?" + " AND "
                        + " T." + MusicCommentListBeanDao.Properties.Id.columnName + "= ?"
                        +"< ? order by"+" T." + MusicCommentListBeanDao.Properties.Id.columnName + " DESC"
                , new String[] {channel,String.valueOf(id),System.currentTimeMillis() + ""});
    }
}
