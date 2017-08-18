package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import android.os.Bundle;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/25/13:51
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_ListInfoFragmentPresenter extends AppBasePresenter<QA_ListInfoConstact.Repository, QA_ListInfoConstact.View> implements QA_ListInfoConstact.Presenter {

    @Inject
    public QA_ListInfoFragmentPresenter(QA_ListInfoConstact.Repository repository, QA_ListInfoConstact.View rootView) {
        super(repository, rootView);
    }

    @Override
    public boolean istourist() {
        return false;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestNetData(String subject, Long maxId, String type, boolean isLoadMore) {
        mRepository.getQAQuestion(subject, maxId, type)
                .subscribe(new BaseSubscribeForV2<List<QAListInfoBean>>() {
                    @Override
                    protected void onSuccess(List<QAListInfoBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public boolean handleTouristControl() {
        return false;
    }

    @Override
    public List<QAListInfoBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QAListInfoBean> data, boolean isLoadMore) {
        return false;
    }

}
