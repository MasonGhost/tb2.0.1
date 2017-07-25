package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QA$RewardPresenter extends BasePresenter<QA$RewardContract.Repository, QA$RewardContract.View>
        implements QA$RewardContract.Presenter{

    @Inject
    public QA$RewardPresenter(QA$RewardContract.Repository repository, QA$RewardContract.View rootView) {
        super(repository, rootView);
    }
}
