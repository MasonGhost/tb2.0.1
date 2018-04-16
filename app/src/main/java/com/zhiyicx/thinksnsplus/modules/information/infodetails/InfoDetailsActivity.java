package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;

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
                //.shareModule(new ShareModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //UmengSharePolicyImpl.onDestroy(this);
    }
}
