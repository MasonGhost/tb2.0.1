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
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
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
public class PublishInfoPresenter extends AppBasePresenter<PublishInfoContract.Repository, PublishInfoContract.View>
        implements PublishInfoContract.Presenter {

    @Inject
    UpLoadRepository mUpLoadRepository;

    @Inject
    public PublishInfoPresenter(PublishInfoContract.Repository repository, PublishInfoContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void uploadPic(String filePath, String mimeType, boolean isPic, int photoWidth, int photoHeight) {
        mUpLoadRepository.upLoadFileWithProgress(filePath, mimeType, true, photoWidth, photoHeight, (bytesWritten, contentLength, done) ->
        {
            LogUtils.d("bytesWritten::" + bytesWritten + "\n" +
                    "mContentLength::" + contentLength + "\n" +
                    "done::" + done);

            LogUtils.d("currentThread::" +Thread.currentThread().getName());

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
            observable = mRepository.updateInfo(infoPublishBean);
        } else {
            observable = mRepository.publishInfo(infoPublishBean);
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

    @Override
    public void pareseBody(String body) {
        Observable.just(body)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return Observable.from(RegexUtils.cutStringByImgTag(s));
                    }
                })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> mRootView.onPareseBodyEnd(true))
                .subscribe(text -> {
                    boolean isLast = text.contains("tym_last");
                    text = text.replaceAll("tym_last", "");
                    if (text.matches("[\\s\\S]*@!\\[\\S*][\\s\\S]*")) {
                        int id = RegexUtils.getImageId(text);
                        String imagePath = APP_DOMAIN + "api/" + API_VERSION_2 + "/files/" + id + "?q=80";
                        if (id > 0) {
                            mRootView.addImageViewAtIndex(imagePath, id, text, isLast);
                        } else {
                            mRootView.showSnackErrorMessage("图片" + 1 + "已丢失，请重新插入！");
                        }
                    } else {
                        mRootView.addEditTextAtIndex(text);
                    }
                });
    }
}
