package com.zhiyicx.thinksnsplus.modules.q_a.search.container;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */

public class QASearchContainerPresenter extends BasePresenter<QASearchContainerContract.View>
        implements QASearchContainerContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public QASearchContainerPresenter(QASearchContainerContract.View rootView) {
        super(rootView);
    }

}
