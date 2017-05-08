package com.zhiyicx.thinksnsplus.data.source.local;

import android.app.Application;
import android.text.TextUtils;

import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/16
 * @Contact master.jungle68@gmail.com
 */
public class DynamicCommentBeanGreenDaoImplTest {
    private static final String TAG = "DynamicCommentBeanGreen";
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;

    @Before
    public void setUp() throws Exception {
        mDynamicCommentBeanGreenDao = new DynamicCommentBeanGreenDaoImpl((Application) AppApplication.getContext());
    }

    /**
     * summary     插入或者更新评论
     * steps       创建评论，插入数据库，删除数据，查看数据库
     * expected    不存在数据
     */
    @Test
    public void deleteSingleCache() throws Exception {
        Long feed_mark = System.currentTimeMillis();
        DynamicCommentBean dynamicCommentBean = new DynamicCommentBean();
        dynamicCommentBean.setComment_mark(Long.valueOf("50" + System.currentTimeMillis()));
        dynamicCommentBean.setUser_id(6);
        dynamicCommentBean.setReply_to_user_id(10);
        dynamicCommentBean.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        dynamicCommentBean.setComment_content("test comment");
        dynamicCommentBean.setComment_id(System.currentTimeMillis());
        dynamicCommentBean.setFeed_mark(feed_mark);
        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBean);
        mDynamicCommentBeanGreenDao.deleteSingleCache(dynamicCommentBean);
        List<DynamicCommentBean> result = mDynamicCommentBeanGreenDao.getLocalComments(feed_mark);
        Assert.assertTrue(result.size() == 0);
    }

    /**
     * summary   插入或者更新评论
     * steps       创建评论，插入数据库，查看数据库
     * expected   存在数据
     */
    @Test
    public void insertOrReplace() throws Exception {
        Long feed_mark = System.currentTimeMillis();
        DynamicCommentBean dynamicCommentBean = new DynamicCommentBean();
        dynamicCommentBean.setComment_mark(Long.valueOf("50" + System.currentTimeMillis()));
        dynamicCommentBean.setUser_id(6);
        dynamicCommentBean.setReply_to_user_id(10);
        dynamicCommentBean.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        dynamicCommentBean.setComment_content("test comment");
        dynamicCommentBean.setComment_id(System.currentTimeMillis());
        dynamicCommentBean.setFeed_mark(feed_mark);
        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBean);
        List<DynamicCommentBean> result = mDynamicCommentBeanGreenDao.getLocalComments(feed_mark);
        Assert.assertTrue(result.size() > 0);
    }

    /**
     * summary    通过 feed_mark 获取评论
     * steps       创建评论，插入数据库，查看数据库
     * expected   存在数据
     */
    @Test
    public void getLocalComments() throws Exception {
        Long feed_mark = System.currentTimeMillis();
        DynamicCommentBean dynamicCommentBean = new DynamicCommentBean();
        dynamicCommentBean.setComment_mark(Long.valueOf("50" + System.currentTimeMillis()));
        dynamicCommentBean.setUser_id(6);
        dynamicCommentBean.setReply_to_user_id(10);
        dynamicCommentBean.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        dynamicCommentBean.setComment_content("test comment");
        dynamicCommentBean.setComment_id(System.currentTimeMillis());
        dynamicCommentBean.setFeed_mark(feed_mark);
        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBean);
        List<DynamicCommentBean> result = mDynamicCommentBeanGreenDao.getLocalComments(feed_mark);
        LogUtils.d(TAG, "result = " + result.toString());
        Assert.assertTrue(result.size() > 0);
    }

    /**
     * summary    通过 comment_mark 获取评论
     * steps       创建评论，插入数据库，查看数据库
     * expected   存在数据
     */
    @Test
    public void getCommentByCommentMark() throws Exception {
        Long comment_mark = Long.valueOf("50" + System.currentTimeMillis());
        DynamicCommentBean dynamicCommentBean = new DynamicCommentBean();
        dynamicCommentBean.setComment_mark(comment_mark);
        dynamicCommentBean.setUser_id(6);
        dynamicCommentBean.setReply_to_user_id(10);
        dynamicCommentBean.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        dynamicCommentBean.setComment_content("test comment");
        dynamicCommentBean.setComment_id(System.currentTimeMillis());
        dynamicCommentBean.setFeed_mark(System.currentTimeMillis());
        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBean);
        DynamicCommentBean result = mDynamicCommentBeanGreenDao.getCommentByCommentMark(comment_mark);
        Assert.assertTrue(!TextUtils.isEmpty(result.getComment_content()));
    }

}