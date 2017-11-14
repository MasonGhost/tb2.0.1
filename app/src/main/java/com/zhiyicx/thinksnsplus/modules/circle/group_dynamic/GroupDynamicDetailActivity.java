package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 圈子动态页面
 * @date 2017/7/18
 * @contact email:648129313@qq.com
 */
public class GroupDynamicDetailActivity extends TSActivity<GroupDynamicDetailPresenter, GroupDynamicDetailFragment> {

    @Override
    protected GroupDynamicDetailFragment getFragment() {
        return GroupDynamicDetailFragment.initFragment(
                getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerGroupDynamicDetailComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .shareModule(new ShareModule(GroupDynamicDetailActivity.this))
                .groupDynamicDetailPresenterModule(new GroupDynamicDetailPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }
}
