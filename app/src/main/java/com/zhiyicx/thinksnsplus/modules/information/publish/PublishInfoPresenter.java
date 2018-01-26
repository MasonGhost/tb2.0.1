package com.zhiyicx.thinksnsplus.modules.information.publish;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishInfoPresenter extends AppBasePresenter<PublishInfoContract.View>
        implements PublishInfoContract.Presenter {

    @Inject
    UpLoadRepository mUpLoadRepository;
    @Inject
    BaseInfoRepository mBaseInfoRepository;

    @Inject
    public PublishInfoPresenter(PublishInfoContract.View rootView) {
        super(rootView);
    }

    @Override
    public void uploadPic(String filePath, String mimeType, boolean isPic, int photoWidth, int photoHeight) {
        mUpLoadRepository.upLoadFileWithProgress(filePath, mimeType, true, photoWidth, photoHeight, (bytesWritten, contentLength, done) ->
        {
            LogUtils.d("bytesWritten::" + bytesWritten + "\n" +
                    "mContentLength::" + contentLength + "\n" +
                    "done::" + done);

            LogUtils.d("currentThread::" + Thread.currentThread().getName());

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    if (mRootView.showUplaoding()) {
                        mRootView.showSnackLoadingMessage("图片上传中...");
                    }
                })
                .subscribe(new BaseSubscribe<Integer>() {
                    @Override
                    protected void onSuccess(Integer data) {
                        mRootView.uploadPicSuccess(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage("图片上传失败");
                        mRootView.uploadPicFailed();
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage("图片解析错误");
                        mRootView.uploadPicFailed();
                    }
                });

    }

    @Override
    public void publishInfo(InfoPublishBean infoPublishBean) {
        Observable<BaseJsonV2<Object>> observable;
        if (infoPublishBean.isRefuse()) {
            observable = mBaseInfoRepository.updateInfo(infoPublishBean);
        } else {
            observable = mBaseInfoRepository.publishInfo(infoPublishBean);
        }
        observable
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.info_publishing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.showSnackMessage(mContext.getString(R.string.info_publishsuccess), Prompt.DONE);
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

}
