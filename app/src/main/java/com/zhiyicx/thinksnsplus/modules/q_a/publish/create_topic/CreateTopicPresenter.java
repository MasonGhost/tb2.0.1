package com.zhiyicx.thinksnsplus.modules.q_a.publish.create_topic;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;

import javax.inject.Inject;

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
        mRepository.createTopic(name, desc)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("申请中..."))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.showSnackSuccessMessage("申请成功");
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage("申请失败");
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage("申请失败");
                    }
                });
    }
}
