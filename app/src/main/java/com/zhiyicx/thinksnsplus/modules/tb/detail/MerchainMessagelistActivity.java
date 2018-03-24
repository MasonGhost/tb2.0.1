package com.zhiyicx.thinksnsplus.modules.tb.detail;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.tb.privacy.DaggerPrivacyComponent;
import com.zhiyicx.thinksnsplus.modules.tb.privacy.PrivacyFragment;
import com.zhiyicx.thinksnsplus.modules.tb.privacy.PrivacyPresenter;
import com.zhiyicx.thinksnsplus.modules.tb.privacy.PrivacyPresenterModule;

/**
 * @Author Jliuer
 * @Date 2018/02/28/18:36
 * @Email Jliuer@aliyun.com
 * @Description 隐私管理
 */
public class MerchainMessagelistActivity extends TSActivity<MerchainMessageListPresenter, MerchainMessageListFragment> {
    public static final String BUNDLE_USER = "user";

    @Override
    protected MerchainMessageListFragment getFragment() {
        return MerchainMessageListFragment.newInstance((UserInfoBean) getIntent().getSerializableExtra(BUNDLE_USER));
    }

    @Override
    protected void componentInject() {

    }
}
