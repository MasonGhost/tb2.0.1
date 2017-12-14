package com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.QuestionCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean.SEND_ING;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/18
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QuestionCommentPresenter extends AppBasePresenter<QuestionCommentContract.Repository, QuestionCommentContract.View>
        implements QuestionCommentContract.Presenter {

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    QuestionCommentBeanGreenDaoImpl mQuestionCommentBeanGreenDao;

    @Inject
    public QuestionCommentPresenter(QuestionCommentContract.Repository repository, QuestionCommentContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mRepository.getQuestionCommentList(mRootView.getCurrentQuestion().getId(), maxId)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<QuestionCommentBean>>() {

                    @Override
                    protected void onSuccess(List<QuestionCommentBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QuestionCommentBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void sendComment(int reply_id, String content) {
        QuestionCommentBean createComment = new QuestionCommentBean();

        createComment.setState(SEND_ING);

        createComment.setBody(content);

        createComment.setReply_user((long) reply_id);

        createComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());

        createComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());

        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id()
                + "" + System.currentTimeMillis();
        createComment.setComment_mark(Long.parseLong(comment_mark));

        if (reply_id == 0) {// 回复
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(0L);
            createComment.setToUserInfoBean(userInfoBean);
        } else {
            createComment.setToUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                    (long) reply_id));
        }
        createComment.setFromUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                AppApplication.getmCurrentLoginAuth().getUser_id()));
        mQuestionCommentBeanGreenDao.insertOrReplace(createComment);
        if (mRootView.getListDatas().get(0).getBody() == null) {
            mRootView.getListDatas().remove(0);// 去掉占位图
        }
        mRootView.getListDatas().add(0, createComment);
        mRootView.getCurrentQuestion().setComments_count(mRootView.getCurrentQuestion().getComments_count() + 1);
        mRootView.updateCommentCount();
        mRootView.refreshData();
        mRepository.sendComment(content, mRootView.getCurrentQuestion().getId(), reply_id,
                createComment.getComment_mark());
    }

    @Override
    public void deleteComment(long question_id, long answer_id, int position) {
        mRootView.setLoading(true, false, mContext.getString(R.string.bill_doing));
        Subscription subscription = mRepository.deleteComment(question_id, answer_id)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {

                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.getListDatas().remove(position);
                        mRootView.getCurrentQuestion().setComments_count(mRootView.getCurrentQuestion().getComments_count() - 1);
                        mRootView.refreshData();
                        mRootView.setLoading(false, true, mContext.getString(R.string.qa_question_comment_delete_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.setLoading(false, false, message);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 处理发送动态数据
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_QUESTION_LIST)
    public void handleSendComment(QuestionCommentBean questionCommentBean) {
        LogUtils.d(TAG, "questionCommentBean = " + questionCommentBean.toString());
        Subscription subscribe = Observable.just(questionCommentBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(questionCommentBean1 -> {
                    int size = mRootView.getListDatas().size();
                    int infoPosition = -1;
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getComment_mark()
                                == questionCommentBean1.getComment_mark()) {
                            infoPosition = i;
                            mRootView.getListDatas().get(i).setState(questionCommentBean1
                                    .getState());
                            mRootView.getListDatas().get(i).setId(questionCommentBean1.getId());
                            mRootView.getListDatas().get(i).setComment_mark
                                    (questionCommentBean1.getComment_mark());
                            break;
                        }
                    }
                    return infoPosition;
                })
                .subscribe(integer -> {
                    if (integer > 0) {
                        mRootView.refreshData(); // 加上 header
                    }

                }, throwable -> throwable.printStackTrace());
        addSubscrebe(subscribe);

    }
}
