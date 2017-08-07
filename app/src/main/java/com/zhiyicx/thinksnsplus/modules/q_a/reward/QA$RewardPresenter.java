package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import android.os.Bundle;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;

import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.q_a.reward.QA$RewardFragment.BUNDLE_RESULT;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QA$RewardPresenter extends BasePresenter<QA$RewardContract.RepositoryPublish, QA$RewardContract.View>
        implements QA$RewardContract.Presenter{

    @Inject
    public QA$RewardPresenter(QA$RewardContract.RepositoryPublish repository, QA$RewardContract.View rootView) {
        super(repository, rootView);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_CHANGE_EXPERT)
    public void updateInviteState(Bundle bundle){
        if (bundle != null){
            ExpertBean expertBean = bundle.getParcelable(BUNDLE_RESULT);
            if (expertBean != null){
                mRootView.setSelectResult(expertBean);
            }
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
