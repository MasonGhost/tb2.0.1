package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.ReportResultBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.QAClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IReportRepository;
import com.zhiyicx.thinksnsplus.modules.report.ReportContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe 举报数据处理类
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
public class ReportRepository implements IReportRepository {

    private InfoMainClient mInfoMainClient;
    private DynamicClient mDynamicClient;
    private QAClient mQAClient;
    private CircleClient mCircleClient;
    private UserInfoClient mUserInfoClient;
    private CommonClient mCommonClient;


    @Inject
    public ReportRepository(ServiceManager serviceManager) {
        mInfoMainClient = serviceManager.getInfoMainClient();
        mDynamicClient = serviceManager.getDynamicClient();
        mQAClient = serviceManager.getQAClient();
        mCircleClient = serviceManager.getCircleClient();
        mUserInfoClient = serviceManager.getUserInfoClient();
        mCommonClient = serviceManager.getCommonClient();
    }

    /**
     * 举报动态
     *
     * @param feedId 动态 id
     * @param reason 举报原因
     * @return
     */
    @Override
    public Observable<ReportResultBean> reportDynamic(String feedId, String reason) {
        return mDynamicClient.reportDynamic(feedId, reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 举报资讯
     *
     * @param newsId 资讯 id
     * @param reason 举报原因
     * @return
     */
    @Override
    public Observable<ReportResultBean> reportInfo(String newsId, String reason) {
        return mInfoMainClient.reportInfo(newsId, reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 举报问题
     *
     * @param questionId 动态 id
     * @param reason     举报原因
     * @return
     */
    @Override
    public Observable<ReportResultBean> reportQA(String questionId, String reason) {
        return mQAClient.reportQA(questionId, reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 举报问题答案
     *
     * @param answerId 动态 id
     * @param reason   举报原因
     * @return
     */
    @Override
    public Observable<ReportResultBean> reportQAAnswer(String answerId, String reason) {
        return mQAClient.reportQAAnswer(answerId, reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 举报圈子
     *
     * @param groupId 圈子 id
     * @param reason  举报原因
     * @return
     */
    @Override
    public Observable<ReportResultBean> reportCircle(String groupId, String reason) {
        return mCircleClient.reportCircle(groupId, reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 举报圈子帖子
     *
     * @param postId 帖子 id
     * @param reason 举报原因
     * @return
     */
    @Override
    public Observable<ReportResultBean> reportCirclePost(String postId, String reason) {
        return mCircleClient.reportCirclePost(postId, reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 圈子评论举报
     *
     * @param commentId 评论 id
     * @param reason    举报原因
     * @return
     */
    @Override
    public Observable<ReportResultBean> reportCircleComment(String commentId, String reason) {
        return mCircleClient.reportComment(commentId, reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 通用评论举报
     *
     * @param commentId 评论 id
     * @param reason    举报原因
     * @return
     */
    @Override
    public Observable<ReportResultBean> reportComment(String commentId, String reason) {
        return mCommonClient.reportComment(commentId, reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 举报用户
     *
     * @param userId 用户 id
     * @param reason 举报原因
     * @return
     */
    @Override
    public Observable<ReportResultBean> reportUser(String userId, String reason) {
        return mUserInfoClient.reportUser(userId, reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
