package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment
        .BUNDLE_INFO;

/**
 * @Author Jliuer
 * @Date 2017/03/07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailsActivity extends TSActivity<InfoDetailsPresenter, InfoDetailsFragment> {

    @Override
    protected InfoDetailsFragment getFragment() {
        return InfoDetailsFragment.newInstance(getIntent().getBundleExtra(BUNDLE_INFO));
    }

    @Override
    protected void componentInject() {
        DaggerInfoDetailsComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .infoDetailsPresenterMudule(new InfoDetailsPresenterMudule(mContanierFragment))
                .shareModule(new ShareModule(this))
                .build()
                .inject(this);
    }
}
