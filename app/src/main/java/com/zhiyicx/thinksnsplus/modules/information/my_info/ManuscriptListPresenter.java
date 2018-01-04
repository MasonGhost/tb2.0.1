package com.zhiyicx.thinksnsplus.modules.information.my_info;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/08/28/14:27
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ManuscriptListPresenter extends AppBasePresenter<ManuscriptListContract.View>
        implements ManuscriptListContract.Presenter {

    @Inject
    BaseInfoRepository mBaseInfoRepository;

    @Inject
    public ManuscriptListPresenter(ManuscriptListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mBaseInfoRepository.getMyInfoList(mRootView.getMyInfoType(), maxId).subscribe(new BaseSubscribeForV2<List<InfoListDataBean>>() {
            @Override
            protected void onSuccess(List<InfoListDataBean> data) {
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
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<InfoListDataBean> data, boolean isLoadMore) {
        return false;
    }
}
