package com.zhiyicx.thinksnsplus.modules.circle.manager.earning;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/12/12/11:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleEarningPresenter extends AppBasePresenter<CircleEarningContract.Repository, CircleEarningContract.View>
        implements CircleEarningContract.Presenter {

    @Inject
    public CircleEarningPresenter(CircleEarningContract.Repository repository, CircleEarningContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void setCirclePermissions(long circleId, List<String> permissions) {
        mRepository.setCirclePermissions(circleId, permissions).subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
            @Override
            protected void onSuccess(BaseJsonV2<Object> data) {

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
}
