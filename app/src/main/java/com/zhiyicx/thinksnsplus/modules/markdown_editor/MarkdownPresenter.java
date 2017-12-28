package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import android.os.Bundle;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.data.source.local.PostDraftBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
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
    public MarkdownPresenter(MarkdownContract.View
                                     rootView) {
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
}
