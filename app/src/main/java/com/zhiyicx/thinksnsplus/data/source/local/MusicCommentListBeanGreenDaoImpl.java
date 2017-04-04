package com.zhiyicx.thinksnsplus.data.source.local;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
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

    @Inject
    public MusicCommentListBeanGreenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public long saveSingleData(MusicCommentListBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<MusicCommentListBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public MusicCommentListBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<MusicCommentListBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(MusicCommentListBean dta) {

    }

    @Override
    public void updateSingleData(MusicCommentListBean newData) {

    }

    @Override
    public long insertOrReplace(MusicCommentListBean newData) {
        return 0;
    }
}
