package com.zhiyicx.thinksnsplus.modules.tb.dynamic.comment;

import android.os.Bundle;
import android.text.TextUtils;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.ErrorCodeConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseRewardRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/3/21
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class DynamicCommentListPresenter extends AppBasePresenter<
        DynamicCommentListContract.View> implements DynamicCommentListContract.Presenter,
        OnShareCallbackListener {

    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;

    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;


    @Inject
    BaseDynamicRepository mBaseDynamicRepository;
    private boolean mIsNeedDynamicListRefresh = false;
    private boolean mIsAllDataReady = false;


    @Inject
    public DynamicCommentListPresenter(DynamicCommentListContract.View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        // 更新评论列表
        Subscription subscribe = mBaseDynamicRepository.getDynamicCommentListV2(mRootView.getCurrentDynamic().getFeed_mark(), mRootView
                .getCurrentDynamic().getId(), maxId)
                .subscribe(new BaseSubscribeForV2<List<DynamicCommentBean>>() {
                    @Override
                    protected void onSuccess(List<DynamicCommentBean> data) {
                        if (!isLoadMore) { // 刷新时，把自己还未发送成功的评论加载到前面
                            List<DynamicCommentBean> myComments = mDynamicCommentBeanGreenDao
                                    .getMySendingComment(mRootView.getCurrentDynamic()
                                            .getFeed_mark());
                            if (!myComments.isEmpty()) {
                                for (int i = 0; i < myComments.size(); i++) {
                                    myComments.get(i).setCommentUser(mUserInfoBeanGreenDao
                                            .getSingleDataFromCache(myComments.get(i).getUser_id
                                                    ()));
                                    if (myComments.get(i).getReply_to_user_id() != 0) {
                                        myComments.get(i).setReplyUser(mUserInfoBeanGreenDao
                                                .getSingleDataFromCache(myComments.get(i)
                                                        .getReply_to_user_id()));
                                    }
                                }
                                myComments.addAll(data);
                                data.clear();
                                data.addAll(myComments);
                            }
                        }
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        if (mRootView.getCurrentDynamic() == null || AppApplication.getmCurrentLoginAuth() ==
                null) {
            mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
        } else {

            // 从数据库获取评论列表
            mRootView.onCacheResponseSuccess(mDynamicCommentBeanGreenDao.getLocalComments(mRootView.getCurrentDynamic()
                    .getFeed_mark()), isLoadMore);
        }
    }

    @Override
    public boolean insertOrUpdateData(List<DynamicCommentBean> data, boolean isLoadMore) {
        if (data == null) {
            return false;
        }
        mDynamicCommentBeanGreenDao.deleteCacheByFeedMark(mRootView.getCurrentDynamic()
                .getFeed_mark());// 删除本条动态的本地评论
        mDynamicCommentBeanGreenDao.insertOrReplace(data);
        return true;
    }


    @Override
    public void allDataReady() {
        mIsAllDataReady = true;
    }


    /**
     * 处理动态被删除了
     *
     * @param code
     * @param feed_id
     */
    private void handleDynamicHasBeDeleted(int code, Long feed_id) {
        if (code == ErrorCodeConfig.DATA_HAS_BE_DELETED) {
            mDynamicDetailBeanV2GreenDao.deleteDynamicByFeedId(feed_id);
            mRootView.dynamicHasBeDeleted();
        } else {
            mRootView.loadAllError();
        }
    }

    @Override
    public void deleteCommentV2(long comment_id, int commentPosition) {
        mIsNeedDynamicListRefresh = true;
        mRootView.getCurrentDynamic().setFeed_comment_count(mRootView.getCurrentDynamic()
                .getFeed_comment_count() - 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
        mDynamicCommentBeanGreenDao.deleteSingleCache(mRootView.getListDatas()
                .get(commentPosition));
        mRootView.getListDatas().remove(commentPosition);
        if (mRootView.getListDatas().isEmpty()) {
            DynamicCommentBean emptyData = new DynamicCommentBean();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
        mBaseDynamicRepository.deleteCommentV2(mRootView.getCurrentDynamic().getId(), comment_id);
    }

    /**
     * check current dynamic is has been deleted
     *
     * @param user_id   the dynamic is belong to
     * @param feed_mark the dynamic's feed_mark
     * @return
     */
    @Override
    public boolean checkCurrentDynamicIsDeleted(Long user_id, Long feed_mark) {
        if (user_id == AppApplication.getMyUserIdWithdefault() &&
                mDynamicDetailBeanV2GreenDao.getDynamicByFeedMark(feed_mark) == null) { //
            // 检查当前动态是否已经被删除了
            return true;
        }
        return false;
    }

    @Override
    public void sendCommentV2(long replyToUserId, String commentContent) {
        mIsNeedDynamicListRefresh = true;
        // 生成一条评论
        DynamicCommentBean creatComment = new DynamicCommentBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setComment_content(commentContent);
        creatComment.setFeed_mark(mRootView.getCurrentDynamic().getFeed_mark());
        String comment_mark = AppApplication.getMyUserIdWithdefault() + "" + System
                .currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setReply_to_user_id(replyToUserId);
        //当回复动态的时候
        if (replyToUserId == 0) {
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(replyToUserId);
            creatComment.setReplyUser(userInfoBean);
        } else {
            creatComment.setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(replyToUserId));
        }
        creatComment.setUser_id(AppApplication.getMyUserIdWithdefault());
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()));
        creatComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        // 处理评论数
        mRootView.getCurrentDynamic().setFeed_comment_count(mRootView.getCurrentDynamic()
                .getFeed_comment_count() + 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
        if (mRootView.getListDatas().size() == 1 && TextUtils.isEmpty(mRootView.getListDatas()
                .get(0).getComment_content())) {
            mRootView.getListDatas().clear();
        }
        mRootView.getListDatas().add(0, creatComment);
        mRootView.refreshData();
        mBaseDynamicRepository.sendCommentV2(commentContent, mRootView.getCurrentDynamic().getId(),
                replyToUserId, creatComment.getComment_mark());
    }

    @Override
    public void reSendComment(DynamicCommentBean commentBean, long feed_id) {
        commentBean.setState(DynamicCommentBean.SEND_ING);
        mBaseDynamicRepository.sendCommentV2(commentBean.getComment_content(), feed_id, commentBean.getReply_to_user_id(),
                commentBean.getComment_mark());
        mRootView.refreshData();
    }

    /**
     * 处理发送动态数据,动态发布成功回调
     *
     * @param dynamicCommentBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_DYNAMIC_LIST)
    public void handleSendComment(DynamicCommentBean dynamicCommentBean) {

        Subscription subscribe = Observable.just(dynamicCommentBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(dynamicCommentBean1 -> {
                    int size = mRootView.getListDatas().size();
                    int dynamicPosition = -1;
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getFeed_mark().equals
                                (dynamicCommentBean1.getFeed_mark())) {
                            dynamicPosition = i;
                            mRootView.getListDatas().get(i).setState(dynamicCommentBean1.getState
                                    ());
                            mRootView.getListDatas().get(i).setComment_id
                                    (dynamicCommentBean1.getComment_id());
                            mRootView.getListDatas().get(i).setComment_mark
                                    (dynamicCommentBean1.getComment_mark());
                            break;
                        }
                    }
                    return dynamicPosition;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData(); // 加上 header
                    }

                }, throwable -> throwable.printStackTrace());
        addSubscrebe(subscribe);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mIsAllDataReady) { // 数据加载完毕就更新动态列表
            return;
        }

        // 清除占位图数据
        if (mRootView.getListDatas() != null && mRootView.getListDatas().size() == 1 && TextUtils
                .isEmpty(mRootView.getListDatas().get(0).getComment_content())) {
            mRootView.getListDatas().clear();
        }
        Bundle bundle = mRootView.getArgumentsBundle();
        if (bundle != null && bundle.containsKey(DYNAMIC_DETAIL_DATA)) {
            mRootView.getCurrentDynamic().setComments(mRootView.getListDatas());
            bundle.putParcelable(DYNAMIC_DETAIL_DATA, mRootView.getCurrentDynamic());
            bundle.putBoolean(DYNAMIC_LIST_NEED_REFRESH, mIsNeedDynamicListRefresh);
            EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_DYNAMIC);
        }
    }

    @Override
    public void setNeedDynamicListRefresh(boolean needDynamicListRefresh) {
        mIsNeedDynamicListRefresh = needDynamicListRefresh;
    }

    @Override
    public void onStart(Share share) {
    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(mContext.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_cancel));
    }
}
