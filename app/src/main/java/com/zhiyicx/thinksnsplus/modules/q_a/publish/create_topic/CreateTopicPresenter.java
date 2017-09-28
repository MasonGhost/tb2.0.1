package com.zhiyicx.thinksnsplus.modules.q_a.publish.create_topic;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/09/15/10:01
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CreateTopicPresenter extends AppBasePresenter<CreateTopicContract.Repository, CreateTopicContract.View>
        implements CreateTopicContract.Presenter {

    @Inject
    public CreateTopicPresenter(CreateTopicContract.Repository repository, CreateTopicContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void createTopic(String name, String desc) {
        Subscription subscribe = mRepository.createTopic(name, desc)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.apply_doing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.apply_for_success_no_audit));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.apply_for_failed));
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.apply_for_failed));
                    }
                });
        addSubscrebe(subscribe);
    }
}
