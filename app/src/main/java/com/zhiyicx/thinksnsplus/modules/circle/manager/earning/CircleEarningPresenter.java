package com.zhiyicx.thinksnsplus.modules.circle.manager.earning;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action0;

/**
 * @Author Jliuer
 * @Date 2017/12/12/11:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleEarningPresenter extends AppBasePresenter< CircleEarningContract.View>
        implements CircleEarningContract.Presenter {

    @Inject
    BaseCircleRepository mBaseCircleRepository;
    @Inject
    public CircleEarningPresenter( CircleEarningContract.View rootView) {
        super( rootView);
    }

    @Override
    public void setCirclePermissions(long circleId, List<String> permissions) {
        Subscription subscription = mBaseCircleRepository.setCirclePermissions(circleId, permissions)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
            @Override
            protected void onSuccess(BaseJsonV2<Object> data) {
                mRootView.permissionResult(permissions);
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
                mRootView.showSnackErrorMessage(message);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
                mRootView.showSnackErrorMessage(throwable.getMessage());
            }
        });
        addSubscrebe(subscription);
    }
}
