package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;

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
            mRootView.onUploading(tagId, filePath, (int) progress);

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Integer>() {
                    @Override
                    protected void onSuccess(Integer data) {
                        mRootView.onUploading(tagId, filePath, 100);
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
    public void publishInfo(InfoPublishBean infoPublishBean) {

    }

    @Override
    public void pareseBody(String body) {

    }
}
