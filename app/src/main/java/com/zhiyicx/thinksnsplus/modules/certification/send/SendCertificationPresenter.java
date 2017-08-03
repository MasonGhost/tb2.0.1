package com.zhiyicx.thinksnsplus.modules.certification.send;


import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;


import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class SendCertificationPresenter extends BasePresenter<SendCertificationContract.Repository, SendCertificationContract.View>
        implements SendCertificationContract.Presenter{

    @Inject
    public SendCertificationPresenter(SendCertificationContract.Repository repository, SendCertificationContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void sendCertification(SendCertificationBean bean) {
        mRootView.updateSendState(true, false, mContext.getString(R.string.send_certification_ing));
        Subscription subscription = mRepository.sendCertification(bean)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.updateSendState(false, false, message);
                        EventBus.getDefault().post("", EventBusTagConfig.EVENT_SEND_CERTIFICATON_SUCCESS);
                    }


                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        String message = "";
                        if (data.getMessage() != null && data.getMessage().size() > 0){
                            message = data.getMessage().get(0);
                        }
                        mRootView.updateSendState(false, true, message);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
