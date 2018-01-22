package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import android.os.Bundle;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.data.source.local.PostDraftBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailFragment;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/11/17/17:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MarkdownPresenter extends AppBasePresenter<
        MarkdownContract.View> implements MarkdownContract.Presenter {
    @Inject
    BaseCircleRepository mBaseCircleRepository;
    @Inject
    UpLoadRepository mUpLoadRepository;
    @Inject
    PostDraftBeanGreenDaoImpl mPostDraftBeanGreenDao;
    @Inject
    BaseQARepository mBaseQARepository;

    @Inject
    public MarkdownPresenter(MarkdownContract.View rootView) {
        super(rootView);
    }

    /**
     * 上传图片，进度监听
     *
     * @param filePath
     * @param tagId
     */
    @Override
    public void uploadPic(String filePath, long tagId) {
        Subscription subscribe = mUpLoadRepository.upLoadFileWithProgress(filePath, "", true, 0, 0, (bytesWritten,
                                                                                                     contentLength, done) ->
        {
            LogUtils.d("bytesWritten::" + bytesWritten + "\n" +
                    "mContentLength::" + contentLength + "\n" +
                    "done::" + done);
            float progress = 0f;
            if (contentLength > 0) {
                progress = (float) ((double) bytesWritten / (double) contentLength) * 100;
            }
            if (progress == 100) {
                return;
            }
            mRootView.onUploading(tagId, filePath, (int) progress, -1);

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Integer>() {
                    @Override
                    protected void onSuccess(Integer data) {
                        mRootView.onUploading(tagId, filePath, 100, data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage("图片上传失败");
                        mRootView.onFailed(filePath, tagId);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage("图片上传失败");
                        mRootView.onFailed(filePath, tagId);
                    }
                });
        addSubscrebe(subscribe);

    }

    @Override
    public void publishPost(PostPublishBean postPublishBean) {
        Subscription subscribe = mBaseCircleRepository.sendCirclePost(postPublishBean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.post_publishing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<CirclePostListBean>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<CirclePostListBean> data) {
                        mRootView.dismissSnackBar();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(CirclePostDetailFragment.POST_DATA, data.getData());
                        bundle.putBoolean(CirclePostDetailFragment.POST_LIST_NEED_REFRESH, true);
                        EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_CIRCLE_POST);
                        mRootView.sendPostSuccess(data.getData());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string
                                .info_publishfailed));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void saveDraft(BaseDraftBean postDraftBean) {
        if (postDraftBean instanceof PostDraftBean) {
            mPostDraftBeanGreenDao.saveSingleData((PostDraftBean) postDraftBean);
        }
    }

    @Override
    public void pareseBody(String body) {

    }

    @Override
    public void publishAnswer(Long questionId, String body, int anonymity) {
        Subscription subscribe = mBaseQARepository.publishAnswer(questionId, body, anonymity)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.publish_doing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<AnswerInfoBean>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<AnswerInfoBean> data) {
                        data.getData().setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
                        data.getData().setUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()));
                        EventBus.getDefault().post(data.getData(), EventBusTagConfig.EVENT_PUBLISH_ANSWER);
                        mRootView.showSnackMessage(mContext.getString(R.string.publish_success), Prompt.DONE);
                        mRootView.publishSuccess(data.getData());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.publish_failed));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void updateAnswer(Long answerId, String body, int anonymity) {
        Subscription subscribe = mBaseQARepository.updateAnswer(answerId, body, anonymity)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.update_ing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        EventBus.getDefault().post(0L, EventBusTagConfig.EVENT_UPDATE_ANSWER_OR_QUESTION);
                        mRootView.showSnackMessage(mContext.getString(R.string.update_success), Prompt.DONE);
                        mRootView.updateSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.update_failed));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void updateQuestion(Long questionId, String body, int anonymity) {
        Subscription subscribe = mBaseQARepository.updateQuestion(questionId, body, anonymity)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.update_ing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        EventBus.getDefault().post(0L, EventBusTagConfig.EVENT_UPDATE_ANSWER_OR_QUESTION);
                        mRootView.showSnackMessage(mContext.getString(R.string.update_success), Prompt.DONE);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.update_failed));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void deleteAnswer(AnswerDraftBean answer) {

    }

    @Override
    public void saveAnswer(AnswerDraftBean answerDraftBean) {

    }
}
