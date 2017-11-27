package com.zhiyicx.thinksnsplus.modules.circle.all_circle.container;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AllCircleContainerPresenter extends AppBasePresenter<AllCircleContainerContract.Repository, AllCircleContainerContract.View>
        implements AllCircleContainerContract.Presenter {

    @Inject
    public AllCircleContainerPresenter(AllCircleContainerContract.Repository repository, AllCircleContainerContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void getCategroiesList(int limit, int offet) {
        Subscription subscription = mRepository.getCategroiesList(limit, offet)
                .subscribe(new BaseSubscribeForV2<List<CircleTypeBean>>() {

            @Override
            protected void onSuccess(List<CircleTypeBean> data) {
                mRootView.setCategroiesList(data);
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
        addSubscrebe(subscription);
    }
}
