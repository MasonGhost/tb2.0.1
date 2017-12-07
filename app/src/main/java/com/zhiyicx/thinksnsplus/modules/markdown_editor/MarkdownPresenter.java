package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailActivity;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/11/17/17:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MarkdownPresenter extends AppBasePresenter<MarkdownContract.Repository,
        MarkdownContract.View> implements MarkdownContract.Presenter {

    @Inject
    UpLoadRepository mUpLoadRepository;

    @Inject
    public MarkdownPresenter(MarkdownContract.Repository repository, MarkdownContract.View
            rootView) {
        super(repository, rootView);
    }

    /**
     * 上传图片，进度监听
     *
     * @param filePath
     * @param tagId
     */
    @Override
    public void uploadPic(String filePath, long tagId) {
        mUpLoadRepository.upLoadFileWithProgress(filePath, "", true, 0, 0, (bytesWritten,
                                                                            contentLength, done) ->
        {
            LogUtils.d("bytesWritten::" + bytesWritten + "\n" +
                    "contentLength::" + contentLength + "\n" +
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
                        mRootView.showSnackErrorMessage("图片解析错误");
                        mRootView.onFailed(filePath, tagId);
                    }
                });

    }

    @Override
    public void publishPost(PostPublishBean postPublishBean) {
        mRepository.sendCirclePost(postPublishBean)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.info_publishing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<CirclePostListBean>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<CirclePostListBean> data) {
                        mRootView.dismissSnackBar();
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
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.info_publishfailed));
                    }
                });
    }

    @Override
    public void pareseBody(String body) {

    }
}
