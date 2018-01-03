package com.zhiyicx.thinksnsplus.modules.circle.search.container;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.q_a.search.container.QASearchContainerContract;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */

public class CircleSearchContainerPresenter extends BasePresenter<CircleSearchContainerContract.View>
        implements CircleSearchContainerContract.Presenter {

    @Inject
    public CircleSearchContainerPresenter(CircleSearchContainerContract.View rootView) {
        super(rootView);
    }

}
