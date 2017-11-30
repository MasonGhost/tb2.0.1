package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/11/30/13:42
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostCommentBeanGreenDaoImpl extends CommonCacheImpl<CirclePostCommentBean> {

    @Inject
    public CirclePostCommentBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(CirclePostCommentBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<CirclePostCommentBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public CirclePostCommentBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<CirclePostCommentBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {

    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(CirclePostCommentBean dta) {

    }

    @Override
    public void updateSingleData(CirclePostCommentBean newData) {

    }

    @Override
    public long insertOrReplace(CirclePostCommentBean newData) {
        return 0;
    }

    public List<CirclePostCommentBean> getMySendingComment(int i) {
        return new ArrayList<>();
    }
}
