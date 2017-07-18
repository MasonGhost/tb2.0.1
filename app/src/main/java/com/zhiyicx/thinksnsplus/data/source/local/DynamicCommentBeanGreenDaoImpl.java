package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.db.CommonCacheImpl;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.TimeStringSortClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/24
 * @contact email:450127106@qq.com
 */

public class DynamicCommentBeanGreenDaoImpl extends CommonCacheImpl<DynamicCommentBean> {

    @Inject
    public DynamicCommentBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(DynamicCommentBean singleData) {
        return 0;
    }

    @Override
    public void saveMultiData(List<DynamicCommentBean> multiData) {

    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public DynamicCommentBean getSingleDataFromCache(Long primaryKey) {
        return null;
    }

    @Override
    public List<DynamicCommentBean> getMultiDataFromCache() {
        return null;
    }

    @Override
    public void clearTable() {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        dynamicCommentBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {

    }

    @Override
    public void deleteSingleCache(DynamicCommentBean dynamicCommentBean) {
        if (dynamicCommentBean == null) {
            return;
        }
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        if (dynamicCommentBean.get_id() == null) {
            dynamicCommentBean = dynamicCommentBeanDao.queryBuilder()
                    .where(DynamicCommentBeanDao.Properties.Feed_mark.eq(dynamicCommentBean.getComment_mark()))
                    .unique();
        }
        dynamicCommentBeanDao.delete(dynamicCommentBean);


    }

    /**
     * 通过 feedMark 删除评论
     *
     * @param feedMark
     */
    public void deleteCacheByFeedMark(Long feedMark) {
        Observable.from(getLocalComments(feedMark))
                .subscribeOn(Schedulers.io())
                .filter(dynamicCommentBean -> dynamicCommentBean.getComment_id() != null && dynamicCommentBean.getComment_id() != 0).subscribe(new Observer<DynamicCommentBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DynamicCommentBean dynamicCommentBean) {
                deleteSingleCache(dynamicCommentBean);
            }
        });
    }


    @Override
    public void updateSingleData(DynamicCommentBean newData) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        dynamicCommentBeanDao.deleteByKey(newData.get_id());
    }

    @Override
    public long insertOrReplace(DynamicCommentBean newData) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        return dynamicCommentBeanDao.insertOrReplace(newData);
    }

    public void insertOrReplace(List<DynamicCommentBean> newData) {
        if (newData == null || newData.isEmpty()) {
            return;
        }
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        dynamicCommentBeanDao.insertOrReplaceInTx(newData);
    }

    /**
     * 获取最新的动态列表
     */
    public List<DynamicCommentBean> getLocalComments(Long feedMark) {
//        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        List<DynamicCommentBean> dynamicCommentBeen = new ArrayList<>();
        dynamicCommentBeen.addAll(getLocalCommentsByTop(feedMark));
        dynamicCommentBeen.addAll(getMySendingComment(feedMark));
        dynamicCommentBeen.addAll(getLocalCommentsByNotTop(feedMark));
//        dynamicCommentBeen.addAll(dynamicCommentBeanDao.queryBuilder()
//                .where(DynamicCommentBeanDao.Properties.Feed_mark.eq(feedMark), DynamicCommentBeanDao.Properties.Comment_id.isNotNull())
//                .orderDesc(DynamicCommentBeanDao.Properties.Comment_id)
//                .list());

        return dynamicCommentBeen;
    }

    public List<DynamicCommentBean> getLocalCommentsByTop(Long feedMark) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        return dynamicCommentBeanDao.queryBuilder()
                .where(DynamicCommentBeanDao.Properties.Feed_mark.eq(feedMark),
                        DynamicCommentBeanDao.Properties.Pinned.eq(1),
                        DynamicCommentBeanDao.Properties.Comment_id.isNotNull())
                .orderAsc(DynamicCommentBeanDao.Properties.Comment_id)
                .list();
    }

    public List<DynamicCommentBean> getLocalCommentsByNotTop(Long feedMark) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        List<DynamicCommentBean> normalData = dynamicCommentBeanDao.queryBuilder()
                .where(DynamicCommentBeanDao.Properties.Feed_mark.eq(feedMark),
                        DynamicCommentBeanDao.Properties.Pinned.notEq(1),
                        DynamicCommentBeanDao.Properties.Comment_id.isNotNull())
                .orderAsc(DynamicCommentBeanDao.Properties.Comment_id)
                .list();
        return normalData;
    }

    /**
     * 获取我发送中或者失败的评论
     *
     * @return
     */
    public List<DynamicCommentBean> getMySendingComment(Long feedMark) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        return dynamicCommentBeanDao.queryBuilder()
                .where(DynamicCommentBeanDao.Properties.User_id.eq(AppApplication.getmCurrentLoginAuth().getUser_id()), DynamicCommentBeanDao.Properties.Comment_id.isNull(), DynamicCommentBeanDao.Properties.Feed_mark.eq(feedMark))
                .orderDesc(DynamicCommentBeanDao.Properties._id)
                .list();
    }

    /**
     * 通过 CommentMark 获取评论内容
     *
     * @return
     */
    public DynamicCommentBean getCommentByCommentMark(Long commentMark) {
        DynamicCommentBeanDao dynamicCommentBeanDao = getWDaoSession().getDynamicCommentBeanDao();
        List<DynamicCommentBean> result = dynamicCommentBeanDao.queryBuilder()
                .where(DynamicCommentBeanDao.Properties.Comment_mark.eq(commentMark))
                .list();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }
}
