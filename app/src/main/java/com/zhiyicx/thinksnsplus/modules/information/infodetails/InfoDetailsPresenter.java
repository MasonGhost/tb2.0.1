package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class InfoDetailsPresenter extends BasePresenter<InfoDetailsConstract.Repository,
        InfoDetailsConstract.View> implements InfoDetailsConstract.Presenter {

    @Inject
    public InfoDetailsPresenter(InfoDetailsConstract.Repository repository, InfoDetailsConstract
            .View rootView) {
        super(repository, rootView);
    }

    public InfoDetailsPresenter() {
    }
}
