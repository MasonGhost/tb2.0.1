package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QARewardPresenter extends AppBasePresenter<QARewardContract.RepositoryPublish, QARewardContract.View>
        implements QARewardContract.Presenter {

    @Inject
    public QARewardPresenter(QARewardContract.RepositoryPublish repository, QARewardContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void publishQuestion(QAPublishBean qaPublishBean) {
        mRepository.publishQuestion(qaPublishBean).subscribe(new BaseSubscribeForV2<BaseJsonV2<QAPublishBean>>() {
            @Override
            protected void onSuccess(BaseJsonV2<QAPublishBean> data) {
                mRootView.showSnackSuccessMessage(data.getMessage().get(0));
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
    }
}
