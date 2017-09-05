package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.QAAnswerBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
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
 * @Date 2017/07/26/16:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishContentPresenter extends AppBasePresenter<PublishContentConstact.Repository, PublishContentConstact.View>
        implements PublishContentConstact.Presenter {

    @Inject
    UpLoadRepository mUpLoadRepository;

    @Inject
    public PublishContentPresenter(PublishContentConstact.Repository repository, PublishContentConstact.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void uploadPic(String filePath, String mimeType, boolean isPic, int photoWidth, int photoHeight) {
        mUpLoadRepository.upLoadSingleFileV2(filePath, mimeType, true, photoWidth, photoHeight)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
    public void publishAnswer(Long question_id, String body, int anonymity) {
        mRepository.publishAnswer(question_id, body, anonymity).subscribe(new BaseSubscribeForV2<BaseJsonV2<QAAnswerBean>>() {
            @Override
            protected void onSuccess(BaseJsonV2<QAAnswerBean> data) {
                mRootView.publishSuccess(data.getData());
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
            }
        });
    }

    @Override
    public void updateAnswer(Long answer_id, String body, int anonymity) {
        mRepository.updateAnswer(answer_id, body, anonymity).subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
            @Override
            protected void onSuccess(BaseJsonV2<Object> data) {
                mRootView.updateSuccess();
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
            }
        });
    }

    @Override
    public void updateQuestion(Long question_id, String body, int anonymity) {
        mRepository.updateQuestion(question_id, body, anonymity)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.updateSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
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
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.d("doAfterTerminate");
                    }
                })
                .subscribe(text -> {
                    boolean isLast = text.contains("tym_last");
                    text = text.replaceAll("tym_last", "");
                    if (text.matches("![\\S+]")) {
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

    @Override
    public QAPublishBean getDraftQuestion(long qestion_mark) {
        return mRepository.getDraftQuestion(qestion_mark);
    }

    @Override
    public void saveQuestion(QAPublishBean qestion) {
        mRepository.saveQuestion(qestion);
    }

    @Override
    public void saveAnswer(AnswerDraftBean answer) {

    }

    @Override
    public void deleteAnswer(AnswerDraftBean answer) {

    }

    @Override
    public QAPublishBean getDraftAnswer(long answer_mark) {
        return null;
    }
}
